/**
 * Returns a function that will invoke `func` property on its argument
 * @param {Function} func
 * @returns {Function}
 */
export var callFunc = funcName => obj => obj[funcName]();

export var fetchJson = url => fetch(url, {credentials: 'same-origin'}).then(callFunc('json'))

export function debounce(cb, delay = 200){
  var timeout = null;
  return function(){
    if(null !== timeout) clearTimeout(timeout);
    timeout = setTimeout(cb, delay);
  }
}

export var years = () => [2011, 2012, 2013, 2014, 2015];