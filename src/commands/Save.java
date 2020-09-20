// **********************************************************
// Assignment2:
// Student1:
// Author: Shawn Santhoshgeorge
// Github: @ShawnGeorge03
//
// Student2:
// Author: Keshavaa Shaiskandan
// Github: @skeshavaa
//
// Student3:
// Author: Tirth Patel
// Github:@ProgramTP
//
// Student4:
// Author: Abhay Patel
// Github: @PatelAbhay
//
//
// Honor Code: I pledge that this program represents my own
// program code and that I have coded on my own. I received
// help from no one in designing and debugging my program.
// I have also read the plagiarism section in the course info
// sheet of CSC B07 and understand the consequences.
// *********************************************************
package commands;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Deque;

import data.*;
import errors.FileException;
import errors.InvalidArgsProvidedException;
import errors.InvalidRedirectionError;

/**
 * Class Save is responsible for saving the current filesystem to a json file on the users computer
 */
public class Save implements CommandI {

  /**
   * Declare instance variable of FileWriter to write to the file
   */
  private FileWriter writer;
  
  /**
   * Declare instance variable of RedirectionManager to handle the redirection operations
   */
  private RedirectionManager redirect;

  /**
   * Declare instance variable of String to hold the filepath
   */
  private String filePath;

  /**
   * Declare instance variable of String to hold the output of the command (error if error occurs else null)
   */
  private String output;


  /**
   * Declare instance variable of String to hold the contents of the json file (only the node data)
   */
  private String nodes;

  /**
   * Declare instance variable of String to hold the contents of the json file (only the filesystem)
   */
  private String filesystem;

  /**
   * Declare instance variable of String to hold the contents of the json file (only the command log)
   */
  private String commandLog;

  /**
   * Declare instance variable of String to hold the contents of the json file (only the directory stack)
   */
  private String directoryStack;

  /**
   * Declare instance variable of String to hold the contents of the json file (only the current path)
   */
  private String currentPath;

  /**
   * Declare instance variable of String to hold the current directory stack
   */
  private String stackString;

  /**
   * Constructor for Save that initializes instance variables
   */
  public Save() {
    //initialize the RedirectionManager
    this.redirect = new RedirectionManager();
    //initialize the String output
    this.output = null;
    //initialize the String nodes
    this.nodes = "";
    //initialize the String filesystem
    this.filesystem = "";
    //initialize the String commandLog
    this.commandLog = "";
    //initialize the String directoryStack
    this.directoryStack = "";
    //initialize the String currentPath
    this.currentPath = "";
    //initialize the String stackString
    this.stackString = "";
  }

  /**
   * Method that checks if the parameters provided by the user is considered valid or not. 
   * If the user inputs more than 1 parameter for the command then it returns false.
   * If the user simply inputs the command name and no parameter then returns false.
   * Returns true if the user inputs only 1 parameter after the command.
   * 
   * @param fs  FileSystem Object that stores the current filesystem
   * @param arguments  String array that holds the paramters that the user inputted
   * @param fullInput  String object that stores the full input provided by user
   * 
   * @throws InvalidArgsProvidedException if the user provides any invalid arguments
   * 
   * @return boolean  false if the user inputed no parameters or more than 1 parameter
   */
  @Override
	public boolean checkArgs(FileSystemI fs, String[] arguments, String fullInput) throws InvalidArgsProvidedException {
    //checks if user inputted a parameter or not
    if(arguments.length == 0){ 
      throw new InvalidArgsProvidedException("Error : No parameters provided");
    }
    //checks if user inputted more than one parameter
    else if(arguments.length > 1){  
      throw new InvalidArgsProvidedException("Error : Multiple Parameters have been provided : " + String.join(" ", arguments) + " Only one is required");
    }
    return true;
	}

  /**
   * Method that creates the file on the users computer and writes to the file. 
   * If the user tries to use redirection, the method returns an error.
   * If the user inputted invalid parameters, invalid filepath, or invalid filename then method returns corresponding error.
   * Creates file and writes the data as sections: NODES, FILESYSTEM, COMMAND LOG, DIRECTORY STACK, CURRENT PATH
   * NODES section holds the data associated with each individual node
   * FILESYSTEM section visually displays the saved FileSystem (similar to tree)
   * COMMAND LOG section stores the command log of the saved FileSystem  
   * DIRECTORY STACK
   * CURRENT PATH
   * If no errors occur, method returns null
   * 
   * @param fs  FileSystem Object that stores the current filesystem
   * @param arguments  String array that holds the paramters that the user inputted
   * @param fullInput  String object that stores the full input provided by user
   * @param val  boolean value that is true if we are in speakMode and false otherwise
   * @return String  if an error occured then the error message is returned, else null is returned
   */ 
  @Override
  public String run(FileSystemI filesys, String[] arguments, String fullInput, boolean val) {
    //Seperates the parameters from everything else from the user input
    String[] args = redirect.setParams(fullInput);
    try {
      //checks if user tries redirection
      redirect.isRedirectionableCommand(fullInput); 
      //checks if the arguments are valid
      if(checkArgs(filesys, args, fullInput)){ 
        //formats the filepath to work (since windows requires \\ to be used in the path instead of / or \)
        filePath = formatArguments(args);
        try {
          //checks for valid filename
          validateFileName(filesys, fullInput);
          writer = new FileWriter(filePath);
          writeToFile(filesys, writer, args);
          writer.close();
        } 
        // Invalid arguments were provided
        catch(InvalidArgsProvidedException e1){
          return e1.getLocalizedMessage();
        // Could not find file/Invalid Path was provided
        }catch (IOException e1) {
          output = "Error: Invalid Path : " + args[0];
        }
      }
    } 
    // Invalid arguments were provided
    catch (InvalidArgsProvidedException e) {
      return e.getLocalizedMessage();
    }
    return output;
  }

  /**
   * Method that writes to the different sections to the json file
   * Writes each section by using helper functions that add the information while the method creates teh sections
   * Sets the output to an error if the iser inputs an invalid path
   * 
   * @param filesys  FileSystem Object that stores the current filesystem
   * @param writer  FileWriter object that is used to write to the json file
   * @param arguments  String array that holds the paramters that the user inputted
   */ 
  private void writeToFile(FileSystemI filesys, FileWriter writer, String[] args) {
    //Adds the node information of every node to the file
    try {
      writer.write("NODES\n{\n");
      storeNodeInformation(writer, filesys);
      writer.write("}");
      //Adds the filesystem structure to the file (similar to tree)
      writer.write("\n\nFILESYSTEM\n{\n");
      storeFileSystem(writer, filesys);
      writer.write("}");
      //Adds the command log to the file
      writer.write("\n\nCOMMAND LOG\n{\n");
      storeCommandHistoryToFile(writer, filesys);
      writer.write("}");
      //Adds the directory stack to the file
      writer.write("\n\nDIRECTORY STACK\n{\n");
      addCurrentDirectoryStack(writer, filesys);
      writer.write("}");
      //Adds the current path to the file
      writer.write("\n\nCURRENT PATH\n{\n");
      currentPath = "\t\"" + filesys.getCurrentPath() + "\"\n";
      writer.write("\t\"" + filesys.getCurrentPath() + "\"\n");
      writer.write("}");
    } catch (IOException e) {
      output = "Error: Invalid Path : " + args[0];
    }
  }

  private void addCurrentDirectoryStack(FileWriter writer, FileSystemI filesys){
    //creates teh stack
    Deque<String> stack = filesys.getStack();
    //recursive helper function to get the stack
    getStackAsString(stack);
    try {
      //if stack string is not null
      if(stackString != null){
        //add to file
        writer.write(stackString);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void getStackAsString(Deque<String> stack){
    // If stack is empty then return  
    if(stack.isEmpty()) return;  
    //Gets the string 
    String element = stack.peek();  
    //Pop the top element of the stack  
    stack.pop();  
    //Recursively call the function getStackAsString  
    getStackAsString(stack);  
    //Append the stack element
    stackString += "\t\"" + element + "\"\n";
    //Push the same element onto the stack to keep the order
    stack.push(element);  
  }

  /**
   * Method that checks if the file name that the user inputted is a valid file name. If the file name
   * contains illegal characters then this method returns false. If the file name does not contain any
   * illegal characters then it returns true.
   * 
   * @param fileName  String that stores the file name that the user inputted
   * @return boolean false if file name contains illegal characters, otherwise returns true
   */
  public boolean isValidName(String fileName) {
    String[] invalidChars = {"/", "\\s+", "!", "@", "#", "$", "%", "^",
        "&", "*", "(", ")", "{", "}", "~", "|", "<", ">", "?", "'", "[", "]"};
    for (int i = 0; i < invalidChars.length; i++) {
      if (fileName.contains(invalidChars[i])) {
        return false;
      }
    }
    return true;
  }

  /**
   * Method that grabs the file content of the file being saved. 
   * Used primarily to test if the nodes are being correctly stored.
   * Returns the Node data from the NODES section as that essentially returns the filesystem structure
   * If the user tries to use redirection, the method returns an error.
   * If the user inputted invalid parameters, invalid filepath, or invalid filename then method returns corresponding error.
   * If no errors occur, then the method returns null
   * 
   * @param fs  FileSystem Object that stores the current filesystem
   * @param arguments  String array that holds the paramters that the user inputted
   * @param fullInput  String object that stores the full input provided by user
   * @param section  int value that tells us which section we want to return (1-5)(starts from 1 which is NODES)
   * @return String  if an error occured then the error message is returned, else the fileContents are retured
   */
  public String getFileContent(FileSystemI filesys, String[] args, String fullInput, int section) {
    try {
      //Checks if redirection is allowed 
      redirect.isRedirectionableCommand(fullInput);
    } 
    //Redirection is not allowed so throw an error
    catch (InvalidRedirectionError e) {
      return e.getLocalizedMessage();
    }
    //Output hold the result of the command execution
    output = run(filesys, args, fullInput, false);
    //If there was an error then return the error
    if(output != null){
      if(output.startsWith("Error:") || output.startsWith("Error :")) return output;
    }
    //return the fileContents (according to the section)
    switch(section){
      case 1: return nodes.trim().replaceAll("\"", "");
      case 2: return filesystem.trim().replaceAll("\"", "");
      case 3: return commandLog.trim().replaceAll("\"", "");
      case 4: return directoryStack.trim().replaceAll("\"", "");
      case 5: return currentPath.trim().replaceAll("\"", "");
    }
    return null;
  }
  
  /**
   * Method that checks if the filename/filepath that the user inputted is valid.
   * If the filename is invalid, then method throws FileException.
   * If the file type is not a json file, then the method throws FileException
   * If the user does not specify a file type, then by default it is a json file
   * 
   * @param filesys  FileSystem Object that stores the current filesystem
   * @param fullInput  String object that stores the full input provided by user
   */
  private void validateFileName(FileSystemI filesys, String fullInput) throws FileException{
    //Checks of the filename is valid
    if(!checkFileName(filePath, filesys)) {
      throw new FileException("Error: Invalid File : " + fullInput);
    }
    //If the fileName contains a file type 
    if(filePath.contains(".")){
      //Checks if json file
      if(!filePath.substring(filePath.length()-5, filePath.length()).equals(".json")) {
        throw new FileException("Error: Invalid File : " + fullInput);
      }
    }
    //there is no .json at the end so append .json to the fileName
    else filePath += ".json";
  }

  /**
   * Method that checks if the filename is a valid filename. 
   * If the filename contains any invalid characters then the method returns false.
   * If the filename does not conatin any invalid characters then the method returns true.
   * 
   * @param filePath  String object that stores the full input provided by user
   * @param filesys  FileSystem Object that stores the current filesystem
   * @return  boolean that is false if filename contains an invalid character, else returns true
   */
  private boolean checkFileName(String filePath, FileSystemI filesys){
    String fileName = "";
    //If filename is given as absolute path then grab the fileName
    if(filePath.contains("\\")) filePath.substring(filePath.lastIndexOf("\\"), filePath.length());
    else fileName = filePath;
    //Check if filename is valid
    if(isValidName(fileName)) return true;
    return false;
  }

  /**
   * Method that formats the user input to make sure the file path is valid in Windows
   * Windows require path to be split using \\ instead of \ or /
   * This method replaces / and \ with \\ for full compatibility
   * 
   * @param args  String array that stores the arguments inputted by the user
   * @return  String that is the formatted version of the user inputted arguments
   */
  private String formatArguments(String[] args) {
    //replaces / with \\
    if(args[0].contains("/")) return args[0].replace("/", "\\\\");
    //replaces \ with \\
    if(args[0].contains("\\")) return args[0].replace("\\", "\\\\");
    return args[0];
  }
  
  /**
   * Method that adds the FILESYSTEM section to the json file
   * Starts from root and calls helper function that traverses the filesystem
   * 
   * @param writer  FileWriter object that is used to write to the file
   * @param filesys  FileSystem object that stores the filesystem being saved
   */
  private void storeFileSystem(FileWriter writer, FileSystemI filesys) {
    Node root = filesys.getRoot();
    //Traverses the filesystem using Depth First Search 
    traverseFileSystem(root, writer, 1, 1);
  }
  
  /**
   * Method that adds the node data to the NODES section in the json file
   * Starts from root and calls helper function that traverses the filesystem
   * 
   * @param writer  FileWriter object that is used to write to the file
   * @param filesys  FileSystem object that stores the filesystem being saved
   */
  private void storeNodeInformation(FileWriter writer, FileSystemI filesys){
    Node root = filesys.getRoot();
    //Traverses the filesystem using Depth First Search
    traverseFileSystem(root, writer, 1, 2);
  }
  
  /**
   * Method that traverses the file system and adds either the node data or visual representation of filesystem 
   * Uses Depth First Search to go through the file system and adds the corresponding information depending on the mode paramter
   * If mode is 1, then the method writes the Visual Display of FileSystem to the FILESYSTEM section 
   * If mode is 2, then the method writes the data corresponding to each node to the NODES section 
   * 
   * @param root  Node object that represents the current node on the FileSystem traversal
   * @param writer  FileWriter object that is used to write to the file
   * @param depth  int value that starts with 1 and increases as we go deeper into the FileSystem
   * @param mode  int value that differentiates between writing to the NODES section or FILESYSTEM section of the json file
   */
  private void traverseFileSystem(Node root, FileWriter writer, int depth, int mode){
    //If we want to add the node to our filesystem section of the file
    if(mode == 1) addNodeNameToFile(root, writer, depth);
    //If we want to add the individual node information to out nodes section of the file
    if(mode == 2) addNodeInformationToFile(root, writer, depth);
    //for loop to iterate through the arraylist of other nodes
    for(int i = 0; i < root.getList().size(); i++) {
      //needs this to add a depth when going deeper into filesystem
      depth += 1;
      //traverse filesystem again
      traverseFileSystem(root.getList().get(i), writer, depth, mode);
      //when we return from the previous call, the depth is one less
      depth -= 1;
    }
  }
  
  /**
   * Method that adds the node name to the FILESYSTEM section
   * Adds the current node to the visual filesystem representation
   * 
   * @param current  Node object that represents the current node on the FileSystem traversal
   * @param writer  FileWriter object that is used to write to the file
   * @param depth  int value that starts with 1 and increases as we go deeper into the FileSystem
   */
  private void addNodeNameToFile(Node current, FileWriter writer, int depth) {
    try {
      String result = "";
      //for loop that adds a tab representing the depth
      for(int i = 0; i < depth; i++) {
        result += "\t";
      }
      //adds the node to the filesystem section of the file
      filesystem += result + "\"" + current.getName() + "\"\n";
      writer.write(result + "\"" + current.getName() + "\"\n");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
  
  /**
   * Method that adds the node name to the NODES section
   * Adds the current node and the corresponding data to the json file
   * 
   * @param current  Node object that represents the current node on the FileSystem traversal
   * @param writer  FileWriter object that is used to write to the file
   * @param depth  int value that starts with 1 and increases as we go deeper into the FileSystem
   */
  private void addNodeInformationToFile(Node current, FileWriter writer, int depth) {
    try {
      //Adds the node information to the nodes section of the file
      nodes += "\t\"name\" : \"" + current.getName() + "\"\n";
      nodes += "\t\"isDir\" : \"" + current.getisDir() + "\"\n";
      writer.write("\t\"name\" : \"" + current.getName() + "\"\n");
      writer.write("\t\"isDir\" : \"" + current.getisDir() + "\"\n");
      //If the parent of the node is not null then we add the parent name
      if(current.getParent() != null){
        nodes += "\t\"parent\" : \"" + current.getParent().getName() + "\"\n";
        writer.write("\t\"parent\" : \"" + current.getParent().getName() + "\"\n");
      }
      //Else set the parent to be null (only case this occurs is the root node)
      else{
        nodes += "\t\"parent\" : \"null\"\n";
        writer.write("\t\"parent\" : \"null\"\n");
      }
      writer.write("\t\"content\" : \"" + getContentOfNode(current) + "\"\n\n");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
  
  /**
   * Method that gets the contents of the node to be added to the file
   * Replaces every instance of the new line character to make sure the content fits on one line
   * 
   * @param current  Node object that represents the current node on the FileSystem traversal
   * @return  String object that holds the new formatted contents of the node
   */
  private String getContentOfNode(Node current){
    String finalOutput = null;
    //grabs current content of node
    String content = current.getContent();
    //replaces all newline characters with "\n" so it would fit in one line in the json file
    if(content != null)finalOutput = content.replaceAll("\n", "\\\\n");
    return finalOutput;
  }

  /**
   * Method that stores the command log to the COMMAND LOG section 
   * Loops through the command log arraylist in FileSystem object and adds each index to the file
   * 
   * @param writer  FileWriter object that is used to write to the file
   * @param filesys  FileSystem object that stores the filesystem being saved
   */
  private void storeCommandHistoryToFile(FileWriter writer, FileSystemI filesys) {
    //loops through the command log of the filesystem
    for(int i = 0; i < filesys.getCommandLog().size(); i++) {
      try {
        //adds the command to the command log section of the file
        commandLog += "\t\"" + filesys.getCommandLog().get(i) + "\"\n";
        writer.write("\t\"" + filesys.getCommandLog().get(i) + "\"\n");
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }
  
}
