package commands;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import data.FileSystemI;
import data.Node;

public class Load implements CommandI{

  private FileReader fileReader;
  private BufferedReader reader;
  private String filePath;
  private String output;
  private ErrorHandler error;
  
  public Load(){
    this.error = new ErrorHandler();
    this.output = null;
  }
  
  /*
   * Things to work on:
   *    - Error Checking
   *    - JavaDoc
   *    - Test cases
  */

  @Override
  public String run(FileSystemI filesys, String[] args, String fullInput, boolean val) {
    
    if(args[0].length() > 0 && checkCommandLog(filesys)) {
      filePath = formatArguments(args);
      try {
        fileReader = new FileReader(filePath);
        reader = new BufferedReader(fileReader);
        String line = reader.readLine();

        while(line != null) {
          if(line.equals("NODES")) uploadNodes(line, filesys);
          else if(line.equals("COMMAND LOG")) {
            uploadCommandLog(line, filesys);
          }
          line = reader.readLine();
        }

      } catch (FileNotFoundException e) { //from trying to find file path 
        e.printStackTrace();
      } catch (IOException e) { // from reading a line
        e.printStackTrace();
      }
    }
    else{
      if(checkCommandLog(filesys)) output = error.getError("No parameters provided", fullInput);
      else System.out.println("Error (load was not the first command inputted)");
    }
    
    return output;
  }
  
  private boolean checkCommandLog(FileSystemI filesys) {
    if(filesys.getCommandLog().size() == 1) return true;
    return false;
  }
  
  private void uploadNodes(String line, FileSystemI filesys) {
    try {
      line = reader.readLine();
      line = reader.readLine();
      while(!line.equals("}")) {
        String[] nodeInformation = new String[4];
        for(int i = 0; i < nodeInformation.length; i++) {
          nodeInformation[i] = line;
          line = reader.readLine();
        }
        createNode(nodeInformation, filesys);
        line = reader.readLine();
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
  
  private void uploadCommandLog(String line, FileSystemI filesys) {
    try {
      line = reader.readLine();
      line = reader.readLine().trim().replaceAll("\"", "");
      while(!line.equals("}")) {
        filesys.getCommandLog().add(line);
        line = reader.readLine().trim().replaceAll("\"", "");
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
  
  private void createNode(String[] nodeInformation, FileSystemI filesys) {
    String[] parsedNodeInformation = new String[4];
    for(int i = 0; i < nodeInformation.length; i++) {
      String parseInfo = nodeInformation[i].replaceAll("\"", "").trim();
      parsedNodeInformation[i] = parseInfo.split(":")[1].trim();
    }
    if(!parsedNodeInformation[0].equals(filesys.getRoot().getName())) {
      Node newNode = new Node();
      newNode.setName(parsedNodeInformation[0]);
      newNode.setDir(Boolean.valueOf(parsedNodeInformation[1]));
      newNode.setContent(parsedNodeInformation[3]);
      if(parsedNodeInformation[2].equals(filesys.getCurrent().getName()))
        filesys.addToDirectory(newNode);
      else{
        addNodeToFileSystem(newNode, parsedNodeInformation[2], filesys);
      }
    }
  }
  
  private void addNodeToFileSystem(Node newNode, String parentName, FileSystemI filesys) {
    filesys.assignCurrent(filesys.getRoot());
    traverseFileSystem(filesys.getCurrent(), parentName, newNode,filesys);
  }
  
  private void traverseFileSystem(Node current, String desiredParentName, Node newNode, FileSystemI filesys) {
    if(current.getName().equals(desiredParentName)) {
      newNode.setParent(current);
      filesys.addToDirectory(newNode);
    }
    else {
      for(int i = 0; i < current.getList().size(); i++) {
        filesys.assignCurrent(current.getList().get(i));
        traverseFileSystem(filesys.getCurrent(), desiredParentName, newNode, filesys);
      }
    }
  }
  
  private String formatArguments(String[] args) {
    if(args[0].contains("/")) return args[0].replace("/", "\\\\");
    if(args[0].contains("\\")) return args[0].replace("\\", "\\\\");
    return args[0];
  }
  
}
