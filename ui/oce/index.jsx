import React from "react";

export default class OCApp extends React.Component{
  constructor(props, config){
    super(props);
    this.config = config;
  }

  __(text){
    return text;
  }

  filters(){
    return <h1>filters</h1>
  }

  comparison(){
    return <h1>comparison</h1>
  }

  navigation(){
    return <h1>navigation</h1>
  }

  content(){
    return <h1>content</h1>
  }

  yearsBar(){
    return <h1>years bar</h1>
  }
}