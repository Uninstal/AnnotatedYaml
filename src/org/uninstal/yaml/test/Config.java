package org.uninstal.yaml.test;

import org.uninstal.yaml.Yaml;
import org.uninstal.yaml.YamlAnnotations.*;
import org.uninstal.yaml.serialize.YamlSerializer;
import org.uninstal.yaml.serialize.YamlSerializers;

public class Config extends Yaml {
  
  @Path("test67")
  @Value("test not work")
  @CustomSerializable(id = "test")
  public static String TEST;
  
  @Path("key")
  @Value("value1")
  public static String KEY;
  
  @Path("key2")
  @Value("value1")
  public static String KEY2;
  
  @Path("key3")
  @Value("value1")
  public static String KEY3;
  
  @Path("key4")
  @Value("value1")
  public static String KEY4;

  @Path("test.value1")
  @Value("-900")
  public static int VALUE1;

  @Comment("Value num 2")
  @Path("test.value2")
  @Value("900")
  public static int VALUE2;

  @Path("test.test.value1")
  @Value("-900")
  public static int VALUE3;

  @Comment("Value num 2")
  @Path("test.test.value2")
  @Value("900")
  public static int VALUE4;

  @Override
  public void onPrepare() {

    YamlSerializers.register("test", new YamlSerializer() {
      
      @Override
      public String serialize(Object value) {
        return "test not work";
      }

      @Override
      public Object deserialize(String key) {
        return key.replace(" not", "");
      }
    });
  }

  @Override
  public void onLoad() {
    
  }
}