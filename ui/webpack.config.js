var config = require('./webpack.dev.config.js');
var webpack = require('webpack');
config.entry = "./index.jsx";
config.output.filename = "index.min.js";
delete config.output.publicPath;
delete config.devtool;
config.plugins.push(new webpack.optimize.UglifyJsPlugin());
config.plugins.push(new webpack.optimize.DedupePlugin());

module.exports = config;