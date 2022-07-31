package org.uninstal.yaml.serialize;

import java.util.HashMap;
import java.util.Map;

public class YamlSerializers {
  
  private final static Map<String, YamlSerializer> SERIALIZERS = new HashMap<>();
  
  public static void register(String id, YamlSerializer serializer) {
    SERIALIZERS.put(id, serializer);
  }
  
  public static String serialize(String id, Object value) {
    if(!SERIALIZERS.containsKey(id)) return null;
    return SERIALIZERS.get(id).serialize(value);
  }
  
  public static Object deserialize(String id, String value) {
    if(!SERIALIZERS.containsKey(id)) return null;
    return SERIALIZERS.get(id).deserialize(value);
  }
  
  public static String serialize(Object value) {
    return String.valueOf(value);
  }

  public static Object deserialize(Class<?> target, String value) {
    if(target.equals(String.class)) return value;
    else if(target.equals(int.class)) return Integer.parseInt(value);
    else if(target.equals(long.class)) return Long.parseLong(value);
    else if(target.equals(double.class)) return Double.parseDouble(value);
    else return null;
  }
}