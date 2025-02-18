export default function reduce(state = {}, action) {
  switch (action.type) {
    case 'BINARY_TREE_PATTERN_VALUE_CHANGED':
      return Object.assign({}, state, {
        binaryTreePattern: action.binaryTreePattern
      })
    case 'COMMA_SEPARATED_VALUE_CHANGED':
      return Object.assign({}, state, {
        commaSepValue: action.commaSepValue
      })
    case 'BT_RECEIVED':
        return Object.assign({}, state, {
          htmlContent: action.htmlContent
        })
    default:
      return state
  }
}
