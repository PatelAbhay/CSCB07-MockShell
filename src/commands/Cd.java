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
 * Class Cd is responsible for changing directories within the FileSystem
 */
public class Cd extends DirectoryManager implements CommandI {

	/**
	 * Declares an instance of boolean
	 */
	private boolean successfulPath = false;

	/**
	 * Declares an instance of RedirectionManager to handle redirection
	 */
	RedirectionManager rManager;

	/**
	 * Constructor for class Cd which initalizes instance variables
	 */
	public Cd() {
		// Initializing the RedirectionManager object
		rManager = new RedirectionManager();
	}

	/**
	 * Starting run method which checks if arguments were given, then passes it to
	 * another run method which processes the command
	 * 
	 * @param filesys   refrence of FileSystemI object (MockFileSystem or
	 *                  FileSystem)
	 * @param args      the string array of arguments
	 * @param fullInput the full line of input that the user gives into jshell
	 * @param val       the boolean we are using
	 * 
	 * @return any error messages if there are any or null
	 */
	public String run(FileSystemI filesys, String[] args, String fullInput, boolean val) {
		// Seperates the parameters from everything else from the user input
		String[] arguments = rManager.setParams(fullInput);
		try {
			// Checks if the user used any redirection in the parameter
			if (rManager.isRedirectionableCommand(fullInput))
				;
			// Checks the rest of the parameters
			checkArgs(filesys, arguments, fullInput);
			// Catches the error for using redirection or mistakes with the parameter
		} catch (InvalidArgsProvidedException e) {
			// Returns the error message
			return e.getLocalizedMessage();
		}
		// If the traversal went correctly
		return null;
	}

	/**
	 * Checks if the arguments provided by the user follows the requirements or else
	 * throw an exception
	 * 
	 * @param filesys   refrence of FileSystemI object (MockFileSystem or
	 *                  FileSystem)
	 * @param arguments the list of arguments from user which may contain a
	 *                  redirection error
	 * @param fullInput the user input
	 * 
	 * @throws InvalidArgsProvidedException
	 * 
	 * @return true if the parameter meet requirements and false if not
	 */
	public boolean checkArgs(FileSystemI filesys, String[] arguments, String fullInput)
			throws InvalidArgsProvidedException {
		// Collects the path name(s) from the user input
		String paths = fullInput.substring(fullInput.indexOf("cd") + 2).trim();
		// If the user provided no path name
		if (arguments.length == 0) {
			// Throws an error
			throw new InvalidArgsProvidedException("Error : No parameters provided");
			// If the user provides multiple path names
		} else if (arguments.length > 1) {
			// Throws an error
			throw new InvalidArgsProvidedException("Error : Multiple Parameters have been provided : " + paths);
		}
		// If the change of directory was unsuccessful, then an error msg is returned
		if (!run(arguments, filesys)) {
			// Throws an error
			throw new InvalidArgsProvidedException("Error: Invalid Directory : " + paths);
		}
		// If the user follewd the requirements for the command
		return true;
	}

	/**
	 * Main run method that executes the performance of changing directories based
	 * on what argument is given. If argument is ".", nothing happens If argument is
	 * "..", go up one directory If argument is "/", change the cd to root If
	 * argument is absolute path, check if the path exists, then change to that
	 * directory If argument is relative path, check if that path exists, then
	 * change to that directory
	 * 
	 * @param arguments the array of arguments provided by user
	 * @param filesys   refrence of FileSystemI object (MockFileSystem or
	 *                  FileSystem)
	 * 
	 * @return true if the argument was processed and the change of directory was
	 *         successful
	 */
	public boolean run(String[] arguments, FileSystemI filesys) {
		// Initializing variables
		String[] splitArgs = arguments[0].split("/");
		// Changing to root
		if (splitArgs.length == 0 && arguments[0].length() == 1) {
			filesys.assignCurrent(filesys.getRoot());
			return true;
		}
		if (splitArgs.length == 1) {
			successfulPath = run_1_arg(splitArgs[0], splitArgs, filesys);
		} else if (splitArgs.length > 1) {
			successfulPath = run_more_args(splitArgs, filesys);
		}
		return successfulPath;
	}

	/**
	 * 
	 * @param argument  part of the the path for traversal
	 * @param splitArgs the path for traversal
	 * @param filesys   refrence of FileSystemI object (MockFileSystem or
	 *                  FileSystem)
	 * 
	 * @return
	 */
	private boolean run_1_arg(String argument, String[] splitArgs, FileSystemI filesys) {
		// Processes change of directory with all the one argument options
		if (argument.equals("..")) {
			// Traverse one directory up
			if (filesys.getCurrent().getName().equals(filesys.getRoot().getName())) {
				return true;
			}
			filesys.assignCurrent(filesys.getCurrent().getParent());
			return true;
		} else if (argument.equals(".")) {
			return true;
		} else {
			// Relative path to requested dir
			successfulPath = this.makeRelativePath(splitArgs, filesys);
		}
		return successfulPath;
	}

	/**
	 * 
	 * @param splitArgs the path for traversal
	 * @param filesys   refrence of FileSystemI object (MockFileSystem or
	 *                  FileSystem)
	 * 
	 * @return
	 */
	private boolean run_more_args(String[] splitArgs, FileSystemI filesys) {
		// If there are more arguments, it will create a relative path depending on the
		// first element
		// being a root or not
		if (splitArgs[0].equals("")) {
			successfulPath = this.makePathFromRoot(splitArgs, filesys);
		} else {
			successfulPath = this.makeRelativePath(splitArgs, filesys);
		}
		return successfulPath;
	}

}
