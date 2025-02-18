/*eslint-disable no-unused-vars */
import React, { Component } from 'react'
import { sendComaSeparatedValue, sendBinaryTreePatternValue, comaSeparatedValueChanged,binaryTreePatternValueChanged } from '../actions/BinaryTreeActionCreators'
import { connect } from 'react-redux'
import { bindActionCreators } from 'redux'
import PropTypes from 'prop-types'

class BinaryTreeDisplayContainer extends Component {

  constructor(props) {
    super(props);
  }


  render() {
    return <div>
      <button onClick={()=>this.props.sendBinaryTreePatternValue(this.props.binaryTreePattern)}>
        Get Binary tree from server with pattern
      </button>
      <input type='text' name='binaryTreePattern' value={this.props.binaryTreePattern} 
                  onChange={(e)=>this.props.binaryTreePatternValueChanged(e.target.value)} ></input>
                  <span>ex: 4(2(3)(1))(6(5)(7(9)))</span>
      <hr />
      <button onClick={()=>this.props.sendComaSeparatedValue(this.props.commaSepValue)}>
        Get Binary tree from server with pattern
      </button>
      <input type='text' name='commaSepValue' value={this.props.commaSepValue} 
                  onChange={(e)=>this.props.comaSeparatedValueChanged(e.target.value)} ></input><span> ex:2,8,9,3,6,-1</span>
      <hr />
      <div>
        {/*Here can be an XSS attack, just for example purpose*/}
        <div><pre>{this.props.htmlContent}</pre></div> 
      </div>
    </div>

  }

}


BinaryTreeDisplayContainer.propTypes = {
  htmlContent: PropTypes.string,
  commaSepValue: PropTypes.string,
  binaryTreePattern: PropTypes.string,

}

const  mapStateToProps=(state)=> (
  {
    htmlContent: state.htmlContent || '',    
    commaSepValue: state.commaSepValue || '',
    binaryTreePattern: state.binaryTreePattern || ''
  } 
  
)

const mapDispatchToProps = (dispatch)=>{
  return {  sendComaSeparatedValue: bindActionCreators(sendComaSeparatedValue, dispatch), 
            sendBinaryTreePatternValue: bindActionCreators(sendBinaryTreePatternValue, dispatch),
            comaSeparatedValueChanged: bindActionCreators(comaSeparatedValueChanged, dispatch),
            binaryTreePatternValueChanged: bindActionCreators(binaryTreePatternValueChanged, dispatch)
          }
}


export default connect(mapStateToProps, mapDispatchToProps)(BinaryTreeDisplayContainer)

