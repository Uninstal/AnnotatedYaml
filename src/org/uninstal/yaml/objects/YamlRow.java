package org.uninstal.yaml.objects;

import org.uninstal.yaml.YamlStrings;

import java.io.FileWriter;

public class YamlRow extends YamlObject {
  
  private final String value;
  private final String[] comments;

  public YamlRow(String path, String value, String[] comments) {
    super(path);
    this.value = value;
    this.comments = comments;
  }

  @Override
  public void create(FileWriter writer, int deep) throws Exception {
    String indent = YamlStrings.repeat(" ", deep * 2);
    if(comments != null && comments.length > 0) {
      for(String comment : comments)
        writer.write(indent + "# " + comment + " \n");
    }
    writer.write(indent + key + ": " + value + " \n");
  }

  @Override
  public String toString() {
    return "<ROW(" + key + ")>";
  }
}