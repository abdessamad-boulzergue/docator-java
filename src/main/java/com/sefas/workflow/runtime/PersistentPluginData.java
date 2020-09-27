package com.sefas.workflow.runtime;

import java.io.Serializable;

import javax.swing.ImageIcon;

import com.apos.plugins.RemoteImageIcon;

public class PersistentPluginData
implements Serializable {


  /**
	 * 
	 */
  private static final long serialVersionUID = -6862317013306384631L;
  private byte[] _icon = null ;
  private byte[] _tinyIcon = null ;
  private String _name = null ;
  private String _description = null  ;
  private int _type = 0 ;
  private String _className = null ;
  private String _pythonFileName = null ;
  private String _resTypes = null;

  public PersistentPluginData( RemoteImageIcon icon ,
                               RemoteImageIcon tinyIcon ,
                               String name ,
                               String description ,
                               int type ,
                               String className ,
                               String pythonFileName ,
                               String resType) {
    _icon = icon.getImageData() ;
    _tinyIcon = tinyIcon.getImageData() ;
    _name = name ;
    _description = description ;
    _type = type ;
    _pythonFileName = pythonFileName ;
    _className= className;
    _resTypes = resType;
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
  public ImageIcon getTinyIcon() {
    return new ImageIcon(_tinyIcon) ;
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
}
