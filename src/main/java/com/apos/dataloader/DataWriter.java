package com.apos.dataloader;

public interface DataWriter<T> {
	
	public void write(Data<T> data) throws Exception;
}
