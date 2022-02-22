package com.apos.utils;


public  interface LazyLoaded<T> {

public  void setLoadService(T arg0);

public  void load();
}