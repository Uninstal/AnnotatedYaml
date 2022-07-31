package org.uninstal.yaml.test;

public class Main {
  
  public static void main(String[] args) {
    
    Config config = new Config("E:\\Programs\\Eclipse\\saves 3\\Yaml", "test");
    config.setPrefix("[Test] ");
    config.loadOrCreate();
    config.debug();
  }
}