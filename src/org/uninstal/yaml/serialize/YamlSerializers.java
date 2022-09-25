package org.uninstal.yaml.serialize;

import java.util.*;

public class YamlSerializers {
  
  private final static Map<String, YamlSerializer> SERIALIZERS = new HashMap<>();
  private final static Map<String, YamlListSerializer<?>> LIST_SERIALIZERS = new HashMap<>();
  
  public static void register(String id, YamlSerializer serializer) {
    SERIALIZERS.put(id, serializer);
  }
  
  public static void register(String id, YamlListSerializer<?> serializer) {
    LIST_SERIALIZERS.put(id, serializer);
  }
  
  public static Object deserialize(String id, Object value) {
    if(value.getClass().isArray()) {
      if(!LIST_SERIALIZERS.containsKey(id)) return null;
      return LIST_SERIALIZERS.get(id).deserialize((String[]) value);
    } else {
      if(!SERIALIZERS.containsKey(id)) return null;
      return SERIALIZERS.get(id).deserialize((String) value);
    }
  }

  public static Object deserialize(Class<?> target, Object value) {
    if(value.getClass().isArray()) {
      String[] toArray = (String[]) value;
      if(target.equals(List.class)) return Arrays.asList(toArray);
      else if(target.equals(ArrayList.class)) return Arrays.asList(toArray);
      else return null;
    } else {
      String toString = (String) value;
      if(target.equals(String.class)) return toString.contains("&") ? toString.replace("&", "§") : toString;
      else if(target.equals(int.class)) return Integer.parseInt(toString);
      else if(target.equals(long.class)) return Long.parseLong(toString);
      else if(target.equals(double.class)) return Double.parseDouble(toString);
      else return null;
    }
  }
}