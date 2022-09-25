package org.uninstal.yaml.test;

public class Main {
  
  public static void main(String[] args) {
    
    Config config = new Config("C:\\Program Files\\Java Projects\\saves 3\\Yaml", "test");
    config.setPrefix("[Test] ");
    config.loadOrCreate();
    config.debug();
  }
}