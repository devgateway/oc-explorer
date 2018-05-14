/**
 * Returns a function that will invoke `func` property on its argument
 * @param {Function} func
 * @returns {Function}
 */
export const callFunc = funcName => obj => obj[funcName]();

export const pluck = fieldName => obj => obj[fieldName];

export const fetchJson = url => fetch(url, { credentials: 'same-origin' }).then(callFunc('json'));

export function debounce(cb, delay = 200) {
  let timeout = null;
  return function() {
    if (timeout !== null) clearTimeout(timeout);
    timeout = setTimeout(cb, delay);
  };
}

export const years = () => [2011, 2012, 2013, 2014, 2015];

export const identity = _ => _;

export const toK = number => number >= 1000 ? Math.round(number / 1000) + 'K' : number;

export const log = fn => (...args) => {
  console.log(...args);
  return fn(...args);
};
