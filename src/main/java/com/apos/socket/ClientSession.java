package com.apos.socket;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

import com.apos.rest.exceptions.SocketSendReceiveException;

public class ClientSession {
	public final static String  EOT                   = "\n";
	private static final int _BLOBSTR_MSGLEN_SIZE_ = 7;
	private boolean isRuning=false;
	private Socket client;
	private BufferedReader _inStream;
	private BufferedWriter _outStream;
	
	public boolean isRunning() {
		return isRuning;
	}

	public void start(String host, int port,String encoding) {
		try {
			
			 this.client = new Socket(host, port);
			initInputOutputStream(encoding);
			this.isRuning=true;
			
		} catch (IOException e) {
			this.isRuning = false;
			this._inStream = null;
			this._outStream = null;
		}
	}

	private void initInputOutputStream( String charsetName) {
		try {
			InputStreamReader reader = new InputStreamReader(client.getInputStream(),charsetName);
			_inStream = new BufferedReader(reader);
			
			OutputStreamWriter writer = new OutputStreamWriter(client.getOutputStream(),charsetName);
			_outStream = new BufferedWriter(writer);
			

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void send(String str) throws SocketSendReceiveException {
		if (_outStream == null)
	      throw new SocketSendReceiveException("IO ERROR : connection reset by peer , outputstream is null");
	    try {
	      for (int ii = 0; ii < str.length(); ii++)
	    	  _outStream.write((byte) str.charAt(ii));
	      _outStream.flush(); 

	    } catch (IOException e) {
	      throw new SocketSendReceiveException("IO ERROR send : "+str+", exception: " + e.getMessage());
	    }
	}
	  /**
	  receive a client string ending with CR/LF
	  */
	  public synchronized String receiveBlobStr() throws Exception {
	    try {
	      int c = _inStream.read();
	      StringBuffer buf = new StringBuffer();

	      for (int ii = 0; ii < _BLOBSTR_MSGLEN_SIZE_; ii++) {
	        buf.append((char) c);
	        c = _inStream.read();
	      }
	      String strLength = buf.toString();
	      buf = new StringBuffer(); // reset
	      int blobLength = Integer.parseInt(strLength);
	      for (int ii = 0; ii < blobLength; ii++) {
	        buf.append((char) c);
	        if (ii != blobLength - 1) // bypass when length is reached
	          c = _inStream.read();
	      }
	      return buf.toString();
	    } catch (IOException e) {
	      throw new Exception("IO ERROR reading from SMD client :" + e.getMessage());
	    }

	  }
	public String receive() throws SocketSendReceiveException {
		
		if (_inStream == null) {
		      throw new SocketSendReceiveException("IO ERROR :  input stream is null");
		}
		String result="";
		try {
		int c = _inStream.read();
		StringBuilder buff = new StringBuilder();
		while(c>=0 && c!='\n') {
				buff.append((char)c);
				c = _inStream.read();
			}
		result = buff.toString();
			//The character read, as an integer in the range0 to 65535 (0x00-0xffff), 
			//or -1 if the end of the stream has been reached
		if(c==-1 && "".equals(result)) {
			throw new SocketSendReceiveException("the end of the stream has been reached: nothing received");
		}
		}catch(IOException ex) {
			throw new SocketSendReceiveException("IOException,  exception : "+ex.getMessage());
		}
		return result;
		
	}
	public void stop() {
		try {
			    if(this.client!=null)
			    	this.client.close(); // Closing this socket will also close the socket's InputStream and OutputStream. 
				this._inStream =null;
				this._outStream =null;
				this.client =null;
			} catch (IOException e) {
				e.printStackTrace();
			} 
			finally {
				isRuning=false;
			}
		}

}
