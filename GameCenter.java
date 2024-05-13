import java.io.BufferedReader;
import java.io.InputStreamReader;

public class GameCenter{
    public static void main(String args[])throws Exception{
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String input = "";
        do{
            printSpace();
            System.out.println(
                "Enter the number of the game you would like to play:\n" +
                "1. Hangman\n" +
                "2. Tic-Tac-Toe\n" +
                "3. Battleship\n" +
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
                case "3":
                    playBattleship(br);
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

    public static void printSpace(){
        StringBuffer sb = new StringBuffer();
        for(int i = 0; i < 50; i++){
            sb.append("\n");
        }
        System.out.println(sb);
    }
}