package com.apos.dataloader;

public class Data<T> {

	private T value;
	public Data(T value ) {
		this.value =value;
	}
	public void setValue(T data) {
		this.value = data;
	}
	public T getValue() {
		return this.value;
	}
}
