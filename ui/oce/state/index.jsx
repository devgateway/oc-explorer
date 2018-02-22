const NOTHING = Symbol();
const pluck = field => obj => obj[field];

function consolify(data) {
  if (data.toJS) return data.toJS();
  if (typeof data === 'object') return data;
  return '';
}

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
    if (typeof newState === 'undefined') {
      console.log(`${this.name} refuses to update to 'undefined'`);
      return;
    }
    if( this.state === newState) {
      console.log(`${this.name} did not update because new state(${newState}) is the same as the old one`, newState);
      return;
    }

    this.state = newState;
    this.version++;
    console.log(
      `${this.name} has updated to ${this.state} version ${this.version}`,
      consolify(this.state)
    );
    this.listeners.forEach(setTimeout);
  }
}

class Variable extends Node {
  constructor({ name, initial }) {
    super({ name });
    if (typeof initial !== 'undefined' && initial !== NOTHING) {
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
    const unitialized = this.deps.filter(dep => dep.state === NOTHING);
    if (unitialized.length) {
      const names = unitialized.map(pluck('name')).join(', ');
      console.log(
        `${this.name} skipped update because ${names} ${unitialized.length > 1 ? 'are' : 'is'} uninitialized`
      );
      return
    }
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