import java.io.BufferedReader;
import java.util.HashSet;
import java.util.concurrent.TimeUnit;

public class Battleship {
    private String[][] p1Ocean;
    private String[][] p2Ocean;
    private HashSet<String> p1OccupiedSpaces;
    private HashSet<String> p2OccupiedSpaces;
    private String[][] p1OceanView;
    private String[][] p2OceanView;
    private HashSet<String> p1UsedCoordinates;
    private HashSet<String> p2UsedCoordinates;
    private String[] p1SShip;
    private String[] p1MShip;
    private String[] p1BShip;
    private boolean p1SSunk;
    private boolean p1MSunk;
    private boolean p1BSunk;
    private String[] p2SShip;
    private String[] p2MShip;
    private String[] p2BShip;
    private boolean p2SSunk = false;
    private boolean p2MSunk = false;
    private boolean p2BSunk = false;
    private boolean p1Won;
    private boolean p2Won;
    private int turn;
    private BufferedReader br;
    private TimeUnit time;
    
    public Battleship(BufferedReader br){
        this.br = br;
        p1Ocean = new String[][] {
            {"0,0","0,1","0,2","0,3","0,4"},
            {"1,0","1,1","1,2","1,3","1,4"},
            {"2,0","2,1","2,2","2,3","2,4"},
            {"3,0","3,1","3,2","3,3","3,4"},
            {"4,0","4,1","4,2","4,3","4,4"}};
        p2Ocean = new String[][] {
            {"0,0","0,1","0,2","0,3","0,4"},
            {"1,0","1,1","1,2","1,3","1,4"},
            {"2,0","2,1","2,2","2,3","2,4"},
            {"3,0","3,1","3,2","3,3","3,4"},
            {"4,0","4,1","4,2","4,3","4,4"}};
        p1OccupiedSpaces = new HashSet<String>();
        p2OccupiedSpaces = new HashSet<String>();
        p1OceanView = new String[][] {
            {"0,0","0,1","0,2","0,3","0,4"},
            {"1,0","1,1","1,2","1,3","1,4"},
            {"2,0","2,1","2,2","2,3","2,4"},
            {"3,0","3,1","3,2","3,3","3,4"},
            {"4,0","4,1","4,2","4,3","4,4"}};
        p2OceanView = new String[][] {
            {"0,0","0,1","0,2","0,3","0,4"},
            {"1,0","1,1","1,2","1,3","1,4"},
            {"2,0","2,1","2,2","2,3","2,4"},
            {"3,0","3,1","3,2","3,3","3,4"},
            {"4,0","4,1","4,2","4,3","4,4"}};
        p1UsedCoordinates = new HashSet<String>();
        p2UsedCoordinates = new HashSet<String>();
        p1SShip = new String[] {"",""};
        p1MShip = new String[] {"","",""};
        p1BShip = new String[] {"","","",""};
        p1SSunk = false;
        p1MSunk = false;
        p1BSunk = false;
        p2SShip = new String[] {"",""};
        p2MShip = new String[] {"","",""};
        p2BShip = new String[] {"","","",""};
        p1Won = false;
        p2Won = false;
        turn = 1;
        time = TimeUnit.SECONDS;
    }

    public void startGame() throws Exception {
        setupGame();
        while(!(p1Won ^ p2Won)){
            gameUI();
            p1Won = p2SSunk & p2MSunk & p2BSunk;
            p2Won = p1SSunk & p1MSunk & p1BSunk;
            turn++;
        }
        if(p1Won)
            System.out.println("Player one won!!!");
        else
            System.out.println("Player two won!!!");
    }

    public void printBoard(String[][] playerTargetView){
        for(int i = 0; i < 50; i++)System.out.println();
        int boardSquare = 1;
        for(String [] row: playerTargetView){
            for(String coordinate: row){
                System.out.print(coordinate);
                if(boardSquare%row.length == 0)System.out.println();
                else System.out.print("|");
                boardSquare++;
            }
        }
    }

    public boolean validImput(String imput){
        if(imput.matches("[0-4],[0-4]")){
            if(turn%2 == 1){
                if(!p1UsedCoordinates.contains(imput))return true;
            }else{
                if(!p2UsedCoordinates.contains(imput))return true;
            }
        }
        return false;
    }

    public void setupGame() throws Exception{
        for(int p = 1; p  <= 2; p++){
            String shipCoords = "";
            boolean shipPlacementValid = false;
            for(int shipNum = 2; shipNum <= 4; shipNum++){
                do{
                    if(p == 1)printBoard(p1Ocean);
                    else printBoard(p2Ocean);
                    System.out.printf("Player %d,\nEnter a coordinate to place your %d unit ship\n",p,shipNum);
                    shipCoords = br.readLine();
                    if(p==1){
                        if(shipNum==2)
                            shipPlacementValid = validPlacement(shipCoords,p1SShip);
                        else if(shipNum==3){
                            shipPlacementValid = validPlacement(shipCoords,p1MShip);
                        }else{
                            shipPlacementValid = validPlacement(shipCoords,p1BShip);
                        }
                    }else{
                        if(shipNum==2)
                            shipPlacementValid = validPlacement(shipCoords,p2SShip);
                        else if(shipNum==3){
                            shipPlacementValid = validPlacement(shipCoords,p2MShip);
                        }else{
                            shipPlacementValid = validPlacement(shipCoords,p2BShip);
                        }
                    }
                }while(!shipPlacementValid);
            }
            if(p==1)printBoard(p1Ocean);
            else printBoard(p2Ocean);
            turn++;
            try{time.sleep(1);
            }catch(InterruptedException e){System.out.println("Interrupted lol");}
        }
    }

    public boolean validPlacement(String coordinate, String [] ship){
        if(coordinate.matches("[0-4],[0-4]")){
            int r = Character.getNumericValue(coordinate.charAt(0));
            int c = Character.getNumericValue(coordinate.charAt(2));
            if(turn%2 == 1){
                if(ship.length == 4 && r<=1){
                    for(int i = 0; i < 4; i++){
                        p1OccupiedSpaces.add(p1Ocean[r+i][c]);
                        p1BShip[i] = p1Ocean[r+i][c];
                        p1Ocean[r+i][c] = " B ";
                    }
                    return true;
                }else if(ship.length == 3 && r<=2 && !p1OccupiedSpaces.contains(coordinate)){
                    if(!(p1Ocean[r+1][c].matches(" B ") || p1Ocean[r+2][c].matches(" B "))){
                        for(int i = 0; i < 3; i++){
                            p1OccupiedSpaces.add(p1Ocean[r+i][c]);
                            p1MShip[i] = p1Ocean[r+i][c];
                            p1Ocean[r+i][c] = " M ";
                        }
                        return true;
                    }
                }else if(ship.length == 2 && r<=3 && !p1OccupiedSpaces.contains(coordinate)){
                    if(!(p1Ocean[r+1][c].matches(" B ") || p1Ocean[r+1][c].matches(" M "))){
                        for(int i = 0; i < 2; i++){
                            p1OccupiedSpaces.add(p1Ocean[r+i][c]);
                            p1SShip[i] = p1Ocean[r+i][c];
                            p1Ocean[r+i][c] = " S ";
                        }
                        return true;
                    }
                }
            }else{
                if(ship.length == 4 && r<=1){
                    for(int i = 0; i < 4; i++){
                        p2OccupiedSpaces.add(p2Ocean[r+i][c]);
                        p2BShip[i] = p2Ocean[r+i][c];
                        p2Ocean[r+i][c] = " B ";
                    }
                    return true;
                }else if(ship.length == 3 && r<=2 && !p2OccupiedSpaces.contains(coordinate)){
                    if(!(p2Ocean[r+1][c].matches(" B ") || p2Ocean[r+2][c].matches(" B "))){
                        for(int i = 0; i < 3; i++){
                            p2OccupiedSpaces.add(p2Ocean[r+i][c]);
                            p2MShip[i] = p2Ocean[r+i][c];
                            p2Ocean[r+i][c] = " M ";
                        }
                        return true;
                    }
                }else if(ship.length == 2 && r<=3 && !p2OccupiedSpaces.contains(coordinate)){
                    if(!(p2Ocean[r+1][c].matches(" B ") || p2Ocean[r+1][c].matches(" M "))){
                        for(int i = 0; i < 2; i++){
                            p2OccupiedSpaces.add(p2Ocean[r+i][c]);
                            p2SShip[i] = p2Ocean[r+i][c];
                            p2Ocean[r+i][c] = " S ";
                        }
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public void gameUI() throws Exception{
        String coordinate = "";
        boolean valCoord = false;
        if(turn%2 == 1)printBoard(p1OceanView);//modify method to take empty & base printBoard off turn
        else printBoard(p2OceanView);
        do{
            System.out.printf("\nPlayer %s, please enter a valid coordinate:\n", (turn%2 == 1? "one":"two"));

            coordinate = br.readLine();
            valCoord = validImput(coordinate);
        }while(!valCoord);
        if(turn%2 == 1){
            p1UsedCoordinates.add(coordinate);
            updateBoard(coordinate);
            printBoard(p1OceanView);
        }else{
            p2UsedCoordinates.add(coordinate);
            updateBoard(coordinate);
            printBoard(p2OceanView);
        }
        try{
            time.sleep(2);
        }catch(InterruptedException e){
            System.out.println("Interrupted lol");
        }
    }

    public void updateBoard(String coordinate){
        String marker = hitShip(coordinate)? " ! ":" - ";
        int r = Character.getNumericValue(coordinate.charAt(0));
        int c = Character.getNumericValue(coordinate.charAt(2));
        if(turn%2 == 1)
            p1OceanView[r][c] = marker;
        else
            p2OceanView[r][c] = marker;
    }

    public boolean hitShip(String target){
        if(turn%2 == 1){
            if(!p2SSunk){
                for(int i = 0; i < 2; i++)
                    if(p2SShip[i].equals(target)){
                        p2SShip[i] = " ! ";
                        p2SSunk = isShipSunk(p2SShip);
                        return true;
                    }
            }if(!p2MSunk){
                for(int i = 0; i < 3; i++)
                    if(p2MShip[i].equals(target)){
                        p2MShip[i] = " ! ";
                        p2MSunk = isShipSunk(p2MShip);
                        return true;
                    }
            }if(!p2BSunk){
                for(int i = 0; i < 4; i++)
                    if(p2BShip[i].equals(target)){
                        p2BShip[i] = " ! ";
                        p2BSunk = isShipSunk(p2BShip);
                        return true;
                    }
            }
        }else{
            if(!p1SSunk){
                for(int i = 0; i < 2; i++)
                    if(p1SShip[i].equals(target)){
                        p1SShip[i] = " ! ";
                        p1SSunk = isShipSunk(p1SShip);
                        return true;
                    }
            }
            if(!p1MSunk){
                for(int i = 0; i < 3; i++)
                    if(p1MShip[i].equals(target)){
                        p1MShip[i] = " ! ";
                        p1MSunk = isShipSunk(p1MShip);
                        return true;
                    }
            }
            if(!p1BSunk){
                for(int i = 0; i < 4; i++)
                    if(p1BShip[i].equals(target)){
                        p1BShip[i] = " ! ";
                        p1BSunk = isShipSunk(p1BShip);
                        return true;
                    }
            }
        }
        return false;
    }

    public boolean isShipSunk(String[] ship){
        int hits = 0;
        for(String section: ship)if(section.equals(" ! "))hits++;
        return hits == ship.length;
    }

}