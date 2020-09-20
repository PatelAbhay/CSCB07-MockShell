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
import errors.DirectoryException;
import errors.InvalidArgsProvidedException;

/**
 * Class Mkdir handles making directories anywhere in the filesystem
 */
public class Mkdir extends DirectoryManager implements CommandI {
	/**
	 * Declare instance variable of ArrayList to hold all arguments
	 */
	ArrayList<String> args;

	/**
	 * Declare instance variable of RedirectionManager to handle the redirection
	 * operations
	 */
	private RedirectionManager rManager;

	/**
	 * Declare instance variable of Cd to traverse filesystem
	 */
	private Cd traverse;

	// String fullinput;
	String fileName;
	String[] newArgs = { "" };
	String[] currentPath = { "" };
	FileSystemI fs;

	/**
	 * Constructor of Mkdir to initialize error
	 */
	public Mkdir() {
		//Initializes the following objects
		this.rManager = new RedirectionManager();
		traverse = new Cd();
	}

	/**
	 * Generic run method to call on method that does the work of creating
	 * directories
	 * 
	 * @param args      the string array of all arguments
	 * @param fullInput the string of the entire raw input provided by user in
	 *                  JShell
	 * @return String null always
	 */
	public String run(FileSystemI filesys, String[] arg, String fullInput, boolean val) {
		// Seperates the parameters from everything else from the user input
		String[] args = rManager.setParams(fullInput);
		try {
			fs = filesys;
			rManager.isRedirectionableCommand(fullInput);
			return MakeDirectory(args, filesys, fullInput);
		} catch (InvalidArgsProvidedException e) {
			return e.getLocalizedMessage();
		}
	}

	/**
	 * Makes directories at locations in filesystem based on the path given
	 * 
	 * @param arguments The string array of all arguments provided
	 * @return String An error message, else null
	 */
	public String MakeDirectory(String[] arguments, FileSystemI filesys, String fullinput)
			throws InvalidArgsProvidedException, DirectoryException {
		// Sets arguments into array list
		this.args = new ArrayList<String>(Arrays.asList(arguments));
		// Records current location of user
		currentPath[0] = fs.getCurrentPath();
		// Checks validity of arguments
		checkArgs(fs, arguments, fullinput);
		// Checks for reptitions
		checkRepitition();
		// Loops over arguments
		for (int i = 0; i < args.size(); i++) {
			// Sets path to traverse to
			setPathAndFile(i);
			// traverse to path
			traverse.run(fs, newArgs, "cd " + newArgs[0], false);
			// Creates new node
			Node newNode = new Node.Builder(true, fileName).setParent(fs.getCurrent()).build();
			// adds it to the directory
			fs.addToDirectory(newNode);
			// traverse back to original location of user
			traverse.run(fs, currentPath, "cd " + currentPath[0], false);
		}
		return null;

	}

	/**
	 * Checks arguments to verify if they are valid or not
	 * 
	 * @param fs        Filesystem to be mutated
	 * @param arguments Arguments contaning directory(ies) to be mad
	 * @param fullInput Full input string from the user
	 * @return Boolean indicating if the arguments are valid or not
	 */
	@Override
	public boolean checkArgs(FileSystemI fs, String[] arguments, String fullInput) throws InvalidArgsProvidedException {
		// Checks if no arguments
		if (args.size() == 0) {
			traverse.run(currentPath, fs);
			throw new InvalidArgsProvidedException("Error: Invalid Argument : Expected at least 1 argument");
		}
		// Loops over arguments to check validity
		for (int i = 0; i < args.size(); i++) {
			// Sets path name and new directory name
			setPathAndFile(i);
			// Checks filename validity
			if (!fs.isValidName(fileName)) {
				traverse.run(currentPath, fs);
				throw new InvalidArgsProvidedException(
						"Error: Invalid Directory : " + fileName + " is not a valid directory name");
			}
			// Checks path validity
			if (!traverse.run(newArgs, fs)) {
				traverse.run(currentPath, fs);
				throw new InvalidArgsProvidedException(
						"Error: Invalid Directory : " + newArgs[0] + " is not a valid directory");
			}
		}
		return true;
	}

	/**
	 * Sets the path and filename to traverse to and create the new directories
	 * 
	 * @param i index of argument for filenames and path to set
	 */
	public void setPathAndFile(int i) {
		// Sets path and filenames based of if a / is present or not
		if (args.get(i).contains("/")) {
			newArgs[0] = args.get(i).substring(0, args.get(i).lastIndexOf('/'));
			// Edge case for making a directory in the root
			if (newArgs[0].equals("")) {
				newArgs[0] = "/";
			}
			fileName = args.get(i).substring(args.get(i).lastIndexOf('/') + 1);
		} else {
			newArgs[0] = fs.getCurrentPath();
			fileName = args.get(i);
		}
	}

	/**
	 * Checks if directory to be made already exists
	 * 
	 * @throws DirectoryException Throws an exception if duplicate directory exists
	 */
	public void checkRepitition() throws DirectoryException {
		// Loops through paths to check for repitiions in directory to be created
		for (int i = 0; i < args.size(); i++) {
			setPathAndFile(i);
			traverse.run(fs, newArgs, "cd " + newArgs[0], false);
			ArrayList<Node> parentList = fs.getCurrent().getList();
			for (int j = 0; j < parentList.size(); j++) {
				if (parentList.get(j).getName().equals(fileName) && parentList.get(j).getisDir()) {
					traverse.run(currentPath, fs);
					// Throws exception if duplicate is found
					throw new DirectoryException("Invalid Directory: " + fileName + " already exists in " + newArgs[0]);
				}
			}
		}
	}

}
