package com.apos.socket;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;

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
			
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void initInputOutputStream( String charsetName) {
		try {
			InputStreamReader reader = new InputStreamReader(client.getInputStream(),charsetName);
			_inStream = new BufferedReader(reader);
			
			OutputStreamWriter writer = new OutputStreamWriter(client.getOutputStream(),charsetName);
			_outStream = new BufferedWriter(writer);
			

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void send(String str) throws Exception {
		if (_outStream == null)
	      throw new Exception("IO ERROR : connection reset by peer");
	    try {
	      // send buffer next
	      for (int ii = 0; ii < str.length(); ii++)
	    	  _outStream.write((byte) str.charAt(ii));
	      _outStream.flush(); // SEND buffer now

	    } catch (IOException e) {
	      throw new Exception("IO ERROR send : "+str+", exception: " + e.getMessage());
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
	public String receive() throws Exception {
		String result="";
		if(_inStream !=null) {
			int c = _inStream.read();
			StringBuffer buff = new StringBuffer();
			while(c>=0 && c!='\n') {
				buff.append((char)c);
				c = _inStream.read();
			}
			result = buff.toString();
			//The character read, as an integer in the range0 to 65535 (0x00-0xffff), 
			//or -1 if the end of the stream has been reached
			if(c==-1) {
				if("".equals(result)) {
					throw new Exception("the end of the stream has been reached: nothing received");
				}
			}
			return result;
		}else {
			throw new Exception("IO Error");
		}
	}
	public void stop() {
		try {
				this.client.close(); // Closing this socket will also close the socket's InputStream and OutputStream. 
				this._inStream =null;
				this._outStream =null;
				this.client =null;
			} catch (IOException e) {
				e.printStackTrace();
			} catch (NullPointerException e) {
				e.printStackTrace();
			}
			finally {
				isRuning=false;
			}
		}

}
