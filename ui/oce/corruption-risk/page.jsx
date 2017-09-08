import translatable from '../translatable';

class Page extends translatable(React.Component){
  scrollTop(){
    window.scrollTo(0, 0);
  }

  componentDidMount(){
    this.scrollTop();
  }
}

export default Page;
