import URI from 'urijs';
import debug from 'debug';
import { fetchEP } from '../tools';

const NOTHING = Symbol();
const pluck = field => obj => obj[field];

function consolify(data) {
  if (data.toJS) return data.toJS();
  if (typeof data === 'object') return data;
  return '';
}

class Node {
  constructor({ name, log }) {
    this.name = name;
    this.listeners = [];
    this.state = NOTHING;
    this.version = 0;
    this.log = log;
  }

  subscribe(listener) {
    this.listeners.push(listener);
    if (this.state !== NOTHING) {
      listener();
    }
  }

  assign(newState) {
    if (typeof newState === 'undefined') {
      this.log(`refusing to update to 'undefined'`);
      return;
    }
    if( this.state === newState) {
      this.log(`did not update because new state(${newState}) is the same as the old one`, newState);
      return;
    }

    this.state = newState;
    this.version++;
    this.log(`Updated to version ${this.version}, state:`, consolify(this.state));
    this.listeners.forEach(setTimeout);
  }
}

class Variable extends Node {
  constructor({ name, initial, ...opts }) {
    super({ name, ...opts });
    if (typeof initial !== 'undefined' && initial !== NOTHING) {
      this.assign(initial);
    }
  } 
}

class Mapping extends Node {
  constructor({ name, deps, mapper, ...opts }) {
    super({ name, ...opts });
    this.mapper = mapper;
    this.deps = deps;
    this.deps.forEach(dep => dep.subscribe(this.doMapping.bind(this)));
  }

  depsOK() {
    const unitialized = this.deps.filter(dep => dep.state === NOTHING);
    if (unitialized.length) {
      const names = unitialized.map(pluck('name')).join(', ');
      this.log(
        `skipped update because ${names} ${unitialized.length > 1 ? 'are' : 'is'} uninitialized`
      );
      return false;
    }
    return true;
  }

  doMapping() {
    if (!this.depsOK()) return;
    this.assign(
      this.mapper(...this.deps.map(pluck('state')))
    );
  }
}

class Endpoint extends Mapping {
  constructor({ url, ...opts }) {
    super({
      deps: [url],
      ...opts
    });
  }

  doMapping() {
    if (!this.depsOK()) return;
    const url = new URI(this.deps[0].state);
    this.log(`started a request to ${url}`);
    fetchEP(url).then(data => this.assign(data));
  }
}

export default class State {
  constructor() {
    this.entities = {};
  }

  input(opts) {
    const { name } = opts;
    this.entities[name] = new Variable({
      log: debug(`oce:state:${name}`),
      ...opts
    })
  }

  assign(name, value) {
    this.entities[name].assign(value);
  }

  map({ deps, ...opts }) {
    const { name } = opts;

    this.entities[name] = new Mapping({
      log: debug(`oce:state:${name}`),
      deps: deps.map(dep => this.entities[dep]),
      ...opts,
    });
  }

  endpoint({ url, ...opts }) {
    const { name } = opts;
    this.entities[name] = new Endpoint({
      log: debug(`oce:state:${name}`),
      url: this.entities[url],
      ...opts,
    });
  }
}