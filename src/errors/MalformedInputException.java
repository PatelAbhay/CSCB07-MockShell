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
package errors;

/**
 * Class MalformedInputException handles expections related to malformed text by
 * improper use of quotations
 */

@SuppressWarnings("serial")
public class MalformedInputException extends InvalidArgsProvidedException {

	/**
	 * Handles the message to be sent to the console
	 * 
	 * @param message the message to be sent about error
	 */
	public MalformedInputException(String message) {
		// Returns an error message from where it was thrown
		super(message);
	}
}