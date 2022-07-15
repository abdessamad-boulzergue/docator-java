package com.apos.parser;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.util.Hashtable;
import java.util.Vector;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class DomHierarchieParse extends HierarchieParse{

	private final static String CLASS_ID="$Identity";
	private final static boolean NAMESPACE_AWARE=false;
	
	
 	
	protected DocumentBuilder docBuilder = null;
	protected DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
	protected Document document = null;
	protected Node localNode = null;
	protected boolean nameSpaceAware = NAMESPACE_AWARE;
	private Node localFromNode = null;
 

	public DomHierarchieParse() {
		init();
		document = docBuilder.newDocument();
	}
	
	public DomHierarchieParse(String xml) {
		init();
		load(xml);
	}
	
	public DomHierarchieParse(String xml, String encoding) {
		this.encoding = encoding;
		init();
		load(xml);
	}
	public DomHierarchieParse(Node node, String encode, boolean nameSpaceAware, DocumentBuilder builder) {
		encoding=encode;
	   setNameSpaceAware(nameSpaceAware);
	    docBuilder = builder;
	    if (docBuilder==null)
	      init();
	    document=docBuilder.newDocument();
	    addNode(document,node,true, document,false);
	    localNode=document.getFirstChild();
	    localFromNode = node;
	}

	private void setNameSpaceAware(boolean nameSpaceAware) {
		this.nameSpaceAware = nameSpaceAware;
	}

	 public static void addNode(Node to,Node from,boolean withFrom, Document locDom, boolean atStart){
	    int idChild;
	    if (from.hasChildNodes()|| withFrom){
	      NodeList nl=null;
	      int nbChilds=1;
	      if (!withFrom){
	        nl=from.getChildNodes();
	        nbChilds=nl.getLength();
	      }
	      for (idChild=0;idChild<nbChilds;idChild++){
	        Node curChild;
	        if (!withFrom)
	          curChild=nl.item(idChild);
	        else
	          curChild=from;
	        Node newChild=null;
	        switch (curChild.getNodeType()){
	          case Node.CDATA_SECTION_NODE:
	            newChild=locDom.createCDATASection(curChild.getNodeValue());
	            break;
	          case Node.TEXT_NODE:
	            newChild=locDom.createTextNode(curChild.getNodeValue());
	            break;
	          case Node.ELEMENT_NODE:
	            if (curChild.getNamespaceURI()!=null)
	              newChild=locDom.createElementNS(curChild.getNamespaceURI(),curChild.getNodeName());
	            else
	              newChild=locDom.createElement(curChild.getNodeName());
	            break;
	        }
	        if (newChild!=null){
	          addAttributs(newChild,curChild,locDom);
	          addNode(newChild,curChild,false,locDom,false);
	          if (atStart && (to.getFirstChild()!=null))
	            to.insertBefore(newChild,to.getFirstChild());
	          else
	            to.appendChild(newChild);
	        }
	      }
	    }
	  
	}

	 public static void addAttributs(Node to, Node from, Document domDoc){
		    int idAttr;
		    if (from.hasAttributes()){
		      NamedNodeMap nnmFromChild=from.getAttributes();
		      NamedNodeMap nnmToChild=to.getAttributes();
		      int nbAttr=nnmFromChild.getLength();
		      Node newAttr=null;
		      for (idAttr=0;idAttr<nbAttr;idAttr++){
		        Node fromAttr=nnmFromChild.item(idAttr);
		        String locName = getLocalName(fromAttr);
		        switch (fromAttr.getNodeType()){
		          case Node.ATTRIBUTE_NODE:
		            String namespaceURI = fromAttr.getNamespaceURI();
		            if ((namespaceURI!=null)&&(namespaceURI.length()>0)){
		              if (namespaceURI.equals("http://www.w3.org/2000/xmlns/")) // Compatibilit� xerces 2
		                newAttr = domDoc.createAttribute(locName);
		              else
		                newAttr = domDoc.createAttributeNS(namespaceURI, locName);
		              newAttr.setNodeValue(fromAttr.getNodeValue());
		              nnmToChild.setNamedItemNS(newAttr);
		            }
		            else{
		              newAttr=domDoc.createAttribute(fromAttr.getNodeName());
		              newAttr.setNodeValue(fromAttr.getNodeValue());
		              nnmToChild.setNamedItem(newAttr);
		            }
		            break;
		        }
		      }
		    }
		  }
	public void load(String xml) {
		
		try {
			if(encoding ==null) {
				
				    StringReader byteStream = new StringReader(xml);
				    InputSource is = new InputSource(byteStream) ;
				    synchronized (docBuilder) {
						document = docBuilder.parse(is );
					}
			}
			else {
				 synchronized (docBuilder) {
					 ByteArrayInputStream byteStream = new ByteArrayInputStream(xml.getBytes(encoding));
						document = docBuilder.parse(byteStream);
					}
			}
			localNode = document.getFirstChild();
			
			while(localNode.getNextSibling()!=null) {
				localNode = localNode.getNextSibling();
			}
			
		} catch (SAXException | IOException e) {
			e.printStackTrace();
		}
		
	}
	
	@Override
	void init() {
		synchronized(CLASS_ID) {
			
			if(docBuilder == null) {
				docBuilderFactory.setNamespaceAware(nameSpaceAware);
				docBuilderFactory.setValidating(false);
				
				try {
					docBuilder = docBuilderFactory.newDocumentBuilder();
					docBuilder.setErrorHandler(null);
				} catch (ParserConfigurationException e) {
					docBuilder=null;
					e.printStackTrace();
				}
				
			}
			
		}
	}
	@Override
	public String toString() {
		return __toStringMainTag(true,true);
	}
	 private String __toStringMainTag(boolean withEnd, boolean withoutHeader)/* throws HierarchieParseException */{
		    try {
		      return HierarchieParseDomSerializer.writeToString(document,withoutHeader,false);
		    } catch (Exception e) {
		      e.printStackTrace();
		      return "<ERROR>" + e.getMessage() + "</ERROR>";
		    }
		  }

	public Node getNode() {
		return localNode;
	}
	  public static String getLocalName(String nodeName)
	  {
	      String type = nodeName;
	      int index = nodeName.indexOf(":");
	      if (index > 0)
	          type = nodeName.substring(index+1);
	      return type;
	  }


	  /** */
	  public static boolean hasLocalName(Node node)
	  {
	      return hasLocalName(node.getNodeName());
	  }
	  public static boolean hasLocalName(String nodeName)
	  {
	      int index = nodeName.indexOf(":");
	      return (index > 0);
	  }
	  public static String getLocalName(Node node)
	  {
	      return getLocalName(node.getNodeName());
	  }
	
	  public static String getType(Node node, boolean _nameSpaceAware)
	  {
	      String type = null;
	      if (_nameSpaceAware && hasLocalName(node))
	          type = getLocalName(node);
	      else
	          type = node.getNodeName();
	      return type;
	  }

	
	  public String getType(boolean _nameSpaceAware){
	    return getType(localNode,_nameSpaceAware);
	  }

	
	  public String getType(){
	    String type = getType(false);
	    return type;
	  }
	  public static Hashtable<String,String> getAttributs(Node node){
		     Hashtable<String,String> _attributes=new Hashtable<String,String>();
		      if (node.hasAttributes()){
		        NamedNodeMap nnm=node.getAttributes();
		        int len=nnm.getLength();
		        for (int i=0;i<len;i++){
		          Node n2=nnm.item(i);
		          switch (n2.getNodeType()){
		            case Node.ATTRIBUTE_NODE:
		              _attributes.put(n2.getNodeName(),n2.getNodeValue());
		          }
		        }
		      }
		    return _attributes;
		  }
	  
	 /**
	   * set or modify an attribut
	  */
	  public void setAttribut(String Key, String Value){
	    if ((Key != null) && (Value != null)) {
	      _attributes = null;
	      setAttribut(Key, Value, localNode, document);
	    }
	  }
		  public static void setAttribut(String Key, String Value, Node node, Node _domDoc){
		   if ((Key != null) && (Value != null)) {
		    boolean isAdded=false;

		    NamedNodeMap nnm=node.getAttributes();
		    if (node.hasAttributes()){
		      Node n=nnm.getNamedItem(Key);
		      if (n!=null){
		        n.setNodeValue(Value);
		        isAdded=true;
		      }
		    }
		    if (!isAdded){
		      if ((_domDoc != null) && (_domDoc instanceof Document)){
		        Document domDoc=(Document)_domDoc;
		        Node n=domDoc.createAttribute(Key);
		        n.setNodeValue(Value);
		        // _locNode.appendChild(n);
		        nnm.setNamedItem(n);
		      }
		    }
		   }
		  }

		  public Vector getHierarchie(String tag,boolean all)/* throws HierarchieParseException */{
		      int idNameSpace=tag.indexOf(":");
		      String nameSpaceURI="",locName;
		      if (idNameSpace>0){
		        nameSpaceURI=tag.substring(0,idNameSpace);
		        locName=tag.substring(idNameSpace+1);
		      }
		      else
		        locName=tag;
		      Vector resu=new Vector();
		      if (localNode.hasChildNodes()){
		        NodeList nl=localNode.getChildNodes();
		        int len=nl.getLength();
		        for (int i=0;i<len;i++){
		          Node n2=nl.item(i);
		          String locName2 = getLocalName(n2);
		          String nameRUI2 = n2.getNamespaceURI();
		            switch (n2.getNodeType()){
		              case Node.ELEMENT_NODE:
		                if (n2.getNodeName().equals(tag) ||
		                    (hasLocalName(n2) && locName2.equals(locName) && (nameRUI2!=null) && nameRUI2.equals(nameSpaceURI))) {
							resu.add(new DomHierarchieParse(n2,encoding,nameSpaceAware,docBuilder)); //FBA25
		                    if (!all)
		                      return resu;
						}
		            }
		        }
		      }
		    return resu;
		  }
}
