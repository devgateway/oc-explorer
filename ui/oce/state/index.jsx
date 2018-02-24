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
    this.log = debug(this.name);
    this.listeners = {};
    this.version = 0;
    this.state = NOTHING;
  }

  subscribe(lName, listener) {
    this.listeners[lName] = listener;
    if (this.state !== NOTHING) {
      listener(this.name);
    }
  }

  unsubscribe(name) {
    delete this.listeners[name];
  }

  assign(sender, newState) {
    if (sender === this) {
      if (this.state === NOTHING) {
        this.log('initializing');
      }
    } else {
      this.log(`${sender} sent:`, maybeToJS(newState));
    }
    if (typeof newState === 'undefined') {
      this.log('refusing to update to "undefined"');
      return;
    }
    if (newState === this.state) {
      this.log('newState === oldState', maybeToJS(newState));
      return;
    }

    this.state = newState;
    this.version++;
    this.log(`updated to version ${this.version}, state:`, maybeToJS(this.state));
    Object.values(this.listeners).forEach((listener) => {
      schedule(() => {
        listener(this.name);
      });
    });
  }
}

class HVar extends Node {
  constructor({ name, parent, initial }) {
    super({ name, parent });
    if (!isNothing(initial)) {
      this.assign(this, initial);
    } else {
      this.log('started uninitialized');
    }
  }
}

class Mapping extends Node {
  constructor({ deps, mapper, ...opts }) {
    super(opts);
    this.deps = deps.map(d => d.name);
    this.mapper = mapper;
    if (deps.length) this.attach();
  }

  attach() {
    this.deps.forEach(dep =>
      this.parent.resolveDep(dep).subscribe(
        this.name,
        this.doMapping.bind(this)
      )
    );
  }

  depsOK() {
    const unitialized = this.deps.filter(
      dep => this.parent.resolveDep(dep).state === NOTHING
    );
    if (unitialized.length) {
      const names = unitialized.join(', ');
      this.log(
        `skipped update because ${names} ${unitialized.length > 1 ? 'are' : 'is'} uninitialized`
      );
      return false;
    }
    return true;
  }

  doMapping(sender) {
    this.log(`was notified by ${sender}`);
    if (!this.depsOK()) return;
    this.assign(
      this,
      this.mapper(...this.deps.map(dep => this.parent.resolveDep(dep).state))
    );
  }
}

export default class State extends Mapping {
  constructor({ name, parent }) {
    super({ name, parent, deps: [] });
    this.entities = [];
  }

  field(Class, { name, ...opts }) {
    this[name] = new Class({ name, ...opts, parent: this });
    this.entities.push(name);
    return this[name];
  }

  input(opts) { return this.field(HVar, opts); }

  substate(opts) { return this.field(State, opts); }

  mapping(opts) { return this.field(Mapping, opts); }

  remote(opts) { return this.field(Remote, opts); }

  resolveDep(name) {
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
  constructor({ initialUrl, urlMapping, initialParams, paramsMapping, ...opts }) {
    super(opts);
    if (initialUrl) {
      this.input({
        name: 'url',
        initial: initialUrl,
      });
    } else if (urlMapping) {
      const urlMappingOpts = urlMapping;
      this.mapping({
        name: 'url',
        ...urlMappingOpts,
      });
    }

    if (paramsMapping) {
      const paramsMappingOpts = paramsMapping;
      this.mapping({
        name: 'params',
        ...paramsMappingOpts,
      });
    } else {
      this.input({
        name: 'params',
        initial: initialParams || {},
      });
    }

    this.deps = ['url', 'params'];
    this.url.subscribe(this.name, this.fetch.bind(this));
    this.params.subscribe(this.params, this.fetch.bind(this));

    this.input({
      name: 'status',
    });

    this.input({
      name: 'error',
    });

    this.input({
      name: 'result',
    });
  }

  fetch(sender) {
    if (this.params.state === NOTHING) return;
    const uri = new URI(this.url.state).addSearch(this.params.state);
    fetchEP(uri).then(data => this.result.assign(this.name, data));
    this.status.assign(this.name, 'Loading...');
    this.log(`Fetching ${uri}`);
  }
}