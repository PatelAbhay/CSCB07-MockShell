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

import java.lang.reflect.Field;
import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import commands.Find;
import commands.Cat;

public class FindTest {

	/**
     * Declare instance of MockFileSystem so we can access the preset filesystem
     */
	private MockFileSystem fs;

	/**
     * Declare instance of Find to be tested
     */
	private Find findTest;

	 /**
     * Declare instance of Cat
     */
	private Cat catTest;

	/**
	 * setup for findTest to initialize required instance variables
	 * 
	 * @throws Exception if there are any exceptions to be sent
	 */
	@Before
	public void setup() throws Exception {
		fs = MockFileSystem.getMockFileSys("MOCKENV");
		findTest = new Find();
		catTest = new Cat();
	}

	/**
	 * Destroys mock filesystem after testcases are run
	 * 
	 * @throws Exception if there are any exceptions to be sent
	 */
	@After
	public void teardown() throws Exception {
		// Declares and initializes a Field variable
		// to the fileSys variable in FileSystem
		Field feild = fs.getClass().getDeclaredField("filesys");
		// Allows the value of this variable in FileSystem to be accessed
		feild.setAccessible(true);
		// Changes the value of the variable in FileSystem to null
		feild.set(null, null);
	}

	/**
	 * Test : Finding single dir in filesystem
	 */
	@Test
	public void testSingleDir() {
		String[] args = { "/", "-type", "d", "-name", "\"homework\"" };
		assertEquals("/downloads\n", findTest.run(fs, args, "find / -type d -name \"homework\"", true));
	}

	/**
	 * Test : Finding single file in filesystem
	 */
	@Test
	public void testSingleFileEntireFs() {
		String[] args = { "/", "-type", "f", "-name", "\"txtone\"" };
		assertEquals("/documents\n", findTest.run(fs, args, "find / -type f -name \"txtone\"", true));
	}

	/**
	 * Test : Finding dir from multiple paths in filesystem
	 */
	@Test
	public void testMultipleDirs() {
		String[] args = { "/", "/downloads", "-type", "d", "-name", "\"homework\"" };
		assertEquals("/downloads\n", findTest.run(fs, args, "find / /downloads -type d -name \"homework\"", true));
	}

	/**
	 * Test : Finding dir that doesnt exist
	 */
	@Test
	public void testNoDir() {
		String[] args = { "/", "-type", "d", "-name", "\"test123\"" };
		assertEquals("", findTest.run(fs, args, "find / -type d -name \"test123\"", true));
	}

	/**
	 * Test : Finding file that doesn't exist
	 */
	@Test
	public void testNoFile() {
		String[] args = { "/", "-type", "f", "-name", "\"test123\"" };
		assertEquals("", findTest.run(fs, args, "find / -type f -name \"test123\"", true));
	}

	/**
	 * Test : Finding single dir using multiple paths
	 */
	@Test
	public void testMultiplePathsForDir() {
		String[] args = { "/", "/downloads", "-type", "d", "-name", "\"homework\"" };
		assertEquals("/downloads\n", findTest.run(fs, args, "find / /downloads -type d -name \"homework\"", true));
	}

	/**
	 * Test : Finding single file using multiple paths
	 */
	@Test
	public void testMultiplePathsForFile() {
		String[] args = { "/", "/documents", "-type", "f", "-name", "\"txtone\"" };
		assertEquals("/documents\n", findTest.run(fs, args, "find / /documents -type f -name \"txtone\"", true));
	}

	/**
	 * Test : Redirection output to a file
	 */
	@Test
	public void testRedirection() {
		String[] args = { "/", "-type", "f", "-name", "\"txtone\"" };
		String[] args2 = { "/documents/txtone" };
		String[] args3 = { "/", "-type", "f", "-name", "\"txtone\"", ">", "/documents/txtone" };
		String expected = findTest.run(fs, args, "find / -type f -name \"txtone\"", true);
		findTest.run(fs, args3, "find / -type f -name \"txtone\" > /documents/txtone", true);
		String actual = catTest.run(fs, args2, "cat /documents/txtone", true);
		assertEquals(expected, actual);
	}
}