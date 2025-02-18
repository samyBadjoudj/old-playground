package com.samy.bt.printer;

import com.samy.bt.printer.model.ParsableTree;
import com.samy.bt.printer.model.Position;
import com.samy.bt.printer.model.printable.BinaryTreeNode;
import com.samy.bt.printer.model.printable.Printable;

import java.util.*;
import java.util.stream.Collectors;

public class BinaryTreeNodeUtils {


    private static final int NUMBER_OF_CHAR_BOTTOM_TREE = 5;

    public static BinaryTreeNode buildTreeFromSortedArray(List<?> integers, Integer left, Integer right, BinaryTreeNode root) {
        final int middle = (right + left) / 2;
        if (left > right) {
            return null;
        }
        if (root == null) {
            root = new BinaryTreeNode(null, null, integers.get(middle) + "");
        }
        root.setLeft(buildTreeFromSortedArray(integers, left, middle - 1, root.getLeft()));
        root.setRight(buildTreeFromSortedArray(integers, middle + 1, right, root.getRight()));
        return root;
    }

    public static BinaryTreeNode buildTreeFromArray(List<Integer> integers, Integer current) {
        int left = (2 * current) + 1;
        int right = (2 * current) + 2;
        if (current >= integers.size()) {
            return null;
        }
        BinaryTreeNode root = new BinaryTreeNode(null, null, integers.get(current) + "");
        root.setLeft(buildTreeFromArray(integers, left));
        root.setRight(buildTreeFromArray(integers, right));
        return root;
    }

    public static Integer getHeight(BinaryTreeNode node) {
        if (Objects.isNull(node)) {
            return 0;
        }
        return Math.max(getHeight(node.getLeft()), getHeight(node.getRight())) + 1;
    }


    public static Integer getLevel(BinaryTreeNode currentNode, BinaryTreeNode toFind, Integer currentLevel) {
        if (Objects.isNull(currentNode)) {
            return 0;
        }
        if (currentNode.equals(toFind)) {
            return currentLevel;
        }
        Integer leftLevel = getLevel(currentNode.getLeft(), toFind, currentLevel + 1);
        if (leftLevel != 0) {
            return leftLevel;
        }
        Integer rightLevel = getLevel(currentNode.getRight(), toFind, currentLevel + 1);
        if (rightLevel != 0) {
            return rightLevel;
        }
        return 0;
    }

    public static Map<Integer, List<BinaryTreeNode>> getBinaryNodesByLevel(BinaryTreeNode root) {
        Queue<BinaryTreeNode> queue = new ArrayDeque<>();
        queue.add(root);
        Map<Integer, List<BinaryTreeNode>> binaryNodeByLevel = new HashMap<>();
        while (!queue.isEmpty()) {
            final BinaryTreeNode currentNode = queue.poll();
            final Integer level = getLevel(root, currentNode, 0);
            final List<BinaryTreeNode> nodeAtLevel = binaryNodeByLevel.getOrDefault(level, new ArrayList<>());
            nodeAtLevel.add(currentNode);
            binaryNodeByLevel.put(level, nodeAtLevel);
            if (Objects.nonNull(currentNode.getLeft())) {
                queue.add(currentNode.getLeft());
            }
            if (Objects.nonNull(currentNode.getRight())) {
                queue.add(currentNode.getRight());
            }
        }
        return binaryNodeByLevel;
    }

    public static Integer getMaxWidth(BinaryTreeNode node) {
        return getBinaryNodesByLevel(node).values().stream().map(List::size).max(Integer::compareTo).orElse(0);
    }


    public static String getStringPrintableFromPrintableByPosition(final Map<Position, Printable<String>> positionPrintableMap, String returnCarriage, String space) {
        final Map<Integer, List<Position>> positionsByLine = positionPrintableMap.keySet().stream().collect(Collectors.groupingBy(Position::getRow));
        final List<Integer> lines = positionsByLine.keySet().stream().sorted().collect(Collectors.toList());
        StringBuilder content = new StringBuilder();
        lines.forEach(currentLine -> {
            final List<Position> positionAtGivenRow =
                    positionsByLine.get(currentLine).stream().sorted(Comparator.comparing(Position::getColumn)).collect(Collectors.toList());
            content.append(buildStringWithPositionValue(positionAtGivenRow, positionPrintableMap, space)).append(returnCarriage);
        });

        return content.toString();

    }

    public static Map<Position, Printable<String>> getStringByPosition(BinaryTreeNode root, Printable<String> leftVertex, Printable<String> rightVertex) {
        Queue<BinaryTreeNode> queue = new ArrayDeque<>();
        queue.add(root);
        Integer height = getHeight(root);
        int maxLengthBottomTree = (int) Math.pow(2, height - 1) * NUMBER_OF_CHAR_BOTTOM_TREE;
        Map<Printable<String>, Position> printingPosition = new HashMap<>();
        Map<Position, Printable<String>> vertices = new HashMap<>();
        printingPosition.put(root, new Position(0, maxLengthBottomTree));
        while (!queue.isEmpty()) {
            final BinaryTreeNode currentNode = queue.poll();
            Position parentColumnPosition = printingPosition.get(currentNode);
            if (Objects.nonNull(currentNode.getLeft())) {
                queue.add(currentNode.getLeft());
                final Integer currentLeftChildLevel = getLevel(root, currentNode.getLeft(), 0);
                int balancedDelta = getRebalancingModulo(currentLeftChildLevel);
                Integer numberLineBetweenLevels = height - currentLeftChildLevel + 1;
                final int line = parentColumnPosition.getRow() + numberLineBetweenLevels;
                int offset = getOffsetBetweenParentAndCurrentNodes(maxLengthBottomTree, currentLeftChildLevel);
                Position leftChildPosition = new Position(line, (
                        parentColumnPosition.getColumn()
                                - currentNode.getLeft().getPrintableOffset()
                                + balancedDelta
                                - (offset)));
                vertices.putAll(getVercticesWithPosition(parentColumnPosition.getRow(),
                        line,
                        parentColumnPosition.getColumn(),
                        leftChildPosition.getColumn(),
                        leftVertex));
                printingPosition.put(currentNode.getLeft(), leftChildPosition);
            }
            if (Objects.nonNull(currentNode.getRight())) {
                queue.add(currentNode.getRight());
                final Integer currentRightChildLevel = getLevel(root, currentNode.getRight(), 0);
                int offset = getOffsetBetweenParentAndCurrentNodes(maxLengthBottomTree, currentRightChildLevel);
                Integer numberLineBetweenLevels = height - currentRightChildLevel + 1;
                int deltaModulo = getRebalancingModulo(currentRightChildLevel);
                final int line = parentColumnPosition.getRow() + numberLineBetweenLevels;
                Position rightChildPosition = new Position(line, (
                        parentColumnPosition.getColumn()
                                - currentNode.getRight().getPrintableOffset() - deltaModulo
                                + (offset)));

                vertices.putAll(getVercticesWithPosition(parentColumnPosition.getRow(), line,
                        parentColumnPosition.getColumn(),
                        rightChildPosition.getColumn(),
                        rightVertex));

                printingPosition.put(currentNode.getRight(), rightChildPosition);
            }

        }
        Map<Position, Printable<String>> reversed = printingPosition.entrySet().stream().collect(Collectors.toMap(Map.Entry::getValue, Map.Entry::getKey));
        reversed.putAll(vertices);
        return reversed;

    }

    private static int getRebalancingModulo(Integer currentLeftChild) {
        return ((currentLeftChild + 1) % 2) == 0 ? 1 : -1;
    }

    private static int getOffsetBetweenParentAndCurrentNodes(int starting, Integer level) {
        return (int) (starting / (Math.pow(2, level)));
    }

    private static Map<? extends Position, ? extends Printable<String>> getVercticesWithPosition(Integer fromLine, int toLine, Integer fromColumn, Integer toColumn, Printable<String> vertex) {
        Map<Position, Printable<String>> edges = new HashMap<>();
        int numberOfStepShifts = 1;
        int step;
        if (fromColumn < toColumn) {
            step = (toColumn - fromColumn) / (toLine - fromLine);
        } else {
            step = -(fromColumn - toColumn) / (toLine - fromLine);
        }
        for (int i = fromLine + 1; i < toLine; i++) {
            edges.put(new Position(i, fromColumn + (step * numberOfStepShifts)), vertex);
            numberOfStepShifts++;
        }
        return edges;
    }

    public static String buildStringWithPositionValue(List<Position> positions, Map<Position, Printable<String>> mapOfNodes, String space) {
        StringBuilder line = new StringBuilder();
        Position lastElementPositioned = null;
        int lastElementContentLength = 0;
        for (Position currentPosition : positions) {
            final int offset = Objects.isNull(lastElementPositioned) ? currentPosition.getColumn() : currentPosition.getColumn() - (lastElementPositioned.getColumn() + lastElementContentLength);
            addSpace(line, offset, space);
            line.append(mapOfNodes.get(currentPosition).getPrintableValue());
            lastElementPositioned = currentPosition;
            lastElementContentLength = mapOfNodes.get(currentPosition).getPrintableLengthValue();
        }
        return line.toString();
    }

    private static void addSpace(StringBuilder line, Integer column, String space) {
        for (int i = 0; i < column; i++) {
            line.append(space);
        }
    }


    public static BinaryTreeNode getBinaryTreeFromString(ParsableTree pt) {
        BinaryTreeNode root = new BinaryTreeNode(null, null, null);
        if (pt.getCurrentChar() != '(') {
            String intVal = parseValue(pt);
            root.setValue(intVal);
        }

        BinaryTreeNode leftNode = null, rightNode = null;
        if (pt.parsingNotFinished() && pt.getCurrentChar() == '(') {
            pt.shiftRight();
            leftNode = getBinaryTreeFromString(pt);
        }
        if (pt.parsingNotFinished() && pt.getCurrentChar() == '(') {
            pt.shiftRight();
            rightNode = getBinaryTreeFromString(pt);
        }
        root.setLeft(leftNode);
        root.setRight(rightNode);
        pt.shiftRight();
        return root;
    }

    private static String parseValue(ParsableTree pt) {
        StringBuilder sb = new StringBuilder();
        while (pt.parsingNotFinished()) {
            if (pt.getCurrentChar() == '(' || pt.getCurrentChar() == ')')
                break;
            sb.append(pt.getCurrentChar());
            pt.shiftRight();
        }
        return sb.toString();
    }
}
