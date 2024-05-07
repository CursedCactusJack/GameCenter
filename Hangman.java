import java.io.BufferedReader;
import java.util.concurrent.TimeUnit;

public class Hangman{
     String hangman;
     String phrase;
     String emptyPhrase;
     String lettersRight;
     String lettersWrong;
     boolean gameWon;
     boolean gameLost;

    public Hangman(){
        hangman = "  __\n |  |\n    |\n    |\n    |\n    |\n____|__";
        phrase = "";
        emptyPhrase = "";
        lettersRight = "";
        lettersWrong = "";
        gameWon = false;
        gameLost = false;
    }

    public void startGame(BufferedReader br) throws Exception{
        drawSpace();
        System.out.println("Enter the hangman phrase:");
        //make formatPhrases()
        phrase = br.readLine().toUpperCase().trim();
        emptyPhrase = phrase.replaceAll("[A-Za-z]","_");
        
        while(!gameWon && !gameLost){
            printSpaceHangmanEmptyPhraseRightWrongLetters();
            String letter = br.readLine().substring(0,1).toUpperCase();
            //check for alphabetic imput
            updateEmptyPhrase(letter);

            if(!gameWon){
                printSpaceHangmanEmptyPhraseRightWrongLetters();
                TimeUnit time = TimeUnit.SECONDS;
                try{
                    time.sleep(1);
                }catch(InterruptedException e){
                    System.out.println("Interrupted lol");
                }
                drawSpace();
                if(!gameWon && !gameLost){
                    System.out.println("Would you like to guess the phrase?\nType \"yes\" or \"no\"");
                    if(br.readLine().toUpperCase().matches("YES")){
                        printSpaceHangmanEmptyPhraseRightWrongLetters();
                        System.out.println("Take a guess:");
                        String userGuess = br.readLine().toUpperCase().trim();
                        if(userGuess.matches(phrase)){
                            gameWon = true;
                        }else{
                            drawSpace();
                            System.out.println("\nNot quite!");
                            try{
                                time.sleep(1);
                            }catch(Exception e){
                                System.out.println("Interrupted lol");
                            }
                        }
                    }
                }
            }
            gameStatus();
        }
    }
    
    private void printSpaceHangmanEmptyPhraseRightWrongLetters(){
        drawSpace();
        drawHangman(lettersWrong.length());
        printEmptyPhraseLettersRightWrong();
    }
    
    private void drawSpace(){
        for(int i = 0; i < 50; i++)System.out.println("");
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