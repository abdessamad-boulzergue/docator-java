package com.apos.plugins;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.swing.ImageIcon;

public class RemoteImageIcon extends ImageIcon {

  private static final long serialVersionUID = 1L;

  private byte[]            _imageData       = null;

  /**
   * Server Side Java constructor from server side png/jpg/gif resource
   *
   * @param baseClass
   * @param file
   */
  public RemoteImageIcon(final Class<?> baseClass, final String file) throws Exception {
    _imageData = createImageData(baseClass, file);
  }

  /**
   * ServerSide Python plugin resource constructor
   *
   * @param pckg
   * @param imageLoc
   */
  public RemoteImageIcon(String pckg, String imageLoc) throws Exception {

    _imageData = createImageData(pckg, imageLoc);
  }

  /**
   * client side from serialized data
   *
   * @param serialized
   */
  public RemoteImageIcon(byte[] imageData) {
    _imageData = imageData;
  }

  public byte[] getImageData() {
    return _imageData;
  }

  private byte[] createImageData(final Class<?> baseClass, final String file) throws Exception {
    InputStream resource = baseClass.getResourceAsStream(file);

    final byte[][] buffer = new byte[1][];
    try {
      if (resource == null) { throw new IOException("File " + file + " not found"); }
      BufferedInputStream in = new BufferedInputStream(resource);
      ByteArrayOutputStream out = new ByteArrayOutputStream(1024);
      buffer[0] = new byte[1024];
      int n;
      while ((n = in.read(buffer[0])) > 0) {
        out.write(buffer[0], 0, n);
      }
      in.close();
      out.flush();
      buffer[0] = out.toByteArray();
    } catch (IOException ioe) {
      throw new Exception("createImageData IOError : ", ioe);
    } finally {
      if (resource != null) {
        try {
          resource.close();
        } catch (IOException e) {
          System.out.println(getClass()+"> " + "impossible to close the resource : " + file + e.getMessage());
        }
      }
    }

    if (buffer[0] == null) { throw new Exception(baseClass.getName() + "/" + file + " not found."); }
    if (buffer[0].length == 0) { throw new Exception("Warning: " + file + " is zero-length"); }
    return buffer[0];
  }

  private byte[] createImageData(String pckg, String imageLoc) throws Exception {
    File parentDir = new File(pckg);
    File image = new File(parentDir, imageLoc);
    int size = (int) image.length();

    try {
      FileInputStream rdr = new FileInputStream(image);
      byte[] imageData = new byte[size];
      rdr.read(imageData);
      rdr.close();
      return imageData;
    } catch (IOException e) {
     System.out.println("IOerror reading image : "+image.getAbsolutePath());
      throw new Exception("IOerror reading image :" + image.getAbsolutePath(), e);
    }
  }

}
