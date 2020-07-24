package com.apos.parser;

import java.util.Hashtable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XMLtoJSON {
  
  private DomHierarchieParse _hpInputXml;
  private static final String _SPACE_                 = " ";
  private static final String _EMPTYSTRING_           = "";
  private static final String _TABULATION             = "\t";
  private static final String _NEWLINE_               = "\n";
  private static final String _CARRIAGERETURN_        = "\r";
  
  public XMLtoJSON(String inputXml) throws HierarchieParseException{
    this(new DomHierarchieParse(inputXml,HierarchieParse.ENCODING_NONE));
  }

  public XMLtoJSON(DomHierarchieParse hpInputXml){
    _hpInputXml = hpInputXml;
  }
  
  /**
   * set or modify an attribut
  */
  public void setAttribut(String Key, String Value){
    if ((Key != null) && (Value != null) && _hpInputXml!=null) {
      _hpInputXml.setAttribut(Key, Value);
    }
  }
  
  private static JSONArray toJson(Node node) throws JSONException {
    return toJson(node, false);
  }
  
  private static JSONArray toJson(Node node, Boolean isTextContentAllowed) throws JSONException {
    JSONArray resu = new JSONArray();
    if (node != null){
      resu.put(node.getNodeName());
      resu.put(toJson(DomHierarchieParse.getAttributs(node)));
      resu.put(normalyseJsonContent(node, isTextContentAllowed));
    }
    return resu;
  }
  
  public static JSONObject toJson(Hashtable<String,String> attributs) throws JSONException {
    JSONObject resu = new JSONObject();
    if (attributs != null){
      for (String key : attributs.keySet()){
        resu.put(key, attributs.get(key)
            .replace("\"", "&quot;")
            .replace(XMLtoJSON._NEWLINE_, "&#10;")
            .replace("<", "&lt;")
            .replace(">", "&gt;"));
      }
    }
    return resu;
  }
  
  // default original method, does not manage textNodeContent
  private static JSONArray normalyseJsonContent(Node node) throws JSONException {
    // isTextContentAllowed = false
    return normalyseJsonContent(node, false);
  }
    
  private static JSONArray normalyseJsonContent(Node node, Boolean isTextContentAllowed) throws JSONException {
    JSONArray resu = new JSONArray();
    if (node.hasChildNodes()){
      NodeList nl=node.getChildNodes();
      int len = nl.getLength();
      for (int i=0;i<len;i++){
        Node n = nl.item(i);
        switch (n.getNodeType()){
          case Node.CDATA_SECTION_NODE:
            resu.put(n.getNodeValue());
          case Node.TEXT_NODE:
            // if textContent is allowed
            if (isTextContentAllowed){
               String textValue = null;
              // a valid String is a trimmed String which is different from 
              // tabulation, space and new line character
              if (n!=null && n.getNodeValue()!=null && !n.getNodeValue().trim().equals(_EMPTYSTRING_)){
                textValue = n.getNodeValue();
                
                // replace each tabulation of textValue by space
                textValue = textValue.replace(_TABULATION, _SPACE_);
                
                // replace each \n (newline character) of textValue by ""
                //textValue = textValue.replace(_NEWLINE_, _EMPTYSTRING_);
                
                // replace each \r (carriage return character) of textValue by ""
                textValue = textValue.replace(_CARRIAGERETURN_, _EMPTYSTRING_);
                
                // replace one or more space by one space character only
                // don't know if it's really necessary
                // for the time being, I prefer comment it
                //textValue = textValue.replaceAll(" +", _SPACE_);

                // remove trailing and leading space
                textValue = textValue.trim();
                if (!textValue.equals(_EMPTYSTRING_) ){
                  resu.put(textValue);
                }
              }
            }
          break;
          case Node.ELEMENT_NODE:
            resu.put(toJson(n, isTextContentAllowed));
            break;
        }
      }
    }
    return resu;
  }
  
  public JSONArray toJson() throws JSONException  {
    // isTextContentAllowed = false
    return toJson(_hpInputXml.getNode(), _hpInputXml.getType().equals("ERROR"));
  }

  public JSONArray toJson(boolean isTextContentAllowed) throws JSONException  {
    return toJson(_hpInputXml.getNode(), isTextContentAllowed);
  }

  public String getType() {
    String type = _hpInputXml.getType();
    if (type.indexOf(":") >= 0)
      type = type.substring(type.indexOf(":")+1);
    return type;
  }

  public DomHierarchieParse getXml() {
    return _hpInputXml;
  }

}
