package commands;

import java.io.FileWriter;
import java.io.IOException;
import data.*;

public class Save implements CommandI{

  private FileWriter writer;
  private String filePath;
  private ErrorHandler error;
  private String output;
  
  public Save(){
    this.error = new ErrorHandler();
    this.output = null;
  }
  
  /*
   * Things to work on:
   *    - JavaDoc
   *    - Test cases
  */
  
  @Override
  public String run(FileSystemI filesys, String[] args, String fullInput, boolean val) {
    if(args.length > 0) {
      filePath = formatArguments(args);
      validateFileName(filesys, fullInput);
      if(output != null) return output;
      try {
        writer = new FileWriter(filePath); 
        
        writer.write("NODES\n{\n");
        storeNodeInformation(writer, filesys);
        writer.write("}");
        
        writer.write("\n\nFILESYSTEM\n{\n");
        storeFileSystem(writer, filesys);
        writer.write("}");
             
        writer.write("\n\nCOMMAND LOG\n{\n");
        storeCommandHistoryToFile(writer, filesys);
        writer.write("}");
        
        writer.close();
      } catch(IOException e) { //could not find file
        output = "Error: Invalid Path : " + args[0];
      }
    }
    else output = error.getError("No parameters provided", fullInput);
    return output;
  }
  
  private void validateFileName(FileSystemI filesys, String fullInput){
    if(!checkFileName(filePath, filesys)) {
      output = error.getError("Invalid File", fullInput);
    }
    if(filePath.contains(".")){
      if(!filePath.substring(filePath.length()-5, filePath.length()).equals(".json")) {
        output = error.getError("Invalid File", fullInput);
      }
    }
    else filePath += ".json";
  }

  private boolean checkFileName(String filePath, FileSystemI filesys){
    String fileName = "";
    if(filePath.contains("\\")) filePath.substring(filePath.lastIndexOf("\\"), filePath.length());
    else fileName = filePath;
    if(filesys.isValidName(fileName)) return true;
    return false;
  }

  private String formatArguments(String[] args) {
    if(args[0].contains("/")) return args[0].replace("/", "\\\\");
    if(args[0].contains("\\")) return args[0].replace("\\", "\\\\");
    return args[0];
  }
  
  private void storeFileSystem(FileWriter writer, FileSystemI filesys) {
    Node root = filesys.getRoot();
    traverseFileSystem(root, writer, 1, 1);
  }
  
  private void storeNodeInformation(FileWriter writer, FileSystemI filesys){
    Node root = filesys.getRoot();
    traverseFileSystem(root, writer, 1, 2);
  }
  
  private void traverseFileSystem(Node root, FileWriter writer, int depth, int mode){
    if(mode == 1) addNodeNameToFile(root, writer, depth);
    if(mode == 2) addNodeInformationToFile(root, writer, depth);
    for(int i = 0; i < root.getList().size(); i++) {
      depth += 1;
      traverseFileSystem(root.getList().get(i), writer, depth, mode);
      depth -= 1;
    }
  }
  
  private void addNodeNameToFile(Node current, FileWriter writer, int depth) {
    try {
      String output = "";
      for(int i = 0; i < depth; i++) {
        output += "\t";
      }
      writer.write(output + "\"" + current.getName() + "\"\n");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
  
  private void addNodeInformationToFile(Node current, FileWriter writer, int depth) {
    try {
      writer.write("\t\"name\" : \"" + current.getName() + "\"\n");
      writer.write("\t\"isDir\" : \"" + current.getisDir() + "\"\n");
      if(current.getParent() != null)
        writer.write("\t\"parent\" : \"" + current.getParent().getName() + "\"\n");
      else
        writer.write("\t\"parent\" : \"null\"\n");
      writer.write("\t\"content\" : \"" + current.getContent() + "\"\n\n");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
  
  private void storeCommandHistoryToFile(FileWriter writer, FileSystemI filesys) {
    for(int i = 0; i < filesys.getCommandLog().size(); i++) {
      try {
        writer.write("\t\"" + filesys.getCommandLog().get(i) + "\"\n");
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }
  
}
