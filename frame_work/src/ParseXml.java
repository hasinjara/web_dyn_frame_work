package parser;

import java.io.File;
import java.util.Vector;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;

public class ParseXml {

    public Vector parse(String name_file, String parent_balise, String[] child_balise) throws Exception{
        Vector val = new Vector<>();
        try {
            File inputFile = new File(name_file);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(inputFile);
            doc.getDocumentElement().normalize();
            System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
            NodeList nList = doc.getElementsByTagName(parent_balise);
            System.out.println("----------------------------");
            for (int temp = 0; temp < nList.getLength(); temp++) {
                Node nNode = nList.item(temp);
                System.out.println("\nCurrent Element :" + nNode.getNodeName());
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
                    for (int i = 0; i < child_balise.length; i++) {
                        val.add(eElement.getElementsByTagName(child_balise[i]).item(0).getTextContent());
                    }
                    // System.out.println("Student roll no : " + eElement.getAttribute("rollno"));
                    // System.out.println("First Name : " + eElement.getElementsByTagName("firstname").item(0).getTextContent());
                    // System.out.println("Last Name : "+ eElement.getElementsByTagName("lastname").item(0).getTextContent());
                    // System.out.println("Nick Name : " + eElement.getElementsByTagName("nickname").item(0).getTextContent());
                    // System.out.println("Marks : " + eElement.getElementsByTagName("marks").item(0).getTextContent());
                }
            }
        } catch (Exception e) {
            // TODO: handle exception
            throw e;
        }
    return val;
    }

    public Vector getAttribute(String name_file, String balise) throws Exception{
        Vector val = new Vector<>();
        try {
            File inputFile = new File(name_file);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(inputFile);
            doc.getDocumentElement().normalize();
            System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
            NodeList nList = doc.getElementsByTagName(balise);
            System.out.println("----------------------------");
            for (int temp = 0; temp < nList.getLength(); temp++) {
                Node nNode = nList.item(temp);
                System.out.println("\nCurrent Element :" + nNode.getNodeName());
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
                    eElement.getAttribute(balise);
                    // System.out.println("Student roll no : " + eElement.getAttribute("rollno"));
                    // System.out.println("First Name : " + eElement.getElementsByTagName("firstname").item(0).getTextContent());
                    // System.out.println("Last Name : "+ eElement.getElementsByTagName("lastname").item(0).getTextContent());
                    // System.out.println("Nick Name : " + eElement.getElementsByTagName("nickname").item(0).getTextContent());
                    // System.out.println("Marks : " + eElement.getElementsByTagName("marks").item(0).getTextContent());
                }
            }
        } catch (Exception e) {
            // TODO: handle exception
            throw e;
        }
    return val;
    }
} 