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
  >
    {content}
  </a>
);
