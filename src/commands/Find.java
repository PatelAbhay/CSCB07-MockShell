package commands;

import java.util.ArrayList;
import java.util.Arrays;

import data.FileSystemI;
import data.Node;


class Find extends DirectoryManager implements CommandI {
  
  public Find() {
    
  }
  
  public boolean isValid(String[] args) {
    if (args.length != 4) {
      return false;
    }
    else if (!args[0].equals("-type")) {
      return false;
    }
    else if (!args[1].equals("f") || !args[1].equals("d")) {
      return false;
    }
    else if (!args[2].equals("-name")) {
      return false;
    }
    else if (!args[3].equals(null)) {
      return false;
    }
    else {
      return true;
    }
  }

  @Override
  public String run(FileSystemI filesys, String[] args, String fullInput, boolean val) {
    ArrayList<String> paths = new ArrayList<String>();
    ArrayList<String> arguments = new ArrayList<String>(Arrays.asList(args));
    args = fullInput.split(" ");
    for (int i = 0; i < arguments.size(); i++) {
      paths.add(args[i]);
      if (i < args.length && args[i+1].equals("-type")) {
        break;
      }
    }
    
    if (isValid(args)) {
      for (String x : paths) {
        checkList(filesys, x, args[0], args[3]);
      }
    }
    System.out.println(paths);
    System.out.println(arguments);
    return null;
  }
  
  public void checkList(FileSystemI filesys, String path, String expression, String type) {
    String currPath = this.getCurrentPath(filesys);
    String[] splitPath = path.split("/");
    Cd newPath = new Cd();
    if (newPath.run(filesys, splitPath, path, true) != null){
      ArrayList<Node> toCheck = filesys.getCurrent().getList();
      for (int i = 0; i < toCheck.size(); i++) {
        if (type.equals("d")) {
          if (toCheck.get(i).isDir() && toCheck.get(i).getName().equals(expression)) {
            System.out.println(toCheck.get(i).getName());
          }
        }
        else if (type.equals("f")) {
          if (!toCheck.get(i).isDir() && toCheck.get(i).getName().equals(expression)) {
            System.out.println(toCheck.get(i).getName());
          }
        }
      }
    }
    
    
    
  }
  
  
  //public static void main(String[] args) {
  //  Find f = new Find();
  //  String[] g = {"/users/desktop", "/users/pics", "-type", "f", "lmao"};
  //  f.run(g, "/users/desktop /users/pics -type f 'lmao'", false);
  //  }

  
}