import java.io.BufferedReader;
import java.io.IOException;

public class Hangman implements Game{
    private String hangman;
    private String phrase;
    private String emptyPhrase;
    private String lettersRight;
    private String lettersWrong;
    private boolean gameWon;
    private boolean gameLost;
    private BufferedReader br;

    public Hangman(BufferedReader br){
        hangman = "  __\n |  |\n    |\n    |\n    |\n    |\n____|__";
        phrase = "";
        emptyPhrase = "";
        lettersRight = "";
        lettersWrong = "";
        gameWon = false;
        gameLost = false;
        this.br = br;
    }

    public void startGame()throws IOException{
        boolean isValidInput = false;
        String input = "";
        do{
            GameCenter.printSpace();
            System.out.println("Enter the hangman phrase:");
            input = br.readLine().toUpperCase().trim();
            isValidInput = (input.length() > 0);
        }while(!isValidInput);
        isValidInput = false;
        phrase = input;

        emptyPhrase = phrase.replaceAll("[A-Za-z]","_");
        while(!gameWon && !gameLost){
            do{
                printSpaceHangmanEmptyPhraseRightWrongLetters();
                input = br.readLine().toUpperCase().trim();
                isValidInput = isValidInput(input);
            }while(!isValidInput);
            String letter = input.substring(0,1).toUpperCase();
            isValidInput = false;

            //check for alphabetic imput
            updateEmptyPhrase(letter);

            if(!gameWon){
                printSpaceHangmanEmptyPhraseRightWrongLetters();
                GameCenter.holdDisplay(1);
                GameCenter.printSpace();
                if(!gameWon && !gameLost){
                    System.out.println("Would you like to guess the phrase?\nType \"yes\" or \"no\"");
                    if(br.readLine().toUpperCase().matches("YES")){
                        printSpaceHangmanEmptyPhraseRightWrongLetters();
                        System.out.println("Take a guess:");
                        String userGuess = br.readLine().toUpperCase().trim();
                        if(userGuess.matches(phrase)){
                            gameWon = true;
                        }else{
                            GameCenter.printSpace();
                            System.out.println("\nNot quite!");
                            GameCenter.holdDisplay(1);
                        }
                    }
                }
            }
            gameStatus();
        }
        GameCenter.holdDisplay(3);
    }
    
    private boolean isValidInput(String input){
        return input.matches("[A-Za-z]{1}") && !lettersRight.contains(input) && !lettersWrong.contains(input);
    }

    private void printSpaceHangmanEmptyPhraseRightWrongLetters(){
        GameCenter.printSpace();
        drawHangman(lettersWrong.length());
        printEmptyPhraseLettersRightWrong();
    }
    
    private void drawHangman(int numLettersWrong){
        switch(numLettersWrong){
            case 0:break;
            case 1: hangman = "  __\n |  |\n 0  |\n    |\n    |\n    |\n____|__";break;
            case 2: hangman = "  __\n |  |\n 0  |\n |  |\n    |\n    |\n____|__";break;
            case 3: hangman = "  __\n |  |\n 0  |\n/|  |\n    |\n    |\n____|__";break;
            case 4: hangman = "  __\n |  |\n 0  |\n/|\\ |\n    |\n    |\n____|__";break;
            case 5: hangman = "  __\n |  |\n 0  |\n/|\\ |\n/   |\n    |\n____|__";break;
            case 6: hangman = "  __\n |  |\n 0  |\n/|\\ |\n/ \\ |\n    |\n____|__";break;
        }
        System.out.println(hangman);
    }

    private void printEmptyPhraseLettersRightWrong(){
        System.out.println("\n" + emptyPhrase + "\n");
        System.out.printf("Letters guessed right so far: %s\n", lettersRight);
        System.out.printf("Letters guessed wrong so far: %s\n", lettersWrong);
    }
    
    private void updateEmptyPhrase(String letter){
        int numChanges = 0;
        for(int i = 0; i < phrase.length(); i++){
            if(phrase.substring(i,i+1).equals(letter)){
                emptyPhrase = emptyPhrase.substring(0,i) + letter + emptyPhrase.substring(i+1);
                numChanges++;
            }
        }
        if(numChanges == 0)lettersWrong+=letter;
        else lettersRight+=letter;
        gameWon = emptyPhrase.matches(phrase)? true : false;
        gameLost = lettersWrong.length() == 6? true : false;
    }

    private void gameStatus(){
        String gameEndState = "\n";
        if(gameWon){
            gameEndState += "Congratulations!!! " + phrase + " was the phrase!\n";
        }else if(gameLost){
            gameEndState += "Ooooof! Game over!\n\""+ phrase +"\" was the phrase\n";
        }
        System.out.print(gameEndState);
    }
}