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

/**
 * Class Checker is responsible for checking if the user gave valid input and
 * splits up the command from the arguments
 */
public class Checker {

	/**
	 * Declare instance of CommandHandler so this can send command and arguments to
	 * it
	 */
	private CommandHandler manager;

	/**
	 * Declare instance of String to contain the user's input
	 */
	public String userInput;

	/**
	 * Constructor for Checker which initializes the instance variables
	 */
	public Checker() {
		// Initializing the String object output
		this.userInput = "";
		// Initializing a CommandHandler object
		this.manager = new CommandHandler();
	}

	/**
	 * Parses the raw input that the user gives into JShell, which then sends it to
	 * CommandHandler if valid
	 * 
	 * @param input the raw input provided by the user in JShell
	 */
	public void parseInput(String input) {
		// Declares and initialized String objects called parsedInput, commandName and
		// args
		String parsedInput = "";
		String commandName = "";
		String args = "";

		// Checks if the input contains a quotation(s) in it
		if (input.contains("\"")) {
			// Collects and stores the command
			commandName = input.substring(0, input.indexOf("\"")).trim().replaceAll("\\s+", " ");
			// Collects and stores the arguments
			args = input.substring(input.indexOf("\""), input.length());
			// Reconstructs the user input
			parsedInput = commandName + " " + args;
			// If the user input contains single quotations (' ')
		} else if (input.contains("'")) {
			commandName = input.substring(0, input.indexOf("'")).trim().replaceAll("\\s+", " ");
			args = input.substring(input.indexOf("'"), input.length());
			parsedInput = commandName + " " + args;
			// If the quotation contains multiple whitespace(s)
		} else
			// Converts user input by removing all extra whitespace
			parsedInput = input.trim().replaceAll("\\s+", " ");

		// Sets the parsed input to the CommandHandler
		manager.setCommand(parsedInput);
	}

}
