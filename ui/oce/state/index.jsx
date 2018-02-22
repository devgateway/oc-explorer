const NOTHING = Symbol();
const pluck = field => obj => obj[field];

class Node {
  constructor({ name }) {
    this.name = name;
    this.listeners = [];
    this.state = NOTHING;
    this.version = 0;
  }

  subscribe(listener) {
    this.listeners.push(listener);
    if (this.state !== NOTHING) {
      listener();
    }
  }

  assign(newState) {
    this.state = newState;
    this.version++;
    console.log(`${this.name} has updated to ${this.state} version ${this.version}`);
    this.listeners.forEach(setTimeout);
  }
}

class Variable extends Node {
  constructor({ name, initial }) {
    super({ name });
    if (initial !== NOTHING) {
      this.assign(initial);
    }
  } 
}

class Mapping extends Node {
  constructor({ name, deps, mapper }) {
    super({ name });
    this.mapper = mapper;
    this.deps = deps;
    this.deps.forEach(dep => dep.subscribe(this.doMapping.bind(this)));
  }

  assign() { throw `Nope, can't assign to mapping ${this.name} directly`; }

  doMapping() {
    super.assign(
      this.mapper(...this.deps.map(pluck('state')))
    );
  }
}

export default class State {
  constructor() {
    this.entities = {};
  }

  input(opts) {
    const { name } = opts;
    this.entities[name] = new Variable(opts);
  }

  assign(name, value) {
    this.entities[name].assign(value);
  }

  map({ deps, ...opts }) {
    const { name } = opts;
    this.entities[name] = new Mapping({
      ...opts,
      deps: deps.map(dep => this.entities[dep])
    });
  }
}