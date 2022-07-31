package org.uninstal.yaml.serialize;

public interface YamlSerializer {
  
  String serialize(Object value);
  Object deserialize(String key);
}