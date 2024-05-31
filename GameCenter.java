/* TODO:
 * Battleship:      Add JavaDocs
 * Hangman:         Add JavaDocs - possible rewrite necessary
 * Tic-Tac-Toe:     Add JavaDocs - review how input is handled; add CPU; possible rewrite necessary
 * Reccomendations: Connect Four, Chess
 */
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.TimeUnit;

/** A class for running a library of games */
public class GameCenter{
    private static final TimeUnit time = TimeUnit.SECONDS;

    /** A function for starting the program
     * @args NOT USED
     */
    public static void main(String args[]){
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
                try{
                    input = br.readLine().trim();
                }catch(IOException e){
                    System.out.println("There was an error in reading the line.");
                }
                progressFurther = isValidInput(input);
                isFirstDisplayForSession = false;
            }while(!progressFurther);
            progressFurther = false;
            isFirstDisplayForSession = true;
            callGame(br, input);
        }while(!input.equalsIgnoreCase("x"));
        try{
            br.close();
        }catch(IOException e){
            System.out.println("There was an error in closing the stream.");
        }
        printSpace();
        System.out.println("Thanks for playing!");
    }

    /** A function for determining if the user made a valid selection at the game selection menu
     * @param input a string
     * @return true iff a valid option was selected at the game selection menu
     */
    private static boolean isValidInput(String input){
        return input.matches("[1-3Xx]{1}");
    }

    /** A function for starting the selected game
     * @param br the source of user input
     * @param selection a string containing the number of the selected game
     */
    private static void callGame(BufferedReader br, String selection){
        Game game;
        switch(selection){
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

    /** A function for printing notes related to the game selection menu */
    private static void printGameSelectionGameNotes(){
        System.out.println("Game Notes:");
        System.out.println("Only a number between 1 and 3 inclusive or an \'x\' can be entered.");
        System.out.println();
    }

    /** A function for clearing the contents of the terminal */
    public static void printSpace(){
        System.out.print("\033\143");
    }
    
    /** A function for holding previously displayed contents in the terminal
     * @param seconds wait time, in seconds
     */
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