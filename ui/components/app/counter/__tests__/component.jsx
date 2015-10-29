jest.setMock('../style.less', "");
jest.dontMock('../index.jsx');
var Counter = require('../index.jsx');
var React = require('react/addons');
var {TestUtils} = React.addons;

describe("counter", () => {
  it("should display the year correctly", () => {
    var mockValue = 5;
    var counter = TestUtils.renderIntoDocument(<Counter value={mockValue}/>);
    expect(React.findDOMNode(counter).textContent.trim()).toEqual('5');
  });

  it("should increment the counter", () => {
    var mockActions = {
      incCounter: jest.genMockFn()
    };
    var counter = TestUtils.renderIntoDocument(<Counter actions={mockActions}/>);
    var plus = TestUtils.findRenderedDOMComponentWithClass(counter, 'glyphicon-plus');
    TestUtils.Simulate.click(plus);
    expect(mockActions.incCounter).toBeCalled();
  });

  it("should decrement the counter", () => {
    var mockActions = {
      decCounter: jest.genMockFn()
    };
    var counter = TestUtils.renderIntoDocument(<Counter actions={mockActions}/>);
    var minus = TestUtils.findRenderedDOMComponentWithClass(counter, 'glyphicon-minus');
    TestUtils.Simulate.click(minus);
    expect(mockActions.decCounter).toBeCalled();
  });
});