/* TODO:
 * GameCenter: N/A
 * Battleship:
 *      - tell users when they have sank an opponents ship
 *      - review try & catch vs Exceptions
 * Hangman:
 *      - review try & catch vs Exceptions
 * Tic-Tac-Toe:
 *      - review how input is handled
 *      - review try & catch vs Exceptions
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

    private static void callGame(BufferedReader br, String input)throws Exception{
        switch(input){
            case "1":
                playHangman(br);
                break;
            case "2":
                playTicTacToe(br);
                break;
            case "3":
                playBattleship(br);
                break;
            case "x":
                break;
            default: 
                System.out.println("Invalid input. Please try again.");
                break;
        }
    }
    private static void playHangman(BufferedReader br) throws Exception {
        Hangman gameInstance = new Hangman(br);
        gameInstance.startGame();
    }
    private static void playTicTacToe(BufferedReader br) throws Exception {
        TicTacToe gameInstance = new TicTacToe(br);
        gameInstance.startGame();
    }
    private static void playBattleship(BufferedReader br) throws Exception {
        Battleship gameInstance = new Battleship(br);
        gameInstance.startGame();
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