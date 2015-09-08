# DG Toolkit
## Prerequisites
In order to be able to work efficiently you should familiarize yourself with the following technologies:

1. EcmaScript6 aka ES6 aka JS2015 aka Harmony.
The new version of JavaScript. Any valid JS is valid ES6, but ES6 brings to the table some new features
 so it won't hurt to familiarize yourself with it http://es6-features.org/
 _Note: some of these features(ex. constants) might require a polyfill in order to work in IE9._
 _For a quick and dirty solution your try [Babel polyfill](https://babeljs.io/docs/usage/polyfill/)_
2. [React](http://facebook.github.io/react/)
3. [ImmutableJS](https://facebook.github.io/immutable-js/) immutable data structures (not depending on it explicitly,
but NuclearJS does use it for store state)
4. The [Flux](http://facebook.github.io/flux/) architecture. Mainly its [NuclearJS](https://optimizely.github.io/nuclear-js/)
implementation.
5. [Jest](https://facebook.github.io/jest/) testing framework.
6. [Webpack](https://webpack.github.io/)
There's no need to dive deep into it, you'll mainly need to know [how to use loaders](http://webpack.github.io/docs/using-loaders.html)
and [polyfills](http://mts.io/2015/04/08/webpack-shims-polyfills/)

## Usage
Get Node.js

Install module's dependencies

    npm install

Run

    npm start

Navigate to _http://localhost:3000/dev.html_ The browser will be automatically updated
 whenever you save some changes.

To get the project ready for production, run just

    webpack

That will minify and optimize the scripts, use _index.html_ for production.

### Tests
Because of the chaos brought up by iojs splitting up with node and then merging back together, some of Jest's dependencies are
 incompatible with node 0.12. So, in order to run tests you'll need to either downgrade to
 [Node 0.10](https://nodejs.org/en/blog/release/v0.10.36/) or use [nvm](https://github.com/creationix/nvm)
 or [nvmw(for windows)](https://github.com/hakobera/nvmw) to run the tests with 0.10 executable.
 That's a temporary solution until iojs and node merge back together.
 Once you're done with this issue, just run

    npm test

### Pure components
Since we're using immutable data structures, we can reason about whether a component needs to be updated or not
simply by comparing shallowly its previous set of props with current ones. To make use of that, either mix
[PureRenderMixin](https://facebook.github.io/react/docs/pure-render-mixin.html) into your components, or subclass
_components/pure-render-component.jsx_
### Organizing files
#### components/

## Best practises
## Testing

