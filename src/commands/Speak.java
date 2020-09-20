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

import java.util.Arrays;
import com.sun.speech.freetts.Voice;
import com.sun.speech.freetts.VoiceManager;

import data.FileSystemI;
import errors.InvalidRedirectionError;
import errors.MalformedInputException;
import errors.MissingQuotesException;

/**
 * Class TextSpeech is responsible for converting given text to audio Note :
 * Parts of this class was built from
 * https://www.youtube.com/watch?v=d50sZteNC1E
 */

public class Speak implements CommandI {

	/**
	 * Declare instance of Voice so we can convert the text to audio
	 */
	private Voice voice;

	/**
	 * Declare instance of VoiceManager to choose a certain Voice Object
	 */
	private VoiceManager voiceManager;

	/**
	 * Declare instance of String to store the voice that is to be used
	 */
	private static final String VOICENAME = "kevin16";

	/**
	 * Declare instance of String to store the voice that is to be used
	 */
	private static final String PARENTDIR = "freetts.voices";

	/**
	 * Declare instance of String to store the voice that is to be used
	 */
	private static final String VOICEDIR = "com.sun.speech.freetts.en.us.cmu_us_kal.KevinVoiceDirectory";

	private String text;
	private String userText;
	private boolean speakMode;
	private RedirectionManager rManager;

	/**
	 * Constructor for class TextSpeech which initializes instance variables
	 */
	public Speak() {
		// Set Property to where the voice exists
		System.setProperty(PARENTDIR, VOICEDIR);
		// Creates a VoiceManager Object
		this.voiceManager = VoiceManager.getInstance();
		// Creates a Voice Object
		this.voice = this.voiceManager.getVoice(VOICENAME);
		// Allocates the voice
		this.voice.allocate();
		// Initializes the text
		this.text = "";
		this.rManager = new RedirectionManager();

	}

	/**
	 * This function checks if the text given is valid and then converts it audio
	 * 
	 * @param args      the string array of arguments
	 * @param fullInput the full line of input that the user gives into JShell
	 * @param val       tells if the it should enter speakMode or not
	 * @return any error messages if there are any or null
	 */
	public String run(FileSystemI filesys, String[] args, String actualInput, boolean val) {
		this.speakMode = val;

		try {
			rManager.isRedirectionableCommand(actualInput);
		} catch (InvalidRedirectionError e) {
			return e.getLocalizedMessage();
		}

		// Converts a String array containing user words to a single String sentence
		text = Arrays.toString(args);
		text = text.substring(1, text.length() - 1).replace(",", "").trim();

		// If the user enters nothing or just QUIT
		if (text.length() == 0) {
			// The method returns null
			return null;
		}

		// Stores the exact user input
		userText = text;

		// If the text ends with the special keyword QUIR
		if (text.endsWith("QUIT")) {
			// Stores a new copy of the user text except no QUIT
			text = text.substring(0, text.indexOf("QUIT")).trim();
		}

		try {
			// Converts the voice to audio
			if (checkArgs(filesys, args, text))
				voice.speak(text);
		} catch (MissingQuotesException | MalformedInputException e) {
			return e.getLocalizedMessage();
		} catch (Exception e) {
			e.printStackTrace();
		}
		// The method returns null
		return null;
	}

	/**
	 * Overridden method checkArgs which ensures that no arguments have been called
	 * for tree, as it requires no arguments
	 * 
	 * @param fs        the variable we are using to access the filesystem
	 * @param arguments the string array containing all the arguments provided
	 *                  alongside the command
	 * @param fullInput the string that contains the raw input provided to console
	 *                  in jshell
	 * 
	 * @return boolean true if there were no arguments provided
	 * 
	 * @throws InvalidArgsProvidedException the user provided invalid argument
	 */
	public boolean checkArgs(FileSystemI fs, String[] arguments, String text)
			throws MissingQuotesException, MalformedInputException {
		// If we are not in speak mode
		if (!speakMode) {
			// If the user did not use or partially used quote(s) at the beginning
			// and/or end of their input
			if (text.startsWith("\"") && text.endsWith("\"")) {
				// If the use text is not an alphabet
				if (!(text.length() <= 1))
					// Stores a new copy of the user text except no quotes
					text = text.substring(1, text.lastIndexOf("\""));
			} else {
				// Returns an error if the quotes are not used properly
				throw new MissingQuotesException("Error : Missing Quotes : " + userText);
			}

			// If the user used multiple quotations
			if (text.indexOf("\"") != -1) {
				// Returns an error if the quotes are not used properly
				throw new MalformedInputException("Error : Malformed Input :" + userText);
			}
		}
		return true;
	}

}