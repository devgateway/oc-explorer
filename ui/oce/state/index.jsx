import URI from 'urijs';
import debug from 'debug';
import { fetchEP } from '../tools';

const NOTHING = Symbol();
const pluck = field => obj => obj[field];

const consolify = data => data.toJS ? data.toJS() : data;

const isNothing = a => typeof a === 'undefined' ||
  a === NOTHING;

class Node {
  constructor({ name, log }) {
    this.name = name;
    this.listeners = {};
    this.state = NOTHING;
    this.version = 0;
    this.log = log;
  }

  subscribe(name, cb) {
    this.listeners[name] = cb;
    if (this.state !== NOTHING) {
      cb();
    }
  }

  unsubscribe(name) {
    delete this.listeners[name];
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
    Object.values(this.listeners).forEach(setTimeout);
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
    this.deps.forEach(dep => dep.subscribe(this.name, this.doMapping.bind(this)));
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
  constructor({ url, params, ...opts }) {
    super({
      deps: isNothing(params) ? [url] : [url, params],
      ...opts
    });
    this.url = url;
    this.params = params;
  }

  doMapping() {
    if (!this.depsOK()) return;
    const url = new URI(this.deps[0].state);
    if (!isNothing(this.params)) {
      url.addSearch(this.params.state.toJS());
    }
    fetchEP(url).then(data => this.assign(data));
    this.log(`started a request to ${url}`);
  }
}

export default class State {
  constructor() {
    this.entities = {};
  }

  input(opts) {
    const { name } = opts;
    this.entities[name] = new Variable({
      log: debug(`oce.state.${name}`),
      ...opts
    })
  }

  assign(name, value) {
    this.entities[name].assign(value);
  }

  map({ deps, ...opts }) {
    const { name } = opts;
    this.entities[name] = new Mapping({
      log: debug(`oce.state.${name}`),
      deps: deps.map(dep => this.entities[dep]),
      ...opts,
    });
  }

  endpoint({ url, params, ...opts }) {
    const { name } = opts;
    this.entities[name] = new Endpoint({
      log: debug(`oce.state.${name}`),
      url: this.entities[url],
      params: isNothing(params) ? NOTHING : this.entities[params],
      ...opts,
    });
  }

  subscribe(name, sender, listener) {
    this.entities[name].subscribe(sender, listener);

  }

  unsubscribe(name, sender) {
    this.entities[name].unsubscribe(sender);
  }

  getState(name) {
    return this.entities[name].state;
  }
}