import "babel-polyfill"

import React from 'react'
import ReactDOM from 'react-dom'
import { createStore, applyMiddleware } from 'redux'
import createSagaMiddleware from 'redux-saga'
import BinaryTreeDisplayContainer from './components/BinaryTreeDisplayContainer'
import reducer from './reducers/reducers'
import rootSaga from './sagas/sagas'
import { Provider } from 'react-redux'


const sagaMiddleware = createSagaMiddleware()
const store = createStore(
  reducer,
  applyMiddleware(sagaMiddleware)
)
sagaMiddleware.run(rootSaga)

function render() {
  ReactDOM.render(
    <Provider store={store}>
      <BinaryTreeDisplayContainer />
    </Provider>,
    document.getElementById('root')
  )
}

render()
store.subscribe(render)
