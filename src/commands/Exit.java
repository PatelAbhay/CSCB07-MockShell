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

import data.FileSystemI;
import errors.InvalidArgsProvidedException;

/**
 * Class Exit handles exiting the JShell
 */
public class Exit implements CommandI {

  //Declares a RedirectionManager object
  private RedirectionManager rManager;

  /**
   * Constructor for class Exit which initalizes instance variables
   */
  public Exit() {
    //Initializes the RedirectionManager object
    this.rManager = new RedirectionManager();
  }

  /**
   * Starting run method which checks if arguments were given, then passes it to
   * another run method which processes the command
   * 
   * @param filesys   refrence of FileSystemI object (MockFileSystem or FileSystem)
   * @param args      the string array of arguments
   * @param fullInput the full line of input that the user gives into jshell
   * @param val       the boolean we are using
   * 
   * @return any error messages if there are any or null 
   */
  @Override
  public String run(FileSystemI filesys,  String[] args,  String fullInput, boolean val) {
    //Seperates the parameters from everything else from the user input
    String[] arguments = rManager.setParams(fullInput);
    try {
      //Checks if the user used redirection or not
      rManager.isRedirectionableCommand(fullInput);
      //If the arguments are valid
      if (checkArgs(filesys, arguments, fullInput)) {
        // Exits the session of the Shell
        System.exit(0);
      }
    } catch (InvalidArgsProvidedException e) {
      return e.getLocalizedMessage();
    } 
    return null;
  }

  /**
   * Checks if the arguments provided by the user follows the requirements or else throw an exception
   * 
   * @param filesys  refrence of FileSystemI object (MockFileSystem or FileSystem)
   * @param arguments the list of arguments from user which may contain a redirection error
   * @param fullInput the user input
   * 
   * @throws InvalidArgsProvidedException
   * 
   * @return true if the parameter meet requirements and false if not
   */
  @Override
  public boolean checkArgs(FileSystemI fs, String[] arguments, String fullInput) 
    throws InvalidArgsProvidedException {
    if(arguments.length != 0) 
      throw new InvalidArgsProvidedException("Error : Arguments not required : " + 
                  fullInput.substring(fullInput.indexOf("exit ") + 4).trim());
    return true;
  }
  
}
