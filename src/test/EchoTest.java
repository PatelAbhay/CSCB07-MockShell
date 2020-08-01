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

    private static MockFileSystem fs;
    private static Echo echo;

    private static String expected, actual;

    @Before
    public void setup() throws Exception {
        fs = MockFileSystem.getMockFileSys("MOCKENV");
        echo = new Echo();
    }

    @After
    public void tearDown() throws Exception {
        Field feild = fs.getClass().getDeclaredField("filesys");
        feild.setAccessible(true);
        feild.set(null, null);
    }

    @Test
    public void testANoArgs() {
        expected = "Error : No parameters provided";
        actual = echo.run(fs, "echo ", false);
        assertEquals(expected, actual);
    }

    @Test
    public void testBProperText() {
        expected = "Hello";
        actual = echo.run(fs, "echo \"Hello\"", false);
        assertEquals(expected, actual);
    }

    @Test
    public void testCMalformedTextCase1() {
        expected = "Error : Missing Quotes : \"Hello";
        actual = echo.run(fs, "echo \"Hello", false);
        assertEquals(expected, actual);
    }

    @Test
    public void testDMalformedTextCase2() {
        expected = "Error : Missing Quotes : Hello\"";
        actual = echo.run(fs, "echo Hello\"", false);
        assertEquals(expected, actual);
    }

    @Test
    public void testENoFileCase1() {
        expected = "Error : No parameters provided for redirection";
        actual = echo.run(fs, "echo \"Hello\" >", false);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testFNoFileCase2() {
        expected = "Error : No parameters provided for redirection";
        actual = echo.run(fs, "echo \"Hello\" >>", false);
        assertEquals(expected, actual);
    }

    @Test
    public void testGWriteToRelativeFile() {
        expected = null;
        actual = echo.run(fs, "echo \"Hello\" > file", false);
        assertTrue(expected == actual && "Hello".equals(fs.findFile("file", false).getContent()));
    }

    @Test
    public void testHWriteToAbsoluteFile() {
        expected = null;
        actual = echo.run(fs, "echo \"Hello\" > file", false);
        assertTrue(expected == actual && "Hello".equals(fs.findFile("file", false).getContent()));
    }

    @Test
    public void testIOverwriteRelativeFile() {
        expected = null;
        actual= echo.run(fs, "echo \"okay\" > A2", false);
        assertTrue(actual == expected && "okay".equals(fs.findFile("A2", false).getContent()));
    }

    @Test
    public void testJAppendRelativeFile() {
        expected = null;
        actual = echo.run(fs,"echo \"Bye\" >> A2", false);
        assertTrue(actual == expected && "Wow what a project\nBye".equals(fs.findFile("A2", false).getContent()));
    }

    @Test
    public void testKOverwriteAbsoluteFile() {
        expected= null;
        actual = echo.run(fs, "echo \"KeyWASD\" > /downloads/homework/HW8", false);
        assertTrue(actual == expected && 
                "KeyWASD".equals(fs.findFile("/downloads/homework/HW8", false).getContent()));
    }

    @Test
    public void testLAppendAbsoluteFile() {
        expected = null;
        actual = echo.run(fs, "echo \"QWERTY\" >> /documents/txtone", false);
        assertTrue(actual == expected && 
            "this is a document\nQWERTY".equals(fs.findFile("/documents/txtone", false).getContent()));
    }

    @Test
    public void testMRedirectionErrorCase1(){
        expected = "Error : No parameters provided for redirection";
        actual = echo.run(fs, "echo \"QWERTY\" >>", false);
        assertEquals(expected, actual);
    }

    @Test
    public void testNRedirectionErrorCase2(){
        expected = "Error : Multiple Parameters have been provided : [pks, loops] Only one is required for redirection";
        actual = echo.run(fs,  "echo \"QWERTY\" >> pks loops", false);
        assertEquals(expected, actual);
    }

    @Test
    public void testORedirectionErrorCase3() {
        expected =  "Error: Invalid File : Hello$ is not a valid file name";
        actual = echo.run(fs,"echo \"Hello\" > Hello$", false);
        assertEquals(expected, actual);
    }

}