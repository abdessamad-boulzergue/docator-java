package com.apos.workflow.runtime;

import com.apos.plugins.IPluginLoad;
import com.apos.plugins.PluginLoader;

public class PluginLoaderFactory {

  private static IPluginLoad _singleton = null ;


  /**
   * Simple accessor
   * @return non null supposed singleton
   */
  public static IPluginLoad getPluginLoaderSingleton() {
    if (_singleton == null)
      return PluginLoader.getPluginLoader();
    return _singleton ;
  }

}
