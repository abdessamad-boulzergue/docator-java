package com.apos.socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.apos.rest.exceptions.SocketSendReceiveException;

public class ClientStub {

	private static final String DEFAULT_ENCODING = "utf-8";
	private String host;
	private int port;
	private String  encoding = DEFAULT_ENCODING;
	private ClientSession session;
	Logger logger = LoggerFactory.getLogger(ClientStub.class);
	public ClientStub(String host, int port,String encoding) {
		this.port=port;
		this.host=host;
		this.encoding=encoding;
		this.session = new ClientSession();
	}
	public ClientStub(String host, int port) {
		this.port=port;
		this.host=host;
		this.encoding=DEFAULT_ENCODING;
		this.session = new ClientSession();
	}
	
	public void startSession() {
		synchronized(session) {
			
			if(!session.isRunning()) {
				session.start(this.host,this.port,this.encoding);
			}
			
		}
	}
	public void stopSession() {
		synchronized(session) {
			
			if(session.isRunning()) {
				session.stop();
			}
			
		}
	}
	
	public String sendAndWaitReceive(String str) throws SocketSendReceiveException {
		synchronized(this.session) {
				if(!str.endsWith(ClientSession.EOT)) {
					str=str.concat(ClientSession.EOT);
				}
				session.send(str);
				return session.receive();
		
		}
	}
	public String marshall(String fx) throws SocketSendReceiveException  {
	    return sendAndWaitReceive(fx);
	  }
	public String sendAndReceiveBlob(String str) {
		synchronized(this.session) {
			try {
				if(!str.endsWith(ClientSession.EOT)) {
					str = str.concat(ClientSession.EOT);
				}
				session.send(str);
				return session.receiveBlobStr();
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
}
