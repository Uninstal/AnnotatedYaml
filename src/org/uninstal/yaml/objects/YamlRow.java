package org.uninstal.yaml.objects;

import org.apache.commons.lang.math.NumberUtils;

import java.io.FileWriter;

public class YamlRow extends YamlObject {
  
  private final Object value;
  private final String[] comments;

  public YamlRow(String path, Object value, String[] comments) {
    super(path);
    this.value = value;
    this.comments = comments;
  }

  @Override
  public void create(FileWriter writer, int deep) throws Exception {
    String indent = repeat(" ", deep * 2);
    if(comments != null && comments.length > 0) {
      for(String comment : comments)
        writer.write(indent + "# " + comment + " \n");
    }
    
    if(value.getClass().isArray()) {
      writer.write(indent + key + ": " + " \n");
      for(String line : (String[]) value) {
        writer.write("  " + indent + "- '" + line + "' \n");
      }
    } else {
      String toString = (String) value;
      if(!NumberUtils.isNumber(toString) && !toString.equalsIgnoreCase("true") && !toString.equalsIgnoreCase("false"))
        writer.write(indent + key + ": '" + value + "' \n");
      else writer.write(indent + key + ": " + value + " \n");
    }
  }

  @Override
  public String toString() {
    return "<ROW(" + key + ")>";
  }
}