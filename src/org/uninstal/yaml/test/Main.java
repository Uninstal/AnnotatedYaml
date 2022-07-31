package org.uninstal.yaml.test;

public class Main {

  public static void main(String[] args) {
    
    Config config = new Config();
    config.setPrefix("[Test] ");
    config.create("E:\\Programs\\Eclipse\\saves 3\\Yaml", "test");
    System.out.println(Config.TEST);
  }
}