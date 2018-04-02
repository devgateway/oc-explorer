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
    this.initialized = false;
    this.state = new Promise(resolve => this.resolve = resolve);
    this.state.then(value => {
      this.initialized = true;
      this.log('initialized', maybeToJS(value))
    });
  }

  invalidateDep(dName) {
    schedule(() => this.parent.resolveDep(dName).invalidate(this.name));
  }

  addDep(dName) {
    this.dependents.push(dName);
  }

  addListener(name, cb) {
    this.listeners[name] = cb;
    cb();
  }

  removeListener(name) {
    delete this.listeners[name];
  }

  invalidateDeps() {
    this.dependents.forEach(this.invalidateDep.bind(this));
    Object.values(this.listeners).forEach(cb => {
      cb(this.name);
    });
  }

  getState() {
    return this.state;
  }
}

export class HVar extends Node {
  constructor({ name, parent, initial }) {
    super({ name, parent });
    this.initial = initial;
    if (typeof initial === 'undefined') {
      this.log('Starting unitialized');
    } else {
      this.log('Starting initialized', maybeToJS(initial));
      this.initialized = true;
      this.state = Promise.resolve(initial);
    }
  }

  assign(sender, newState) {
    if (!this.initialized) {
      this.initialized = true;
      this.resolve(newState);
    } else {
      this.state.then(oldState => {
        if (oldState === newState) {
          this.log('oldState === newState', maybeToJS(newState), `sender: ${sender}`)
        } else {
          this.state = Promise.resolve(newState);
          this.state.then(newState => this.log('updated to state', maybeToJS(newState), `sender: ${sender}`))
          this.invalidateDeps();
        }
      })
    }
  }
}

export class Mapping extends Node {
  constructor({ deps, mapper, eager, ...opts }) {
    super(opts);
    this.deps = deps.map(d => d.name);
    this.mapper = mapper;
    this.initialized = false;
    this.eager = eager;
    if (this.eager) {
      this.log('initializing(eager)');
      this.getState();
    } else {
      this.log('initialization pending');
    }
  }

  depsPromise() {
    return Promise.all(
      this.deps.map(dep => this.parent.resolveDep(dep).getState(this.name))
    )
  }

  getState(sender) {
    if (this.initializing || this.updating) {
    } else if (!this.initialized) {
      if (!this.eager) {
        this.log(`initialization triggered by ${sender}`)
      }
      this.initializing = true;
      this.resolve(
        this.depsPromise().then(args => this.mapper(...args))
      )
      this.state.then(() => {
        this.initializing = false
        this.attach();
      });
    } else if (!this.valid) {
      if (!this.eager && !Object.values(this.listeners).length) {
        this.log(`update triggered by ${sender}`);
      }
      this.updating = true;
      this.state = this.depsPromise().then(args => this.mapper(...args));
      this.state.then(newState => {
        this.log('updated', maybeToJS(newState))
        this.updating = false;
        this.valid = true;
      });
    }
    return this.state;
  }

  subscribeTo(depName) {
    this.parent.resolveDep(depName).addDep(this.name);
  }

  attach() {
    this.deps.forEach(this.subscribeTo.bind(this));
  }

  invalidate(sender) {
    this.log(`Invalidated by ${sender}`)
    this.valid = false;
    if (this.eager) {
      this.log('eagerly updating');
      this.getState();
    } else if (Object.values(this.listeners).length) {
      this.log('doing it for the listeners')
      this.getState();
    } else {
      this.log('is lazy');
    }
    this.invalidateDeps();
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
  constructor({ url, params, ...opts }) {
    let deps = [];
    if (url instanceof Node) {
      deps.push(url);
    }
    if (params) {
      deps.push(params);
    }

    super({
      deps,
      ...opts,
      mapper(url, params) {
        const uri = new URI(url);
        if (params) {
          uri.addSearch(maybeToJS(params));
        }
        this.log(`fetching ${uri}`);
        return fetchEP(uri);
      }
    });
  }
}
