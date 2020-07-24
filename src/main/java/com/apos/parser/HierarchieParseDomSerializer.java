package com.apos.parser;

import java.util.Arrays;
import java.util.Comparator;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * This class serialized DOM Node to a String
 *
 * @author gillesP
 *
 */
public class HierarchieParseDomSerializer {

  private static final boolean _NO_CHILDS_IN_ATTR_ = false; // Only Crimson has no childs in Attr Node.

  /**
   * Main method to serilized the node a string, with or without the xml header (<?xml version=\"1.0\"?>)
   * @param node the DOM node to be serialized
   * @param withoutHeader Without xml header
   * @return the string containing the XML serialization
   */
  public static String writeToString(Node node, boolean withoutHeader,boolean escapeTextNodeContent) {
    StringBuffer resu = new StringBuffer();
    if (!withoutHeader)
      resu.append("<?xml version=\"1.0\"?>\n");
    appendToStringBuffer(resu, node, false, false, false,escapeTextNodeContent,false);
    return resu.toString();
  }
  public static String writeToString(Node node, boolean withoutHeader,boolean escapeTextNodeContent,boolean sorted) {
    StringBuffer resu = new StringBuffer();
    if (!withoutHeader)
      resu.append("<?xml version=\"1.0\"?>\n");
    appendToStringBuffer(resu, node, false, false, false,escapeTextNodeContent,sorted);
    return resu.toString();
  }

  /**
   * Main method to serilized the node a string, with or without the xml header (<?xml version=\"1.0\"?>)
   * @param node the DOM node to be serialized
   * @param withoutCDATA will remove the CDATA in the generation to keep only text
   * @param ignoreWhiteSpace will remove text without CDATA to ignore XML formating information 
   * @return the string containing the XML serialization
   */
  public static String writeContentToString(Node node, boolean withoutCDATA, boolean ignoreWhiteSpace) {
    StringBuffer resu = new StringBuffer();
    appendToStringBuffer(resu, node, true, withoutCDATA, ignoreWhiteSpace,false,false);
    return resu.toString();
  }

  /**
   * Append the serialization of the current node to the result.
   * This function is called recursively with the children of the of the node
   * @param resu the result of the node serialization is append in the resu StringBuffer
   * @param node the node to be serilized
   */
  private static void appendToStringBuffer(StringBuffer resu, Node node, boolean onlyChidren, boolean removeCDataMark, boolean ignoreWhiteSpace,boolean escapeTextNode,boolean sorted) {
    if (node == null)
      return;
    switch (node.getNodeType()) {
      case Node.CDATA_SECTION_NODE:
        append_CDATA_SECTION_ToStringBuffer(resu, node, removeCDataMark);
        break;
      case Node.ATTRIBUTE_NODE:
        append_ATTRIBUTE_ToStringBuffer(resu, node);
        break;
      case Node.COMMENT_NODE:
        append_COMMENT_ToStringBuffer(resu, node);
        break;
      case Node.DOCUMENT_FRAGMENT_NODE:
        append_DOCUMENT_FRAGMENT_ToStringBuffer(resu, node);
        break;
      case Node.DOCUMENT_NODE:
        append_DOCUMENT_ToStringBuffer(resu, node, onlyChidren, removeCDataMark,ignoreWhiteSpace,escapeTextNode);
        break;
      case Node.DOCUMENT_TYPE_NODE:
        append_DOCUMENT_TYPE_ToStringBuffer(resu, node);
        break;
      case Node.ELEMENT_NODE:
        append_ELEMENT_ToStringBuffer(resu, node, onlyChidren, removeCDataMark,ignoreWhiteSpace,escapeTextNode,sorted);
        break;
      case Node.ENTITY_NODE:
        append_ENTITY_ToStringBuffer(resu, node);
        break;
      case Node.ENTITY_REFERENCE_NODE:
        append_ENTITY_REFERENCE_ToStringBuffer(resu, node);
        break;
      case Node.NOTATION_NODE:
        append_NOTATION_ToStringBuffer(resu, node);
        break;
      case Node.PROCESSING_INSTRUCTION_NODE:
        append_PROCESSING_INSTRUCTION_ToStringBuffer(resu, node);
        break;
      case Node.TEXT_NODE:
        append_TEXT_ToStringBuffer(resu, node, onlyChidren, removeCDataMark,ignoreWhiteSpace,escapeTextNode);
        break;
    }
  }

  private static void appendToStringBuffer(StringBuffer resu, NodeList nodeList, boolean removeCDataMark, boolean ignoreWhiteSpace,boolean escapeTextNode) {
    if (nodeList == null)
      return;
    for (int i = 0; i < nodeList.getLength(); i++)
      appendToStringBuffer(resu, nodeList.item(i),false,removeCDataMark,ignoreWhiteSpace,escapeTextNode,false);
  }

  private static void appendToStringBuffer(StringBuffer resu, NamedNodeMap nodeMap, boolean removeCDataMark, boolean ignoreWhiteSpace,boolean escapeTextNode) {
    if (nodeMap == null)
      return;
    for (int i = 0; i < nodeMap.getLength(); i++) {
      resu.append(' ');
      appendToStringBuffer(resu, nodeMap.item(i),false,removeCDataMark,ignoreWhiteSpace,escapeTextNode,false);
    }
  }

  private static void appendToStringBuffer(StringBuffer resu, NamedNodeMap nodeMap, boolean removeCDataMark, boolean ignoreWhiteSpace,boolean escapeTextNode, boolean sorted) {
    if (nodeMap == null)
      return;
    if (sorted){
      Node nodes [] = new Node[nodeMap.getLength()];
      for (int i = 0; i < nodeMap.getLength(); i++) {
        nodes[i] = nodeMap.item(i);
      }
      Arrays.sort(nodes, new Comparator<Node>() {
        @Override
        public int compare(Node arg0, Node arg1) {
          return (arg0.getNodeName().compareTo(arg1.getNodeName()));
        }});
      for (Node n : nodes){
        resu.append(' ');
        appendToStringBuffer(resu, n,false,removeCDataMark,ignoreWhiteSpace,escapeTextNode,sorted);
      }
    } else {
    for (int i = 0; i < nodeMap.getLength(); i++) {
      resu.append(' ');
      appendToStringBuffer(resu, nodeMap.item(i),false,removeCDataMark,ignoreWhiteSpace,escapeTextNode,sorted);
    }
    }
    
  }
  private static void traceError(Node node, boolean typeManaged, boolean noAttributs, boolean noChildren, String type) {
   System.out.println("traceError");
  }

  private static void append_TEXT_ToStringBuffer(StringBuffer resu, Node node, boolean onlyChidren, boolean removeCDataMark, boolean ignoreWhiteSpace, boolean escapeText) {
    traceError(node, true, true, false, "TEXT_NODE");
    String nodeValue = escapeText?escapeCharacter(node.getNodeValue()):node.getNodeValue(); //NIK DS-480
    if (!onlyChidren && !ignoreWhiteSpace)
      resu.append(nodeValue);
    appendToStringBuffer(resu, node.getChildNodes(),removeCDataMark,ignoreWhiteSpace,escapeText);
  }

  private static void append_PROCESSING_INSTRUCTION_ToStringBuffer(StringBuffer resu, Node node) {
    traceError(node, true, true, true, "PROCESSING_INSTRUCTION_NODE");
    //LoggingFactory.traceWarn(HierarchieParseDomSerializer.class, "PROCESSING_INSTRUCTION Not implemented");
  }

  private static void append_NOTATION_ToStringBuffer(StringBuffer resu, Node node) {
    traceError(node, false, true, true, "NOTATION_NODE");
    //LoggingFactory.traceWarn(HierarchieParseDomSerializer.class, "NOTATION Not implemented");
  }

  private static void append_ENTITY_REFERENCE_ToStringBuffer(StringBuffer resu, Node node) {
    traceError(node, false, true, true, "ENTITY_REFERENCE_NODE");
    //LoggingFactory.traceWarn(HierarchieParseDomSerializer.class, "ENTITY_REFERENCE Not implemented");
  }

  private static void append_ENTITY_ToStringBuffer(StringBuffer resu, Node node) {
    traceError(node, false, true, true, "ENTITY_NODE");
    //LoggingFactory.traceWarn(HierarchieParseDomSerializer.class, "ENTITY Not implemented");
  }

  private static void append_ELEMENT_ToStringBuffer(StringBuffer resu, Node node, boolean onlyChidren, boolean removeCDataMark, boolean ignoreWhiteSpace,boolean escapeTextNodeContent,boolean sorted) {
    if (onlyChidren){
      appendToStringBuffer(resu, node.getChildNodes(),removeCDataMark,ignoreWhiteSpace,escapeTextNodeContent);
    }else{
      resu.append('<');
      resu.append(node.getNodeName());
      appendToStringBuffer(resu, node.getAttributes(),removeCDataMark,ignoreWhiteSpace,escapeTextNodeContent,sorted);
      if (!node.hasChildNodes()) {
        resu.append("/>");
      } else {
        resu.append('>');
        appendToStringBuffer(resu, node.getChildNodes(),removeCDataMark,ignoreWhiteSpace,escapeTextNodeContent);
        resu.append("</");
        resu.append(node.getNodeName());
        resu.append('>');
      }
    }
  }

  private static void append_DOCUMENT_TYPE_ToStringBuffer(StringBuffer resu, Node node) {
    traceError(node, false, true, true, "DOCUMENT_TYPE_NODE");
    //LoggingFactory.traceWarn(HierarchieParseDomSerializer.class.getName(), "DOCUMENT_TYPE Not implemented");
  }

  private static void append_DOCUMENT_ToStringBuffer(StringBuffer resu, Node node, boolean onlyChidren, boolean removeCDataMark,boolean ignoreWhiteSpace,boolean escapeTextNode) {
    traceError(node, true, true, false, "DOCUMENT_NODE");
    if (node.hasChildNodes()){
      // AL 061221-000024 2 Days - Ability to upload sample XML file from the design UI (ie Template Editor)
      int elementNodes = 0;
      Node currentNode = node.getFirstChild();
      while(currentNode != null) {
        if(Node.ELEMENT_NODE == currentNode.getNodeType())
          elementNodes++;
        if(elementNodes > 1) {
          //LoggingFactory.traceError(HierarchieParseDomSerializer.class, "DOCUMENT with more than one child !!!");
          return;
        }
        appendToStringBuffer(resu, currentNode,false,removeCDataMark,ignoreWhiteSpace,escapeTextNode,false);
        currentNode = currentNode.getNextSibling();
      }
    }
      //LoggingFactory.traceError(HierarchieParseDomSerializer.class, "DOCUMENT without child !!!");
  }

  private static void append_DOCUMENT_FRAGMENT_ToStringBuffer(StringBuffer resu, Node node) {
    traceError(node, false, true, true, "DOCUMENT_FRAGMENT_NODE");
    //LoggingFactory.traceWarn(HierarchieParseDomSerializer.class, "DOCUMENT_FRAGMENT Not implemented");
  }

  private static void append_COMMENT_ToStringBuffer(StringBuffer resu, Node node) {
    traceError(node, true, true, true, "COMMENT_NODE");
    resu.append("<!--");
    resu.append(node.getNodeValue());
    resu.append("-->");
  }

  private static void append_ATTRIBUTE_ToStringBuffer(StringBuffer resu, Node node) {
    traceError(node, true, true, _NO_CHILDS_IN_ATTR_, "ATTRIBUTE_NODE");
    resu.append(node.getNodeName());
    resu.append("=\"");
    resu.append(xmlAttribValue(node.getNodeValue()));
    resu.append('"');
  }
  public static String xmlAttribValue(Object attrib) {
	    if (attrib == null)
	      return null;
	    String att = null;//"CAST FAIL : HierarchieParse.xmlAttribValue";
	    if (attrib instanceof String)
	      att = (String) attrib;
	    else if (attrib instanceof Integer)
	      att = ((Integer) attrib).toString();
	    else if (attrib instanceof Boolean)
	      att = ((Boolean) attrib).toString();
	    if (att == null)
	      return "";
	    int len = att.length();
	    StringBuffer resu = new StringBuffer(len);
	    for (int i = 0; i < len; i++) {
	      char c = att.charAt(i);
	      switch (c) {
	        case '"':
	          resu.append("&quot;");
	          break;
	        case '>':
	          resu.append("&gt;");
	          break;
	        case '<':
	          resu.append("&lt;");
	          break;
	        case '&':
	          resu.append("&amp;");
	          break;
	        //FB ajout le 4 nov 2002 des \n et \r pour le pb de regression du custom macro
	        case '\n':
	          resu.append("&#10;");
	          break;
	        case '\r':
	          resu.append("&#13;");
	          break;
	        default:
	          resu.append(c);
	      }
	    }
	    return resu.toString();
	  }
  public static String escapeCharacter(String content) {
	    int len = content.length();
	    StringBuffer resu = new StringBuffer(len);
	    for (int i = 0; i < len; i++) {
	      char c = content.charAt(i);
	      int v = c;
	      switch (v) {
	        case 0x22:
	          resu.append("&quot;");
	          break;
	        case 0x27:
	          resu.append("&apos;");
	          break;
	        case 0x26:
	          resu.append("&amp;");
	          break;
	        case 0x3C:
	          resu.append("&lt;");
	          break;
	        case 0x3E:
	          resu.append("&gt;");
	          break;
	        default:
	          resu.append(c);
	          break;
	      }
	    }
	    return resu.toString();
	  }


  private static void append_CDATA_SECTION_ToStringBuffer(StringBuffer resu, Node node, boolean removeCDataMark) {
    traceError(node, true, true, true, "CDATA_SECTION_NODE");
    if (removeCDataMark){
      resu.append(node.getNodeValue());
    }else{
      resu.append("<![CDATA[");
      resu.append(node.getNodeValue());
      resu.append("]]>");
    }
  }
}
