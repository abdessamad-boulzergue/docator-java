package com.apos.dataloader;

public interface DataFormatter<T,K> {
	public Data<K> format(Data<T> data);
}
