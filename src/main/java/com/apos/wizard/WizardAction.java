package com.apos.wizard;

import java.io.File;

import org.springframework.expression.AccessException;

import com.apos.parser.DomHierarchieParse;
import com.apos.parser.HierarchieParse;
import com.apos.utils.FileUtils;

public class WizardAction {

	  static private File            definitionsFile        = null;

	public WizardAction() throws AccessException {
		
		initActionDefinitions();
	}

	private void initActionDefinitions() throws AccessException {

		if(definitionsFile ==null) {
			String fileName="C:/HCS/e-Access/home/config/eaccess/action_definitions_win.xml";
			definitionsFile = new File(fileName);
			if(!definitionsFile.isFile() || definitionsFile.canRead()) {
				definitionsFile = null;
			}else {
				throw new AccessException("can't acces definitions file : ".concat(definitionsFile.getPath()));
			}
		}
		
		if(definitionsFile != null) {
			String content = FileUtils.readFile(definitionsFile);
			HierarchieParse xmlDom = new DomHierarchieParse(content);
			
		}
		
	}
}
