package com.apos.dataloader;

import org.json.JSONObject;

public class JSONDataReader implements DataReader<JSONObject>{

	private Data<JSONObject> value;
	public JSONDataReader(JSONObject val){
		this.value =new Data<JSONObject>(val);
	}
	@Override
	public Data<JSONObject> read() {
		return value;
	}

}
