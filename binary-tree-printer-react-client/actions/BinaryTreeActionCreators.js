export function sendComaSeparatedValue(commaSepValue) {
    return {
      type: 'SEND_COMMA_SEPARATED_VALUE',
      commaSepValue
    }
  }
  export function sendBinaryTreePatternValue(binaryTreePattern) {
    return {
      type: 'SEND_BINARY_TREE_PATTERN_VALUE',
      binaryTreePattern
    }
  }
  export function comaSeparatedValueChanged(commaSepValue) {
    return {
      type: 'COMMA_SEPARATED_VALUE_CHANGED',
      commaSepValue
    }
  }
  export function binaryTreePatternValueChanged(binaryTreePattern) {
    return {
      type: 'BINARY_TREE_PATTERN_VALUE_CHANGED',
      binaryTreePattern
    }
  }