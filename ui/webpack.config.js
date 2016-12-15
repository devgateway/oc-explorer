var config = require('./webpack.dev.config.js');
var webpack = require('webpack');
config.entry = "./index.jsx";
config.output.filename = "index.min.js";
delete config.devtool;
config.plugins = config.plugins.filter(function(plugin){
  return !(plugin instanceof webpack.HotModuleReplacementPlugin);
}).concat([
  new webpack.DefinePlugin({
    "process.env": {
      NODE_ENV: JSON.stringify("production")
    }
  }),
  new webpack.optimize.UglifyJsPlugin(),
  new webpack.optimize.DedupePlugin()
]);

module.exports = config;
