package org.uninstal.yaml.serialize;

import java.util.List;

public interface YamlListSerializer<T> {
  
  List<T> deserialize(String[] keys);
}