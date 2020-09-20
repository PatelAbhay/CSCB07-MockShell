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

import java.lang.reflect.Field;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import commands.Tree;
import commands.Cat;

public class TreeTest {
	/**
	 * Declare instance of MockFileSystem so we can access the preset filesystem
	 * (empty and full)
	 */
	private MockFileSystem emptyFs;
	private MockFileSystem fullFs;

	/**
	 * Initialize instance of Tree to test it
	 */
	private Tree testTree;

	/**
	 * Initialize instance of Cat to read files
	 */
	private Cat testCat;

	/**
	 * setup for TreeTest to initialize required instance variables
	 * 
	 * @throws Exception if there are any exceptions to be sent
	 */
	@Before
	public void setup() throws Exception {
		emptyFs = MockFileSystem.getMockFileSys("EMPTYSYS");
		fullFs = MockFileSystem.getMockFileSys("MOCKENV");
		testTree = new Tree();
		testCat = new Cat();
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
		Field feildEmpty = emptyFs.getClass().getDeclaredField("filesys");
		Field feildFull = fullFs.getClass().getDeclaredField("filesys");
		// Allows the value of this variable in FileSystem to be accessed
		feildEmpty.setAccessible(true);
		feildFull.setAccessible(true);
		// Changes the value of the variable in FileSystem to null
		feildEmpty.set(null, null);
		feildFull.set(null, null);
	}

	/**
	 * Test : User provides no input in an empty filesystem
	 */
	@Test
	public void testEmptyFsNoArgs() {
		String[] temp = {};
		assertEquals("/\n", testTree.run(emptyFs, temp, "tree", true));

	}

	/**
	 * Test : User provides input to an empty filesystem
	 */
	@Test
	public void testEmptyFsWithArgs() {
		String[] temp = {};
		assertEquals("Error : Multiple Parameters have been provided", testTree.run(emptyFs, temp, "tree test", true));
	}

	/**
	 * Test : User provides no input in a full filesystem
	 */
	@Test
	public void testFullFsNoArgs() {
		String[] temp = {};
		String expected = testTree.run(fullFs, temp, "tree", true);
		assertEquals(expected, testTree.run(fullFs, temp, "tree", true));
	}

	/**
	 * Test : User provides input to a full filesystem
	 */
	@Test
	public void testFullFsWithArgs() {
		String[] temp = { "test" };
		assertEquals("Error : Multiple Parameters have been provided", testTree.run(fullFs, temp, "tree test", true));
	}

	/**
	 * Test : Redirection into a file
	 */
	@Test
	public void testRedirection() {
		String[] temp = { ">", "txttwo" };
		String[] temp2 = { "/documents/txttwo" };
		String[] temp3 = {};
		testTree.run(fullFs, temp, "tree > documents/txttwo", true);
		String expected = testTree.run(fullFs, temp3, "tree", true);
		String actual = testCat.run(fullFs, temp2, "cat /documents/txttwo", true);
		assertEquals(expected, actual);
	}
}
