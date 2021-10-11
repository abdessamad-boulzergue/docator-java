package com.apos.dataloader;

public class StringDataReader implements DataReader<String>{

	private Data<String> value;
	public StringDataReader(String val){
		this.value =new Data<String>(val);
	}
	@Override
	public Data<String> read() {
		return value;
	}

}
