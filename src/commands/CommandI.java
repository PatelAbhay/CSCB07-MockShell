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

import data.FileSystemI;
import errors.InvalidArgsProvidedException;

/**
 * Interface CommandI is responsible for providing a common method to call any
 * class containing that certain command
 */

interface CommandI {

	/**
	 * Runs the command based on the implementation of this method in the respective
	 * class
	 * 
	 * @param args      the string array of arguments
	 * @param fullInput the full line of input that the user gives into JShell
	 * @param val       stores a boolean value
	 * 
	 * @return any error message or valid output for user input
	 */
	public String run(FileSystemI fs, String[] args, String fullInput, boolean val);

	/**
	 * Provides a comman method to check the arguments of the of the command and
	 * throw exception if required
	 * 
	 * @param fs        refrence of FileSystemI object (MockFileSystem or
	 *                  FileSystem)
	 * @param arguments the list of arguments from user which may contain a
	 *                  redirection error
	 * @param fullInput the user input
	 * 
	 * @return true if it meets a the requirements of the command and falso if not
	 * 
	 * @throws InvalidArgsProvidedException
	 */
	public boolean checkArgs(FileSystemI fs, String[] arguments, String fullInput) throws InvalidArgsProvidedException;

}
