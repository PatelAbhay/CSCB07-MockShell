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
package driver;

import java.util.Scanner;
import commands.Checker;
import data.FileSystem;

/**
 * Class JShell is the main driver program where the user inputs commands
 */
public class JShell {
	/**
	 * Main method which handles the main loop for shell commands
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		Checker parser = new Checker();
		FileSystem fs = FileSystem.getFileSys();
		Scanner sc = new Scanner(System.in);

		boolean running = true;

		while (running) {
			System.out.print("$");
			String input = sc.nextLine();
			fs.getCommandLog().add(input);
			parser.parseInput(input);
		}
		sc.close();
	}
}
