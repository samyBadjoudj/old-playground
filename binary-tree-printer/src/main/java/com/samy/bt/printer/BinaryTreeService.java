package com.samy.bt.printer;

import com.samy.bt.printer.model.ParsableTree;
import com.samy.bt.printer.model.Position;
import com.samy.bt.printer.model.printable.BinaryTreeNode;
import com.samy.bt.printer.model.printable.Printable;
import com.samy.bt.printer.model.printable.console.LefVertexConsole;
import com.samy.bt.printer.model.printable.console.RightVertexConsole;
import com.samy.bt.printer.model.printable.html.LeftVertexRawString;
import com.samy.bt.printer.model.printable.html.RightVertexRawString;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
public class BinaryTreeService {

    public String getRawStringBinaryTreeRepresentationFromIntegerList(List<?> values){
        if(Objects.isNull(values) || values.isEmpty()){
            return Strings.EMPTY;
        }
        BinaryTreeNode binaryTreeNode = BinaryTreeNodeUtils.buildTreeFromSortedArray(values, 0, values.size()-1, null);
        final Map<Position, Printable<String>> positionBinaryTreeNodeMap = BinaryTreeNodeUtils.getStringByPosition(binaryTreeNode,new LeftVertexRawString(),new RightVertexRawString());
        return BinaryTreeNodeUtils.getStringPrintableFromPrintableByPosition(positionBinaryTreeNodeMap, System.lineSeparator()," ");
    }

    public String getConsoleStringBinaryTreeRepresentationFromIntegerList(List<?> values){
        if(Objects.isNull(values) || values.isEmpty()){
            return Strings.EMPTY;
        }
        BinaryTreeNode binaryTreeNode = BinaryTreeNodeUtils.buildTreeFromSortedArray(values, 0, values.size()-1, null);
        final Map<Position, Printable<String>> positionBinaryTreeNodeMap2= BinaryTreeNodeUtils.getStringByPosition(binaryTreeNode,new LefVertexConsole(),new RightVertexConsole());
        return BinaryTreeNodeUtils.getStringPrintableFromPrintableByPosition(positionBinaryTreeNodeMap2, System.lineSeparator()," ");
    }


    public String getRawStringBinaryRepresentationFromParsableContent(String content){
        if(Objects.isNull(content) || content.isEmpty()){
            return Strings.EMPTY;
        }
        BinaryTreeNode binaryTreeNode = BinaryTreeNodeUtils.getBinaryTreeFromString(new ParsableTree(content));
        final Map<Position, Printable<String>> positionBinaryTreeNodeMap = BinaryTreeNodeUtils.getStringByPosition(binaryTreeNode,new LeftVertexRawString(),new RightVertexRawString());
        return BinaryTreeNodeUtils.getStringPrintableFromPrintableByPosition(positionBinaryTreeNodeMap, System.lineSeparator()," ");
    }

    public String getStringBinaryRepresentationFromParsableContent(String content){
        if(Objects.isNull(content) || content.isEmpty()){
            return Strings.EMPTY;
        }
        BinaryTreeNode binaryTreeNode = BinaryTreeNodeUtils.getBinaryTreeFromString(new ParsableTree(content));
        final Map<Position, Printable<String>> positionBinaryTreeNodeMap = BinaryTreeNodeUtils.getStringByPosition(binaryTreeNode,new LefVertexConsole(),new RightVertexConsole());
        return BinaryTreeNodeUtils.getStringPrintableFromPrintableByPosition(positionBinaryTreeNodeMap, System.lineSeparator()," ");
    }




}
