package org.uninstal.yaml.objects;

import java.io.FileWriter;

public class YamlList extends YamlObject {
  
  private final String[] value;
  private final String[] comments;
  
  public YamlList(String path, String[] value, String[] comments) {
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
    writer.write(indent + key + ": " + " \n");
    for(String line : value) {
      writer.write("  " + indent + "- '" + line + "' \n");
    }
  }
  
  @Override
  public String toString() {
    return "<LIST(" + key + ")>";
  }
}