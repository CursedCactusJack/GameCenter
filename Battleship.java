import java.io.BufferedReader;
import java.util.concurrent.TimeUnit;

public class Battleship{
    private BufferedReader br;
    private PlayerBoard player1;
    private PlayerBoard player2;
    private boolean gameOver;
    private int turn;
    private final TimeUnit time = TimeUnit.SECONDS;

    public Battleship(BufferedReader br){
        this.br = br;
        gameOver = false;
        turn = 0;
    }

    public void startGame()throws Exception{
        setBoardSize();
        this.player1 = new PlayerBoard("Player One");
        this.player2 = new PlayerBoard("Player Two");
        GameHub.printSpace();
        continueScreen(player1);
        setupShips(player1);
        GameHub.printSpace();
        continueScreen(player2);
        setupShips(player2);
        while(!gameOver){
            ++turn;
            if(turn%2 == 1){
                GameHub.printSpace();
                continueScreen(player1);
                play(player1, player2);
            }else{
                GameHub.printSpace();
                continueScreen(player2);
                play(player2, player1);
            }
            gameOver = (player1.getAllShipsSunk() || player2.getAllShipsSunk());
        }
        GameHub.printSpace();
        if(player1.getAllShipsSunk()){
            System.out.println("Player Two won!");
        }else{
            System.out.println("Player One won!");
        }
        time.sleep(3);
    }
    private void setBoardSize()throws Exception{
        String input = "";
        boolean validDimension = false;
        do{
            GameHub.printSpace();
            System.out.println("Enter a value between 6 and 8 for the size of the board:");
            input = br.readLine();
            validDimension = input.matches("[6-8]{1}");
        }while(!validDimension);

        int dimension = Integer.parseInt(input);
        PlayerBoard.setOceanDimension(dimension);
    }
    private void setupShips(PlayerBoard player)throws Exception{
        String input = "";
        boolean progressFurther = false;
        boolean isFirstAttemptAtPlacingShip = true;
        
        for(int i = 0; i < player.getNumShips(); i++){
            do{
                do{
                    GameHub.printSpace();
                    if(!isFirstAttemptAtPlacingShip){
                        printShipPlacementGameNotes();
                    }
                    printShipPlacementUI(player, i);
                    input = br.readLine();
                    progressFurther = isValidCoordOrientation(input);
                    isFirstAttemptAtPlacingShip = false;
                }while(!progressFurther);
                
                progressFurther = false;
                String coord = input.substring(0, input.indexOf(" ")); 
                String orientation = input.substring(input.indexOf(" ") + 1, input.indexOf(" ") + 2);
                
                if(orientation.equals("H") && player.canPlaceShipHorizontally(player.getShipAt(i), coord)){
                    player.placeShipHorizontally(player.getShipAt(i), coord);
                    progressFurther = true;
                }else if(orientation.equals("V") && player.canPlaceShipVertically(player.getShipAt(i), coord)){
                    player.placeShipVertically(player.getShipAt(i), coord);
                    progressFurther = true;
                }
                GameHub.printSpace();
                PlayerBoard.printOcean(player.getOcean());
            }while(!progressFurther);
            isFirstAttemptAtPlacingShip = true;
        }
        time.sleep(1);
    }
    private boolean isValidCoordOrientation(String s){
        if(!s.contains(" ")){
            return false;
        }else{
            String coord = s.substring(0, s.indexOf(" "));
            String orientation = s.substring(s.indexOf(" ") + 1, s.indexOf(" ") + 2);
            boolean isValidCoord = PlayerBoard.isValidCoord(coord);
            boolean containValidOrientation = orientation.matches("[VH]{1}");
            
            return isValidCoord && containValidOrientation;
        }
    }
    private void play(PlayerBoard player, PlayerBoard opponent) throws Exception{
        String input = "";
        boolean progressFurther = false;
        boolean isFirstCoordinate = true;

        do{
            do{
                GameHub.printSpace();
                if(!isFirstCoordinate){
                    printCoordSelectionGameNotes();
                }
                printCoordSelectionUI(player);
                input = br.readLine();
                progressFurther = PlayerBoard.isValidCoord(input);
                isFirstCoordinate = false;
            }while(!progressFurther);
            progressFurther = !player.alreadyUsedCoord(input);
            if(progressFurther){
                boolean hitShip = opponent.hitShip(input);
                player.updateViewOfOpponentsBoard(opponent, input, hitShip);
                player.addCoordsToUsedList(input);
                GameHub.printSpace();
                PlayerBoard.printOcean(player.getViewOfOpponentsOcean());
                if(hitShip){
                    opponent.updateShips(input);
                    opponent.updateGameStatus();
                }
                time.sleep(1);
            }
        }while(!progressFurther);
    } 
    private void continueScreen(PlayerBoard player)throws Exception{
        System.out.printf("%s's turn. Please hit enter to continue:", player.getName());
        br.readLine();
    }
    private void printShipPlacementGameNotes(){
        System.out.println("Game Notes:");
        System.out.println("Coordinates should be in the following format: Letter-Number");
        System.out.println("Enter a space followed by a V to position your ship vertically.");
        System.out.println("Enter a space followed by a H to position your ship horizontally.");
        System.out.println("Ex:   A-1 H");
        System.out.println();
    }
    private void printShipPlacementUI(PlayerBoard player, int i){
        PlayerBoard.printOcean(player.getOcean());
        System.out.printf("The %s is %d units long.\n", player.getShipAt(i).getName(), player.getShipAt(i).getLength());
        System.out.printf("%s, please enter a valid coordinate to place it:\n", player.getName());
    }
    private void printCoordSelectionGameNotes(){
        System.out.println("Game Notes:");
        System.out.println("Coordinates should be in the following format: Letter-Number");
        System.out.println();
    }
    private void printCoordSelectionUI(PlayerBoard player){
        PlayerBoard.printOcean(player.getViewOfOpponentsOcean());
        System.out.printf("%s, please enter a valid coordinate:\n", player.getName());
    }
}
