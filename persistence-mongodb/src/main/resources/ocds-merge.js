var jsonMergePatch = require('json-merge-patch');
var dir = require('node-dir');

let json = require('./release-schema.json');
let patch = require('./extensions/core/ocds_bid_extension/release-schema.json');

var target = jsonMergePatch.apply(json, patch);

dir.files("extensions/core/ocds_bid_extension/**", function(err, files) {
  if (err)
    throw err;
  

});

console.log(target);
