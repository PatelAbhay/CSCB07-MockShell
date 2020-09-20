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
 * Class Pop is responsible for popping the top most directory 
 * on the directory stack onto the working directory of the 
 * user
 */
public class Pop extends DirectoryManager implements CommandI {

  /**
   * Declare instance variable of RedirectionManager to handle the redirection operations
   */
  RedirectionManager rManager;

  /**
   * Constructor for Save that initializes instance variables
   */
  public Pop() {
    //initialize the RedirectionManager
    rManager = new RedirectionManager();
  }

  /**
   * Run method checks if the user has given any arguments, and removes the top of the stack
   * Returns string output, either an error message or if performed without error then will return
   * null
   * 
   * @param filesys   refrence of FileSystemI object (MockFileSystem or FileSystem)
   * @param args  The string array of all arguments
   * @param fullInput  The full input provided by the user
   * @param val  A boolean for speak mode
   * 
   * @return String  An error message, else null
   */
  public String run(FileSystemI filesys, String[] arg, String fullInput, boolean val) {
    //Seperates the parameters from everything else from the user input
    String[] args = rManager.setParams(fullInput);
    try {
      if (rManager.isRedirectionableCommand(fullInput));
      if(checkArgs(filesys, args, fullInput)){
        String[] path = {filesys.getStack().pop()};
        Cd newWorkingDirectory = new Cd();
        newWorkingDirectory.run(path, filesys);      
      }
    } catch (InvalidArgsProvidedException e) {
      return e.getLocalizedMessage();
    }
    return null;
  }

  /**
   * Checks if arguments provided are valid or not
   * 
   * @param fs  Filesystem to be mutated
   * @param arguments  Arguments, in this case there should be none
   * @param fullInput  Full input string from the user
   * 
   * @return  Boolean indicating if args are valid or not
   */
  @Override
  public boolean checkArgs(FileSystemI fs, String[] arguments, String fullInput) 
    throws InvalidArgsProvidedException {
    if(arguments.length > 0){
      throw new InvalidArgsProvidedException("Error: Invalid Argument : No arguments should be given");
    } else if(fs.getStack().size() == 0){
      throw new InvalidArgsProvidedException("Error: Stack is empty");
    }
    return true;
  }
  
}
