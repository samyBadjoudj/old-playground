import { put, takeEvery, all } from 'redux-saga/effects'


function* watchfetchBTFromArray() {
  yield takeEvery('SEND_COMMA_SEPARATED_VALUE', fetchBTFromArray)
}
function* fetchBTFromArray(action) {  
  const htmlContent = yield fetch('http://localhost:8080/printBstFromArrayString?content='+action.commaSepValue)
        .then(response => response.text());
  yield put({ type: 'BT_RECEIVED', htmlContent: htmlContent });
}

function* watchfetchBTFromPattern() {
  yield takeEvery('SEND_BINARY_TREE_PATTERN_VALUE', fetchBTFromPattern)
}
function* fetchBTFromPattern(action) {  
  const htmlContent = yield fetch('http://localhost:8080/printBtFromString?content='+action.binaryTreePattern)
        .then(response => response.text());
  yield put({ type: 'BT_RECEIVED', htmlContent: htmlContent });
}


export default function* rootSaga() {
  yield all([
    watchfetchBTFromPattern(),
    watchfetchBTFromArray()
  ])
}