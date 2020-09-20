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
import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Before;
import org.junit.After;
import org.junit.Test;

import commands.Mkdir;

import java.lang.reflect.Field;

/**
 * Class MkdirTest runs all the different test cases for Mkdir
 */
public class MkdirTest {

	/**
	 * Declare instance of FileSystem so we can access the filesystem
	 */
	private static MockFileSystem fs;

	/**
	 * Declare instance of Mkdir to be tested
	 */
	private static Mkdir mkdir;

	/**
	 * Declare two different instance of a String objects called expected and actual
	 */
	private String expected, actual;

	/**
	 * Declares an instance of ArrayList containing String objects called
	 * listOfNodes
	 */
	private static ArrayList<String> listofNodes;

	/**
	 * Sets up the test environment and initializes the instance variables
	 * 
	 * @throws Exception
	 */
	@Before
	public void setUp() throws Exception {
		// Gets a specific preset FileSystem
		fs = MockFileSystem.getMockFileSys("MOCKENV");
		// Initializes a test Object
		mkdir = new Mkdir();

		// Initializes the Arraylist
		listofNodes = new ArrayList<String>();
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
	 * Returns a list of node names contained by the directory node
	 * 
	 * @param path the path to get to the node to be looked into
	 */
	private void collectNodeNames(String path) {
		// Clears the ArrayList
		listofNodes.clear();
		// If the path given is not root
		if (!path.equals("/")) {
			// Find the Node that is that is to be looked into
			for (int i = 0; i < fs.getCurrent().getList().size(); i++) {
				// If this is the node that is to be looked into
				if (fs.getCurrent().getList().get(i).getName().equals(path)) {
					// Sets teh current node to be the that one
					fs.setCurrent(fs.getCurrent().getList().get(i));
					// Exits the loop
					break;
				}
			}
		}
		// Collects the names of the Node contained in the Node of intrest
		for (int i = 0; i < fs.getCurrent().getList().size(); i++)
			listofNodes.add(fs.getCurrent().getList().get(i).getName());
	}

	/**
	 * Test : User provides no input
	 */
	@Test
	public void testNoPath() {
		// Declares an empty array
		String[] testNoArg = {};
		// Expected return from mkdir
		expected = "Error: Invalid Argument : Expected at least 1 argument";
		// Actual return from mkdir
		actual = mkdir.run(fs, testNoArg, "mkdir", false);
		// Collects the node that are in the given working directory
		collectNodeNames("/");
		// Checks if the values are equal or not
		assertTrue(actual.equals(expected)
				&& listofNodes.containsAll(Arrays.asList("users documents downloads A2 desktop".split(" "))));
	}

	/**
	 * Test : User provides an invalid directory name
	 */
	@Test
	public void testInvalidName() {
		// Expected return from mkdir
		expected = "Error: Invalid Directory : ... is not a valid directory name";
		// Actual return from mkdir
		actual = mkdir.run(fs, "...".split(" "), "mkdir ...", false);
		// Collects the node that are in the given working directory
		collectNodeNames("/");
		// Checks if the values are equal or not
		assertTrue(actual.equals(expected)
				&& listofNodes.containsAll(Arrays.asList("users documents downloads A2 desktop".split(" "))));
	}

	/**
	 * Test : User provides an invalid path for making a directory
	 */
	@Test
	public void testInvalidPath() {
		// Expected return from mkdir
		expected = "Error: Invalid Directory : /hello is not a valid directory";
		// Actual return from mkdir
		actual = mkdir.run(fs, "/hello/hi".split(" "), "mkdir /hello/hi", false);
		// Collects the node that are in the given working directory
		collectNodeNames("/");
		// Checks if the values are equal or not
		assertTrue(actual.equals(expected)
				&& listofNodes.containsAll(Arrays.asList("users documents downloads A2 desktop".split(" "))));
	}

	/**
	 * Test : User provided a valid absolute path
	 */
	@Test
	public void testValidAbsolutePath() {
		// Expected return from mkdir
		expected = null;
		// Actual return from mkdir
		actual = mkdir.run(fs, "/System32".split(" "), "mkdir /System32", false);
		// Collects the node that are in the given working directory
		collectNodeNames("/");
		// Checks if the values are equal or not
		assertTrue(actual == expected
				&& listofNodes.containsAll(Arrays.asList("System32 users documents downloads A2 desktop".split(" "))));
	}

	/**
	 * Test : User provided a valid relative path
	 */
	@Test
	public void testValidRelativePath() {
		// Expected return from mkdir
		expected = null;
		// Actual return from mkdir
		actual = mkdir.run(fs, "System32".split(" "), "mkdir System32", false);
		// Collects the node that are in the given working directory
		collectNodeNames("/");
		// Checks if the values are equal or not
		assertTrue(actual == expected
				&& listofNodes.containsAll(Arrays.asList("System32 users documents downloads A2 desktop".split(" "))));
	}

	/**
	 * Test : User provides a name of directory that already exists
	 */
	@Test
	public void testRepeatedPath() {
		// Expected return from mkdir
		expected = "Invalid Directory: users already exists in /";
		// Actual return from mkdir
		actual = mkdir.run(fs, "/users".split(" "), "mkdir /users", false);
		// Collects the node that are in the given working directory
		collectNodeNames("/");
		// Checks if the values are equal or not
		assertTrue(actual.equals(expected)
				&& listofNodes.containsAll(Arrays.asList("users documents downloads A2 desktop".split(" "))));
	}

	/**
	 * Test : User provides a path that is relative to the current working directory
	 */
	@Test
	public void testRelativeFromDirPath() {
		// Expected return from mkdir
		expected = null;
		// Actual return from mkdir
		actual = mkdir.run(fs, "desktop/project".split(" "), "mkdir desktop/project", false);
		// Collects the node that are in the given working directory
		collectNodeNames("desktop");
		// Checks if the values are equal or not
		assertTrue(listofNodes.equals(Arrays.asList("project".split(" "))) && actual == expected);
	}

	/**
	 * Test : User provides multiple relative paths
	 */
	@Test
	public void testMultipleArgsRelativePaths() {
		// Expected return from mkdir
		expected = null;
		// Actual return from mkdir
		actual = mkdir.run(fs, "Business A6".split(" "), "mkdir Business A6", false);
		// Collects the node that are in the given working directory
		collectNodeNames("/");
		// Checks if the values are equal or not
		assertTrue(listofNodes.containsAll(Arrays.asList("users documents downloads A2 desktop Business A6".split(" ")))
				&& actual == expected);
	}

	/**
	 * Test : User provides multiple absolute paths
	 */
	@Test
	public void testMultipleAbsolutePaths() {
		// Expected return from mkdir
		expected = null;
		// Actual return from mkdir
		actual = mkdir.run(fs, "/A3 /A5".split(" "), "mkdir /A3 /A5", false);
		// Collects the node that are in the given working directory
		collectNodeNames("/");
		// Checks if the values are equal or not
		assertTrue(listofNodes.containsAll(Arrays.asList("users documents downloads A2 desktop A3 A5".split(" ")))
				&& actual == expected);
	}

	/**
	 * Test : User provides multiple directory paths, but one is invalid
	 */
	@Test
	public void testMultipleArgsOneDoesNotExist() {
		// Expected return from mkdir
		expected = "Error: Invalid Directory : /lol is not a valid directory";
		// Actual return from mkdir
		actual = mkdir.run(fs, "/lol/F anotherFile".split(" "), "mkdir /lol/F anotherFile", false);
		// Collects the node that are in the given working directory
		collectNodeNames("/");
		// Checks if the values are equal or not
		assertTrue(listofNodes.containsAll(Arrays.asList("users documents downloads A2 desktop".split(" ")))
				&& actual.equals(expected));
	}

	/**
	 * Test : User uses redirection for a non redirectionable command
	 */
	@Test
	public void testRedirectionError() {
		// Expected return from mkdir
		expected = "Error : Redirection Error : mkdir does not support redirection";
		// Actual return from mkdir
		actual = mkdir.run(fs, "hello > test".split(" "), "mkdir hello > text", false);
		// Collects the node that are in the given working directory
		collectNodeNames("/");
		// Checks if the values are equal or not
		assertTrue(!listofNodes.contains("hello") && actual.equals(expected));
	}

}
