import java.io.BufferedReader;
import java.io.InputStreamReader;

public class GameHub{
    public static void main(String args[])throws Exception{
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String input = "";
        do{
            System.out.println(
                "Enter the number of the game you would like to play:\n" +
                "1. Hangman\n" +
                "2. Tic-Tac-Toe\n" +
                //"3. Battleship\n" +
                "Type \"x\" to exit."
            );

            input = br.readLine().substring(0,1);
            switch(input){
                case "1":
                    playHangman(br);
                    break;
                case "2":
                    playTicTacToe(br);
                    break;
                case "x":
                    break;
                default: 
                    System.out.println("Invalid input. Please try again.");
                    break;
            }
        }while(!input.equalsIgnoreCase("x"));
        br.close();
        System.out.println("Thanks for playing!");
    }

    public static void playHangman(BufferedReader br) throws Exception{
        Hangman gameInstance = new Hangman();
        gameInstance.startGame(br);
        
    }
    public static void playTicTacToe(BufferedReader br) throws Exception{
        TicTacToe gameInstance = new TicTacToe();
        gameInstance.startGame(br);
        
    }
}