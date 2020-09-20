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
import data.Node;
import errors.FileException;
import errors.InvalidArgsProvidedException;

/**
 * Class Cat views the contents of requested file(s) and is able to redirect it
 * to a files
 */
public class Cat implements CommandI {

	/**
	 * Declare instance variable of String to contain the output that Cat may return
	 */
	private String output;

	/**
	 * Declares an instance of RedirectionManager to handle redirection
	 */
	private RedirectionManager redirect;

	/**
	 * Constructor for Cat that initializes instance variables
	 */
	public Cat() {
		// Initializing the String object output
		this.output = "";
		// Initializing the RedirectionManager object
		this.redirect = new RedirectionManager();
	}

	/**
	 * Checks if arguments are valid, then sends it to readFile to read the file
	 * 
	 * @param filesys   refrence of FileSystemI object (MockFileSystem or
	 *                  FileSystem)
	 * @param args      the string array of given arguments
	 * @param fullInput the string that contains the raw input given to JShell
	 * @param val       stores a boolean value
	 * 
	 * @return the contents of file or error message or null
	 */
	public String run(FileSystemI filesys, String[] args, String fullInput, boolean val) {
		// Seperates the parameters from everything else from the user input
		String[] arguments = redirect.setParams(fullInput);
		try {
			// If the parameters meet the requirements
			if (checkArgs(filesys, arguments, fullInput)) {
				// Initializing the String object output after each time the method is called
				output = "";
				// Calls the readFile function to return what is in the file
				readFile(arguments, filesys);
			}
			// If the parameter has not meet all of the requirements
		} catch (InvalidArgsProvidedException e) {
			// Returns the error message
			return e.getLocalizedMessage();
		}

		// Returns the output for the arguments
		return redirect.outputResult(filesys, output);
	}

	/**
	 * Reads the files from the given list of files, if any are invalid it throws an
	 * exception
	 * 
	 * @param filePaths the list of file to be read
	 * @param filesys   refrence of FileSystemI object (MockFileSystem or
	 *                  FileSystem)
	 * 
	 * @throws FileException when file does not exist in the file system
	 */
	private void readFile(String[] filePaths, FileSystemI filesys) throws FileException {
		// Declares and initialized a Node to null
		Node file = null;
		// Runs through all the filePaths and stores the output for each case
		for (int i = 0; i < filePaths.length; i++) {
			file = filesys.findFile(filePaths[i], false);
			// If the file does exist
			if (file != null) {
				// Collect and append the text to the String object output
				output += filesys.getContent(file);
				// If the file does not exist
			} else {
				// Collect and append the error of File Not Found
				throw new FileException("Error: File Not Found : " + filePaths[i]);
			}

			// If it is not one file or it is the last file in the filePaths
			if (!(filePaths.length == 1 || i == filePaths.length - 1))
				// Append multiple line breaks to the String object output
				output += "\n" + "\n" + "\n";
		}
	}

	/**
	 * Checks the user input for any redirection error if used and other issues from
	 * user if there are none then it return true else throws the exception
	 * 
	 * @param filesys   refrence of FileSystemI object (MockFileSystem or
	 *                  FileSystem)
	 * @param arguments the list of arguments from user which may contain a
	 *                  redirection error
	 * @param fullInput the user input
	 * 
	 * @throws InvalidArgsProvidedException if the user provided invalid input
	 * 
	 * @return true if the parameter meet requirements and false if not
	 */
	public boolean checkArgs(FileSystemI filesys, String[] arguments, String fullInput)
			throws InvalidArgsProvidedException {
		// Collects the content of the array as one string with spaces in between
		// elements or array
		String error = String.join(" ", arguments);
		// If the user provided no input
		if (arguments.length == 0) {
			// Throws an error
			throw new InvalidArgsProvidedException("Error : No parameters provided");
			// If the user provided no file names for redirection
		} else if (error.contains("Error : No parameters provided")) {
			// Throws an error
			throw new InvalidArgsProvidedException(error);
			// If the user provided multiple file names for redirection
		} else if (error.contains("Error : Multiple Parameters have been provided")) {
			// Throws an error
			throw new InvalidArgsProvidedException(error);
		}
		// If the user follewd the requirements for the command
		return true;
	}
}
