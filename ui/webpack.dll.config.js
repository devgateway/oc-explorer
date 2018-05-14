var webpack = require('webpack');
var path = require('path');

module.exports = {
  entry: {
    lib: [
      'react',
      'react-dom',
      'react-bootstrap',
      'plotly.js',
      'react-leaflet',
      'plotly.js/lib/core',
      'plotly.js/lib/bar',
      'plotly.js/lib/pie.js',
      'immutable'
    ]
  },

  output: {
    path: path.join(__dirname, 'dll'),
    filename: '[name].js',
    library: '[name]',
    libraryTarget: 'var'
  },

  plugins: [
    new webpack.DllPlugin({
      path: './dll/manifest.json',
      name: '[name]',
      context: __dirname
    })
  ]
}
