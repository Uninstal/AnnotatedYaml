package org.uninstal.yaml.objects;

import org.uninstal.yaml.YamlStrings;

import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

public class YamlSection extends YamlObject {
  
  private final List<YamlObject> objects;

  public YamlSection(String path) {
    super(path);
    this.objects = new ArrayList<>();
  }

  public List<YamlObject> getObjects() {
    return objects;
  }
  
  public void addObject(YamlObject object) {
    objects.add(object);
  }

  @Override
  public void create(FileWriter writer, int deep) throws Exception {
    if(!key.isEmpty())
      writer.write(YamlStrings.repeat(" ", deep * 2) + key + ": \n");
    for(YamlObject object : objects)
      object.create(writer, key.isEmpty() ? deep : deep + 1);
  }

  @Override
  public String toString() {
    return "<SECTION(" + key + ")>";
  }
}