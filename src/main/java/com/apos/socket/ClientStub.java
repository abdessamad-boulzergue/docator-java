package com.apos.socket;

import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.apos.plugins.UnmarshallException;
import com.apos.rest.exceptions.SocketSendReceiveException;

public class ClientStub {

	private static final String DEFAULT_ENCODING = "utf-8";
	private String host;
	private int port;
	private String  encoding = DEFAULT_ENCODING;
	private ClientSession session;
	Logger logger = LoggerFactory.getLogger(ClientStub.class);
	String runningCtx=null;
	private static final  String RETOK = "<OK>";
	private static final  String RET_ERROR = "<ERROR>";
	private static final  String ENDRET_ERROR = "</ERROR>";
    private static final  String ENDRETOK = "</OK>";
	
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
	public String runCommand(String mth, List<Object> params)  {

		String result ="";
		try {
			marshall("ALC");
			marshall("MTH ".concat(mth));

		
		params.stream().forEach(param->{
			try {
					if(param instanceof String) {
						marshall("DPRM args s");
					}
					else if(param instanceof Integer) {
						marshall("DPRM args d");	
					}
					marshall("SPRM args = '".concat(String.valueOf(param)).concat("'"));
			} catch (SocketSendReceiveException e) {
				logger.error(e.getMessage());
			}
		});
		 result  =   marshall("CALL\n");
		 return unmarshall(result);
		} catch (SocketSendReceiveException e1) {
			logger.error(e1.getMessage());
		} catch (UnmarshallException e) {
			logger.error(e.getMessage());
		}
		return result;
	}
	
	protected String unmarshall(String content) throws UnmarshallException {
	    int whereRet = content.indexOf(RETOK);

	    if (whereRet == -1) {
		    int whereEndRet = content.indexOf(ENDRETOK);
		    if(whereEndRet==-1) {
			    int whereError = content.indexOf(RET_ERROR);
			    int whereEndError = content.indexOf(ENDRET_ERROR);
			    int start = whereError + RET_ERROR.length();
			    if(whereError!=-1 && whereEndError!=-1)
			      content = content.substring(start, whereEndError);
				throw new UnmarshallException(content);
		    }else {
		    	return "";
		    }
	    }

	    int whereEndRet = content.indexOf(ENDRETOK);
	    int start = whereRet + RETOK.length();

	    return content.substring(start, whereEndRet);
	  }
	public String initContext(List<Object> asList) {
		if(runningCtx==null) {
			runningCtx =  runCommand("initContext",Arrays.asList());
		}
		return this.runningCtx;
	}
}
