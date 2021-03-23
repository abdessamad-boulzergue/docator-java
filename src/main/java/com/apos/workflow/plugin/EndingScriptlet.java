package com.apos.workflow.plugin;

public class EndingScriptlet extends WorkflowScriptlet {


	@Override
	public void run() {
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		logInfo("----------END EndingScriptlet------");
	}
}
