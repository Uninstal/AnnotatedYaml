package org.uninstal.yaml;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.uninstal.yaml.objects.YamlRow;
import org.uninstal.yaml.YamlAnnotations.*;
import org.uninstal.yaml.serialize.YamlSerializers;

import java.io.File;
import java.io.FileWriter;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public abstract class Yaml {
  
  protected String prefix = "[Yaml] ";
  protected long loadTime = -1;
  protected File file;
  protected YamlConfiguration yaml;

  public void setPrefix(String prefix) {
    this.prefix = prefix;
  }

  public void onPrepare() {}
  public void onLoad() {}
  
  public void create(JavaPlugin plugin, String fileName) {
    create(plugin.getDataFolder().toString(), fileName);
  }
  
  public void create(String folderPath, String fileName) {
    onPrepare();
    long start = System.currentTimeMillis();
    
    try {
      File folder = new File(folderPath);
      if(!folder.exists() && !folder.mkdir()) {
        System.out.println("[ERROR] Error with creating folder: " + folder.getPath());
        return;
      }
      
      file = new File(folderPath, fileName + ".yml");
      if(!file.exists() && !file.createNewFile()) {
        System.out.println("[ERROR] Error with creating file " + fileName + ".yml");
        return;
      }
      
      List<YamlRow> rows = new ArrayList<>();
      long start1 = System.currentTimeMillis();
      Field[] fields = getClass().getDeclaredFields();
      for(Field field : fields) {
        if(field.isAnnotationPresent(Ignore.class))
          continue;
        
        Path pathAnnotation = field.getAnnotation(Path.class);
        Value valueAnnotation = field.getAnnotation(Value.class);
        Comment commentAnnotation = field.getAnnotation(Comment.class);
        
        if(pathAnnotation == null) System.out.println("[ERROR] Path for " + field.getName() + " is null.");
        else if(valueAnnotation == null) System.out.println("[ERROR] Value for " + field.getName() + " is null.");
        else {
          String path = pathAnnotation.value();
          String value = valueAnnotation.value();
          String[] comments = commentAnnotation == null ? null : commentAnnotation.value();
          rows.add(new YamlRow(path, value, comments));
          
          Object deserial = field.isAnnotationPresent(CustomSerializable.class)
            ? YamlSerializers.deserialize(field.getAnnotation(CustomSerializable.class).id(), value)
            : YamlSerializers.deserialize(field.getType(), value);
          
          if(deserial == null) System.out.println("[ERROR] Cannot deserialize value for \"" + field.getName() + "\"");
          else setFieldValue(field, deserial);
        }
      }
      
      FileWriter fw = new FileWriter(file);
      new YamlBuilder(rows).build(fw);
      fw.flush();
      fw.close();

      // yaml = YamlConfiguration.loadConfiguration(file);

    } catch (Exception e) {
      e.printStackTrace();
    }
    
    loadTime = System.currentTimeMillis() - start;
    System.out.println(prefix + "File \"" + fileName + ".yml\" loaded in " + loadTime + " ms");
    onLoad();
  }
  
  private void setFieldValue(Field field, Object value) {
    
    try {
      if(!field.isAccessible()) {
        field.setAccessible(true);
        field.set(field, value);
        field.setAccessible(false);
      } else field.set(field, value);
    } catch(Exception e) {
      e.printStackTrace();
    }
  }
}