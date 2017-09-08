var translatable = Class => class Translatable extends Class {
  __(text) {
    console.warn('__ is deprecated, use t');
    var translations = this.props.translations || {};
    return translations[text] || text;
  }

  __n(sg, pl, n) {
    console.warn('__n is deprecated, use t_n');
    return n + " " + this.__(1 === n ? sg : pl);
  }

  t(key) {
    const { translations, t } = this.props;
    if (typeof t === 'function') {
      return t(key);
    }
    console.warn('Passing translation objects is deprecated!');
    if(!translations) return key;
    if(!translations) console.error('Missing translations', this.constructor.name);
    if(!translations[key]) console.error('Missing translation for key', key);
    return translations[key];
  }

  t_n(sg, pl, n) {
    return n + " " + this.t(1 == n ? sg : pl);
  }
};

export default translatable;
