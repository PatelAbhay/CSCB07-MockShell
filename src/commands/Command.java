package commands;

import java.util.Arrays;

public class Command {

  String[] splitInput;
  String command;
  
  boolean speakMode = false;
  
  private TextSpeech tts;
  private PushAndPop pushpop;
  private PushAndPop popStack;
  private Pwd pwd_exe;
  private History history;
  private Cat cat;
  private Echo echo;

  public Command() {
   this.tts = new TextSpeech();
   this.pushpop = new PushAndPop();
   this.popStack = new PushAndPop();
   this.pwd_exe = new Pwd();
   this.cat = new Cat();
   this.history = new History();
   this.echo = new Echo();
  }
  
  public void setCommand(String parsedInput) {
    splitInput = parsedInput.split(" ");
    command = splitInput[0];
    String args[] = Arrays.copyOfRange(splitInput, 1, splitInput.length);
    
    if(speakMode) {
      this.command = "speak";
      args = splitInput;
    }
    
    run(command, args, parsedInput);
  }

  public void run(String command, String[] arguments, String fullInput) {  
    switch(command) {        
      case "speak":
        if(arguments.length == 0) {
          speakMode = true;
        } 
        tts.convertTextToSpeech(arguments, speakMode);        
        if(fullInput.endsWith("QUIT")) { 
          speakMode = false;
        }
        break;
      case "mkdir":
        Mkdir mkdir_exe = new Mkdir(arguments);
        mkdir_exe.MakeDirectory();
        break;
      case "cd":
        Cd cd_exe = new Cd(arguments);
        cd_exe.run();
        break;
      case "ls":
        Ls ls_exe = new Ls(arguments);
        ls_exe.listDirectory();
        break;
      case "pwd":
        pwd_exe.printDirectory();
        break;
      case "pushd":
        pushpop.pushPath(arguments);
        break;
      case "popd":
    	popStack.pop();
    	break;
      case "history":
        history.run(arguments);
        break;
      case "cat":
        cat.readFile(arguments);
        break;
      case "echo":
        echo.compile_arguments(fullInput);
        break;
      case "man":
        Man man_exe = new Man();
    	man_exe.printDocumentation(arguments);
        break;
      case "exit":
        System.exit(0);
       default:
         System.out.println("Invalid Command"); 
    } 
  }
}