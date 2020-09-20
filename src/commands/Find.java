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
 * Class Find used to locate files/directories in a given set of paths with a certain name
 */
public class Find extends DirectoryManager implements CommandI {

	/**
	 * Declaring string output which will contain the output of the command Find
	 */
	String output = "";
	/**
	 * Declaring instance of RedirectionManager
	 */
	RedirectionManager rManager;

	/**
	 * Constructor for Find which initializes the RedirectionManager
	 */
	public Find() {
    	//Initializes the RedirectionManager object
		rManager = new RedirectionManager();
	}

	/**
	 * Main overridden run method that runs the find command and returns the desired
	 * output
	 * 
	 * @param filesys   the variable we use to access the filesystem
	 * @param arg       the string array with arguments provided by user
	 * @param fullInput the raw string input that was provided to jshell
	 * @param val       the boolean that is used when calling this method
	 * 
	 * @return string the output of this method, which is a list of paths that
	 *         contain the expression
	 */
	@Override
	public String run(FileSystemI filesys, String[] arg, String fullInput, boolean val) {
		// Seperates the parameters from everything else from the user input
		String[] args = rManager.setParams(fullInput);
		// Creating arraylists for the paths and all the arguments
		ArrayList<String> paths = new ArrayList<String>();
		ArrayList<String> arguments = new ArrayList<String>(Arrays.asList(args));
		try {
			// If the arguments are valid, then we seperate the paths from the arguments and
			// put them
			// in their respective arraylists
			for (int i = 0; i < args.length; i++) {
				paths.add(args[i]);
				if (i < args.length - 1) {
					if (args[i + 1].equals("-type")) {
						break;
					}
				}
			}
			// Remove paths from arguments arraylist
			for (String x : paths) {
				arguments.remove(x);
			}
			// Loop through each path and recursively search all subdirectories under them
			// to find
			// desired file
			args = arguments.toArray(new String[arguments.size()]);
			if (checkArgs(filesys, args, fullInput)) {
				for (String x : paths) {
					String[] pathArr = { x };
					output = rManager.outputResult(filesys,
							checkList(filesys, pathArr, arguments.get(3), arguments.get(1)));
				}
			}

		} catch (InvalidArgsProvidedException e) {
			return e.getLocalizedMessage();
		}

		return output;
	}

	/**
	 * Overridden method checkArgs which ensures that arguments are valid
	 * 
	 * @param fs        the variable we are using to access the filesystem
	 * @param arguments the string array containing all the arguments provided
	 *                  alongside the command
	 * @param fullInput the string that contains the raw input provided to console
	 *                  in jshell
	 * 
	 * @return boolean true if the arguments were valid
	 * 
	 * @throws InvalidArgsProvidedException the user provided invalid argument
	 */
	@Override
	public boolean checkArgs(FileSystemI fs, String[] arguments, String fullInput) throws InvalidArgsProvidedException {
		ArrayList<String> args = new ArrayList<String>(Arrays.asList(arguments));

		if (args.size() == 0 || args.size() < 4) {
			throw new InvalidArgsProvidedException("Error: Invalid Argument : Missing Arguments");
		} else if (args.size() > 4) {
			throw new InvalidArgsProvidedException("Error : Multiple Parameters have been provided");
		} else if (!(args.get(0).equals("-type"))) {
			throw new InvalidArgsProvidedException("Error: Invalid Argument : " + args.get(0));
		} else if (!(args.get(1).equals("f") || args.get(1).equals("d"))) {
			throw new InvalidArgsProvidedException("Error: Invalid Argument : " + args.get(1));
		} else if (!(args.get(2).equals("-name"))) {
			throw new InvalidArgsProvidedException("Error: Invalid Argument : " + args.get(2));
		} else if (args.get(3).equals(null) || (args.get(3).charAt(0)) != ('"')
				|| (args.get(3).charAt((args.get(3).length() - 1)) != ('"'))) {
			throw new InvalidArgsProvidedException("Error: Invalid Argument : " + args.get(3));
		}
		return true;
	}

	private String checkList(FileSystemI filesys, String[] path, String expression, String type)
			throws DirectoryException {
		// Removing quotations around the expression
		expression = expression.substring(1, expression.length() - 1);
		// getting current path so we can traverse back to it after the method is done
		String[] currPath = { filesys.getCurrentPath() };
		Cd newPath = new Cd();
		// fill output with paths that contain the expression
		if (newPath.run(path, filesys)) {
			output = recursiveDirSearch(filesys, path, expression, type, newPath, filesys.getCurrent(),
					filesys.getCurrentPath(), output);
		} else {
			throw new DirectoryException("Error: Directory Not Found : " + path[0]);
		}
		// Return to original path
		newPath.run(currPath, filesys);
		return output;
	}

	private String recursiveDirSearch(FileSystemI filesys, String[] path, String expression, String type, Cd newPath,
			Node currNode, String currPath, String output) {
		// For each directory we visit, we check it's children nodes to see if the
		// requested expression is there
		output = printMatches(filesys, expression, type, output);
		// Recursively search its subdirectories
		for (int i = 0; i < currNode.getList().size(); i++) {
			String[] tempPath = { currNode.getList().get(i).getName() };
			if (newPath.run(tempPath, filesys)) {
				output = recursiveDirSearch(filesys, path, expression, type, newPath, filesys.getCurrent(), currPath,
						output);
			}
		}
		// change directory back to original path
		String[] temp = { currPath };
		newPath.run(temp, filesys);
		return output;
	}

	private String printMatches(FileSystemI filesys, String expression, String type, String output) {
		// Check if any nodes in the arraylist contain the expression
		ArrayList<Node> toCheck = filesys.getCurrent().getList();
		for (int i = 0; i < toCheck.size(); i++) {
			if (type.equals("d")) {
				if (toCheck.get(i).getisDir() && toCheck.get(i).getName().equals(expression)) {
					if (!output.contains(filesys.getCurrentPath() + "\n")) {
						output = output.concat(filesys.getCurrentPath());
						output = output.concat("\n");
					}
				}
			} else if (type.equals("f")) {
				if (!toCheck.get(i).getisDir() && toCheck.get(i).getName().equals(expression)) {
					if (!output.contains(filesys.getCurrentPath() + "\n")) {
						output = output.concat(filesys.getCurrentPath());
						output = output.concat("\n");
					}
				}
			}
		}
		return output;
	}
}
