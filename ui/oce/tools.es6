/**
 * Returns a function that will invoke `func` property on its argument
 * @param {Function} func
 * @returns {Function}
 */
export var callFunc = funcName => obj => obj[funcName]();

export var pluck = fieldName => obj => obj[fieldName];

export var pluckImm = fieldName => imm => imm.get(fieldName);

export var fetchJson = url => fetch(url, {credentials: 'same-origin'}).then(callFunc('json'))

export function debounce(cb, delay = 200){
  var timeout = null;
  return function(){
    if(null !== timeout) clearTimeout(timeout);
    timeout = setTimeout(cb, delay);
  }
}

export var toK = number => number >= 1000 ? Math.round(number / 1000) + "K" : number;

export var identity = _ => _;

export let response2obj = (field, arr) => arr.reduce((obj, elem) => {
  obj[elem._id] = elem[field];
  return obj;
}, {});

var shallowCompArr = (a, b) => a.every((el, index) => el == b[index]);

export var cacheFn = fn => {
  var lastArgs, lastResult;
  return (...args) => {
    if(!lastArgs || !shallowCompArr(lastArgs, args)){
      lastArgs = args;
      lastResult = fn(...args);
    }
    return lastResult;
  }
};

export let max = (a, b) => a > b ? a : b;