package com.apos.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class DataSerializer {

  public static String serialize ( Serializable instance  )
  throws Exception{
    try {
      ByteArrayOutputStream stream = new ByteArrayOutputStream() ;
      ObjectOutputStream out = new ObjectOutputStream(stream) ;
      out.writeObject(instance) ;
      out.flush();
      return Base64.encode(stream.toByteArray()) ;
    } catch( IOException e ) {
      throw new Exception(DataSerializer.class + " serialize IOERROR : " , e ) ;
    }
  }

  public static Object deserialize ( String serialized )
  throws Exception {
    try {
      byte [] decoded = Base64.decode(serialized) ;
      ByteArrayInputStream stream = new ByteArrayInputStream(decoded) ;
      ObjectInputStream in = new ObjectInputStream(stream) ;
      return in.readObject() ;
    } catch( IOException e ) {
      throw new Exception(DataSerializer.class + " deserialize IOERROR : " , e ) ;
    } catch (ClassNotFoundException e) {
      throw new Exception(DataSerializer.class + " deserialize ClassNotFound : " , e ) ;
    }
  }
}