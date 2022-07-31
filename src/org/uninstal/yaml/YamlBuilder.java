package org.uninstal.yaml;

import org.uninstal.yaml.objects.YamlRow;
import org.uninstal.yaml.objects.YamlSection;

import java.io.FileWriter;
import java.util.*;

public class YamlBuilder {
  
  private final YamlSection main;
  private final List<YamlRow> rows;
  
  public YamlBuilder(List<YamlRow> rows) {
    this.main = new YamlSection("");
    this.rows = rows;
  }
  
  public void build(FileWriter writer) {
    Map<String, YamlSection> temp = buildSections();

    for(YamlRow row : rows)
      temp.get(row.getPath()).addObject(row);
    
    try {
      main.create(writer, 0);
    } catch(Exception e) {
      e.printStackTrace();
    }
  }
  
  private Map<String, YamlSection> buildSections() {
    
    Map<String, YamlSection> sections = new HashMap<>();
    sections.put("", main);
    
    for(YamlRow row : rows) {
      
      String path = row.getPath();
      if(sections.containsKey(path))
        continue;
            StringBuilder pathBuilder = new StringBuilder();
      String[] names = path.split("\\.");

      for(int i = 0; i < names.length; i++) {
        pathBuilder.append(i != 0 ? "." : "").append(names[i]);
        if(!sections.containsKey(pathBuilder.toString())) {
          YamlSection section = new YamlSection(pathBuilder.toString());
          (i != 0 ? sections.get(pathBuilder.substring(0, pathBuilder.lastIndexOf("."))) : main)
            .addObject(section);
          sections.put(pathBuilder.toString(), section);
        }
      }
    }
    
    return sections;
  }
}