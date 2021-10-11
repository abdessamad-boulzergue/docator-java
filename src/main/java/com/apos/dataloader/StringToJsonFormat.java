package com.apos.dataloader;

import org.json.JSONObject;

public class StringToJsonFormat implements DataFormatter<String, JSONObject> {

	@Override
	public Data<JSONObject> format(Data<String> data) {
		
		JSONObject obj = new JSONObject(data.getValue());
		
		return new Data<JSONObject>(obj);
	}

}
