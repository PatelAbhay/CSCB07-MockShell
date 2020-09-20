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
package test;

import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;

import commands.Exit;

/**
 * Class ExitTest runs the test case for Exit
 */
public class ExitTest {

	/**
	 * Declare instance of MockFileSystem so we can access the preset filesystem
	 */
	private MockFileSystem fs;

	/**
	 * Declare instance of Exit to be tested
	 */
	private Exit exit;

	/**
	 * Declare two different instance of a String objects called expected and actual
	 */
	private String expected, actual;

	/**
	 * Sets up the test environment and initializes the instance variables
	 * 
	 * @throws Exception
	 */
	@Before
	public void setup() {
		// Gets a specific preset FileSystem
		fs = MockFileSystem.getMockFileSys("EMPTYSYS");
		// Initializes a Exit Object
		exit = new Exit();
	}

	/**
	 * Destroys the MockFileSystem after each testcases have run
	 * 
	 * @throws Exception
	 */
	@After
	public void tearDown() throws Exception {
		// Declares and initializes a Feild variable
		// to the fileSys variable in MockFileSystem
		Field feild = fs.getClass().getDeclaredField("filesys");
		// Allows the value of this variable in MockFileSystem to be accessed
		feild.setAccessible(true);
		// Changes the value of the variable in MockFileSystem to null
		feild.set(null, null);
	}

	/**
	 * Test : User provides input after the command name
	 */
	@Test
	public void testProvidedArgs() {
		// Expected return from Exit
		expected = "Error : Arguments not required : LOL LOL";
		// Actual return from Exit after the operation has been run
		actual = exit.run(fs, "LOL LOL".split(" "), "exit LOL LOL", false);
		// Checks if the values are equal or not
		assertEquals(expected, actual);
	}

	/**
	 * Test : User used redirection for a nonredirectionable command
	 */
	@Test
	public void testRedirectionError() {
		// Expected return from Exit
		expected = "Error : Redirection Error : exit does not support redirection";
		// Actual return from Exit after the operation has been run
		actual = exit.run(fs, "> LOL".trim().split(" "), "exit > LOL", false);
		// Checks if the values are equal or not
		assertEquals(expected, actual);
	}
}