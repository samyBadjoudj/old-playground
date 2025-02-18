package company.graphic;

import company.util.ImageManipulation;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SpriteSheetXml {

   public Map<String, BufferedImage> sprites = new HashMap<>();

    public SpriteSheetXml(File xml, BufferedImage tileSheet) throws IOException {
        Document dom;
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

            DocumentBuilder db = null;
            try {
                db = dbf.newDocumentBuilder();
                dom = db.parse(xml);
                NodeList childNodes = dom.getElementsByTagName("SubTexture");
                for (int i = 0; i< childNodes.getLength(); i++) {
                    Node item = childNodes.item(i);
                    int width = Integer.parseInt(item.getAttributes().getNamedItem("width").getNodeValue());
                    int height = Integer.parseInt(item.getAttributes().getNamedItem("height").getNodeValue());
                    int positionX = Integer.parseInt(item.getAttributes().getNamedItem("x").getNodeValue());
                    int positionY = Integer.parseInt(item.getAttributes().getNamedItem("y").getNodeValue());
                    String name = item.getAttributes().getNamedItem("name").getNodeValue();

                    sprites.put(name, tileSheet.getSubimage(positionX, positionY, width, height));
                    sprites.put(name + "vm", ImageManipulation.flipHorizontally(tileSheet.getSubimage(positionX, positionY, width, height)));
                }
            } catch (ParserConfigurationException | SAXException e) {
                e.printStackTrace();
            }


    }

}
