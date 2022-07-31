package org.uninstal.yaml.test;

import org.uninstal.yaml.Yaml;
import org.uninstal.yaml.YamlAnnotations.*;
import org.uninstal.yaml.serialize.YamlListSerializer;
import org.uninstal.yaml.serialize.YamlSerializer;
import org.uninstal.yaml.serialize.YamlSerializers;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Config extends Yaml {
  
  @Path("list.test") @ListValue({"test1", "test2", "test3"}) public static List<String> LIST;
  @Path("list.test2") @ListValue({"1", "2", "3"}) @CustomSerializable(id = "test2") public static List<String> LIST2;
  @Path("list.test3") @ListValue({"1", "2", "4"}) @CustomSerializable(id = "test2") public static List<String> LIST3;
  @Path("test67") @Value("test not work") @CustomSerializable(id = "test") public static String TEST;
  @Path("key") @Value("value1") public static String KEY;
  @Path("key2") @Value("value1") public static String KEY2;
  @Path("key3") @Value("value1") public static String KEY3;
  @Path("key4") @Value("value1") public static String KEY4;
  @Path("test.value1") @Value("-900") public static int VALUE1;
  @Comment("Value num 2") @Path("test.value2") @Value("900") public static int VALUE2;
  @Path("test.test.value1") @Value("-900") public static int VALUE3;
  @Comment("Value num 2") @Path("test.test.value2") @Value("900") public static int VALUE4;

  public Config(String folder, String fileName) {
    super(folder, fileName);
  }

  @Override
  public void onPrepare() {
    
    YamlSerializers.register("test", (YamlSerializer) key -> key.replace(" not", ""));
    YamlSerializers.register("test2", (YamlListSerializer<Integer>) keys -> 
      Arrays.stream(keys).map(Integer::parseInt).collect(Collectors.toList()));
  }

  @Override
  public void onLoad() {
    
  }
}