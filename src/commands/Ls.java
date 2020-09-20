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
import java.util.ArrayList;
import data.FileSystemI;
import data.Node;
import errors.DirectoryException;
import errors.InvalidArgsProvidedException;

/**
 * Class Ls handles viewing the child nodes of requested directory
 */
public class Ls extends DirectoryManager implements CommandI {
	/**
	 * Declare instance variable of ArrayList to contain all arguments
	 */
	ArrayList<String> args;

	/**
	 * Declare instance variable of RedirectionManager to handle the redirection
	 * operations
	 */
	private RedirectionManager redirect;
	/**
	 * String instance variable to store the output
	 */
	private String output;

	/**
	 * Constructor for Ls to initialize error
	 */
	public Ls() {
		this.redirect = new RedirectionManager();
	}

	/**
	 * Displays all of the given directory's child nodes
	 * 
	 * @param args      the string array that contains the arguments
	 * @param fullInput the string that contains the raw input that the user
	 *                  provides to JShell
	 * @return null always
	 */
	public String run(FileSystemI filesys, String[] args, String fullInput, boolean val) {
		String[] parsedArgs = redirect.setParams(fullInput);
		try {
			if (checkArgs(filesys, parsedArgs, fullInput)) {
				output = redirect.outputResult(filesys, runLs(filesys, parsedArgs));
			}
		} catch (InvalidArgsProvidedException e) {
			return e.getLocalizedMessage();
		}
		return output;
	}

	/**
	 * A simple function to check if the arguments supplied are valid, inherited
	 * from CommandI
	 * 
	 * @param filesys   Filesystem to be mutated
	 * @param arguments Arguments containing path names, etc.
	 * @param fullInput String containing full user input
	 * 
	 * @throws InvalidArgsProvidedException Throws an error if invalid arguments are
	 *                                      supplied
	 * @throws DirectoryException           Throws an error if an invalid directory
	 *                                      is supplied
	 * 
	 * @return A boolean indicating if the args are valid or not
	 */
	@Override
	public boolean checkArgs(FileSystemI fs, String[] arguments, String fullInput) throws InvalidArgsProvidedException {
		if (String.join(" ", arguments).contains("Error : No parameters provided")) {
			throw new InvalidArgsProvidedException(String.join(" ", arguments));
		} else if (String.join(" ", arguments).contains("Error : Multiple Parameters have been provided")) {
			throw new InvalidArgsProvidedException(String.join(" ", arguments));
		}
		return true;
	}

	private String runLs(FileSystemI filesys, String[] arguments) {
		// sets arguments into array list
		this.args = new ArrayList<String>(Arrays.asList(arguments));
		// Calls function based of flag and arguments specified
		try {
			if (args.size() == 0) {
				return unrecursiveMode(filesys);
			}
			if (!args.get(0).equals("-R")) {
				return unrecursiveMode(filesys);
			} else {
				return recursiveMode(filesys);
			}
		} catch (DirectoryException e) {
			return e.getLocalizedMessage();
		}
	}

	private String unrecursiveMode(FileSystemI filesys) throws DirectoryException {
		// Lists all directories unrecursively
		String output = "";
		// Lists all directories within current directory if no args provided
		if (args.size() == 0) {
			Node curr = filesys.getCurrent();
			for (int i = 0; i < curr.getList().size(); i++) {
				output += curr.getList().get(i).getName() + '\n';
			}
		} else {
			// Loops through args and lists all files/directories within each path
			for (int i = 0; i < args.size(); i++) {
				String[] path = { args.get(i) };
				String[] currentPath = { filesys.getCurrentPath() };

				Cd traverse = new Cd();
				if (traverse.run(path, filesys)) {
					Node current = filesys.getCurrent();
					output += "Path: " + filesys.getCurrentPath() + '\n';
					for (int j = 0; j < current.getList().size(); j++) {
						output += current.getList().get(j).getName() + '\n';
					}
				} else {
					// Throws an exception if a path is not valid
					throw new DirectoryException(
							"Error: Invalid Directory : " + args.get(i) + " is not a valid directory\n");
				}
				traverse.run(currentPath, filesys);
				output += '\n';
			}
		}
		if (output.equals("")) {
			return null;
		}
		return output;
	}

	private String recursiveMode(FileSystemI filesys) throws DirectoryException {
		// Recursively lists contents within paths specified
		String output = "";
		Cd traverse = new Cd();
		String[] currentPath = { filesys.getCurrentPath() };
		// Recursively lists files in ENTIRE filesystem if no paths provided
		if (args.size() == 1) {
			output = listDirectory(filesys.getRoot(), filesys, output);
		} else {
			// Else loops through all paths provided and lists out their content recursively
			for (int i = 1; i < args.size(); i++) {
				String[] path = { args.get(i) };
				if (traverse.run(path, filesys)) {
					output = listDirectory(filesys.getCurrent(), filesys, output);
				} else {
					// throws an exception if one of them is not valid
					throw new DirectoryException(
							"Error: Invalid Directory : " + args.get(i) + " is not a valid directory\n\n");
				}
				traverse.run(currentPath, filesys);
			}
		}
		traverse.run(currentPath, filesys);
		if (output.equals("")) {
			return null;
		}
		return output;
	}

	private String listDirectory(Node root, FileSystemI filesys, String output) {
		// Recursive method to be used when -R is specified.
		if (!root.getisDir()) {
			return output;
		}
		filesys.assignCurrent(root);
		// Lists out path and contents within, then calls this function on each of those
		// directories inside
		// This achieves the recursive output
		output += "Path: " + filesys.getCurrentPath() + '\n';
		for (int i = 0; i < root.getList().size(); i++) {
			output += root.getList().get(i).getName() + '\n';
		}
		output += "\n";
		for (int i = 0; i < root.getList().size(); i++) {
			output = listDirectory(root.getList().get(i), filesys, output);
		}
		return output;
	}
}
