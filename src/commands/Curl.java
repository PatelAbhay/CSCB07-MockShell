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
package commands;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import java.util.Arrays;

import data.FileSystemI;
import errors.InvalidArgsProvidedException;

/**
 * Class Curl returns a file from the given URL and adds it to the current
 * working directory.
 * 
 * Note : Some parts of this code was from the following website
 * https://docs.oracle.com/javase/tutorial/networking/urls/readingURL.html
 */
public class Curl implements CommandI {

  /**
   * Declare instance variable of RedirectionManager to handle the redirection operations
   */
  private RedirectionManager rManager;

  /**
   * Constructor for class Curl which initalizes instance variables
   */
  public Curl() {
    this.rManager = new RedirectionManager();
  }

  /**
   * This function retrives the file from the URL and adds it to the current
   * directory
   * 
   * @param args      the string array of URL(s)
   * @param fullInput the full line of input that the user gives into JShell
   * @param val       a boolean value
   * @return any error messages if there are any or null
   */
  public String run(FileSystemI filesys,  String[] args,  String fullInput, boolean val) {
    //Seperates the parameters from everything else from the user input
    String[] arguments = rManager.setParams(fullInput);

    // Declare instance of URL to handle the user input
    URL site;

    try {
      rManager.isRedirectionableCommand(fullInput);
      // If the user provides one URL
      if (checkArgs(filesys, arguments, fullInput)) {
        // Initializes a URL object
        site = new URL(arguments[0]);
        // Declares and Initializes a URLConnection object to setup the connection with
        // the file server
        final URLConnection siteConnection = site.openConnection();
        // Collects the name of the file from the end of the URL
        String fileName = site.getFile().trim().substring(site.getFile().trim().lastIndexOf("/"),
            site.getFile().trim().lastIndexOf("."));

        // Declares and Initializes a InputStreamReader to collect the data of the file
        // from the server
        final InputStreamReader reader = new InputStreamReader(siteConnection.getInputStream());
        // Declares and Initializes a BufferedReader to read the collected data
        final BufferedReader info = new BufferedReader(reader);

        // Declares and Initializes some String to hold the the text
        String inputLine, text = "";

        // Reads the content returned from the file server line by line
        while ((inputLine = info.readLine()) != null) {
          // Appends each line of content and continues on until the end
          text += inputLine + "\n";
        }

        // Closes the BufferedReader
        info.close();
        // Closes the InputStreamReader
        reader.close();

        filesys.fileOverwrite(text, fileName);

      }
      //Catches a malformed URL
    } catch (MalformedURLException e) {
      // Returns an error for malformed URL
      return "Parameter given is invalid " + arguments[0];
      // If the user provides a URL with out a file or file is not on the server
    } catch (FileNotFoundException | StringIndexOutOfBoundsException e) {
      // Returns an error for this expection
      return "URL provided does not contain a file " + arguments[0];
      // If a connection could not be made to the server for various reasons
    } catch (IOException e) {
      // Returns the appropriate error
      return "Connection could not be made to " + arguments[0];
    } catch (InvalidArgsProvidedException e) {
      return e.getLocalizedMessage();
    }

    // If all goes well then it returns null
    return null;
  }

	/**
	 * A simple function to check if the arguments supplied are valid, inherited from CommandI
	 * 
	 * @param filesys  Filesystem to be mutated
	 * @param arguments  Arguments containing path names, etc.
	 * @param fullInput  String containing full user input
	 * 
	 * @throws InvalidArgsProvidedException  Throws an error if invalid arguments are supplied
	 * 
	 * @return  A boolean indicating if the args are valid or not
	 */
  @Override
  public boolean checkArgs(FileSystemI fs, String[] arguments, String fullInput) 
    throws InvalidArgsProvidedException {
    if (arguments.length == 0) {
      // Returns an error when no URL is provided
      throw new InvalidArgsProvidedException("Error : No parameters provided");
      // If the user provides more than one URL
    } else if (arguments.length > 1) {
      // Returns an error when more than one URL is provided
      throw new InvalidArgsProvidedException(
          "Error : Multiple Parameters have been provided : " + Arrays.toString(arguments));
    }
    return true;
  }
}
