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
import org.junit.Before;
import org.junit.After;
import org.junit.Test;

import commands.Echo;

import java.lang.reflect.Field;
/**
 * Class EchoTest runs all the different cases for Echo
 */
public class EchoTest {

    /**
    * Declare instance of FileSystem so we can access the filesystem
    */
    private static MockFileSystem fs;

    /**
     * Declares instance of Cat to be tested
     */
    private static Echo echo;

    /**
    * Declare two different instance of a String 
    * objects called expected and actual 
    */
    private static String expected, actual;

    /**
     * Sets up the test environment and initializes the instance variables
     * 
     * @throws Exception
    */
    @Before
    public void setup() throws Exception {
        fs = MockFileSystem.getMockFileSys("MOCKENV");
        echo = new Echo();
    }

    /**
     * Destroys the FileSystem after all the testcases have run
     * 
     * @throws Exception
    */
    @After
    public void tearDown() throws Exception {
        //Declares and initializes a Feild variable 
        //to the fileSys variable in FileSystem
        Field feild = fs.getClass().getDeclaredField("filesys");
        //Allows the value of this variable in FileSystem to be accessed
        feild.setAccessible(true);
        //Changes the value of the variable in FileSystem to null
        feild.set(null, null);
    }


    /**
     * Test : User provides no input
    */
    @Test
    public void testNoArgs() {
        //Declares an empty array
        String[] emptyArr = {};
        //Expected return from Echo
        expected = "Error : No parameters provided";
        //Actual return from Echo after the operation has been run
        actual = echo.run(fs,emptyArr, "echo ", false);
        //Checks if the values are equal or not
        assertEquals(expected, actual);
    }

    /**
     * Test : User provides proper input
     */
    @Test
    public void testProperText() {
        //Expected return from Echo
        expected = "Hello";
        //Actual return from Echo after the operation has been run
        actual = echo.run(fs,"echo \"Hello\"".split(" "), "echo \"Hello\"", false);
        //Checks if the values are equal or not
        assertEquals(expected, actual);
    }

    /**
     * Test : User provides malformed input
     */
    @Test
    public void testMalformedTextCase1() {
        //Expected return from Echo
        expected = "Error : Missing Quotes : \"Hello";
        //Actual return from Echo after the operation has been run
        actual = echo.run(fs,"echo \"Hello".split(" "), "echo \"Hello", false);
        //Checks if the values are equal or not
        assertEquals(expected, actual);
    }

    /**
     * Test : User provides malformed input
     */
    @Test
    public void testMalformedTextCase2() {
        //Expected return from Echo
        expected = "Error : Missing Quotes : Hello\"";
        //Actual return from Echo after the operation has been run
        actual = echo.run(fs,"echo Hello\"".split(" "), "echo Hello\"", false);
        //Checks if the values are equal or not
        assertEquals(expected, actual);
    }

    
    /**
     * Test : User chooses to redirect output but no file to overwrite to 
     */
    @Test
    public void testNoFileCase1() {
        //Expected return from Echo
        expected = "Error : No parameters provided for redirection";
        //Actual return from Echo after the operation has been run
        actual = echo.run(fs,"echo \"Hello\" >".split(" "), "echo \"Hello\" >", false);
        //Checks if the values are equal or not
        assertEquals(expected, actual);
    }

    /**
     * Test : User chooses to redirect output but no file to append to 
     */
    @Test
    public void testNoFileCase2() {
        //Expected return from Echo
        expected = "Error : No parameters provided for redirection";
        //Actual return from Echo after the operation has been run
        actual = echo.run(fs,"echo \"Hello\" >>".split(" "), "echo \"Hello\" >>", false);
        //Checks if the values are equal or not
        assertEquals(expected, actual);
    }

    /**
     * Test : User chooses to redirect output to a relative path file
     */
    @Test
    public void testWriteToRelativeFile() {
        //Expected return from Echo
        expected = null;
        //Actual return from Echo after the operation has been run
        actual = echo.run(fs,"echo \"Hello\" > file".split(" "), "echo \"Hello\" > file", false);
        //Checks if the values are equal or not
        assertTrue(expected == actual && "Hello".equals(fs.findFile("file", false).getContent()));
    }

    /**
     * Test : User chooses to redirect output to a absolute path file
     */
    @Test
    public void testWriteToAbsoluteFile() {
        //Expected return from Echo
        expected = null;
        //Actual return from Echo after the operation has been run
        actual = echo.run(fs,"echo \"Hello\" > file".split(" "), "echo \"Hello\" > file", false);
        //Checks if the values are equal or not
        assertTrue(expected == actual && "Hello".equals(fs.findFile("file", false).getContent()));
    }

    /**
     * Test : User chooses to overwrite contents of a file with the contents to relative filepath
     */
    @Test
    public void testOverwriteRelativeFile() {
        //Expected return from Echo
        expected = null;
        //Actual return from Echo after the operation has been run
        actual= echo.run(fs,"echo \"okay\" > A2".split(" "), "echo \"okay\" > A2", false);
        //Checks if the values are equal or not
        assertTrue(actual == expected && "okay".equals(fs.findFile("A2", false).getContent()));
    }

    /**
     * Test : User chooses to append the result to the contents of a file to relative path
     */
    @Test
    public void testAppendRelativeFile() {
        //Expected return from Echo
        expected = null;
        //Actual return from Echo after the operation has been run
        actual = echo.run(fs,"echo \"Bye\" >> A2".split(" "), "echo \"Bye\" >> A2", false);
        //Checks if the values are equal or not
        assertTrue(actual == expected && "Wow what a project\nBye".equals(fs.findFile("A2", false).getContent()));
    }

    /**
     * Test : User chooses to overwrite contents of a file with the contents to absolute filepath
     */
    @Test
    public void testOverwriteAbsoluteFile() {
        //Expected return from Echo
        expected= null;
        //Actual return from Echo after the operation has been run
        actual = echo.run(fs,"echo \"KeyWASD\" > /downloads/homework/HW8".split(" "),
                "echo \"KeyWASD\" > /downloads/homework/HW8", false);        //Checks if the values are equal or not
        assertTrue(actual == expected && 
                "KeyWASD".equals(fs.findFile("/downloads/homework/HW8", false).getContent()));
    }

    /**
     * Test : User chooses to append the result to the contents of a file to absolute path
     */
    @Test
    public void testAppendAbsoluteFile() {
        //Expected return from Echo
        expected = null;
        //Actual return from Echo after the operation has been run
        actual = echo.run(fs,"echo \"QWERTY\" >> /documents/txtone".split(" "),
                "echo \"QWERTY\" >> /documents/txtone", false);        //Checks if the values are equal or not
        assertTrue(actual == expected && 
            "this is a document\nQWERTY".equals(fs.findFile("/documents/txtone", false).getContent()));
    }

    /**
     * Test : User provides no filename for redirection
     */
    @Test
    public void testRedirectionErrorCase1(){
        //Expected return from Echo
        expected = "Error : No parameters provided for redirection";
        //Actual return from Echo after the operation has been run
        actual = echo.run(fs,"echo \"QWERTY\" >>".split(" "),
        "echo \"QWERTY\" >>", false);
        //Checks if the values are equal or not
        assertEquals(expected, actual);
    }

    /**
     * Test : User provides multiple file names fro redirection
     */
    @Test
    public void testRedirectionErrorCase2(){
        //Expected return from Echo
        expected = "Error : Multiple Parameters have been provided : [pks, loops] Only one is required for redirection";
        //Actual return from Echo after the operation has been run
        actual = echo.run(fs,"echo \"QWERTY\" >> pks loops".split(" "),
        "echo \"QWERTY\" >> pks loops", false);
        //Checks if the values are equal or not
        assertEquals(expected, actual);
    }

    /**
     * Test : User provides a file anme for redirection but it an invalid name
     */
    @Test
    public void testRedirectionErrorCase3() {
        //Expected return from Echo
        expected =  "Error: Invalid File : Hello$ is not a valid file name";
        //Actual return from Echo after the operation has been run
        actual = echo.run(fs,"echo \"Hello\" > Hello$".split(" "), "echo \"Hello\" > Hello$", false);
        //Checks if the values are equal or not
        assertEquals(expected, actual);
    }

}