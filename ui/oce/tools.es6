import URI from 'urijs';
/**
 * Returns a function that will invoke `func` property on its argument
 * @param {Function} func
 * @returns {Function}
 */
export const callFunc = funcName => obj => obj[funcName]();

export const pluck = fieldName => obj => obj[fieldName];

export const pluckImm = (fieldName, ...args) => imm => imm.get(fieldName, ...args);

export const fetchJson = url => fetch(url, { credentials: 'same-origin' }).then(callFunc('json'));

export function debounce(cb, delay = 200) {
  let timeout = null;
  return () => {
    if (timeout !== null) clearTimeout(timeout);
    timeout = setTimeout(cb, delay);
  };
}

export const toK = number => number >= 1000 ?
  Math.round(number / 1000) + 'K' :
  number;

export const identity = _ => _;

/**
 * Takes two strings and an array of objects, returning on object whose keys are the values of the
 * first field and whose values are the values of the second field.
 * I guess an example would be more clear
 * fieldsToObj('field1', 'field2', [{
 *   field1: 'a',
 *   field2: '1'
 * }, {
 *   field1: 'b',
 *   field2: '2'
 * }])
 * // => {a: 1, b: 2}
 */
const fieldsToObj = (keyField, valueField, arr) => arr.reduce((obj, elem) => {
  obj[elem[keyField]] = elem[valueField];
  return obj;
}, {});

export const yearlyResponse2obj = fieldsToObj.bind(null, 'year');

export const monthlyResponse2obj = fieldsToObj.bind(null, 'month');

const shallowCompArr = (a, b) => a.every((el, index) => el === b[index]);

export const cacheFn = (fn) => {
  let lastArgs;
  let lastResult;
  return (...args) => {
    if (!lastArgs || !shallowCompArr(lastArgs, args)) {
      lastArgs = args;
      lastResult = fn(...args);
    }
    return lastResult;
  };
};

export const max = (a, b) => a > b ? a : b;

// takes and URI object and makes a POST request to it's base url and query as payload
export const send = url => fetch(url.clone().query(''), {
  method: 'POST',
  headers: {
    'Content-Type': 'application/x-www-form-urlencoded',
  },
  credentials: 'same-origin',
  body: url.query(),
});

export const isIE = navigator.appName === 'Microsoft Internet Explorer' || !!(navigator.userAgent.match(/Trident/)
    || navigator.userAgent.match(/rv 11/));

export const download = ({ ep, filters, years, months, t }) => {
  let url = new URI(`/api/ocds/${ep}`)
    .addSearch(filters.toJS())
    .addSearch('year', years.toArray())
    // this sin shall be atoned for in the future
    .addSearch('language', localStorage.oceLocale || 'en_US');

  if (years.count() === 1) {
    url = url.addSearch('month', months && months.toJS()).addSearch('monthly', true);
  }

  return send(url).then((response) => {
    const { userAgent } = navigator;
    const isSafari = userAgent.indexOf('Safari') > -1 && userAgent.indexOf('Chrom') === -1;// excludes both Chrome and Chromium

    if (isSafari || isIE) {
      location.href = url;
      return response;
    }
    const [, filename] = response.headers.get('Content-Disposition').split('filename=');
    response.blob().then((blob) => {
      const link = document.createElement('a');
      link.href = URL.createObjectURL(blob);
      link.download = filename;
      document.body.appendChild(link);
      link.click();
    });
    return response;
  }).catch(() => {
    alert(t('export:error'));
  });
};

export const shallowCopy = (original) => {
  const copy = {};
  Object.keys(original).forEach((key) => { copy[key] = original[key]; });
  return copy;
};

export const arrReplace = (a, b, [head, ...tail]) => typeof head === 'undefined' ?
  tail :
  [a === head ? b : head].concat(arrReplace(a, b, tail));

export const range = (from, to) => from > to ? [] : [from].concat(range(from + 1, to));

export const fetchEP = url => fetch(url.clone().query(''), {
  method: 'POST',
  headers: {
    'Content-Type': 'application/x-www-form-urlencoded',
  },
  credentials: 'same-origin',
  body: url.query(),
}).then(callFunc('json'));
