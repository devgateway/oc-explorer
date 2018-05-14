import { List } from 'immutable';

// copypasted from https://www.sitepoint.com/javascript-generate-lighter-darker-color/
export function colorLuminance(hex, lum) {

  // validate hex string
  hex = String(hex).replace(/[^0-9a-f]/gi, '');
  if (hex.length < 6) {
    hex = hex[0]+hex[0]+hex[1]+hex[1]+hex[2]+hex[2];
  }
  lum = lum || 0;

  // convert to decimal and change luminosity
  var rgb = "#", c, i;
  for (i = 0; i < 3; i++) {
    c = parseInt(hex.substr(i*2,2), 16);
    c = Math.round(Math.min(Math.max(0, c + (c * lum)), 255)).toString(16);
    rgb += ("00"+c).substr(c.length);
  }

  return rgb;
}

export const getAwardAmount = (contract) => {
  const winningAward = contract.get('awards', List()).find(award => award.get('status') === 'active');
  if (winningAward) {
    const amount = winningAward.getIn(['value', 'amount'], 'N/A');
    const currency = winningAward.getIn(['value', 'currency'], '');
    return `${amount} ${currency}`;
  }
  return 'N/A';
};

export const mkContractLink = navigate => (content, { id }) => (
  <a
    href={`#!/crd/contract/${id}`}
    onClick={() => navigate('contract', id)}
    className="oce-3-line-text"
  >
    {content}
  </a>
);

const ROUTINE_PROPS = ['filters', 'years', 'months', 'monthly', 'translations', 'width']

export const copyProps = (keys, source, target) =>
  keys.forEach(key => target[key] = source[key]);

export function cherrypickProps(keys, source) {
  const target = {};
  copyProps(keys, source, target);
  return target;
}

export function wireProps(parent, _prefix) {
  const { props: parentProps } = parent;
  const props = cherrypickProps(ROUTINE_PROPS, parentProps);
  if (_prefix) {
    const prefix = Array.isArray(_prefix) ? _prefix : [_prefix];
    props.data = parentProps.data.getIn(prefix);
    props.requestNewData = (path, data) =>
      parentProps.requestNewData(path.concat(prefix), data);
  } else {
    copyProps(['data', 'requestNewData'], parentProps, props);
  }
  return props;
}

export const _3LineText = content => <div className="oce-3-line-text">{content}</div>

export const cloneChild = (component, props) =>
  React.cloneElement(
    React.Children.only(component.props.children),
    props
  );

export const sortByField = field => (a, b) => a.get(field) - b.get(field);
