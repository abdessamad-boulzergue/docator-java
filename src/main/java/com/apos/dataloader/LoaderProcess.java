package com.apos.dataloader;

public  class LoaderProcess<T,K> {
	
	DataReader<T> input;
	DataWriter<K> writer;
	private DataFormatter<T, K> formatter;
	
	public void setInput(DataReader<T> in) {
		input = in;
	}
	public void setWriter(DataWriter<K> out) {
		writer = out;
	}
	public void setFormatter(DataFormatter<T,K> format) {
		formatter = format;
	}
	
	public void process() throws Exception{
		Data<T> data = input.read();	
		Data<K> formated=null;
		formated= (formatter !=null)? formatter.format(data) :  (Data<K>) data;
		if(writer!=null) {
			writer.write(formated);
		}
	}
}
