import java.io.BufferedReader;
import java.util.ArrayList;

public class TicTacToe{

    private String[][] gameBoard = {{"1","2","3"},{"4","5","6"},{"7","8","9"}};
    private String spacesTaken;
    private boolean p1turn;
    private boolean xWon;
    private boolean oWon;
    private BufferedReader br;

    public TicTacToe(BufferedReader br){
        spacesTaken = "";
        p1turn = false;
        xWon = false;
        oWon = false;
        this.br = br;
    }

    public void startGame() throws Exception{
        while(!(xWon ^ oWon) && spacesTaken.length() != 9){
            printBoard();
            
            System.out.printf("Player %s,\nEnter a square number:\n", p1turn? "\"o\"":"\"x\"");
            String imput = br.readLine();
            
            while(!validImput(imput)){    
                printBoard();

                System.out.println("\nInvalid imput.\nPlease enter the number of a square\nthat is not occupied by an \"X\" or an \"O\":");
                imput = br.readLine();
            }

            spacesTaken+=imput;
            replaceOnBoard(Character.getNumericValue(imput.charAt(0)));
            isGameWon();
            printBoard();
            p1turn = !p1turn;
        }

        for(int i = 0; i < 50; i++)System.out.println();
        if(xWon){
            System.out.println("x won!!!");
        }else if(oWon){
            System.out.println("o won!!!");
        }else{
            System.out.println("The cat won!!!");
        }
    }

    private void printBoard(){
        GameCenter.printSpace();
        int boardSquare = 1;
        for(String [] row: gameBoard){
            for(String box: row){
                System.out.print(box);
                if(boardSquare%3 == 0)System.out.println();
                else System.out.print("|");
                boardSquare++;
            }
        }
    }

    private boolean validImput(String imput){
        return imput.matches("[1-9]{1}") && !spacesTaken.contains(imput);
    }

    private void replaceOnBoard(int num){
        String mark = !p1turn? "x" : "o";
        switch(num){
            case 1: gameBoard[0][0] = mark; break;
            case 2: gameBoard[0][1] = mark; break;
            case 3: gameBoard[0][2] = mark; break;
            case 4: gameBoard[1][0] = mark; break;
            case 5: gameBoard[1][1] = mark; break;
            case 6: gameBoard[1][2] = mark; break;
            case 7: gameBoard[2][0] = mark; break;
            case 8: gameBoard[2][1] = mark; break;
            case 9: gameBoard[2][2] = mark; break;
        }
    }

    private boolean isGameWon(){
        ArrayList<String> currentPositions = gamePositionArrayList();
        for(String position: currentPositions){
            if(position.equals("xxx")){
                xWon = true;
                break;
            }else if(position.equals("ooo")){
                oWon = true;
                break;
            }
        }
        return xWon ^ oWon;
    }

    private ArrayList<String> gamePositionArrayList(){
        ArrayList<String> currentPositions = new ArrayList<String>();
        for(int i = 0; i < 3; i ++){
            String position1 = "";
            String position2 = "";
            for(int j = 0; j < 3; j++){
                position1+=gameBoard[i][j];
                position2+=gameBoard[j][i];
            }
            currentPositions.add(position1);
            currentPositions.add(position2);
        }
        String diagonalNegSlope = gameBoard[0][0] + gameBoard[1][1] +  gameBoard[2][2];
        String diagonalPosSlope = gameBoard[2][0] + gameBoard[1][1] +  gameBoard[0][2];
        currentPositions.add(diagonalNegSlope);
        currentPositions.add(diagonalPosSlope);

        return currentPositions;
    }
}