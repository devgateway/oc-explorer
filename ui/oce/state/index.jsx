import URI from 'urijs';
import debug from 'debug';
import { fetchEP } from '../tools';

const NOTHING = Symbol();
const pluck = field => obj => obj[field];

const maybeToJS = data => data && data.toJS ? data.toJS() : data;

const isNothing = a => typeof a === 'undefined' ||
  a === NOTHING;

function schedule(cb) {
  requestIdleCallback ?
    requestIdleCallback (cb) :
    setTimeout(cb);
}

class Node {
  constructor({ name, parent }) {
    if (parent) {
      this.name = `${parent.name}.${name}`;
    } else {
      this.name = name;
    }
    this.parent = parent;
    if (this.parent) {
      this.parent.register(this, name);
    }
    this.log = debug(this.name);
    this.dependents = [];
    this.listeners = {};
    this.version = 0;
  }

  notifyDep(dName) {
    const dep = this.parent.resolveDep(dName);
    this.parent.resolveDep(dName).onDepUpdated(this.name);
  }

  addDep(dName) {
    this.dependents.push(dName);
    if (this.state) {
      this.notifyDep(dName);
    }
  }

  addListener(name, cb) {
    this.listeners[name] = cb;
    if (!this.eager && Object.keys(this.listeners).length === 1) {
      this.getState();
    }
  }

  removeListener(name) {
    delete this.listeners[name];
  }

  notifyDeps() {
    this.dependents.forEach(this.notifyDep.bind(this));
    Object.values(this.listeners).forEach(cb => {
      cb(this.name);
    });
  }

  assign(sender, newState) {
    if (this.state === newState) {
      this.log('newState === oldState', newState);
      return;
    }

    this.state = newState;
    this.version++;
    this.log(`Updated to version ${this.version}`, maybeToJS(newState));
    this.notifyDeps();
  }

  getState() {
    return this.state;
  }
}

class HVar extends Node {
  constructor({ name, parent, initial }) {
    super({ name, parent });
    if (!isNothing(initial)) {
      this.log('initilized', maybeToJS(initial));
      this.state = initial;
    } else {
      this.log('started uninitialized');
    }
  }
}

class Mapping extends Node {
  constructor({ deps, mapper, eager, ...opts }) {
    super(opts);
    this.deps = deps.map(d => d.name);
    this.mapper = mapper;
    this.eager = eager;
    this.depVersions = {};
    this.attach();
  }

  subscribeTo(depName) {
    this.parent.resolveDep(depName).addDep(this.name);
  }

  attach() {
    this.deps.forEach(this.subscribeTo.bind(this));
  }

  onDepUpdated(sender) {
    this.log(`Notified by ${sender}`)
    if (this.eager) {
      this.log('eagerly updating');
      this.getState();
    } else if (Object.values(this.listeners).length) {
      this.log('doing it for the listeners')
      this.getState();
    } else {
      this.log('is lazy');
      this.notifyDeps();
    }
  }

  getState() {
    let values = [];
    let versions = {};
    if (this.deps.every(dName => {
      const dep = this.parent.resolveDep(dName);
      const state = dep.getState();
      values.push(state);
      versions[dName] = dep.version;
      return typeof state !== 'undefined' &&
        dep.version !== this.depVersions[dName]
    })) {
      this.log('updating');
      Promise.resolve(this.mapper(...values)).then(newState => {
        if (newState !== this.state) {
          this.state = newState;
          this.version++;
          this.depVersions = versions;
          this.notifyDeps();
          this.log(`updated to version ${this.version}`, maybeToJS(this.state));
        } else {
          this.log('oldState === newState', newState)
        }
      })
    }

    return this.state;
  }
}

export default class State extends Mapping {
  constructor({ name, parent, ...opts }) {
    super({ name, parent, deps: [], ...opts });
    this.entities = [];
  }

  register(entity, name) {
    this.entities.push(name);
    this[name] = entity;
  }

  unregister(name) {
    const indexOf = this.entities.indexOf(name);
    this.entities.splice(indexOf, 1);
    delete this[name];
  }

  field(Class, { name, ...opts }) {
    let oldDependents = [];
    if (this[name]) {
      this.log(`replacing ${name}`);
      oldDependents = this[name].dependents;
    }
    new Class({ name, ...opts, parent: this });
    oldDependents.forEach(this[name].addDep.bind(this[name]));
    return this[name];
  }

  input(opts) { return this.field(HVar, opts); }

  substate(opts) { return this.field(State, opts); }

  mapping(opts) { return this.field(Mapping, opts); }

  remote(opts) { return this.field(Remote, opts); }

  resolveDep(name) {
    if (name === this.name) return this;
    if (name.indexOf(this.name) === 0) {
      const rest = name.slice(this.name.length + 1);
      const iofSeparator = rest.indexOf('.');
      if (iofSeparator === -1) {
        return this[rest];
      } else {
        const substateName = rest.slice(0, iofSeparator);
        return this[substateName].resolveDep(name);
      }
    }

    return this.parent.resolveDep(name);
  }
}

class Remote extends State {
  constructor(opts) {
    super({
      ...opts,
      mapper(url, params) {
        const uri = new URI(url).addSearch(params);
        this.log(`fetching ${uri}`);
        return fetchEP(uri);
      }
    });
    this.input({ name: 'url' });
    this.input({ name: 'params', initial: {} });

    this.deps = [this.url.name, this.params.name];
    this.url.addDep(this.name);
    this.params.addDep(this.name);
  }
}
