package com.apos.workflow.runtime;

import java.io.Serializable;

import javax.swing.ImageIcon;

import org.json.JSONObject;

import com.apos.plugins.RemoteImageIcon;
import com.apos.utils.Base64;

public class PersistentPluginData
implements Serializable {


  /**
	 * 
	 */
  private static final long serialVersionUID = -6862317013306384631L;
  private byte[] _icon = null ;
  private String _name = null ;
  private String _description = null  ;
  private int _type = 0 ;
  private String _className = null ;
  private String _pythonFileName = null ;
  private String _resTypes = null;
  private String uid = null;
private JSONObject _params;

  public PersistentPluginData( String uid ,RemoteImageIcon icon ,
                               String name ,
                               String description ,
                               int type ,
                               String className ,
                               String pythonFileName ,
                               String resType, JSONObject params) {
	  this.uid = uid;
    _icon = icon.getImageData() ;
    _name = name ;
    _description = description ;
    _type = type ;
    _pythonFileName = pythonFileName ;
    _className= className;
    _resTypes = resType;
    _params = params;
  }
  public static PersistentPluginData fromJson(JSONObject json) {
	  
	RemoteImageIcon icon = new RemoteImageIcon(Base64.decode(json.getString("icon")));
	String name = json.getString("name");
	String uid = json.getString("uid");
	String description = null;
	int type = 0;
	String className = null;
	String pythonFileName = json.getString("name");
	JSONObject params = json.getJSONObject("params");
	String resType = null;
	return new PersistentPluginData(uid,icon, name, description, type, className, pythonFileName, resType,params);
  }
  public String getDescription() {
    return _description ;
  }

  public String getName() {
    return _name ;
  }

  public String getPathName() {
    return _name ;
  }

  public ImageIcon getImageIcon() {
    return new ImageIcon( _icon );
  }
  public byte[] getIcon() {
	    return _icon ;
	  }

  public int getType() {
    return _type ;
  }

  public String getClassName() {
    return _className ;
  }

  public String getPythonFileName() {
    return _pythonFileName ;
  }
  public String getDropResourceTypes() {
    return _resTypes;
  }
  public String getUId() {
		return this.uid;
  }
  public JSONObject getScripletParams() {
	  return this._params;
  }
}
