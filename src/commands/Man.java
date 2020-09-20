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

import java.util.Hashtable;

import data.FileSystemI;
import errors.InvalidArgsProvidedException;

/**
 * Class man provides documentation for requested command
 */
public class Man implements CommandI {
        /**
         * Declare instance variable of Hashtable
         */
        Hashtable<String, String> manMap;

        /**
        * Declare instance variable of RedirectionManager to handle the redirection operations
        */
        private RedirectionManager redirect;

        /**
        * Declare instance variable of output to store any errors
        */
        String output;

        /**
         * Constructor for Man which initializes instance variables and fills Hashtable
         */
        public Man() {
                // Creates a HashTable Object called manMap
                manMap = new Hashtable<String, String>();
                // Initializes a ErrorHandler Object
                this.redirect = new RedirectionManager();
                // Initializes the Hashtable with the keys and values
                setDictionary();
        }

        /**
         * Provides documentation depending on the command requested
         * 
         * @param filesys   the refrence to the FileSystemI
         *                  object(FileSystem/MockFileSystem)
         * @param args      the string array with all arguements provided by user
         * @param fullInput the string that contains the raw input provided by user in
         *                  JShell
         * @param val       stores a boolean value
         * 
         * @return the documentation of the requested commands or error message
         */
        public String run(FileSystemI filesys, String[] args, String fullInput, boolean val) {
                String[] parsedArgs = redirect.setParams(fullInput);

                try {
                        if (checkArgs(filesys, parsedArgs, fullInput)) {
                                output = redirect.outputResult(filesys, manMap.get(parsedArgs[0]));
                        }
                } catch (InvalidArgsProvidedException e) {
                        return e.getLocalizedMessage();
                }

                return output;
        }

        /**
         * Checks if the arguments provided by the user follows the requirements or else throw an exception
         * 
         * @param filesys  refrence of FileSystemI object (MockFileSystem or FileSystem)
         * @param arguments the list of arguments from user which may contain a redirection error
         * @param fullInput the user input
         * 
         * @throws InvalidArgsProvidedException for invalid arguments
         * 
         * @return true if the parameter meet requirements and false if not
         */
        @Override
        public boolean checkArgs(FileSystemI fs, String[] arguments, String fullInput)
                        throws InvalidArgsProvidedException {
                if (String.join(" ", arguments).contains("Error : No parameters provided")) {
                        throw new InvalidArgsProvidedException(String.join(" ", arguments));
                } else if (String.join(" ", arguments).contains("Error : Multiple Parameters have been provided")) {
                        throw new InvalidArgsProvidedException(String.join(" ", arguments));
                        // If the user provides no command to checked with
                } else if (arguments.length == 0) {
                        throw new InvalidArgsProvidedException(
                                        "Error : No argument(s) provided : Man requires one supported command");
                        // If the user provides more than one command
                } else if (arguments.length > 1) {
                        // Returns an error
                        throw new InvalidArgsProvidedException(
                                        "Error : Multiple Arguments have been provided : Only one supported command is required");
                        // If the command is not supported by Man
                } else if (!manMap.containsKey(arguments[0])) {
                        // Returns an error
                        throw new InvalidArgsProvidedException("Error: Invalid Argument : " + arguments[0]
                                        + " is not a supported command supported one is required");
                }
                return true;
        }

        /**
         * Fills Hashtable with all commands and their respective documentation
         */
        public void setDictionary() {
                // Adds a key named speak and adds its manual
                manMap.put("speak", "Command : speak" + "\n\tConverts text to audiable speech"
                                + "\n\n\tParameter : Text must be inside of quotes(\" \") "
                                + "\n\tto be converted to audio "
                                + "\n\n\tParameter : If there was no text provided then all"
                                + "\n\tinput provided next will be converted to audio, it"
                                + "\n\twill only exit if the special key word QUIT is used"
                                + "\n\n\tSample Output : $ speak \"Hello World\""
                                + "\n\tConverts Hello World to audio and exits" + "\n\n\tSample Output : $ speak"
                                + "\n\t$ What's up" + "\n\tConverts Whats up to audio"
                                + "\n\t$ Hello Professor, How are you QUIT"
                                + "\n\tConverts Hello Professor How are you to audio and exits ");

                // Adds a key named mkdir and adds its manual
                manMap.put("mkdir", "Command : mkdir DIR"
                                + "\n\tCreates one or more directory for a given set of DIR(s)"
                                + "\n\n\tParameter : DIR can be either absolute or relative path"
                                + "\n\tParameter : It can take a list of DIR or at least on DIR"
                                + "\n\tParameter : DIR must contain a unique file name and must not contain the following "
                                + "\n\t\tInvalid Chars : /, ., , !, @, #, $, %, ^, &, *, (, ), {, }, ~, |, <, >, ?"
                                + "\n\tIf any or multiple of these parameter are not meet an error will be outputed respectively"
                                + "\n\n\tSample Use Case : $ mkdir user"
                                + "\n\tCreates a directory called user with in the current working directory -> Relative DIR"
                                + "\n\n\tSample Use Case : $ mkdir /user/user1"
                                + "\n\tCreates a directory called user1 inside of user -> Absolute DIR"
                                + "\n\n\tSample Use Case : $ mkdir /user/user1/desktop resources resources/driver"
                                + "\n\tCreates a desktop folder in user1 and a resources folder in the"
                                + "\n\tcurrent directory and a driver folder in resources -> List of DIR");

                // Adds a key named cd and adds its manual
                manMap.put("cd", "Command: cd DIR" + "\n\tChanges the current working directory to DIR"
                                + "\n\n\tParameter : DIR can be either absolute or relative path"
                                + "\n\tParameter : It has to be only on DIR"
                                + "\n\tParameter : DIR must be a possible path the exist in the File System"
                                + "\n\tParameter : DIR cannot be a file path"
                                + "\n\tIf any or multiple of these parameter are not meet an error will be outputed respectively"
                                + "\n\n\tSpecial Charaters for DIR:"
                                + "\n\tIf DIR is . then it stays at the current working directory"
                                + "\n\tIf DIR is .. then it moves back one directory from the current working directory"
                                + "\n\tIf DIR is \\ then it sets the current working directory to the root of the File System"
                                + "\n\tIf DIR is ../ or ../.. or some combination of those then it will go back n pairs of directories"
                                + "\n\n\tSample Use Case : cd /" + "\n\tSets the current working directory to /"
                                + "\n\tSample Use Case : cd user"
                                + "\n\tSets the current working directory to the /user"
                                + "\n\tSample Use Case : cd user1/desktop"
                                + "\n\tSets the current working directory to the /user/user1/desktop"
                                + "\n\tSample Use Case : cd ../../.."
                                + "\n\tSets the current working directory to the /");

                // Adds a key named ls and adds its manual
                manMap.put("ls", "Command: ls [-R] [PATH]" 
                                + "\n\tLists all the files and directories within the specified directory"
                                + "\n\n\tParameters: By default PATH is the current working directory or it can be a absolute or relative PATH"
                                + "\n\tParameters: It can take a optional argument of -R which prints all directories and files in the PATH"
                                + "\n\tParameters: PATH can be more one arguments"
                                + "\n\n\tREDIRECTION : This command does allow the output"
                                + "\n\tto be redirected to a file instead of the console "
                                + "\n\tif there is any output for the command");

                // Adds a key named pwd and adds its manual
                manMap.put("pwd", "Command: pwd" + "\n\tPrints the current working directory"
                                + "\n\n\tParameters : pwd takes no arguments"
                                + "\n\tIf any or multiple of these parameter are not meet an error will be outputed respectively"
                                + "\n\n\tREDIRECTION : This command does allow the output"
                                + "\n\tto be redirected to a file instead of the console "
                                + "\n\tif there is any output for the command");

                // Adds a key named pushd and adds its manual
                manMap.put("pushd", "Command: pushd PATH" + "\n\tTakes a relative or absolute path as an argument and "
                                + "pushes it onto\n\t" + "the current directory stack and traverses into that path"
                                + "\n\n\tParameter : PATH must be a valid directory path and only path is needed"
                                + "\n\tIf any or multiple of these parameter are not meet an error will be outputed respectively"
                                + "\n\n\tREDIRECTION : This command does not allow the output"
                                + "\n\tto be redirected to a file instead of the console "
                                + "\n\tif there is any output for the command");

                // Adds a key named popd and adds its manual
                manMap.put("popd", "Command: popd" 
                                + "\n\tPops the latest directory on the directory stack into the\n"
                                + "\tcurrent working directory and traverses into that path"
                                + "\n\n\tParameters : popd takes no arguments"
                                + "\n\tIf any or multiple of these parameter are not meet an error will be outputed respectively"
                                + "\n\n\tREDIRECTION : This command does not allow the output"
                                + "\n\tto be redirected to a file instead of the console "
                                + "\n\tif there is any output for the command");

                // Adds a key named history and adds its manual
                manMap.put("history", "Command : history" + "\n\tPrints out recent commands, one command per line. "
                                + "\n\tIf there is a number after the commad, then the output is "
                                + "\n\ttruncated by the specified numeber"
                                + "\n\n\tParameter : None or a whole number (3, 7, 100 and such)"
                                + "\n\n\tREDIRECTION : This command does allow the output"
                                + "\n\tto be redirected to a file instead of the console "
                                + "\n\tif there is any output for the command" + "\n\n\tSample Output : $ history"
                                + "\n\t 1. ls" + "\n\t 2. cd users " + "\n\t 3. pwd"
                                + "\n\t 4. echo \"Hello World\" > C/pics/picturefile"
                                + "\n\t 5. cat C/pics/picturefile.txt" + "\n\t 6. history"
                                + "\n\n\tSample Output : $ history 4"
                                + "\n\t 4. echo \"Hello World\" > C/pics/picturefile"
                                + "\n\t 5. cat C/pics/picturefile.txt" + "\n\t 6. history" + "\n\t 7. history 4");

                // Adds a key named cat and adds its manual
                manMap.put("cat", "Command : cat FILE(s)" + "\n\tDisplays the content of File(s) in series"
                                + "\n\n\tParameter : FILE(s) can be absolute or relative path(s)"
                                + "\n\tParameter : It requires a file or can take a list of "
                                + "\n\tFILE(s) seperated by a space" + "\n\n\tSample Output : $ cat picturefile"
                                + "\n\tThis is a picture file"
                                + "\n\n\tSample Output : $ cat C/pics/picturefile text C/hello"
                                + "\n\tThis is a picture file" + "\n\n\n\tHello World" + "\n\n\n\tHow are you QUIT"
                                + "\n\n\tREDIRECTION : This command does allow the output"
                                + "\n\tto be redirected to a file instead of the console "
                                + "\n\tif there is any output for the command");

                // Adds a key named echo and adds its manual
                manMap.put("echo", "Command: echo" 
                                + "\n\tPrints the text to the JShell" 
                                + "\n\n\tParameters: String (in quotations)"
                                + "\n\n\tREDIRECTION : This command does allow the output"
                                + "\n\tto be redirected to a file instead of the console "
                                + "\n\tif there is any output for the command");

                // Adds a key named man and adds its manual
                manMap.put("man", "Command: man CMD"
                                + "\n\tProvides documentation on all commands within THIS JShell"
                                + "\n\tProvides information such as arguments and function."
                                + "\n\n\tREDIRECTION : This command allows the output to be redirected "
                                + "\n\tto the a file instead of the console"
                                + "\n\n\tParameter: Requires one supported CMD"
                                + "\n\n\tIf any or multiple of these parameter are not meet an \n\terror will be outputed respectively"
                                + "\n\n\tSample Use Case : man man "
                                + "\n\tThis would output the the mannual for man"
                                + "\n\n\tSample Use Case: man man > mannul"
                                + "\n\tThe file named mannul gets overwritten with the mannual for man");

                // Adds a key named exit and adds its manual
                manMap.put("exit",
                                "Command : exit" + "\n\tCloses the current session and leaves the Shell"
                                                + "\n\n\tParameter : None"
                                                + "\n\n\tIf any or multiple of these parameter are not meet an "
                                                + "\n\terror will be outputed respectively"
                                                + "\n\n\tREDIRECTION : This command does not allow the output"
                                                + "\n\tto be redirected to a file instead of the console "
                                                + "\n\tif there is any output for the command");

                // Adds a key named rm and adds its manual
                manMap.put("rm", "Command: rm DIR" + "\n\tRemoves the given DIR from the file system"
                                + "\n\n\tParameter : DIR can be either absolute or relative path"
                                + "\n\tParameter : It has to be only on DIR"
                                + "\n\tParameter : DIR must be a possible path the exist in the file System"
                                + "\n\tParameter : DIR cannot be a file path"
                                + "\n\n\tIf any or multiple of these parameter are not meet an error will be outputed respectively"
                                + "\n\n\tREDIRECTION : This command does not allow the output"
                                + "\n\tto be redirected to the a file instead of the console"
                                + "\n\n\tSample Use Case : rm resources"
                                + "\n\tRemoves the directory resources with in the current working directory"
                                + "\n\n\tSample Use Case : rm /user/user1/desktop"
                                + "\n\tRemoves the directory desktop which is in the user1 directory"
                                + "\n\twhich is in user which is the root of file system");

                // Adds a key named mv and adds its manual
                manMap.put("mv", "Command: mv OLDPATH NEWPATH" 
                                + "\n\tRemoves OLDPATH and moves it to new NEWPATH"
                                + "\n\n\tParameter : OLDPATH and NEWPATH must not be the root"
                                + "\n\tParameter : OLDPATH must be a valid directory"
                                + "\n\n\tREDIRECTION : This command does not allow the output"
                                + "\n\tto be redirected to a file instead of the console "
                                + "\n\tif there is any output for the command");

                // Adds a key named cp and adds its manual
                manMap.put("cp", "Command: cp OLDPATH NEWPATH" 
                                + "\n\tCreates a copy of OLDPATH and moves it to new NEWPATH"
                                + "\n\n\tParameter : OLDPATH and NEWPATH must not be the root"
                                + "\n\tParameter : OLDPATH must be a valid directory"
                                + "\n\n\tREDIRECTION : This command does not allow the output"
                                + "\n\tto be redirected to a file instead of the console "
                                + "\n\tif there is any output for the command");

                // Adds a key named curl and adds its manual
                manMap.put("curl", "Command: curl URL"
                                + "\n\tRetrives the content of the a file at the URL and adds it to the current working directory"
                                + "\n\n\tParameter : URL must be a valid link and should have the file name in the URL"
                                + "\n\tParameter : It has to be only one URL"
                                + "\n\n\tREDIRECTION : This command does not allow the output"
                                + "\n\tto be redirected to a file instead of the console"
                                + "\n\n\tSample Use Case : curl http://www.cs.utoronto.ca/~trebla//CSCB09-2020-Summer/course-info.html"
                                + "\n\tCreates a course-info file in the current directory and stores the content of the file from the URL");

                // Adds a key named save and adds its manual
                manMap.put("save",
                                "Command: save FILE" + "\n\tSave the current session of the JShell and stores it to a "
                                                + "\n\tFILE on the users actual file system"
                                                + "\n\n\tParameter : FILE can be relative or absolute path"
                                                + "\n\tParameter : There can only be one FILE"
                                                + "\n\n\tREDIRECTION : This command does not allow the output"
                                                + "\n\tto be redirected to a file instead of the console "
                                                + "\n\tif there is any output for the command");

                // Adds a key named load and adds its manual
                manMap.put("load",
                                "Command: load FILE" + "\n\tLoads up a previous session of the JShell "
                                                + "\n\tand sets up the JShell to that state"
                                                + "\n\n\tParameter : FILE can be relative or absolute path"
                                                + "\n\tParameter : There can only be one FILE"
                                                + "\n\n\tREDIRECTION : This command does not allow the output"
                                                + "\n\tto be redirected to a file instead of the console "
                                                + "\n\tif there is any output for the command");

                // Adds a key named find and adds its manual
                manMap.put("find", "Command: find PATH -type[f|d] -name EXPRESSION"
                                + "\n\tSearches the give PATH for a file/directory called EXPRESSION"
                                + "\n\n\tParameter : PATH can be either absolute or relative path"
                                + "\n\tParameter : It can be one PATH or multiple PATH"
                                + "\n\tParameter : PATH must be a possible path the exist in the File System"
                                + "\n\tParameter : PATH cannot be a file path"
                                + "\n\tParamerter : If type is f then it is a file"
                                + "\n\tParamerter : If type is d then it is a directory"
                                + "\n\tParameter : The EXPRESSION must be surrounded by quotes"
                                + "\n\n\tREDIRECTION : This command allows for the output"
                                + "\n\tto be redirected to a file instead of the console "
                                + "\n\tif there is any output for the command"
                                + "\n\n\tSample Use Case: find /users/desktop -type f -name \"A2\""
                                + "\n\tSearches the desktop directory for a file named A2"
                                + "\n\n\tSample Use Case: find / -type d -name \"resources\""
                                + "\n\tSearches the root directory for directories named resources");

                // Adds a key named tree and adds its manual
                manMap.put("tree", "Command: tree" + "\n\tDisplays the entire file system as a tree"
                                + "\n\n\tParameter : None"
                                + "\n\tIf any of these parameter are not meet an error will be outputed respectively"
                                + "\n\n\tREDIRECTION : This command allows for the output"
                                + "\n\tto be redirected to a file instead of the console "
                                + "\n\tif there is any output for the command" + "\n\n\tSample Use Case : tree"
                                + "\n\n  \t\t\\"
                                + "\n\n\tSince there is no other directories in the file \n\tsystem it prints out the root directory only"
                                + "\n\n\tSample Use Case : tree" + "\n\n    \t\t\\\n\t\t users\n\t\t resources"
                                + "\n\n\tThe root directory has two sub \n\tdirectories and so it prints them out"
                                + "\n\n\tSample Use Case : tree"
                                + "\n\n   \t\t\\\n\t\t users\n\t\t\t\b\buser1\n\t\t\t   desktop\n\t\t resources"
                                + "\n\n\tIt prints out all the sub directories in \n\tthe file system one directory at a time");
        }
}