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
package data;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;

import errors.FileException;

/**
 * Class FileSystem hosts the data structure required to contain all directory
 * and file nodes
 */
public class FileSystem implements FileSystemI {

	/**
	 * Declare instance variable FileSystem which is yet to be intialized
	 */
	static FileSystem fileSys = null;
	/**
	 * Declare instance variable of ArrayList to contain the logs of all inputs
	 * given to JShell
	 */
	private static ArrayList<String> CommandLog;
	/**
	 * Declare instance variable of ArrayDeque
	 */
	private static Deque<String> stack;

	/**
	 * Initialize current and root nodes
	 */
	Node root;
	Node current;

	/**
	 * Constructor for FileSystem to set root default values Sets stack for pushd
	 * and popd Sets history list for history command Sets current equal to root
	 * directory
	 */
	private FileSystem() {
		CommandLog = new ArrayList<String>();
		stack = new ArrayDeque<String>();
		root = new Node.Builder(true, "/").setRoot(true).setParent(null).build();
		current = root;
	}

	/**
	 * Returns FileSystem of object, keep in mind that it is returning the same
	 * object unless being called for the first time
	 * 
	 * @return the filesystem object
	 */
	public static FileSystem getFileSys() {
		if (fileSys == null) {
			fileSys = new FileSystem();
		}
		return fileSys;
	}

	// Trivial methods below that require no JavaDoc
	public Node getRoot() {
		return root;
	}

	public Node getCurrent() {
		return current;
	}

	public String getContent(Node file) {
		return file.getContent();
	}

	public void assignCurrent(Node currentDirectory) {
		current = currentDirectory;
	}

	public void addToDirectory(Node newNode) {
		newNode.setParent(current);
		current.getList().add(newNode);
	}

	public void removeFromDirectory(int i) {
		current.getList().remove(i);
	}

	public ArrayList<String> getCommandLog() {
		return CommandLog;
	}

	public Deque<String> getStack() {
		return stack;
	}

	/**
	 * Method that checks if the file name that the user inputted is a valid file
	 * name. If the file name contains illegal characters then this method returns
	 * false. If the file name does not contain any illegal characters then it
	 * returns true.
	 * 
	 * @param fileName String that stores the file name that the user inputted
	 * @return boolean false if file name contains illegal characters, otherwise
	 *         returns true
	 */
	public boolean isValidName(String fileName) {
		String[] invalidChars = { "/", ".", "\\s+", "!", "@", "#", "$", "%", "^", "&", "*", "(", ")", "{", "}", "~",
				"|", "<", ">", "?", "'", "[", "]" };
		for (int i = 0; i < invalidChars.length; i++) {
			if (fileName.contains(invalidChars[i])) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Finds the current working directory
	 * 
	 * @return the path to the current directory
	 */
	public String getCurrentPath() {
		// Grabs the current directory
		Node current = getCurrent();
		// Grabs the parent of the current directory
		Node parent = current.getParent();

		// If we are in the root folder
		if (parent == null)
			return current.getName();
		// Create the absolute path
		String currentPath = parent.getName() + "/" + current.getName();
		parent = parent.getParent();
		while (parent != null) {
			currentPath = parent.getName() + "/" + currentPath;
			parent = parent.getParent();
		}
		// Return the final absolute path
		if (currentPath.startsWith("//"))
			return currentPath.substring(1);
		return currentPath;
	}

	/**
	 * Method that searches the filesystem to check if the filepath already exists.
	 * Converts every input to absolute path using the getCurrent() method in
	 * FileSystem. Looks through the filesystem and if the node exists then the
	 * method returns the node else it returns null
	 * 
	 * @param fileName         String that stores the file name that the user
	 *                         inputted
	 * @param fileIsFolderNode boolean false if the file to be found is a file node
	 *                         and false if the file is a folder node
	 * @return Node that holds the node that the method is searching for, else
	 *         returns null if node is not in filesystem
	 */
	@Override
	public Node findFile(String filePath, boolean fileIsFolderNode) {
		String absolutePath = filePath.trim();
		// System.out.println("Absolute before " + absolutePath);
		if (filePath.startsWith("//"))
			return null;

		if (getCurrent().getName().equals(filePath))
			return getCurrent();
		// If the given path is a relative path then make it a absolute path
		if (!filePath.startsWith("/")) {
			if (getCurrent() != getRoot())
				absolutePath = (getCurrentPath() + "/" + filePath);
			else
				absolutePath = getCurrentPath() + filePath;
		}

		// Grabs root directory
		Node current = getRoot();
		// Splits the absolutePath into the individual folders
		String[] directories = absolutePath.split("/");

		// Loops through the directories array
		for (int i = 1; i < directories.length; i++) {
			// Loops through the ArrayList of directories
			for (int j = 0; j < current.getList().size(); j++) {
				// If the folder matches the one we need then return it
				if (current.getList().get(j).getName().equals(directories[i])) {
					// if we are at the last folder/file in the filepath
					if ((i + 1) == directories.length) {
						// check if the node matches the type of node we are looking for
						if (fileIsFolderNode == current.getList().get(j).getisDir()) {
							// return the node
							current = current.getList().get(j);
							return current;
						}
					}
					// we are looping through the other directories
					else {
						current = current.getList().get(j);
					}
				}
			}
		}
		return null;
	}

	/**
	 * Method that appends the user inputted contents to a file in the filesystem.
	 * Uses helper function findFile to look if the file already exists in the
	 * filesystem If file exists, then method simply appends the inputted contents
	 * If file does not exist, then method creates a file with the given path and
	 * contents If the user inputs an invalid path, such as a directory that does
	 * not exist, then the method throws an error Once file has been created, method
	 * adds it to the filesystem
	 * 
	 * @param content String that stores the content of the file node
	 * @param file    String that stores the filename/file location in the
	 *                filesystem
	 */
	@Override
	public void fileAppend(String content, String file) throws FileException {
		// looks for file in the current filesystem
		Node fileNode = findFile(file, false);
		if (fileNode != null) {
			// append the content to the found file
			fileNode.setContent(fileNode.getContent() + "\n" + content);
		} else {
			// get filename from filepath
			String fileName = file.split("/")[file.split("/").length - 1];
			// if valid filename
			if (isValidName(fileName)) {
				// grab current
				Node currentNode = getCurrent();
				// get filename
				String desiredPath = fileName;
				// convert to absolute path
				if (!desiredPath.startsWith("/")) {
					if (getCurrent() != getRoot())
						desiredPath = (getCurrentPath() + "/" + file);
					else
						desiredPath = (getCurrentPath() + file);
				}
				// grabs the path to the parent node
				desiredPath = desiredPath.substring(0, desiredPath.lastIndexOf("/"));
				Node parent;
				if (desiredPath.equals(""))
					parent = getRoot();
				else
					parent = findFile(desiredPath, true);
				// if invalid path is given
				if (parent == null)
					throw new FileException("Error: Invalid Path : A directory does not exist");
				assignCurrent(parent);
				// create the node
				fileNode = new Node.Builder(false, fileName).setContent(content).build();
				// add to current filesystem
				addToDirectory(fileNode);
				assignCurrent(currentNode);
			} else {
				throw new FileException("Error: Invalid File : " + file + " is not a valid file name");
			}
		}
	}

	/**
	 * Method that overwrites a file with the user inputted contents. Uses helper
	 * function findFile to look if the file already exists in the filesystem If
	 * file exists, then method simply overwrite with the inputted contents If file
	 * does not exist, then method creates a file with the given path and contents
	 * If the user inputs an invalid path, such as a directory that does not exist,
	 * then the method throws an error Once file has been created, method adds it to
	 * the filesystem
	 * 
	 * @param content String that stores the content of the file node
	 * @param file    String that stores the filename/file location in the
	 *                filesystem
	 */
	@Override
	public void fileOverwrite(String content, String file) throws FileException {
		// looks for file in the current filesystem
		Node fileNode = findFile(file, false);
		if (fileNode != null) {
			// overwrite the content to the found file
			fileNode.setContent(content);
		} else {
			// get the filename from filepath
			String fileName = file.split("/")[file.split("/").length - 1];
			// if valid filename
			if (isValidName(fileName)) {
				Node currentNode = getCurrent();
				String desiredPath = file;
				// converts to absolute path
				if (!desiredPath.startsWith("/")) {
					if (getCurrent() != getRoot())
						desiredPath = (getCurrentPath() + "/" + file);
					else
						desiredPath = (getCurrentPath() + file);
				}
				// grabs the path to the parent node
				desiredPath = desiredPath.substring(0, desiredPath.lastIndexOf("/"));
				Node parent;
				// find parent node
				if (desiredPath.equals(""))
					parent = getRoot();
				else
					parent = findFile(desiredPath, true);
				// if invalid path is given
				if (parent == null)
					throw new FileException("Error: Invalid Path : A directory does not exist");
				assignCurrent(parent);
				// create the node
				fileNode = new Node.Builder(false, fileName).setContent(content).build();
				// add the new fileNode to the filesystem
				addToDirectory(fileNode);
				assignCurrent(currentNode);
			} else {
				throw new FileException("Error: Invalid File : " + file + " is not a valid file name");
			}
		}
	}

	/**
	 * Checks if the given filename exists within the current directory node of the
	 * user
	 * 
	 * @param name filename to look for within current directory
	 * @return boolean indiciating if the file exists or not in the current
	 *         directory
	 * 
	 */
	@Override
	public boolean checkRepeat(String name) {
		for (int i = 0; i < getCurrent().getList().size(); i++) {
			if (getCurrent().getList().get(i).getName().equals(name)) {
				return false;
			}
		}
		return true;
	}
}
