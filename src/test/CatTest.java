package test;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import commands.Mkdir;
import commands.Echo;
import commands.Cat;
import data.FileSystem;

import java.lang.reflect.Field;

/**
 * Class CatTest runs all the different test cases for Cat
 */
public class CatTest {

    /**
    * Declare instance of FileSystem so we can access the filesystem
    */
    private static FileSystem fs;
    /**
    * Declare instance of Mkdir to make new directories
    */
    private static Mkdir mkdir;
    /**
    * Declare instance of Echo to make new file
    */
    private static Echo echo;
    /**
    * Declare instance of Cat to be tested
    */
    private static Cat cat;

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
        //Get the current FileSystem
        fs = FileSystem.getFileSys();
        // Initializes a Mkdir Object
        mkdir = new Mkdir();
        //Initializes a Echo Object
        echo = new Echo();
        //Initializes a Cat Object
        cat = new Cat();

        // Sets up the C Folder
        mkdir.MakeDirectory("users".split(" "), fs);
        mkdir.MakeDirectory("pics".split(" "), fs);
        mkdir.MakeDirectory("Sys".split(" "), fs);
        echo.run(fs, "/".split(" "), "echo \"Wow what a project\" > A2", false);

        // Sets up the users Folder
        mkdir.MakeDirectory("/users/desktop".split(" ") ,fs);

        // Sets up the desktop Folder
        echo.run(fs, "/users/desktop".split(" "), "echo \"Hello TA\" > /users/desktop/CSCB07", false);
        echo.run(fs, "/users/desktop".split(" "), "echo \"2+2=5\" > /users/desktop/Hwk", false);

        // Sets up the pics Folder
        echo.run(fs, "pics".split(" "), "echo \"this is a picturefile indeed\" > pics/picfile", false);
        echo.run(fs, "pics".split(" "), "echo \"Hello TA from the pics Folder\" > pics/CSCB07", false);

        // Sets up the Sys Folder
        mkdir.MakeDirectory("Sys/IO".split(" "), fs);
        mkdir.MakeDirectory("Sys/LOL".split(" "), fs);

        // Sets up the IO Folder
        mkdir.MakeDirectory("/Sys/IO/keyboard".split(" "), fs);
        mkdir.MakeDirectory("/Sys/IO/Mouse".split(" "), fs);

        // Sets up the keyboard Folder
        echo.run(fs, "/Sys/IO/keyboard".split(" "), "echo \"QWERTY\" > /Sys/IO/keyboard/keys", false);
        echo.run(fs, "/Sys/IO/keyboard".split(" "), "echo \"RGB == ways more      F    P   S\" > /Sys/IO/keyboard/RGB",false);

        // Sets up the Mouse Folder
        echo.run(fs, "/Sys/IO/Mouse".split(" "), "echo \"Mouse is in Mouse Folder\" > /Sys/IO/Mouse/Presses", false);
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
        Field feild = fs.getClass().getDeclaredField("fileSys");
        //Allows the value of this variable in FileSystem to be accessed
        feild.setAccessible(true);
        //Changes the value of the variable in FileSystem to null
        feild.set(null, null);
    }

    /**
     * Test A : User provides no input
    */
    @Test
    public void testANoArgs(){
        //Declares and initializes an empty array
        String[] emptyArr = {};
        //Expected return from Cat
        expected = "Error : No parameters provided : ";
        //Actual return from Cat after the operation has been run
        actual = cat.run(fs, emptyArr, "cat ", false);
        //Checks if the values are equal or not
        assertEquals(expected, actual);
    }

    /**
     * Test B : File not Found , Absolute Path
    */
    @Test
    public void testBFileNotFoundCase1(){
        //Expected return from Cat
        expected = "Error: File Not Found : /pics/picflex";
        //Actual return from Cat after the operation has been run
        actual = cat.run(fs, "/pics/picflex".split(" "), "cat /pics/picflex", false);
         //Checks if the values are equal or not
        assertEquals(expected, actual);
    }
    /**
     * Test C : File Exists Absolute Path
    */
    @Test
    public void testCAbsolutePath(){
        //Expected return from Cat
        expected = "this is a picturefile indeed";
        //Actual return from Cat after the operation has been run
        actual = cat.run(fs, "/pics/picfile".split(" "), "cat /pics/picfile", false);
        //Checks if the values are equal or not
        assertEquals(expected, actual);
    }

    /**
     * Test D : File not Found , Relative Path
    */
    @Test
    public void testDFileNotFoundCase2(){
        //Expected return from Cat
        expected = "Error: File Not Found : A0";
        //Actual return from Cat after the operation has been run
        actual = cat.run(fs, "A0".split(" "), "cat A0", false);
        //Checks if the values are equal or not
        assertEquals(expected, actual);
    }
    /**
     * Test E : File Exists , Relative Path
    */
    @Test
    public void testERelativePath(){
        //Expected return from Cat
        expected = "Wow what a project";
        //Actual return from Cat after the operation has been run
        actual = cat.run(fs, "A2".split(" "), "cat A2", false);
        //Checks if the values are equal or not
        assertEquals(expected, actual);
    }

    /**
     * Test F : Multiple Files of different file paths
    */
    @Test
    public void testFMultipleFiles(){
        //Expected return from Cat    
        expected = "Hello TA" + "\n" + "\n" + "\n" + "Wow what a project";
        //Actual return from Cat after the operation has been run
        actual = cat.run(fs, "/users/desktop/CSCB07 A2".split(" "), "cat /users/desktop/CSCB07 A2", false);
        //Checks if the values are equal or not
        assertEquals(expected, actual);
    }

    /**
     * Test G : Multiple Files of different file paths but one File Not Found
    */
    @Test
    public void testGFileNotFoundCase3(){
        //Expected return from Cat
        expected =  "2+2=5" + "\n" + "\n" + "\n" + "Error: File Not Found : /Sys/LOL"
                + "\n" + "\n" + "\n" + "RGB == ways more      F    P   S";
        //Actual return from Cat after the operation has been run
        actual = cat.run(fs, "/users/desktop/Hwk /Sys/LOL /Sys/IO/keyboard/RGB".split(" "), 
                "cat /users/desktop/Hwk /Sys/LOL /Sys/IO/keyboard/RGB", false);
        //Checks if the values are equal or not
        assertEquals(expected, actual);
    }

}