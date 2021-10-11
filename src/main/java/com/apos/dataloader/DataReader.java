package com.apos.dataloader;

public interface DataReader<T> {
	
	public Data<T> read() throws Exception;
	
}
