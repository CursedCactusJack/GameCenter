import java.io.BufferedReader;
import java.util.ArrayList;
import java.io.IOException;

public class TicTacToe implements Game{
    private BufferedReader br;
    private String[][] gameBoard = {{"1","2","3"},{"4","5","6"},{"7","8","9"}};
    private String spacesTaken;
    private boolean p1turn;
    private boolean xWon;
    private boolean oWon;

    public TicTacToe(BufferedReader br){
        spacesTaken = "";
        p1turn = false;
        xWon = false;
        oWon = false;
        this.br = br;
    }

    public void startGame(){
        while(!(xWon ^ oWon) && spacesTaken.length() != 9){
            String input = "";
            boolean isValidInput = false;
            boolean isFirstInput = true;

            do{
                printBoard();
                if (!isFirstInput) {
                    printNumSelectionGameNotes();
                }
                System.out.printf("Player %s,\nEnter a square number:\n", p1turn? "\"O\"":"\"X\"");
                try{
                    input = br.readLine().trim();
                }catch(IOException e){
                    System.out.println("There was an error in reading the line.");
                }
                isValidInput = isValidImput(input);
                isFirstInput = false;
            }while(!isValidInput);

            spacesTaken += input;
            replaceOnBoard(Character.getNumericValue(input.charAt(0)));
            isGameWon();
            printBoard();
            p1turn = !p1turn;
        }

        GameCenter.printSpace();
        if(xWon){
            System.out.println("X won!!!");
        }else if(oWon){
            System.out.println("O won!!!");
        }else{
            System.out.println("The cat won!!!");
        }
        GameCenter.holdDisplay(3);
    }

    private void printBoard(){
        GameCenter.printSpace();
        for(String [] row: gameBoard){
            for(int i = 0; i < row.length; i++){
                System.out.print(" " + row[i] + " " + (((i+1)%3 == 0)? "\n":"|"));
            }
        }
    }
    private boolean isValidImput(String imput){
        return imput.matches("[1-9]{1}") && !spacesTaken.contains(imput);
    }
    private void replaceOnBoard(int num){
        String mark = !p1turn? "X" : "O";
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
            if(position.equals("XXX")){
                xWon = true;
                break;
            }else if(position.equals("OOO")){
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
    private void printNumSelectionGameNotes(){
        System.out.println(
            "Game Notes:\n" + 
            "Please enter the number of a square that is not occupied by an \"X\" or an \"O\":"
        );
    }
}