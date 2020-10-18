package com.apos.socket;

public class ClientStub {

	private static final String DEFAULT_ENCODING = "utf-8";
	private String host;
	private int port;
	private String  encoding = DEFAULT_ENCODING;
	private ClientSession session;
	
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
	
	public String sendAndWaitReceive(String str) {
		synchronized(this.session) {
			try {
				if(!str.endsWith(ClientSession.EOT)) {
					str=str.concat(ClientSession.EOT);
				}
				session.send(str);
				return session.receive();
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	public String marshall(String fx)  {
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
