import styles from './style.less';

class TwoRowsCenterText extends React.Component {
  render() {
    const { data } = this.props;
    const [fst, snd] = data;
    const sum = fst + snd;
    const percent = (fst / sum) * 100;
    return (
      <div className="center-text two-rows">
        <div>
          {fst}
          <div className="secondary">
            of {sum} ({Math.trunc(percent)}%)
          </div>
        </div>
      </div>
    );
  }
}

export default TwoRowsCenterText;
