package com.salat23.bot.botapi.utility;

import com.salat23.bot.botapi.utility.IConfirmationUtility;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class XMLConfirmationParser implements IConfirmationUtility {

    private static List<String> positive;
    private static List<String> negative;

    private static final String KEYWORD_TAG_NAME = "Keyword";
    private static final String KEYWORD_TYPE_NAME = "type";
    private static final String KEYWORD_TYPE_YES = "yes";
    private static final String KEYWORD_TYPE_NO = "no";

    @Value("${bot.confirmation_file_path}")
    private String configurationFilePath;

    private void initialize() {
        positive = new ArrayList<>();
        negative = new ArrayList<>();

        try {
            //Instantiate a factory and get xml document
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = dbf.newDocumentBuilder();

            Document document = documentBuilder.parse(new File(configurationFilePath));
            //Normalize the document (http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work)
            document.getDocumentElement().normalize();

            NodeList responses = document.getElementsByTagName(KEYWORD_TAG_NAME);

            for (int i = 0; i < responses.getLength(); i++) {
                Node node = responses.item(i);
                if (node.getNodeType() != Node.ELEMENT_NODE) continue;

                Element response = (Element) node;
                String keywordType = response.getAttribute(KEYWORD_TYPE_NAME);
                if (keywordType.equals(KEYWORD_TYPE_YES))
                    positive.add(response.getTextContent());
                else if (keywordType.equals(KEYWORD_TYPE_NO))
                    negative.add(response.getTextContent());
            }
        } catch (IOException | ParserConfigurationException | SAXException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Boolean doesConfirm(String text) {
        if (negative == null || positive == null) initialize();

        if (positive.contains(text.toLowerCase())) return true;
        else if (negative.contains(text.toLowerCase())) return false;

        return false;
    }
}
