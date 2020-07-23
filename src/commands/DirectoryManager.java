// **********************************************************
// Assignment2:
// Student1:
// UTORID user_name: patelt26
// UT Student #: 1005904103
// Author: Shawn Santhoshgeorge
//
// Student2:
// UTORID user_name: shaiskan
// UT Student #: 1006243940
// Author: Keshavaa Shaiskandan
//
// Student3:
// UTORID user_name: patelt26
// UT Student #: 1005904103
// Author: Tirth Patel
//
// Student4:
// UTORID user_name: pate1101
// UT Student #: 1006315765
// Author: Abhay Patel
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

import data.FileSystem;
import data.Node;

/**
 * Class DirectoryManager which provides helper methods for other
 * directory-related commands such as getting the current path and creating new
 * paths
 */
public class DirectoryManager {

	ArrayList<String> path = new ArrayList<String>();
	/**
	 * Declare instance of FileSystem to access filesystem
	 */
	FileSystem filesys = null;
	/**
	 * Declare instance of ErrorHandler to handle error messages
	 */
	ErrorHandler error = new ErrorHandler();

	/**
	 * Constructor for DirectoryManager which initializes filesystem
	 */
	public DirectoryManager() {
		filesys = FileSystem.getFileSys();
	}

	/**
	 * Finds the current path within the FileSystem
	 * 
	 * @return the path to the current directory
	 */
	public String getCurrentPath() {
		String output = "";
		Node current = filesys.getCurrent();

		if (current.equals(this.filesys.getRoot())) {
			return this.filesys.getRoot().getName();
		}

		path.add(current.getName());
		while (current != this.filesys.getRoot()) {
			current = current.getParent();
			path.add(current.getName());
		}

		int pathLength = path.size();
		while (pathLength > 0) {
			output = output.concat(path.get(pathLength - 1) + "/");
			pathLength--;
		}

		path.clear();

		return output.substring(1, output.length() - 1);
	}

	/**
	 * Checks if requested directory exists in the ArrayList of nodes in the current
	 * node
	 * 
	 * @param toCheck ArrayList of Nodes that contain all of a Node's children
	 * @param dir     the requested directory to check
	 * @return the index of the requested directory if it exists, -1 otherwise
	 */
	public int checkDirExists(ArrayList<Node> toCheck, String dir) {
		for (int i = 0; i < toCheck.size(); i++) {
			if ((toCheck.get(i)).getName().equals(dir)) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * Makes path from the root
	 * 
	 * @param path the path that the user is requesting
	 * @return true if path was made successfully, false otherwise
	 */
	public boolean makePathFromRoot(String[] pathArr) {
		Node currNode = filesys.getRoot();
		// Loop through pathArr which is array of all directories in the path and change
		// directory to
		// the
		// last one (if its a valid path)
		for (int i = 0; i < pathArr.length; i++) {
			if (pathArr[i].equals("")) {
				continue;
			}
			int indexOfDir = checkDirExists(currNode.getList(), pathArr[i]);
			if (indexOfDir != -1) {
				if (currNode.getList().get(indexOfDir).isDir()) {
					currNode = currNode.getList().get(indexOfDir);
				} else {
					System.out.println(error.getError("Invalid Directory", pathArr[i]));
				}
			} else {
				System.out.println(error.getError("Directory Not Found", pathArr[i]));
				return false;
			}
		}
		filesys.assignCurrent(currNode);
		return true;
	}

	/**
	 * Makes relative path from current directory
	 * 
	 * @param path the path that the user is requesting
	 * @return true if path was made successfully, false otherwise
	 */
	public boolean makeRelativePath(String[] pathArr) {
		Node currNode = filesys.getCurrent();
		// Loop through pathArr which is array of all directories in the path and change
		// directory to
		// the last one (if its a valid path)
		for (int i = 0; i < pathArr.length; i++) {
			int indexOfDir = checkDirExists(currNode.getList(), pathArr[i]);
			if (indexOfDir != -1) {
				if (currNode.getList().get(indexOfDir).isDir()) {
					currNode = currNode.getList().get(indexOfDir);
				} else {
					System.out.println(error.getError("Invalid Directory", pathArr[i]));
					return false;
				}
			} else {
				System.out.println(error.getError("Directory Not Found", pathArr[i]));
				return false;
			}
		}
		filesys.assignCurrent(currNode);
		return true;
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
	public boolean isValidDirectoryName(String fileName) {
		String[] invalidChars = { "/", ".", "\\s+", "!", "@", "#", "$", "%", "^", "&", "*", "(", ")", "{", "}", "~",
				"|", "<", ">", "?" };
		for (int i = 0; i < invalidChars.length; i++) {
			if (fileName.contains(invalidChars[i])) {
				return false;
			}
		}
		return true;
	}

}
