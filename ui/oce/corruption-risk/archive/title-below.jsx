const TitleBelow = ({ title, children, filters, ...props }) => (
  <div>
    {React.cloneElement(
       React.Children.only(children)
       , props)}
    <h4 className="title">
      <button className="btn btn-default btn-sm zoom-button">
        <i className="glyphicon glyphicon-fullscreen" style={{ pointerEvents: 'none' }}/>
      </button>
      &nbsp;
      {title}
    </h4>
  </div>
);

export default TitleBelow;
