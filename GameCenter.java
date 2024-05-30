/* TODO:
 * GameCenter:      Add JavaDocs
 * Game:            Add JavaDocs
 * Battleship:      Add JavaDocs
 * Hangman:         Add JavaDocs - possible rewrite necessary
 * Tic-Tac-Toe:     Add JavaDocs - review how input is handled; add CPU; possible rewrite necessary
 */
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.TimeUnit;

public class GameCenter{
    private static final TimeUnit time = TimeUnit.SECONDS;
    public static void main(String args[])throws Exception{
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String input = "";
        boolean progressFurther = false;
        boolean isFirstDisplayForSession = true;
        do{
            do{
                printSpace();
                if(!isFirstDisplayForSession){
                    printGameSelectionGameNotes();
                }
                System.out.println(
                    "Enter the number of the game you would like to play:\n" +
                    "1. Hangman\n" +
                    "2. Tic-Tac-Toe\n" +
                    "3. Battleship\n" +
                    "Type \"x\" to exit."
                );
                
                input = br.readLine();
                progressFurther = isValidInput(input);
                isFirstDisplayForSession = false;
            }while(!progressFurther);
            progressFurther = false;
            isFirstDisplayForSession = true;
            callGame(br, input);
        }while(!input.equalsIgnoreCase("x"));
        br.close();
        printSpace();
        System.out.println("Thanks for playing!");
    }

    private static boolean isValidInput(String input){
        return input.matches("[1-3Xx]{1}");
    }

    private static void callGame(BufferedReader br, String input){
        Game game;
        switch(input){
            case "1":
                game = new Hangman(br);
                game.startGame();
                break;
            case "2":
                game = new TicTacToe(br);
                game.startGame();
                break;
            case "3":
                game = new Battleship(br);
                game.startGame();
                break;
            case "x":
                break;
            default: 
                System.out.println("Invalid input. Please try again.");
                break;
        }
    }

    private static void printGameSelectionGameNotes(){
        System.out.println("Game Notes:");
        System.out.println("Only a number between 1 and 3 inclusive or an \'x\' can be entered.");
        System.out.println();
    }

    public static void printSpace(){
        System.out.print("\033\143");
    }
    
    public static void holdDisplay(int seconds){
        InputStream in = System.in;
        try{
            time.sleep(seconds);
        }catch(InterruptedException e){
            System.out.println("Timer was interrupted.");
        }
        try{
            while(in.available() > 0){
                in.read();
            }
        }catch(IOException e){
            System.out.println("Unable to read stream.");
        }
    }
}