const translatable = Class => class Translatable extends Class {
  __(text) {
    const translations = this.props.translations || {};
    return translations[text] || text;
  }

  __n(sg, pl, n) {
    const nr = 1 === n ? sg : pl;
    return `${n} ${nr}`;
  }
};

export default translatable;
