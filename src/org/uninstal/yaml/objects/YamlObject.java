package org.uninstal.yaml.objects;

import java.io.FileWriter;

public abstract class YamlObject {

  protected final String key;
  protected final String path;

  public YamlObject(String path) {
    this.key = !path.contains(".") ? path : path.substring(path.lastIndexOf(".") + 1);
    this.path = !path.contains(".") ? "" : path.substring(0, path.lastIndexOf("."));
  }

  public String getPath() {
    return path;
  }
  
  public String getKey() {
    return key;
  }
  
  public String getFullPath() {
    return (path.isEmpty() ? "" : path + ".") + key;
  }

  public abstract void create(FileWriter writer, int deep) throws Exception;
}