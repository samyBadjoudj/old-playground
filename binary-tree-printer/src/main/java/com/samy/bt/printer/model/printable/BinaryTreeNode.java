package com.samy.bt.printer.model.printable;

import java.util.Objects;

public class BinaryTreeNode implements Printable<String> {

    private BinaryTreeNode left;
    private BinaryTreeNode right;
    private String value;

    public BinaryTreeNode(BinaryTreeNode left, BinaryTreeNode right, String value) {
        this.left = left;
        this.right = right;
        this.value = value;
    }

    public BinaryTreeNode getLeft() {
        return left;
    }

    public void setLeft(BinaryTreeNode left) {
        this.left = left;
    }

    public BinaryTreeNode getRight() {
        return right;
    }

    public void setRight(BinaryTreeNode right) {
        this.right = right;
    }

    public String getValue() {
        return value;
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BinaryTreeNode that = (BinaryTreeNode) o;
        return Objects.equals(left, that.left) &&
                Objects.equals(right, that.right) &&
                Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(left, right, value);
    }

    @Override
    public String toString() {
        return "BinaryTreeNode{" +
                "left=" + left +
                ", right=" + right +
                ", value='" + value + '\'' +
                '}';
    }

    @Override
    public String getPrintableValue() {
        return value;
    }

    @Override
    public int getPrintableOffset() {
        return (int) Math.ceil(value.length()/2.0);
    }
    @Override
    public int getPrintableLengthValue(){
        return value.length();
    }
    public void setValue(String value) {
        this.value = value;
    }
}
