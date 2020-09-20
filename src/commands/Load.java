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

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import data.FileSystemI;
import data.Node;
import errors.InvalidArgsProvidedException;
import errors.InvalidRedirectionError;

/**
 * Class Load is responsible for loading a filesystem saved on a json file to
 * the JShell
 */
public class Load implements CommandI {

	/**
	 * Declare instance variable of FileReader to store the json file located on the
	 * users computer
	 */
	private FileReader fileReader;

	/**
	 * Declare instance variable of BufferedReader to read the file line by line
	 */
	private BufferedReader reader;

	/**
	 * Declare instance variable of String to hold the file path/file location
	 */
	private String filePath;

	/**
	 * Declare instance variable of String to hold the output of the command (error
	 * if error occurs else null)
	 */
	private String output;

	/**
	 * Declare instance variable of String to hold the contents of the compiled data
	 * read from file
	 */
	private String fileContents;

	/**
	 * Declare instance variable of RedirectionManager to handle redirection to file
	 */
	private RedirectionManager redirect;

	/**
	 * Declares instance variable of cd to handle traversals
	 */
	private Cd cdCommand;

	/**
	 * Constructor for class Load which initalizes instance variables
	 */
	public Load() {
		// Initializes a RedirectionManager Object
		this.redirect = new RedirectionManager();
		// Initializes the String output
		this.output = null;
		// Initializes the String fileContents
		this.fileContents = "";
		// Initializes the Cd object
		this.cdCommand = new Cd();
	}

	/**
	 * Method that checks if the parameters provided by the user is considered valid
	 * or not. If the user inputs more than 1 parameter for the command then it
	 * returns false. If the user simply inputs the command name and no parameter
	 * then returns false. Returns true if the user inputs only 1 parameter after
	 * the command.
	 * 
	 * @param fs        FileSystem Object that stores the current filesystem
	 * @param arguments String array that holds the paramters that the user inputted
	 * @param fullInput String object that stores the full input provided by user
	 * @param val       holds a boolean value
	 * 
	 * @return boolean false if the user inputed no parameters or more than 1
	 *         parameter
	 */
	@Override
	public boolean checkArgs(FileSystemI fs, String[] arguments, String fullInput) throws InvalidArgsProvidedException {
		// checks if user inputted a parameter or not
		if (arguments.length == 0) {
			throw new InvalidArgsProvidedException("Error: Invalid Argument : No arguments should be given");
		}
		// checks if user inputted more than one parameter
		else if (arguments.length > 1) {
			throw new InvalidArgsProvidedException("Error : Multiple Parameters have been provided : "
					+ String.join(" ", arguments) + " Only one is required");
		}
		// checks if load was the first command inputted
		else if (!checkCommandLog(fs)) {
			throw new InvalidArgsProvidedException("Error: load was not the first command inputted");
		}
		return true;
	}

	/**
	 * Method that loads a FileSystem to the JShell by reading from a json file on
	 * the users computer. If the user tries to use redirection, the method returns
	 * an error. If the user inputted invalid parameters, invalid filepath, or
	 * invalid filename then method returns corresponding error. Reads file and
	 * recreates the saved FileSystem data from sections: NODES and COMMAND LOG
	 * NODES section holds the data associated with each individual node and each
	 * node created will be added to the filesystem Since the node data were stored
	 * in order (depth first) method adds each node as it create thems COMMAND LOG
	 * section stores the command log of the saved FileSystem Method simply adds
	 * each line read to the current command log If no errors occur, method returns
	 * null
	 * 
	 * @param fs        FileSystem Object that stores the current filesystem
	 * @param arguments String array that holds the paramters that the user inputted
	 * @param fullInput String object that stores the full input provided by user
	 * @param val       boolean value that is true if we are in speakMode and false
	 *                  otherwise
	 * @return String if an error occured then the error message is returned, else
	 *         null is returned
	 */
	@Override
	public String run(FileSystemI filesys, String args[], String fullInput, boolean val) {
		// Seperates the parameters from everything else from the user input
		try {
			// checks for valid arguments
			if (checkArgs(filesys, args, fullInput)) {
				// formats the filepath
				filePath = formatArguments(args);
				// checks if the filename is valid
				validateFileName(filesys, fullInput);
				// FileReader object that holds the file
				fileReader = new FileReader(filePath);
				// BufferedReader object to read the file
				reader = new BufferedReader(fileReader);
				String line = reader.readLine();
				while (line != null) {
					// If we are in the nodes section
					if (line.equals("NODES"))
						// add the nodes
						uploadNodes(line, filesys);
					// if we are in the command log section
					else if (line.equals("COMMAND LOG"))
						// add the command log
						uploadCommandLog(line, filesys);
					else if (line.equals("DIRECTORY STACK"))
						// add the directory stack
						uploadDirectoryStack(filesys, line);
					else if (line.equals("CURRENT PATH"))
						goToCurrentPath(line, filesys);
					line = reader.readLine();
				}
			}
		}
		// Invalid argument error
		catch (InvalidArgsProvidedException e) {
			return e.getLocalizedMessage();
		}
		// Invalid path/file not found error
		catch (FileNotFoundException e) {
			return "Error: Invalid Path : " + args[0];
		}
		// Issue with fileReader and/or BufferedReader
		catch (IOException e) {
			return "Issues with Load";
		}
		return output;
	}

	private void goToCurrentPath(String line, FileSystemI filesys) {
		try {
			// Two readLines need to be used to read the useless \n used to divide sections
			line = reader.readLine();
			line = reader.readLine().trim().replaceAll("\"", "");
			// Cd to go to the current path
			cdCommand.run(line.split(" "), filesys);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void uploadDirectoryStack(FileSystemI filesys, String line) {
		try {
			// Two readLines need to be used to read the useless \n used to divide sections
			line = reader.readLine();
			line = reader.readLine().trim().replaceAll("\"", "");
			// Loop runs until the end of section has been reached
			while (!line.equals("}")) {
				filesys.getStack().push(line);
				line = reader.readLine().trim().replaceAll("\"", "");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
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
		String[] invalidChars = { "/", "\\s+", "!", "@", "#", "$", "%", "^", "&", "*", "(", ")", "{", "}", "~", "|",
				"<", ">", "?", "'", "[", "]" };
		for (int i = 0; i < invalidChars.length; i++) {
			if (fileName.contains(invalidChars[i])) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Method that grabs the file content of the file being read. Used primarily to
	 * test if the data of the nodes are being correctly parsed. Returns the Node
	 * data from the NODES section as that essentially returns the filesystem
	 * structure If the user tries to use redirection, the method returns an error.
	 * If the user inputted invalid parameters, invalid filepath, or invalid
	 * filename then method returns corresponding error. If no errors occur, then
	 * the method returns null
	 * 
	 * @param fs        FileSystem Object that stores the current filesystem
	 * @param arguments String array that holds the paramters that the user inputted
	 * @param fullInput String object that stores the full input provided by user
	 * @param val       boolean value that is true if we are in speakMode and false
	 *                  otherwise
	 * @return String if an error occured then the error message is returned, else
	 *         the fileContents are retured
	 */
	public String getFileContents(FileSystemI filesys, String[] args, String fullInput, boolean val) {
		try {
			// checks if user tries to use redirection with this command
			redirect.isRedirectionableCommand(fullInput);
		}
		// Redirection not compatible with this command
		catch (InvalidRedirectionError e) {
			return e.getLocalizedMessage();
		}
		// Output holds the result of the execution
		output = run(filesys, args, fullInput, false);
		// If there was an error that occured
		if (output != null) {
			if (output.startsWith("Error:") || output.startsWith("Error :"))
				return output;
		}
		// return the fileContents that were read
		return fileContents.trim();
	}

	/**
	 * Method that checks if the filename/filepath that the user inputted is valid.
	 * If the filename is invalid, then method throws FileException. If the file
	 * type is not a json file, then the method throws FileException If the user
	 * does not specify a file type, then by default it is a json file
	 * 
	 * @param filesys   FileSystem Object that stores the current filesystem
	 * @param fullInput String object that stores the full input provided by user
	 */
	private void validateFileName(FileSystemI filesys, String fullInput) throws InvalidArgsProvidedException {
		// checks for valid filename
		if (!checkFileName(filePath, filesys)) {
			throw new InvalidArgsProvidedException("Error: Invalid File : " + fullInput);
		}
		// checks if the file inputted is a .json file
		if (filePath.contains(".")) {
			if (!filePath.substring(filePath.indexOf("."), filePath.length()).equals(".json")) {
				throw new InvalidArgsProvidedException("Error: Invalid File : " + fullInput);
			}
		}
		// doesn't have .json at the end
		else
			filePath += ".json";
	}

	/**
	 * Method that checks the command log of the current filesystem. Returns true if
	 * the size of the command log is less than or equal to 1 If it is greater than
	 * 1, then load was not the first command inputted and method returns false
	 * There must not be any previous command inputted previously (including any
	 * invalid commands)
	 * 
	 * @param filePath String object that stores the full input provided by user
	 * @param filesys  FileSystem Object that stores the current filesystem
	 * @return boolean that is false if filename contains an invalid character, else
	 *         returns true
	 */
	private boolean checkCommandLog(FileSystemI filesys) {
		// checks if load was the first command inputted
		if (filesys.getCommandLog().size() <= 1)
			return true;
		return false;
	}

	/**
	 * Method that checks if the filename is a valid filename. If the filename
	 * contains any invalid characters then the method returns false. If the
	 * filename does not conatin any invalid characters then the method returns
	 * true.
	 * 
	 * @param filePath String object that stores the full input provided by user
	 * @param filesys  FileSystem Object that stores the current filesystem
	 * @return boolean that is false if filename contains an invalid character, else
	 *         returns true
	 */
	private boolean checkFileName(String filePath, FileSystemI filesys) {
		String fileName = "";
		// if given absolute path then grab the filename
		if (filePath.contains("\\"))
			filePath.substring(filePath.lastIndexOf("\\"), filePath.length());
		else
			fileName = filePath;
		// check if filename is valid
		if (isValidName(fileName))
			return true;
		return false;
	}

	/**
	 * Method that reads the NODES section and creates the nodes from the read line
	 * Method reads the data of a node and stores it to a String array and calls
	 * helper function to create the nodes
	 * 
	 * @param line    String object that stores the current line in the json file
	 * @param filesys FileSystem Object that stores the current filesystem
	 */
	private void uploadNodes(String line, FileSystemI filesys) {
		try {
			// Two readLines need to be used to read the useless \n used to divide sections
			line = reader.readLine();
			line = reader.readLine();
			// Loop runs until the end of section has been reached
			while (!line.equals("}")) {
				String[] nodeInformation = new String[4];
				// Grabs the data for the nodes
				for (int i = 0; i < nodeInformation.length; i++) {
					nodeInformation[i] = line;
					fileContents += nodeInformation[i] + "\n";
					line = reader.readLine();
				}
				// create a ndoe using the data read from file
				createNode(nodeInformation, filesys);
				line = reader.readLine();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Method that reads the COMMAND LOG section and adds every line to the
	 * filesystem Method reads the line in the json file and adds that file to the
	 * filesystem command log
	 * 
	 * @param line    String object that stores the current line in the json file
	 * @param filesys FileSystem Object that stores the current filesystem
	 */
	private void uploadCommandLog(String line, FileSystemI filesys) {
		try {
			// Two readLines need to be used to read the useless \n used to divide sections
			line = reader.readLine();
			line = reader.readLine().trim().replaceAll("\"", "");
			// Loop runs until the end of section has been reached
			while (!line.equals("}")) {
				// Adds the command log from the json file to the filesystem command log
				filesys.getCommandLog().add(line);
				fileContents += line + "\n";
				line = reader.readLine().trim().replaceAll("\"", "");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Method that creates the nodes from the inputted array and adds it to the
	 * filesystem Method loops through the array that holds the data and parses it
	 * into data that is used to create the node Index 0 of array represents Node's
	 * Name data field Index 1 of array represents Node's isDir data field Index 2
	 * of array represents Node's Parent data field Index 3 of array represents
	 * Node's Content data field Method then uses the Node Builder to create the
	 * node The content of the node replaces all instances of "\n" to \n character
	 * in order to undo the change save command made Calls filesystem method to add
	 * the newly created node to the filesystem
	 * 
	 * @param nodeInformation String array that stores data read from the file
	 * @param filesys         FileSystem Object that stores the current filesystem
	 */
	private void createNode(String[] nodeInformation, FileSystemI filesys) {
		// Array to hold the parsed data of the node
		String[] parsedNodeInformation = new String[4];
		// for loop to loop through the data read from file
		for (int i = 0; i < nodeInformation.length; i++) {
			// parses the line read from file to extract the needed data
			String parseInfo = nodeInformation[i].split(":")[1].trim();
			parseInfo = parseInfo.substring(1, parseInfo.length() - 1);
			parsedNodeInformation[i] = parseInfo.trim();
		}
		// If the data that we parsed does not represent the root node (cause root node
		// is already in filesystem when created)
		if (!parsedNodeInformation[0].equals(filesys.getRoot().getName())) {
			// Create the node
			Node newNode = new Node.Builder(Boolean.valueOf(parsedNodeInformation[1]), parsedNodeInformation[0])
					.setContent(parsedNodeInformation[3]).build();
			// Replaces the "\n" to \n character as we replaced them before in save command
			newNode.setContent(newNode.getContent().replaceAll("\\\\n", "\n"));
			// if we are in the parent node then just add to the parent node arraylist
			if (parsedNodeInformation[2].equals(filesys.getCurrent().getName()))
				filesys.addToDirectory(newNode);
			else {
				// loop therough and add to the filesystem
				addNodeToFileSystem(newNode, parsedNodeInformation[2], filesys);
			}
		}
	}

	/**
	 * Method that adds the newNode to the correct location Calls a helper function
	 * to traverse the file system to find the parent node
	 * 
	 * @param newNode    Node that holds the newly created node that is to be added
	 *                   to the fiesystem
	 * @param parentName String that holds the name of the parent of the newNode
	 * @param filesys    FileSystem Object that stores the current filesystem
	 */
	private void addNodeToFileSystem(Node newNode, String parentName, FileSystemI filesys) {
		filesys.assignCurrent(filesys.getRoot());
		// Traverse the filesystem using Depth First Search
		traverseFileSystem(filesys.getCurrent(), parentName, newNode, filesys);
	}

	/**
	 * Method that traverse the filesytem to find the parent node to add the newNode
	 * to the correct location Once parent is found, add the newNode to the
	 * filesystem with the parent being the current node
	 * 
	 * @param current           Node that holds the current position node in the
	 *                          fileSystem
	 * @param desiredParentName String that holds the name of the parent of the
	 *                          newNode
	 * @param newNode           Node that holds the newly created node that is to be
	 *                          added to the fiesystem
	 * @param filesys           FileSystem Object that stores the current filesystem
	 */
	private void traverseFileSystem(Node current, String desiredParentName, Node newNode, FileSystemI filesys) {
		// If we have reached the parent node then just add the newNode to the parent
		// node arraylist
		if (current.getName().equals(desiredParentName)) {
			newNode.setParent(current);
			filesys.addToDirectory(newNode);
		} else {
			// loop through the arraylist of nodes
			for (int i = 0; i < current.getList().size(); i++) {
				filesys.assignCurrent(current.getList().get(i));
				// traverse teh filesystem again
				traverseFileSystem(filesys.getCurrent(), desiredParentName, newNode, filesys);
			}
		}
	}

	/**
	 * Method that formats the user input to make sure the file path is valid in
	 * Windows Windows require path to be split using \\ instead of \ or / This
	 * method replaces / and \ with \\ for full compatibility
	 * 
	 * @param args String array that stores the arguments inputted by the user
	 * @return String that is the formatted version of the user inputted arguments
	 */
	private String formatArguments(String[] args) {
		// replaces / with \\
		if (args[0].contains("/"))
			return args[0].replace("/", "\\\\");
		// replaces \ with \\
		if (args[0].contains("\\"))
			return args[0].replace("\\", "\\\\");
		return args[0];
	}

}