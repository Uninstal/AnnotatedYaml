package org.uninstal.yaml;

import org.bukkit.configuration.file.YamlConfiguration;
import org.uninstal.yaml.objects.YamlObject;
import org.uninstal.yaml.YamlAnnotations.*;
import org.uninstal.yaml.objects.YamlRow;
import org.uninstal.yaml.serialize.YamlSerializers;

import java.io.File;
import java.io.FileWriter;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public abstract class Yaml {
  
  protected final File folder;
  protected final String fileName;
  
  protected File file;
  protected YamlConfiguration yaml;
  protected String prefix = "[Yaml] ";
  protected long loadTime = -1;
  
  public Yaml(String folder, String fileName) {
    this.fileName = fileName;
    this.folder = new File(folder);
    this.file = new File(folder, fileName + ".yml");
    this.onPrepare();
  }

  public void setPrefix(String prefix) {
    this.prefix = prefix;
  }

  public void onPrepare() {}
  public void onLoad() {}
  
  public void loadOrCreate() {
    
    if(!file.exists()) create();
    else load();
  }
  
  public void create() {
    long start = System.currentTimeMillis();
    
    deleteFile();
    createFile();

    List<YamlObject> rows = new ArrayList<>();
    for(Field field : getClass().getDeclaredFields()) {
      if(field.isAnnotationPresent(Ignore.class))
        continue;

      Path pathAnnotation = field.getAnnotation(Path.class);
      Comment commentAnnotation = field.getAnnotation(Comment.class);

      if(pathAnnotation == null) {
        log("[ERROR] Path for " + field.getName() + " is unspecified.");
        continue;
      }

      String path = pathAnnotation.value();
      String[] comments = commentAnnotation == null ? null : commentAnnotation.value();
      Object value = getValue(field);

      if(value == null) {
        if(!field.isAnnotationPresent(Nullable.class))
          log("[ERROR] Value for " + field.getName() + " is unspecified.");
        continue;
      }
      
      rows.add(new YamlRow(path, value, comments));
      Object deserial = field.isAnnotationPresent(CustomSerializable.class)
        ? YamlSerializers.deserialize(field.getAnnotation(CustomSerializable.class).id(), value)
        : YamlSerializers.deserialize(field.getType(), value);

      if(deserial == null) log("[ERROR] Cannot deserialize value for \"" + field.getName() + "\"");
      else setFieldValue(field, deserial);
    }
    
    try {
      
      FileWriter fw = new FileWriter(file);
      new YamlBuilder(rows).build(fw);
      fw.flush();
      fw.close();

    } catch (Exception e) {
      e.printStackTrace();
    }
    
    loadTime = System.currentTimeMillis() - start;
    log("File \"" + fileName + ".yml\" created in " + loadTime + " ms");
    onLoad();
  }
  
  public void load() {
    long start = System.currentTimeMillis();
    yaml = YamlConfiguration.loadConfiguration(file);

    for(Field field : getClass().getDeclaredFields()) {
      if(field.isAnnotationPresent(Ignore.class))
        continue;

      Path pathAnnotation = field.getAnnotation(Path.class);
      if(pathAnnotation == null) {
        log("[ERROR] Path for " + field.getName() + " is unspecified.");
        continue;
      }

      String path = pathAnnotation.value();
      Object value = getValue(path);

      if(value == null) {
        if(!field.isAnnotationPresent(Nullable.class))
          log("[ERROR] Value for " + field.getName() + " is unspecified.");
        continue;
      }
      
      Object deserial = field.isAnnotationPresent(CustomSerializable.class)
        ? YamlSerializers.deserialize(field.getAnnotation(CustomSerializable.class).id(), value)
        : YamlSerializers.deserialize(field.getType(), value);

      if(deserial == null) log("[ERROR] Cannot deserialize value for \"" + field.getName() + "\"");
      else setFieldValue(field, deserial);
    }
    
    loadTime = System.currentTimeMillis() - start;
    log("File \"" + fileName + ".yml\" loaded in " + loadTime + " ms");
    onLoad();
  }
  
  public void createFile() {
    
    try {
      
      if(!folder.exists() && !folder.mkdir())
        log("[ERROR] Error with creating folder: " + folder.getPath());

      else if(!file.exists() && !file.createNewFile())
        log("[ERROR] Error with creating file " + fileName + ".yml");
      
    } catch(Exception e) {
      e.printStackTrace();
    }
  }

  public void deleteFile() {
    
    try {
      
      if(file.exists() && !file.delete())
        log("[ERROR] Error with deleting file " + fileName + ".yml");
      
    } catch(Exception e) {
      e.printStackTrace();
    }
  }
  
  public void debug() {
    
    try {
      
      for(Field field : getClass().getDeclaredFields()) {
        if(!field.isAnnotationPresent(Ignore.class))
          log(field.getName() + " = " + field.get(getClass()));
      }
      
    } catch(Exception e) {
      e.printStackTrace();
    }
  }
  
  private Object getValue(Field field) {
    if(field.isAnnotationPresent(Value.class)) return field.getAnnotation(Value.class).value();
    else if(field.isAnnotationPresent(ListValue.class)) return field.getAnnotation(ListValue.class).value();
    else return null;
  }

  private Object getValue(String path) {
    if(!yaml.contains(path)) return null;
    else if(yaml.isList(path)) return yaml.getStringList(path).toArray(new String[0]);
    else return yaml.getString(path);
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
  
  private void log(String message) {
    System.out.println(prefix + message);
  }
}