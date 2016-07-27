var translatable = Class => class Translatable extends Class{
  __(text){
    var translations = this.props.translations || {};
    if(!this.props.translations) console.error('Missing translations', this.constructor.name);
    return translations[text] || text;
  }

  __n(sg, pl, n){
    return n + " " + this.__(1 == n ? sg : pl);
  }
};

export default translatable;