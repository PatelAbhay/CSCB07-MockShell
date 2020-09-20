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
import errors.MissingQuotesException;

/**
 * Class Echo is responsible for printing to the shell and redirecting the input
 * to perform the appropriate task
 */
public class Echo implements CommandI {

  /**
   * Declare instance variable of String to hold the output that will be returned
   */
  String output = "";
  /**
   * Declare instance variable of String to hold the parsed version of the user arguments
   */
  String properArgument = "";
  /**
   * Declare instance variable of RedirectionManager to handle the redirection operations
   */
  private RedirectionManager redirect;

  /**
   * Sets up the class with requires objects
   */
  public Echo() {
    //Initializes a RedirectionManager object
    this.redirect = new RedirectionManager();
  }

  /**
   * Main run method that checks if the user had inputted any arguments to the
   * command. It splices the input so that it can send parsed data to the
   * appropriate implementation of echo. Calls the execute method that will
   * perform the required task for the appropriate scenario. Returns a String that
   * will either contain an error message if there was an error or return null
   * 
   * @param filesys   refrence of FileSystemI object (MockFileSystem or FileSystem)
   * @param args the String array of arguments provided by user (split from a
   *             whitespace)
   * @param fullInput  String containing full user input
	 * @param val holds a boolean value 
   * 
   * @return String will either be null if there were no errors or an appropriate
   *         error message
   */
  public String run(FileSystemI filesys,  String[] args,  String fullInput, boolean val) {
    String[] arguments = redirect.setParams(fullInput);
    try {
      if (checkArgs(filesys, arguments, fullInput)) {
        output = redirect.outputResult(filesys, runEcho(arguments));
      }
    } catch (InvalidArgsProvidedException e) {
      return e.getLocalizedMessage();
    }
    return output;
  }

  	/**
	 * A simple function to check if the arguments supplied are valid, inherited from CommandI
	 * 
   * @param filesys   refrence of FileSystemI object (MockFileSystem or FileSystem)
	 * @param arguments  Arguments containing path names, etc.
	 * @param fullInput  String containing full user input
	 * @param val holds a boolean value 
   *
	 * @throws InvalidArgsProvidedException  Throws an error if invalid arguments are supplied
	 * 
	 * @return  A boolean indicating if the args are valid or not
	 */
  public boolean checkArgs(FileSystemI fs, String[] arguments, String fullInput) 
    throws InvalidArgsProvidedException {
    if (arguments.length == 0) {
      throw new InvalidArgsProvidedException("Error : No parameters provided");
    } else if (String.join(" ", arguments).contains("Error : No parameters provided")) {
      throw new InvalidArgsProvidedException(String.join(" ", arguments));
    } else if (String.join(" ", arguments).contains("Error : Multiple Parameters have been provided")) {
      throw new InvalidArgsProvidedException(String.join(" ", arguments));
    }
    return true;
  }

  /**
   * Runs the actual echo command
   * 
   * @param args takes the parsed user input
   * 
   * @throws MissingQuotesException if user input has no quotations
   * 
   * @return error message or string output
   */
  private String runEcho(String[] args) throws MissingQuotesException {
    for (int i = 0; i < args.length; i++) {
      properArgument += args[i] + " ";
    }
    properArgument = properArgument.trim();
    if (properArgument.startsWith("\"") && properArgument.endsWith("\"")) {
      output = properArgument.substring(1, properArgument.length() - 1);
    } else
      // Missing quotations in user input
      throw new MissingQuotesException("Error : Missing Quotes : " + args[0]);
    return output;
  }

}
