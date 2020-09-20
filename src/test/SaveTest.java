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

import commands.Save;

import java.lang.reflect.Field;

/**
 * Class SaveTest runs all the different test cases for Save
 */
public class SaveTest {

	/**
	 * Declare instance of MockFileSystem so we can access the filesystem
	 */
	private MockFileSystem fs;

	/**
	 * Declares instance of Save to be tested
	 */
	private Save save;

	/**
	 * Declare two different instance of a String objects called expected and actual
	 */
	String expected, actual;

	/**
	 * Sets up the test environment and initializes the instance variables
	 * 
	 * @throws Exception
	 */
	@Before
	public void setup() {
		fs = MockFileSystem.getMockFileSys("EMPTYSYS");
		save = new Save();
	}

	/**
	 * Destroys the FileSystem after all the testcases have run
	 * 
	 * @throws Exception
	 */
	@After
	public void tearDown() throws Exception {
		// Declares and initializes a Feild variable
		// to the fileSys variable in FileSystem
		Field feild = fs.getClass().getDeclaredField("filesys");
		// Allows the value of this variable in FileSystem to be accessed
		feild.setAccessible(true);
		// Changes the value of the variable in FileSystem to null
		feild.set(null, null);
	}

	/**
	 * Test : User does not input a filepath or filename
	 */
	@Test
	public void testNoArgs() {
		// String array that holds the input parameters
		String[] emptyArr = {};
		// Expected string output from save
		expected = "Error : No parameters provided";
		// Actual output when running save
		actual = save.getFileContent(fs, emptyArr, "save", 1);
		// Checks if the values are equal or not
		assertEquals(expected, actual);
	}

	/**
	 * Test : User inputs an invalid filename
	 */
	@Test
	public void testInvalidFileName() {
		// Expected string output from save
		expected = "Error: Invalid File : save !";
		// Actual output when running save
		actual = save.getFileContent(fs, "!".split(" "), "save !", 1);
		// Checks if the values are equal or not
		assertEquals(expected, actual);
	}

	/**
	 * Test : User inputs an invalid filepath
	 */
	@Test
	public void testInvalidPath() {
		// Expected string output from save
		expected = "Error: Invalid Path : //testing";
		// Actual output when running save
		actual = save.getFileContent(fs, "//testing".split(" "), "save //testing", 1);
		// Checks if the values are equal or not
		assertEquals(expected, actual);
	}

	/**
	 * Test : Checks if the NODES section stores the correct data
	 */
	@Test
	public void testCheckNodes() {
		// Expected string output from save
		expected = "name : /\n" + "\tisDir : true\n" + "\tparent : null";
		// Actual output when running save
		actual = save.getFileContent(fs, "savefile".split(" "), "save savefile", 1);
		// Checks if the values are equal or not
		assertEquals(expected, actual);
	}

	/**
	 * Test : Checks if the FILESYSTEM section stores the correct data
	 */
	@Test
	public void testCheckFilesystem() {
		// Expected string output from save
		expected = "/";
		// Actual output when running save
		actual = save.getFileContent(fs, "savefile".split(" "), "save savefile", 2);
		// Checks if the values are equal or not
		assertEquals(expected, actual);
	}

	/**
	 * Test : Checks if the COMMAND LOG section stores the correct data
	 */
	@Test
	public void testCheckCommandLog() {
		// Expected string output from save
		expected = "";
		// Actual output when running save
		actual = save.getFileContent(fs, "savefile".split(" "), "save savefile", 3);
		// Checks if the values are equal or not
		assertEquals(expected, actual);
	}

	/**
	 * Test : Checks if the DIRECTORY STACK section stores the correct data
	 */
	@Test
	public void testCheckDirectoryStack() {
		// Expected string output from save
		expected = "";
		// Actual output when running save
		actual = save.getFileContent(fs, "savefile".split(" "), "save savefile", 4);
		// Checks if the values are equal or not
		assertEquals(expected, actual);
	}

	/**
	 * Test :Checks if the CURRENT PATH section stores the correct data
	 */
	@Test
	public void testCheckCurrentPath() {
		// Expected string output from save
		expected = "/";
		// Actual output when running save
		actual = save.getFileContent(fs, "savefile".split(" "), "save savefile", 5);
		// Checks if the values are equal or not
		assertEquals(expected, actual);
	}

	/**
	 * Test : User uses redirection for a non redirectionable command
	 */
	@Test
	public void testRedirectionError() {
		// Actual output when running save
		actual = save.getFileContent(fs, "input > text".split(" "), "save input > text", 1);
		// Expected string output from save
		expected = "Error : Redirection Error : save does not support redirection";
		// Checks if the values are equal or not
		assertEquals(expected, actual);
	}

	/**
	 * Test : User only inputs the filename that is not a .json
	 */
	@Test
	public void testGivenFileNameWithIncorrectFileType() {
		// Expected string output from save
		expected = "Error: Invalid File : save savefile.txt";
		// Actual output when running save
		actual = save.getFileContent(fs, "savefile".split(" "), "save savefile.txt", 1);
		// Checks if the values are equal or not
		assertEquals(expected, actual);
	}

}