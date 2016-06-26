jest.setMock('../style.less', "");
jest.dontMock('../index.jsx');
let Counter = require('../index.jsx').default;
let React = require('react');
let ReactDOM = require('react-dom');
let TestUtils = require('react-addons-test-utils');

describe("counter", () => {
  it("should display the value correctly", () => {
    let mockValue = 5;
    let counter = TestUtils.renderIntoDocument(<Counter value={mockValue}/>);
    expect(ReactDOM.findDOMNode(counter).textContent.trim()).toEqual('5');
  });

  it("should increment the counter", () => {
    let mockActions = {
      incCounter: jest.genMockFn()
    };
    let counter = TestUtils.renderIntoDocument(<Counter actions={mockActions}/>);
    let plus = TestUtils.findRenderedDOMComponentWithClass(counter, 'glyphicon-plus');
    TestUtils.Simulate.click(plus);
    expect(mockActions.incCounter).toBeCalled();
  });

  it("should decrement the counter", () => {
    let mockActions = {
      decCounter: jest.genMockFn()
    };
    let counter = TestUtils.renderIntoDocument(<Counter actions={mockActions}/>);
    let minus = TestUtils.findRenderedDOMComponentWithClass(counter, 'glyphicon-minus');
    TestUtils.Simulate.click(minus);
    expect(mockActions.decCounter).toBeCalled();
  });
});