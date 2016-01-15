/**
 * Returns a function that will invoke `func` property on its argument
 * @param {Function} func
 * @returns {Function}
 */
export var callFunc = funcName => obj => obj[funcName]();

export var fetchJson = url => fetch(url, {credentials: 'same-origin'}).then(callFunc('json'))