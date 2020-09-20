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

import commands.Cat;

import java.lang.reflect.Field;

/**
 * Class CatTest runs all the different test cases for Cat
 */
public class CatTest {

	/**
	 * Declare instance of FileSystem so we can access the filesystem
	 */
	private static MockFileSystem fs;

	/**
	 * Declares instance of Cat to be tested
	 */
	private static Cat cat;

	/**
	 * Declare two different instance of a String objects called expected and actual
	 */
	private static String expected, actual;

	/**
	 * Sets up the test environment and initializes the instance variables
	 * 
	 * @throws Exception
	 */
	@Before
	public void setup() throws Exception {
		// Gets a specific preset FileSystem
		fs = MockFileSystem.getMockFileSys("MOCKENV");
		// Initializes the class to be tested
		cat = new Cat();
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
	 * Test : User provides no input
	 */
	@Test
	public void testNoArgs() {
		// Declares and initializes an empty array
		String[] emptyArr = {};
		// Expected return from Cat
		expected = "Error : No parameters provided";
		// Actual return from Cat after the operation has been run
		actual = cat.run(fs, emptyArr, "cat", false);
		// Checks if the values are equal or not
		assertEquals(expected, actual);
	}

	/**
	 * Test : File not Found , Absolute Path
	 */
	@Test
	public void testFileNotFoundCase1() {
		// Expected return from Cat
		expected = "Error: File Not Found : /pics/picflex";
		// Actual return from Cat after the operation has been run
		actual = cat.run(fs, "/pics/picflex".split(" "), "cat /pics/picflex", false);
		// Checks if the values are equal or not
		assertEquals(expected, actual);
	}

	/**
	 * Test : File Exists Absolute Path
	 */
	@Test
	public void testAbsolutePath() {
		// Expected return from Cat
		expected = "this is a document";
		// Actual return from Cat after the operation has been run
		actual = cat.run(fs, "/documents/txtone".split(" "), "cat /documents/txtone", false);
		// Checks if the values are equal or not
		assertEquals(expected, actual);
	}

	/**
	 * Test : File not Found , Relative Path
	 */
	@Test
	public void testFileNotFoundCase2() {
		// Expected return from Cat
		expected = "Error: File Not Found : A0";
		// Actual return from Cat after the operation has been run
		actual = cat.run(fs, "A0".split(" "), "cat A0", false);
		// Checks if the values are equal or not
		assertEquals(expected, actual);
	}

	/**
	 * Test : File Exists , Relative Path
	 */
	@Test
	public void testRelativePath() {
		// Expected return from Cat
		expected = "Wow what a project";
		// Actual return from Cat after the operation has been run
		actual = cat.run(fs, "A2".split(" "), "cat A2", false);
		// Checks if the values are equal or not
		assertEquals(expected, actual);
	}

	/**
	 * Test : Multiple Files of different file paths
	 */
	@Test
	public void testMultipleFiles() {
		// Expected return from Cat
		expected = "this is a document 2" + "\n" + "\n" + "\n" + "Wow what a project";
		// Actual return from Cat after the operation has been run
		actual = cat.run(fs, "/documents/txttwo A2".split(" "), "cat /documents/txttwo A2", false);
		// Checks if the values are equal or not
		assertEquals(expected, actual);
	}

	/**
	 * Test : Multiple Files of different file paths but one File Not Found
	 */
	@Test
	public void testFileNotFoundCase3() {
		// Expected return from Cat
		expected = "Error: File Not Found : LOL";
		// Actual return from Cat after the operation has been run
		actual = cat.run(fs, "downloads/homework/HW8 LOL /documents/txttwo".split(" "),
				"cat downloads/homework/HW8 LOL /documents/txttwo", false);
		// Checks if the values are equal or not
		assertEquals(expected, actual);
	}

	/**
	 * Test : User chooses to overwrite contents of a file with the results
	 */
	@Test
	public void testRedirectionOverwrite() {
		// Expected return from Cat
		expected = "2+2=5" + "\n" + "\n" + "\n" + "this is a document 2";
		// Actual return from Cat after the operation has been run
		actual = cat.run(fs, "downloads/homework/HW8 /documents/txttwo > A2".split(" "),
				"cat downloads/homework/HW8 /documents/txttwo > A2", false);
		// Checks if the values are equal or not
		assertEquals(expected, fs.findFile("A2", false).getContent());
	}

	/**
	 * Test : User chooses to append the result to the contents of a file
	 */
	@Test
	public void testRedirectionAppend() {
		// Expected return from Cat
		expected = "2+2=5" + "\n" + "\n" + "\n" + "this is a document 2";
		// Actual return from Cat after the operation has been run
		actual = cat.run(fs, "downloads/homework/HW8 /documents/txttwo > downloads/homework/HW8".split(" "),
				"cat downloads/homework/HW8 /documents/txttwo > downloads/homework/HW8", false);
		// Checks if the values are equal or not
		assertEquals(expected, fs.findFile("downloads/homework/HW8", false).getContent());
	}

	/**
	 * Test : User provides no files for the redirection
	 */
	@Test
	public void testRedirectionErrorCase1() {
		// Expected return from Cat
		expected = "Error : No parameters provided for redirection";
		// Actual return from Cat after the operation has been run
		actual = cat.run(fs, "args >".split(" "), "cat args >", false);
		// Checks if the values are equal or not
		assertEquals(expected, actual);
	}

	/**
	 * Test : User provides multiple files for the redirection
	 */
	@Test
	public void testRedirectionErrorCase2() {
		// Expected return from Cat
		expected = "Error : Multiple Parameters have been provided : [LOL, zip] Only one is required for redirection";
		// Actual return from Cat after the operation has been run
		actual = cat.run(fs, "args > LOL zip".split(" "), "cat args > LOL zip", false);
		// Checks if the values are equal or not
		assertEquals(expected, actual);
	}

	/**
	 * Test : User provides an invalid file and redirects input to file
	 */
	@Test
	public void testRedirectionErrorCase3() {
		// Expected return from Cat
		expected = "Error: File Not Found : LOL";
		// Actual return from Cat after the operation has been run
		actual = cat.run(fs, "downloads/homework/HW8 LOL /documents/txttwo > downloads/homework/HW8".split(" "),
				"cat downloads/homework/HW8 LOL /documents/txttwo > downloads/homework/HW8", false);
		// Checks if the values are equal or not
		assertEquals(expected, actual);
	}

	/**
	 * Test : User chooses to overwrite to a file with invalid name
	 */
	@Test
	public void testRedirectionErrorCase4() {
		// Expected return from Cat
		expected = "Error: Invalid File : Hello$ is not a valid file name";
		// Actual return from Cat after the operation has been run
		actual = cat.run(fs, "downloads/homework/HW8 /documents/txttwo > Hello$".split(" "),
				"cat downloads/homework/HW8 /documents/txttwo > Hello$", false);
		// Checks if the values are equal or not
		assertEquals(expected, actual);
	}
}