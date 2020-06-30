 package driver;

public class Cat extends FileManager {
  
  FileSystem filesys;
  DirectoryManager manager;
  
  public Cat() {
    this.filesys = FileSystem.getFileSys();
    this.manager = new DirectoryManager();

  }
  
  public void readFile(String[] filePaths) {   
    Node file = null;
    for(int i = 0; i < filePaths.length; i++) {
      if(filePaths[i].startsWith("C/")) {
        file = findFileGivenAbsolute(filePaths[i]);
      }else {
        file = findFileGivenRelative(filePaths[i]);
      }
      
      if(file != null) {
        System.out.println(file.content);
      }else {
        System.out.println("FileNotFoundError");
      }
      
      if(!(filePaths.length == 1 || i == filePaths.length - 1 )) {
        System.out.println("---------------------------------");
      }

    }
  }
}