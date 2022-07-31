package org.uninstal.yaml;

public class YamlStrings {

  public static int amountOf(String line, char key) {
    int temp = 0;
    for(char ch : line.toCharArray())
      if(ch == key) ++temp;
    return temp;
  }

  public static String repeat(String key, int amount) {
    StringBuilder builder = new StringBuilder();
    for(int i = 0; i < amount; i++)
      builder.append(key);
    return builder.toString();
  }
}