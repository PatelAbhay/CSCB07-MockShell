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
import errors.FileException;
import errors.InvalidArgsProvidedException;
import errors.InvalidRedirectionError;

/**
 * Class RedirectionManager is responsible for handling redirection of output to
 * file
 */
public class RedirectionManager {

	/**
	 * Declares instance variable of ArrayList that holds all the commands that can
	 * handle redirection
	 */
	private ArrayList<String> redirectClasses;

	/**
	 * Declare instance variables of String to hold teh file name and what type of
	 * redirection is to be used
	 */
	String mode, fileName;

	/**
	 * Constructor for RedirectionManager which initializes the instance variables
	 * and populates teh ArrayList with command names
	 */
	public RedirectionManager() {
		// Creates a ArrayList Object called redirectClasses
		redirectClasses = new ArrayList<String>();
		// Initializes the ArrayList with values
		redirectClasses.addAll(Arrays.asList("cat", "echo", "find", "history", "ls", "man", "pwd", "tree"));
	}

	/**
	 * Checks if the given command is a redirectionable command or throws an
	 * exception
	 * 
	 * @param fullInput the user input
	 * 
	 * @throws InvalidRedirectionError if the command is not part of the list
	 * 
	 * @return true if command is redirectionable and false other wise
	 */
	public boolean isRedirectionableCommand(String fullInput) throws InvalidRedirectionError {
		// Retrives the command from the user input
		String command = fullInput.split(" ")[0];

		// If the given command is not a redirectionalble command
		if (!redirectClasses.contains(command)) {
			// Checks if the command uses either > or >> in
			// the parameters and returns an error if so
			if (Arrays.asList(fullInput.split(" ")).contains(">")
					|| Arrays.asList(fullInput.split(" ")).contains(">>")) {
				// Returns the respective error -> Command is not redirectionable
				throw new InvalidRedirectionError(
						"Error : Redirection Error : " + command + " does not support redirection");
			}
		}
		// Returns if there is an issue
		return false;
	}

	/**
	 * Collects the correct params from the user input
	 * 
	 * @param fs        refrence of the file system(FileSystem or MockFileSystem)
	 * @param fullInput the user input
	 * 
	 * @return the parameters for the command to run or null if multiple file names
	 *         are provided
	 */
	public String[] setParams(String fullInput) {
		// Initializes an array containing the words of the parsedInput
		String[] params = fullInput.split(" ");

		try {
			// If the user used the single arrow >> which sets redirection to overwrite a
			// file
			if (Arrays.asList(params).contains(">")) {
				// Sets the redirection mode to overwrite
				mode = "O";
				// Collects the file name
				fileName = setFileName(params, ">");
				// Collects the parameters for the command
				params = Arrays.copyOfRange(params, 1, Arrays.asList(params).indexOf(">"));
				// If the user used the single arrow >> which sets redirection to append a file
			} else if (Arrays.asList(params).contains(">>")) {
				// Sets the redirection mode to append
				mode = "A";
				// Collects the file name
				fileName = setFileName(params, ">>");
				// Collects the parameters for the command
				params = Arrays.copyOfRange(params, 1, Arrays.asList(params).indexOf(">>"));
				// If the user did not use redirection for a redirectionable command
			} else {
				// Collects the parameters for the command
				params = Arrays.copyOfRange(fullInput.split(" "), 1, fullInput.split(" ").length);
				// Sets the fileName and mode to be empty
				fileName = "";
				mode = "";
			}
			// Catches the error of zero or multiple file names
		} catch (InvalidArgsProvidedException e) {
			// Returns an error
			return e.getLocalizedMessage().split(" ");
		}

		// Returns the parameters
		return params;
	}

	/**
	 * Collects the file name from the user input or throws an exception
	 * 
	 * @param params the user input but split up
	 * @param type   either append(">>") or overwrite(">")
	 * 
	 * @throws InvalidArgsProvidedException if there is no file name or multiple
	 *                                      file names
	 * 
	 * @return the file name or null with the error printed on the console
	 */
	public String setFileName(String[] params, String type) throws InvalidArgsProvidedException {
		// Collects all the content after the either ">" or ">>"
		String[] fileName = Arrays.copyOfRange(params, Arrays.asList(params).indexOf(type) + 1, params.length);
		// If the user provided no file name for the redirection
		if (fileName.length == 0) {
			// Sends the error message instead of a fileName
			throw new InvalidArgsProvidedException("Error : No parameters provided for redirection");
			// If the user provided multiple file names
		} else if (fileName.length > 1) {
			// Converts the array to String
			String parameter = Arrays.toString(fileName);
			parameter = parameter.substring(0, parameter.length()).trim();
			// Sends the error message instead of a fileName
			throw new InvalidArgsProvidedException("Error : Multiple Parameters have been provided : " + parameter
					+ " Only one is required for redirection");
		}
		// If the user provided one file name then return that file name
		return fileName[0];
	}

	/**
	 * Redirects the content to a file or back to the console or throw an exception
	 * if the filename is invalid for redirection
	 *
	 * @param fs     refrence of the file system(FileSystem or MockFileSystem)
	 * @param result the string to be outputed to the console
	 * 
	 * @return returns null is there is not issue or returns the result or does the
	 *         redirection
	 */
	public String outputResult(FileSystemI fs, String result) {
		try {
			// Checks if the operation returns any input or not
			if (result == null) {
				return null;
				// If the return was some sort of an Error prints the error out
			} else if (result.startsWith("Error")) {
				return result;
				// If the user want to overwrite a file with this new result
			} else if (mode.equals("O")) {
				fs.fileOverwrite(result, fileName);
				// Resets the redirection
				mode = "";
				return null;
				// If the user want to append to the file with this new result
			} else if (mode.equals("A")) {
				fs.fileAppend(result, fileName);
				// Resets the redirection
				mode = "";
				return null;
				// If the user chooses not to redirect the results to a file
			} else {
				// If the user is not using redirection at all
				return result;
			}
			// catches if the user provided an innvalid filenames
		} catch (FileException e) {
			// Return the error message
			return e.getLocalizedMessage();
		}
	}
}