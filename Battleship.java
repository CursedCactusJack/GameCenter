import java.io.BufferedReader;
import java.util.HashSet;
import java.util.concurrent.TimeUnit;

public class Battleship {
    private PlayerBoard player1;
    private PlayerBoard player2;
    private boolean p1Won;
    private boolean p2Won;
    private int turn;
    private BufferedReader br;
    private TimeUnit time;
    
    public Battleship(BufferedReader br){
        this.br = br;
        this.player1 = new PlayerBoard();
        this.player2 = new PlayerBoard();
        p1Won = false;
        p2Won = false;
        turn = 1;
        time = TimeUnit.SECONDS;
    }

    public void startGame() throws Exception {
        setupGame();
        while(!(p1Won ^ p2Won)){
            gameUI();
            p1Won = player2.isAllSunk();
            p2Won = player1.isAllSunk();
            turn++;
        }
        if(p1Won){
            System.out.println("Player one won!!!");
        }else{
            System.out.println("Player two won!!!");
        }   
    }
    
    public void printBoard(String[][] board){
        for(int i = 0; i < 50; i++)System.out.println();
        int boardSquare = 1;
        for(String [] row: board){
            for(String coordinate: row){
                System.out.print(coordinate);
                if(boardSquare%row.length == 0)System.out.println();
                else System.out.print("|");
                boardSquare++;
            }
        }
    }
    
    public void setupGame() throws Exception{
        for(int p = 1; p  <= 2; p++){
            String shipCoords = "";
            boolean shipPlacementValid = false;
            for(int shipNum = 2; shipNum <= 4; shipNum++){
                do{
                    if(p == 1){
                        printBoard(player1.getOcean());
                    }else{
                        printBoard(player2.getOcean());
                    }
                    
                    System.out.printf("Player %d,\nEnter a coordinate to place your %d unit ship\n",p,shipNum);
                    shipCoords = br.readLine();

                    if(p==1){
                        if(shipNum==2){
                            shipPlacementValid = player1.validPlacement(shipCoords,player1.getSmallShipCoords());
                        }else if(shipNum==3){
                            shipPlacementValid = player1.validPlacement(shipCoords,player1.getMedShipCoords());
                        }else{
                            shipPlacementValid = player1.validPlacement(shipCoords,player1.getBigShipCoords());
                        }
                    }else{
                        if(shipNum==2)
                            shipPlacementValid = player2.validPlacement(shipCoords,player2.getSmallShipCoords());
                        else if(shipNum==3){
                            shipPlacementValid = player2.validPlacement(shipCoords,player2.getMedShipCoords());
                        }else{
                            shipPlacementValid = player2.validPlacement(shipCoords,player2.getBigShipCoords());
                        }
                    }
                }while(!shipPlacementValid);
            }
            if(p==1){
                printBoard(player1.getOcean());
            }else{
                printBoard(player2.getOcean());
            }
            turn++;
            try{time.sleep(1);
            }catch(InterruptedException e){System.out.println("Interrupted lol");}
        }
    }
    
    public void gameUI() throws Exception{
        String coordinate = "";
        boolean valCoord = false;
        if(turn%2 == 1){
            printBoard(player1.getViewOfOpponentsOcean());
            do{
                System.out.println("\nPlayer one, please enter a valid coordinate:");
    
                coordinate = br.readLine();
                valCoord = player1.isValidCoord(coordinate);
            }while(!valCoord);
            player1.getUsedCoordinates().add(coordinate);
            player1.updateBoard(player2, coordinate);
            printBoard(player1.getViewOfOpponentsOcean());
        }else{
            printBoard(player2.getViewOfOpponentsOcean());
            do{
                System.out.println("\nPlayer two, please enter a valid coordinate:");
    
                coordinate = br.readLine();
                valCoord = player2.isValidCoord(coordinate);
            }while(!valCoord);
            player2.getUsedCoordinates().add(coordinate);
            player2.updateBoard(player1, coordinate);
            printBoard(player2.getViewOfOpponentsOcean());
        }
        
        try{
            time.sleep(2);
        }catch(InterruptedException e){
            System.out.println("Interrupted lol");
        }
    }
 
}

class PlayerBoard {
    private String[][] ocean;
    private String[] smallShipCoords;
    private String[] medShipCoords;
    private String[] bigShipCoords;
    private boolean sunkSmall;
    private boolean sunkMed;
    private boolean sunkBig;
    private HashSet<String> occupiedSpaces;

    private String[][] viewOfOpponentOcean;
    private HashSet<String> usedCoordinates;
    
    
    public PlayerBoard(){
        ocean = new String[][] {
            {"0,0","0,1","0,2","0,3","0,4"},
            {"1,0","1,1","1,2","1,3","1,4"},
            {"2,0","2,1","2,2","2,3","2,4"},
            {"3,0","3,1","3,2","3,3","3,4"},
            {"4,0","4,1","4,2","4,3","4,4"}};
        occupiedSpaces = new HashSet<String>();
        smallShipCoords = new String[] {"",""};
        medShipCoords = new String[] {"","",""};
        bigShipCoords = new String[] {"","","",""};
        sunkSmall = false;
        sunkMed = false;
        sunkBig = false;

        viewOfOpponentOcean = new String[][] {
            {"0,0","0,1","0,2","0,3","0,4"},
            {"1,0","1,1","1,2","1,3","1,4"},
            {"2,0","2,1","2,2","2,3","2,4"},
            {"3,0","3,1","3,2","3,3","3,4"},
            {"4,0","4,1","4,2","4,3","4,4"}};
        usedCoordinates = new HashSet<String>();
    }

    public String[][] getOcean(){
        return ocean;
    }

    public String[][] getViewOfOpponentsOcean(){
        return viewOfOpponentOcean;
    }

    public String[] getSmallShipCoords(){
        return smallShipCoords;
    }

    public String[] getMedShipCoords(){
        return medShipCoords;
    }

    public String[] getBigShipCoords(){
        return bigShipCoords;
    }

    public HashSet<String> getOccupiedSpaces(){
        return occupiedSpaces;
    }

    public HashSet<String> getUsedCoordinates(){
        return usedCoordinates;
    }

    public boolean isValidCoord(String coord){
        return (coord.matches("[0-4],[0-4]") && !usedCoordinates.contains(coord));
    }

    private boolean isShipSunk(String[] ship){
        int hits = 0;
        for(String section: ship)if(section.equals(" ! "))hits++;
        return hits == ship.length;
    }

    public boolean hitShip(String coord){
            if(!sunkSmall){
                for(int i = 0; i < 2; i++)
                    if(smallShipCoords[i].equals(coord)){
                        smallShipCoords[i] = " ! ";
                        sunkSmall = isShipSunk(smallShipCoords);
                        return true;
                    }
            }if(!sunkMed){
                for(int i = 0; i < 3; i++)
                    if(medShipCoords[i].equals(coord)){
                        medShipCoords[i] = " ! ";
                        sunkMed = isShipSunk(medShipCoords);
                        return true;
                    }
            }if(!sunkBig){
                for(int i = 0; i < 4; i++)
                    if(bigShipCoords[i].equals(coord)){
                        bigShipCoords[i] = " ! ";
                        sunkBig = isShipSunk(bigShipCoords);
                        return true;
                    }
            }
        return false;
    }

    public void updateBoard(PlayerBoard opponent, String coordinate){
        String marker = opponent.hitShip(coordinate)? " ! ":" - ";
        int r = Character.getNumericValue(coordinate.charAt(0));
        int c = Character.getNumericValue(coordinate.charAt(2));
        
        viewOfOpponentOcean[r][c] = marker;
    }

    public boolean isAllSunk(){
        return sunkSmall && sunkMed && sunkBig;
    }

    public boolean validPlacement(String coordinate, String [] ship){
        if(coordinate.matches("[0-4],[0-4]")){
            int r = Character.getNumericValue(coordinate.charAt(0));
            int c = Character.getNumericValue(coordinate.charAt(2));
                if(ship.length == 4 && r<=1){
                    for(int i = 0; i < 4; i++){
                        occupiedSpaces.add(ocean[r+i][c]);
                        bigShipCoords[i] = ocean[r+i][c];
                        ocean[r+i][c] = " B ";
                    }
                    return true;
                }else if(ship.length == 3 && r<=2 && !occupiedSpaces.contains(coordinate)){
                    if(!(ocean[r+1][c].matches(" B ") || ocean[r+2][c].matches(" B "))){
                        for(int i = 0; i < 3; i++){
                            occupiedSpaces.add(ocean[r+i][c]);
                            medShipCoords[i] = ocean[r+i][c];
                            ocean[r+i][c] = " M ";
                        }
                        return true;
                    }
                }else if(ship.length == 2 && r<=3 && !occupiedSpaces.contains(coordinate)){
                    if(!(ocean[r+1][c].matches(" B ") || ocean[r+1][c].matches(" M "))){
                        for(int i = 0; i < 2; i++){
                            occupiedSpaces.add(ocean[r+i][c]);
                            smallShipCoords[i] = ocean[r+i][c];
                            ocean[r+i][c] = " S ";
                        }
                        return true;
                    }
                }
        }
        return false;
    }
}
