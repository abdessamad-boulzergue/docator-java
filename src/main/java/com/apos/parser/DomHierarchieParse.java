package com.apos.parser;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.util.Hashtable;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class DomHierarchieParse extends HierarchieParse{

	private final static String CLASS_ID="$Identity";
	private final static boolean NAMESPACE_AWARE=false;
	
	protected DocumentBuilder docBuilder = null;
	protected DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
	protected Document document = null;
	protected Node node = null;
	protected boolean nameSpaceAware = NAMESPACE_AWARE;

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
			node = document.getFirstChild();
			
			while(node.getNextSibling()!=null) {
				node = node.getNextSibling();
			}
			
		} catch (SAXException | IOException e) {
			// TODO Auto-generated catch block
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
		// TODO Auto-generated method stub
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
		// TODO Auto-generated method stub
		return node;
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
	    return getType(node,_nameSpaceAware);
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
	      setAttribut(Key, Value, node, document);
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
}
