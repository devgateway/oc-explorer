var webpack = require('webpack');
module.exports = {
  entry: "./index.jsx",
  output: {
    path: './',
    filename: "index.js"
  },
  module: {
    loaders: [
      { test: /\.(jsx|es6)$/, loaders:['babel'], exclude: /node_modules/ },
      { test: /\.json$/, loader: 'json' },
      { test: /\.css$/, exclude: /\.useable\.css$/, loader: "style!css" },
      { test: /\.less$/, loader: "style!css!less" }
    ]
  },
  devtool: 'source-map',
  plugins: [
    new webpack.ProvidePlugin({
      //fetch: 'imports?this=>global!exports?global.fetch!whatwg-fetch'
    })
  ]
};