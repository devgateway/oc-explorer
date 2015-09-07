jest.setMock('../style.less', "");
jest.dontMock('../index.jsx');
var Counter = require('../index.jsx');
var React = require('react/addons');
var {TestUtils} = React.addons;

describe("counter", () => {
  it("display the year correctly", () => {
    var mockValue = 5;
    var counter = TestUtils.renderIntoDocument(<Counter value={mockValue}/>);
    expect(React.findDOMNode(counter).textContent.trim()).toEqual('5');
  })
});