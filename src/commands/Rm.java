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

import java.util.ArrayList;
import java.util.Arrays;

import data.FileSystemI;
import data.Node;
import errors.InvalidArgsProvidedException;

/**
 * Class Rm is able to delete a directory from the File System
 */
public class Rm extends DirectoryManager implements CommandI {

	/**
	 * Declare instance variable of ArrayList to hold all arguments
	 */
	private ArrayList<String> args;

	/**
	 * Declare instance variable of ErrorHandler to handle error messages
	 */
	private Cd traverseFileSystem;

	/**
	 * Declares an instance of RedirectionManager to handle redirection error if
	 * used
	 */
	private RedirectionManager rManager;

	/**
	 * Constructor for Rm that initializes the instance variables
	 */
	public Rm() {
		// Initializes the RedirectionManager Object
		rManager = new RedirectionManager();
		// Initializes the Cd Object
		traverseFileSystem = new Cd();
	}

	/**
	 * Checks the user input meets the requirement for the class if not throw an
	 * Exception
	 * 
	 * @param filesys   refrence of FileSystemI object (MockFileSystem or
	 *                  FileSystem)
	 * @param arguments the list of arguments from user which may contain a
	 *                  redirection error
	 * @param fullInput the user input
	 * 
	 * @throws InvalidArgsProvidedException if the user provided any invalid
	 *                                      arguments
	 * 
	 * @return true if the parameter meet requirements and false if not
	 */
	@Override
	public boolean checkArgs(FileSystemI filesys, String[] arguments, String fullInput)
			throws InvalidArgsProvidedException {
		ArrayList<String> args = new ArrayList<String>(Arrays.asList(arguments));
		if (args.size() != 1) {
			throw new InvalidArgsProvidedException("Error: Invalid Argument : Expecting 1 Argument only!");
		} else if (args.get(0).equals("/")) {
			throw new InvalidArgsProvidedException("Error: Invalid Directory : Cannot remove root directory");
		}
		return true;
	}

	/**
	 * Collects the arguments and checks if redirection is being used and passes it
	 * of to run the deletion operation
	 * 
	 * @param filesys   refrence of FileSystemI object (MockFileSystem or
	 *                  FileSystem)
	 * @param args      the string array of given arguments
	 * @param fullInput the string that contains the raw input given to JShell
	 * @param val       stores a boolean value
	 * 
	 * @return null or an error message if there is any
	 */
	@Override
	public String run(FileSystemI filesys, String[] arg, String fullInput, boolean val) {
		// Seperates the parameters from everything else from the user input
		String[] arguments = rManager.setParams(fullInput);
		// Converts the array to an arraylist
		this.args = new ArrayList<String>(Arrays.asList(arguments));

		try {
			// Checks if the user uses any redirction in the arguments
			rManager.isRedirectionableCommand(fullInput);
			// Checks the arguments fo any other errors
			if (checkArgs(filesys, arguments, fullInput)) {
				// Removes the directory
				removeDir(filesys, args.get(0).split(" "), filesys.getCurrentPath().split(" "));
			}
			// Catches the error if the user provides any invalid arguments
		} catch (InvalidArgsProvidedException e) {
			// Returns the error message
			return e.getLocalizedMessage();
		}
		// If the operation went well
		return null;
	}

	/**
	 * 
	 * @param fs
	 * @param dirToRemove
	 * @param currentPath
	 * 
	 * @throws InvalidArgsProvidedException
	 * 
	 */
	private void removeDir(FileSystemI fs, String[] dirToRemove, String[] currentPath)
			throws InvalidArgsProvidedException {
		// Checks if the directory exists in the filesystem
		if (traverseFileSystem.run(dirToRemove, fs)) {
			if (!fs.getCurrent().getisDir()) {
				// Returns an error
				throw new InvalidArgsProvidedException(
						"Error: Invalid Directory : " + dirToRemove[0] + " is not a directory");
			}
			Node toRemove = fs.getCurrent();
			fs.assignCurrent(fs.getCurrent().getParent());
			for (int i = 0; i < fs.getCurrent().getList().size(); i++) {
				if (fs.getCurrent().getList().get(i).equals(toRemove)) {
					fs.getCurrent().getList().remove(i);
					traverseFileSystem.run(currentPath, fs);
					return;
				}
			}
		} else {
			// Returns an error
			throw new InvalidArgsProvidedException(
					"Error: Invalid Directory : " + dirToRemove[0] + " is not a directory");
		}
	}
}
