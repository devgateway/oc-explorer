var config = require('./webpack.config.js');
var webpack = require('webpack');
config.output.filename = "index.min.js";
delete config.devtool;
config.plugins.push(new webpack.optimize.UglifyJsPlugin());
config.plugins.push(new webpack.optimize.DedupePlugin());

module.exports = config;