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
 * Class Mv is responsible for moving directories/files from the first given
 * path to the second given path
 */
public class Mv extends DirectoryManager implements CommandI {

	/**
	 * Declare instance variable of ArrayList to hold all arguments
	 */
	ArrayList<String> args;
	/**
	 * Declare instance variable of RedirectionManager
	 */
	private RedirectionManager rManager;
	/**
	 * Declare instance variable of Cd
	 */
	private Cd traverse;
	/**
	 * Declare string and string arrays for traversing filesystem and finding files
	 */
	String[] pathFrom = { "" };
	String[] pathTo = { "" };
	String[] currentPath = { "" };
	String fileName;
	String newDir;
	/**
	 * parentToMove is the node of the directory that the removed node needs to be
	 * added to toMove is the node to move
	 */
	Node parentToMove;
	Node toMove = null;
	/**
	 * ouput String to the user if needed
	 */
	String output;
	/**
	 * index of node to remove when moving nodes
	 */
	int toRemove = -1;

	/**
	 * Constructor for Mv to initialize the instance variables of Redirection
	 * manager and Cd
	 */
	public Mv() {
		//Initializes the following objects
		rManager = new RedirectionManager();
		traverse = new Cd();
	}

	/**
	 * Run method for this class which will execute the mv functionality in the
	 * filesystem
	 * 
	 * @param filesys     Filesystem to be mutated
	 * @param args        Arguments containing paths to be moved
	 * @param actualInput full string input of the user
	 * @param val         Boolean indicating if user is in speak mode or not
	 */
	public String run(FileSystemI filesys, String[] args, String actualInput, boolean val) {
		// Seperates the parameters from everything else from the user input
		String[] arguments = rManager.setParams(actualInput);
		this.args = new ArrayList<String>(Arrays.asList(arguments));
		try {
			rManager.isRedirectionableCommand(actualInput);
			// Checks if arguments are valid
			if (checkArgs(filesys, arguments, actualInput)) {
				// Initialize path to traverse to, and filename to look for
				initPathandFile(filesys);
				// Copy the file node into toMove
				output = copyFile(filesys);
				// Move the file node into the new directory
				output = moveFile(filesys);
			}
		} catch (InvalidArgsProvidedException e) {
			// Catches any exception that is thrown
			return e.getLocalizedMessage();
		}
		// traverse back to the original position the user was in
		traverse.run(currentPath, filesys);

		return null;
	}

	/**
	 * Initializes paths to traverse to, to remove and paste nodes
	 * 
	 * @param fs Filesystem to be mutated
	 * @throws DirectoryException Throws a directory exception if a new directory to
	 *                            be created has an invalid path name
	 */
	public void initPathandFile(FileSystemI fs) throws DirectoryException {
		// Checks if pathFrom contains a /
		// Important for traversing filesystem and finding node to be moved
		if (args.get(0).contains("/")) {
			pathFrom[0] = args.get(0).substring(0, args.get(0).lastIndexOf("/"));
			if (pathFrom[0].equals("")) {
				pathFrom[0] = "/";
			}
			if (args.get(0).lastIndexOf("/") == 0) {
				fileName = args.get(0).substring(1, args.get(0).length());
			} else {
				fileName = args.get(0).substring(args.get(0).lastIndexOf("/") + 1, args.get(0).length());
			}
		} else {
			pathFrom[0] = fs.getCurrentPath(); // CHANGE
			fileName = args.get(0);
		}
		// Checks if pathTo contains a /
		// Important for finding path to move node to, and possible new folder to create
		// to add
		// new node into
		if (args.get(1).contains("/")) {
			pathTo[0] = args.get(1).substring(0, args.get(1).lastIndexOf("/"));
			if (pathTo[0].equals("")) {
				pathTo[0] = "/";
			}
			if (args.get(1).lastIndexOf("/") == 0) {
				newDir = args.get(1).substring(1, args.get(1).length());
			} else {
				newDir = args.get(1).substring(args.get(1).lastIndexOf("/") + 1, args.get(1).length());
			}
		} else {
			pathTo[0] = fs.getCurrentPath();
			newDir = args.get(1);
		}
		// Checks if the possible new directory to be made is valid
		if (!fs.isValidName(newDir)) {
			throw new DirectoryException("Error: Invalid Directory " + newDir + " is not a valid directory name");
		}
		// Stores the users current position within the filesystem
		currentPath[0] = fs.getCurrentPath();
	}

	/**
	 * Checks arguments to see if they are valid
	 * 
	 * @param filesys   Filesystem to be mutated
	 * @param arguments Arguments containing paths to traverse to, to move node
	 * @param fullInput Full string containing user input
	 * 
	 * @return Boolean value indicating if arguments are valid
	 * 
	 * @throws InvalidArgsProvidedException Throws exception if arguments are
	 *                                      invalid
	 * @throws DirectoryException           If user tries to move the root directory
	 */
	public boolean checkArgs(FileSystemI filesys, String[] arguments, String fullInput)
			throws InvalidArgsProvidedException, DirectoryException {
		// Throws an exception if 2 arguments are't supplied
		if (args.size() != 2)
			throw new InvalidArgsProvidedException("Error: Invalid Argument : Expected 2 arguments");
		// Throws an exception is user tries to move the root directory
		if (args.get(0).equals("/"))
			throw new DirectoryException("Error: Invalid Directory : Cannot move the root directory");

		return true;
	}

	/**
	 * Traverse to pathFrom and copies file node into to "toMove" node variable
	 * 
	 * @param fs Filesystem to be mutated
	 * @return String output to the user if required
	 * @throws DirectoryException Throws exception if pathFrom isn't a valid path or
	 *                            file wasn't found
	 */
	public String copyFile(FileSystemI fs) throws InvalidArgsProvidedException, DirectoryException {
		// Traverses to pathFrom path
		if (traverse.run(pathFrom, fs)) {
			pathFrom[0] = fs.getCurrentPath();
			// Assigns parentToMove as the current node the user is in
			parentToMove = fs.getCurrent();
			// Attempts to find the node to be copied into toMove
			for (int i = 0; i < parentToMove.getList().size(); i++) {
				if (parentToMove.getList().get(i).getName().equals(fileName)) {
					// Creates copy of that node, purely for references
					Node copy = parentToMove.getList().get(i);
					// Gets index of node, so it can be removed later
					toRemove = i;
					// Gets contents of node to be copied
					ArrayList<Node> copyContents = new ArrayList<Node>(copy.getList());
					// Builds new node to be removed
					toMove = new Node.Builder(copy.getisDir(), copy.getName()).setContent(copy.getContent())
							.setList(copyContents).setParent(parentToMove).setRoot(false).build();
				}
			}
			// Traverses back to the original position of the user in the filesystem
			traverse.run(currentPath, fs);
			// Throws an exception if the node wasn't found, meaning the filename was
			// invalid
			if (toMove == null) {
				throw new DirectoryException(
						"Error: Directory Not Found : " + fileName + " does not exist in the path you specified!");
			}

		} else {
			// Throws an exception since pathFrom was not valid
			throw new DirectoryException("Error: Invalid Directory : " + pathFrom[0] + " does not exist!");
		}
		return null;
	}

	/**
	 * Traverses pathTo and moves node to the new directory
	 * 
	 * @param fs Filesystem to be mutated
	 * @return String output to the user if required
	 * @throws DirectoryException Throws exception if pathTo isn't a valid directory
	 */
	public String moveFile(FileSystemI fs) throws DirectoryException {
		// Traverses to pathTo path
		if (traverse.run(pathTo, fs)) {
			// Handles edge cased of moving file node to the root directory
			if (newDir.equals("")) {
				fs.addToDirectory(toMove);
				traverse.run(pathFrom, fs);
				fs.removeFromDirectory(toRemove);
				traverse.run(currentPath, fs);
				return null;
			}
			// Assigns parentToMove to current node
			parentToMove = fs.getCurrent();
			// Checks if new directory needs to be made, if so, make it and assign
			// parentToMove that directory, else traverse into the already existing node
			checkDirectory(fs);
			// Created index holder called toOverride to override node with the same name
			int toOverride = -1;
			for (int i = 0; i < parentToMove.getList().size(); i++) {
				if (toMove.getName().equals(parentToMove.getList().get(i).getName())) {
					// Node with the same nice is found, store the index
					toOverride = i;
					break;
				}
			}

			if (toOverride == -1) {
				// If node with the same name isn't found simply add toMove the directory
				fs.addToDirectory(toMove);
			} else {
				// Else remove the old node and add the new node in
				fs.removeFromDirectory(toOverride);
				fs.addToDirectory(toMove);
			}
			// Traverse back to pathFrom
			traverse.run(pathFrom, fs);
			// Remove the node that was moved
			fs.removeFromDirectory(toRemove);
			// Return back to the original position the user was in
			traverse.run(currentPath, fs);
		} else {
			// Throw an exception since pathTo is not a valid path
			throw new DirectoryException("Error: Invalid Directory : " + pathTo[0] + " does not exist!");
		}
		return null;
	}

	/**
	 * Checks if new folder needs to be created, if so then create it if not then
	 * traverse into that folder
	 * 
	 * @param fs Filesystem to be mutated
	 */
	public void checkDirectory(FileSystemI fs) {
		// Checks if directory exists with the same name, if so, assign that as current
		// and return
		for (int i = 0; i < parentToMove.getList().size(); i++) {
			if (parentToMove.getList().get(i).getName().equals(newDir)) {
				parentToMove = parentToMove.getList().get(i);
				fs.assignCurrent(parentToMove);
				return;
			}
		}

		// If directory doesn't exist, create it, add it, and assign it as the current
		// node
		Node newDirectory = new Node.Builder(true, newDir).setParent(parentToMove).build();
		fs.addToDirectory(newDirectory);
		fs.assignCurrent(newDirectory);
		parentToMove = newDirectory;
	}

}
