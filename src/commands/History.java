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

import java.util.Arrays;

import data.FileSystemI;
import errors.InvalidArgsProvidedException;

/**
 * Class History retrives the commands entered into the JShell by user
 */
public class History implements CommandI {

  /**
   * Declare instance variable of RedirectionManager to handle redirection to file
   */
  private RedirectionManager redirect;

  /**
   * Declare instance variable of String to contain the output that the command
   * may return
   */
  String output;

  /**
   * Constructor for History that initializes instance variables
   */
  public History() {
    // Initializes a RedirectionManager Object
    this.redirect = new RedirectionManager();
    // Initializes a String object called output
    this.output = "";
  }

  /**
   * Runs the history command and figures out if redirection is required
   * 
   * @param fs        the refrence to the file system(FileSystem/MockFileSystem)
   * @param args      the string array that contains given arguments
   * @param fullInput the string that contains the raw input given to JShell
   * @param val       stores a boolean value
   * 
   * @return the error message if there is any or null
   */
  public String run(FileSystemI fs,  String[] args,  String fullInput, boolean val) {
    //Seperates the parameters from everything else from the user input
    String[] arguments = redirect.setParams(fullInput);

    try {
      //If the parameters meet the requirements
      if (checkArgs(fs, arguments, fullInput)) {
        //Runs the command and handles redirection after that
        output = runHistory(arguments, fs);
      }
    } catch (InvalidArgsProvidedException e) {
      return e.getLocalizedMessage();
    }
    //Returns the valid output for the given parameters
    return redirect.outputResult(fs, output);

  }

  /**
   * Checks the user input for any redirection error if used and other issues from user
   * if there are none then it return true else throws the exception
   * 
   * @param filesys  refrence of FileSystemI object (MockFileSystem or FileSystem)
   * @param arguments the list of arguments from user which may contain a redirection error
   * @param fullInput the user input
   * 
   * @throws InvalidArgsProvidedException if the user provided invalid input
   * 
   * @return true if the parameter meet requirements and false if not
   */
  public boolean checkArgs(FileSystemI fs, String[] arguments, String fullInput) 
      throws InvalidArgsProvidedException { 
    //If the user provided no file names for redirection
    if(String.join(" ", arguments).contains("Error : No parameters provided")){
      //Throws an error
      throw new InvalidArgsProvidedException(String.join(" ", arguments));
    //If the user provides multiple file names for redirection
    }else if(String.join(" ", arguments).contains("Error : Multiple Parameters have been provided")){
      //Throws an error
      throw new InvalidArgsProvidedException(String.join(" ", arguments));
    }
    //If the user follewd the requirements for the command
    return true;
  }

  /**
   * Parses input and then calls print method to print history
   * 
   * @param param  the parameter by which to recall commands
   * @param fs     refrence of FileSystemI object (MockFileSystem or FileSystem)
   * 
   * @throws InvalidArgsProvidedException user provides invalid arguments 
   * 
   * @return the error message if there is any or the actual history
   */
  private String runHistory(String[] params, FileSystemI fs) throws InvalidArgsProvidedException {
    // If the user provides no arguments
    if (params.length == 0) {
      // Calls the following methods to print the entire history
      printLastXCommands(fs, fs.getCommandLog().size());
      // If the user provided 1 argument
    } else if (params.length == 1) {
      // Created and initialized an integer
      int number = 0;
      // Created and initialized a boolean
      boolean numeric = true;

      // Converts the given input from the user to an integer
      try {
        // Stores the converted value if it was possible
        number = Integer.valueOf(params[0]);
        // If the user provided something other than an Integer
      } catch (NumberFormatException e) {
        // Sets the boolean to false
        numeric = false;
      }

      // If the input is an number and non-negative and an integer
      if (numeric && number >= 0 && number % 1 == 0) {
        // Calls the following method to get the history to a certain number of
        // commands the user inputed
        printLastXCommands(fs, number);
      } else {
        // Sets an error of Invalid Argument to be returned to the user
        throw new InvalidArgsProvidedException("Error: Invalid Argument : " 
              + params[0] + " is not either a number or positive or an integer");
      }
      // If the user provided multiple arguments
    } else if (params.length > 1) {

      // Collects the user input from the fullInput
      String parameter = Arrays.toString(params);
      parameter = parameter.substring(1, parameter.length() - 1).replace(",", "").trim();
      // Returns an error of Mulptile parameters provided
      throw new InvalidArgsProvidedException("Error : Multiple Parameters have been provided : " 
      + parameter + " , either one or no input");
    }

    // Returns the valid output for the user input
    return output.trim();
  }

  /**
   * Reads the command log and returns the result of x last commands
   * 
   * @param fs the refrence to the file system
   * @param x number of last commands to be read
   */
  private void printLastXCommands(FileSystemI fs, int x) {
    // Reads and stores the commands from the CommandLod in FileSystem
    for (int i = fs.getCommandLog().size() - x; i < fs.getCommandLog().size(); i++) {
      // If the counter is a negative number
      if (i < 0) {
        // Skips from reading the commandLog
        continue;
      }
      // Collects the the commands from the CommandLog
      output += (i + 1) + ". " + fs.getCommandLog().get(i) + "\n";
    }
  }
}
