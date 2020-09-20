
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
 * Class Cp is responsible for copying directories/files from the first given
 * path to the second given path
 */
public class Cp extends DirectoryManager implements CommandI {

	/**
	 * Declare instance variable of ArrayList to hold all arguments
	 */
	ArrayList<String> args;
	/**
	 * Declare instance variable of RedirectionManager
	 */
	private final RedirectionManager rManager;
	/**
	 * Declare instance of Cd
	 */
	private final Cd traverse;

	/**
	 * Paths needed to traverse filsystem to copy and paste files/folders
	 */
	String[] pathFrom = { "" };
	String[] pathTo = { "" };
	String[] currentPath = { "" };
	String fileName;
	String newDir;
	/**
	 * Nodes of the parent directory of the copied file and the copied node itself
	 */
	Node parentToMove;
	Node toMove = null;
	/**
	 * Return string for output
	 */
	String output;

	/**
	 * Cp constructor to initialize rManager and traverse instance variables
	 */
	public Cp() {
		// Initializes the following instance vairables
		rManager = new RedirectionManager();
		traverse = new Cd();
	}

	/**
	 * Run method where copying and pasting of file/directory will occur
	 * 
	 * @param filesys     Filesystem to be mutated when copying and pasting
	 *                    file/directory
	 * @param args        Array containing arguments such as the old path to the new
	 *                    path information
	 * @param actualInput String containing full input as a string
	 * @param val         Indicator of speakmode is true or false
	 */
	public String run(FileSystemI filesys, String[] args, String actualInput, boolean val) {
		// Seperates the parameters from everything else from the user input
		final String[] arguments = rManager.setParams(actualInput);
		this.args = new ArrayList<String>(Arrays.asList(arguments));
		try {
			rManager.isRedirectionableCommand(actualInput);
			// Checks arguments to see if they are valid or not
			if (checkArgs(filesys, arguments, actualInput)) {
				// Initializes path to traverse to, to copy and paste
				initPathandFile(filesys);
				// Copies node to node toMove
				output = copyFile(filesys);
				// Pastes node toMove in location
				output = moveFile(filesys);
			}
		} catch (final InvalidArgsProvidedException e) {
			// Catches an exceptions thrown
			return e.getLocalizedMessage();
		}

		traverse.run(currentPath, filesys);

		return null;
	}

	/**
	 * Initializes the pathFrom, pathTo, newDir and filename variables, this is
	 * needed to traverse the system to copy and paste into the right places
	 * 
	 * @param fs Filesystem to be mutated with new files
	 * @throws DirectoryException throws an exception when invalid arguments or
	 *                            directory is given
	 */
	public void initPathandFile(final FileSystemI fs) throws DirectoryException {
		// Checks if pathFrom contains a /
		// This is important info to traverse nodes
		if (args.get(0).contains("/")) {
			pathFrom[0] = args.get(0).substring(0, args.get(0).lastIndexOf("/"));
			if (pathFrom[0].equals("")) {
				// Ensures that edge case of / is handled properly
				pathFrom[0] = "/";
			}
			// Used to set fileName of node to copy
			if (args.get(0).lastIndexOf("/") == 0) {
				fileName = args.get(0).substring(1, args.get(0).length());
			} else {
				fileName = args.get(0).substring(args.get(0).lastIndexOf("/") + 1, args.get(0).length());
			}
		} else {
			pathFrom[0] = fs.getCurrentPath();
			fileName = args.get(0);
		}
		// Checks if pathTo contains a /
		// This is important to find where to traverse TO, to paste the node
		if (args.get(1).contains("/")) {
			pathTo[0] = args.get(1).substring(0, args.get(1).lastIndexOf("/"));
			if (pathTo[0].equals("")) {
				pathTo[0] = "/"; // Handles edge case of trying to move a node to the root
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
		// Checks if the newDirectory to possibly make is a valid directory
		if (!fs.isValidName(newDir)) {
			throw new DirectoryException("Error: Invalid Directory " + newDir + " is not a valid directory name");
		}
		// Stores the current path the user is at
		currentPath[0] = fs.getCurrentPath();
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
	public boolean checkArgs(final FileSystemI filesys, final String[] arguments, final String fullInput)
			throws InvalidArgsProvidedException, DirectoryException {
		// Checks if there are more or less than 2 arguments provided
		if (args.size() != 2)
			throw new InvalidArgsProvidedException("Error: Invalid Argument : Expected 2 arguments");
		// Checks if node to be copied is the root, which is invalid
		if (args.get(0).equals("/"))
			throw new DirectoryException("Error: Invalid Directory : Cannot copy the root directory");

		return true;
	}

	/**
	 * Traverses to the first path given to copy the node. This node is then bundled
	 * into toMove to be pasted later
	 * 
	 * @param fs Filesystem to be mutated
	 * @return A string indicating any message to the user if need be
	 * 
	 * @throws DirectoryException Throws an exception if invalid directory given
	 */
	public String copyFile(final FileSystemI fs) throws DirectoryException {
		// Traverses to node to copy from
		if (traverse.run(pathFrom, fs)) {
			pathFrom[0] = fs.getCurrentPath();
			parentToMove = fs.getCurrent();
			// Tries to find node to be copied based of name
			for (int i = 0; i < parentToMove.getList().size(); i++) {
				if (parentToMove.getList().get(i).getName().equals(fileName)) {
					// Gets copy of node to be created as reference
					final Node copy = parentToMove.getList().get(i);
					// Makes copy of files that the node contains as reference
					final ArrayList<Node> copyContents = new ArrayList<Node>(copy.getList());
					// Creates new node to be pasted into new directory
					toMove = new Node.Builder(copy.getisDir(), copy.getName()).setContent(copy.getContent())
							.setList(copyContents).setParent(parentToMove).setRoot(false).build();
				}
			}
			// Traverses back to the starting point
			traverse.run(currentPath, fs);
			// Throws an exception if no node was built, meaning file/directory doesn't
			// exist in specified path
			if (toMove == null) {
				throw new DirectoryException(
						"Error: Directory Not Found : " + fileName + " does not exist in the path you specified!");
			}

		} else {
			// Throws an exception if pathFrom path is not a valid path
			throw new DirectoryException("Error: Invalid Directory : " + pathFrom[0] + " does not exist!");
		}
		return null;
	}

	/**
	 * Take the newly created toMove node and pastes it into the second path
	 * provided
	 * 
	 * @param fs Filesystem to be mutated
	 * @return A string for user if need be
	 * 
	 * @throws DirectoryException Throws a directory exception if second path is
	 *                            invalid
	 */
	public String moveFile(final FileSystemI fs) throws DirectoryException {
		// Traverse to path for node to be PASTED
		if (traverse.run(pathTo, fs)) {
			// Handles edge case of pasting into the root folder
			if (newDir.equals("")) {
				fs.addToDirectory(toMove);
				traverse.run(currentPath, fs);
				return null;
			}
			// Assigns parentToMove as the directory where node needs to be added IN to
			parentToMove = fs.getCurrent();
			// Checks if new folder needs to be created or already exists
			checkDirectory(fs);
			int toOverride = -1;
			// Checks if node within already exists and needs to be overriden
			for (int i = 0; i < parentToMove.getList().size(); i++) {
				if (toMove.getName().equals(parentToMove.getList().get(i).getName())) {
					toOverride = i;
					break;
				}
			}
			// If file doesn't exist already then simply add the node to the directory
			if (toOverride == -1) {
				fs.addToDirectory(toMove);
			} else {
				// if the file does exist, remove it and add the new one in
				fs.removeFromDirectory(toOverride);
				fs.addToDirectory(toMove);
			}
			// Traverse back to the location where the user was initially
			traverse.run(currentPath, fs);
		} else {
			// Throws an exception if pathTo path is an invalid path
			throw new DirectoryException("Error: Invalid Directory : " + pathTo[0] + " does not exist!");
		}
		return null;
	}

	/**
	 * Checks if the second path has the folder needed to paste the node into, if
	 * not it will create that folder.
	 * 
	 * @param fs Filesystem to be mutated
	 */
	public void checkDirectory(final FileSystemI fs) {
		// Checks if folder is in the directory or not, if it is
		// It assigns the current position of the user to that directory and returns
		for (int i = 0; i < parentToMove.getList().size(); i++) {
			if (parentToMove.getList().get(i).getName().equals(newDir)) {
				parentToMove = parentToMove.getList().get(i);
				fs.assignCurrent(parentToMove);
				return;
			}
		}

		// This runs if the folder wasn't found, this creates the new folder and adds it
		// to the dir
		// above it. It then assigns this new folder as the current position of the
		// user.
		Node newDirectory = new Node.Builder(true, newDir).setParent(parentToMove).build();
		fs.addToDirectory(newDirectory);
		fs.assignCurrent(newDirectory);
		parentToMove = newDirectory;
	}

}
