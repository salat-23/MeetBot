package com.salat23.bot.botapi.message_tools;

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
import java.util.Arrays;
import java.util.List;

@Component
public class XMLResponseParser implements IResponseResourceProvider {

    private static List<MessageResource> staticResources;

    @Value("${bot.config_file_path}")
    private String configurationFilePath;

    private static final String RESPONSE_TAG_NAME = "Response";
    private static final String RESPONSE_TYPE_NAME = "type";
    private static final String BOTH_TAG_NAME = "Any";
    private static final String MALE_TAG_NAME = "Male";
    private static final String FEMALE_TAG_NAME = "Female";

    @Override
    public List<MessageResource> getMessageResources() {
        if (staticResources != null) return staticResources;
        staticResources = new ArrayList<>();

        try {
            //Instantiate a factory and get xml document
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = dbf.newDocumentBuilder();

            Document document = documentBuilder.parse(new File(configurationFilePath));
            //Normalize the document (http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work)
            document.getDocumentElement().normalize();

            NodeList responses = document.getElementsByTagName(RESPONSE_TAG_NAME);

            for (int i = 0; i < responses.getLength(); i++) {

                Node node = responses.item(i);
                if (node.getNodeType() != Node.ELEMENT_NODE) continue;

                Element response = (Element) node;

                //Get the value of attribute "type"
                String responseTypeName = response.getAttribute(RESPONSE_TYPE_NAME);
                //Get all the text variations
                String both = null, male = null, female = null;
                if (response.getElementsByTagName(BOTH_TAG_NAME).item(0) != null)
                    both = response.getElementsByTagName(BOTH_TAG_NAME).item(0).getTextContent();
                if (response.getElementsByTagName(MALE_TAG_NAME).item(0) != null)
                    male = response.getElementsByTagName(MALE_TAG_NAME).item(0).getTextContent();
                if (response.getElementsByTagName(FEMALE_TAG_NAME).item(0) != null)
                    female = response.getElementsByTagName(FEMALE_TAG_NAME).item(0).getTextContent();

                //Throw exception if the both tag is not present
                if (both == null) throw new RuntimeException("The both tag is not present");

                //Filter out only appropriate types and get the first one with this short name
                ResponseTemplateTypes responseType = Arrays.stream(ResponseTemplateTypes.values()).filter(
                                type ->
                                        type.getShortName().equalsIgnoreCase(responseTypeName))
                        .findFirst().orElseThrow(() ->
                                new RuntimeException(
                                        String.format("The type %s is not present", responseTypeName)));

                MessageResource messageResource = new MessageResource(responseType, both, male, female);

                //Add the resource to list
                staticResources.add(messageResource);
            }
            return staticResources;

        } catch (ParserConfigurationException | IOException | SAXException e) {
            e.printStackTrace();
        }
        return null;
    }
}