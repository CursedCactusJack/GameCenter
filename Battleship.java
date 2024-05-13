import java.io.BufferedReader;
import java.util.HashSet;
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
        GameCenter.printSpace();
        continueScreen(player1);
        setupShips(player1);
        GameCenter.printSpace();
        continueScreen(player2);
        setupShips(player2);
        while(!gameOver){
            ++turn;
            if(turn%2 == 1){
                GameCenter.printSpace();
                continueScreen(player1);
                play(player1, player2);
            }else{
                GameCenter.printSpace();
                continueScreen(player2);
                play(player2, player1);
            }
            gameOver = (player1.getAllShipsSunk() || player2.getAllShipsSunk());
        }
        GameCenter.printSpace();
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
            GameCenter.printSpace();
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
                    GameCenter.printSpace();
                    if(!isFirstAttemptAtPlacingShip){
                        printShipPlacementGameNotes();
                    }
                    printShipPlacementUI(player, i);
                    input = br.readLine().toUpperCase();
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
                GameCenter.printSpace();
                PlayerBoard.printOcean(player.getOcean());
                System.out.println();
                System.out.println();
            }while(!progressFurther);
            isFirstAttemptAtPlacingShip = true;
        }
        time.sleep(2);
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
                GameCenter.printSpace();
                if(!isFirstCoordinate){
                    printCoordSelectionGameNotes();
                }
                printCoordSelectionUI(player);
                input = br.readLine().toUpperCase();
                progressFurther = PlayerBoard.isValidCoord(input);
                isFirstCoordinate = false;
            }while(!progressFurther);
            progressFurther = !player.alreadyUsedCoord(input);
            if(progressFurther){
                boolean hitShip = opponent.hitShip(input);
                player.updateViewOfOpponentsBoard(opponent, input, hitShip);
                player.addCoordsToUsedList(input);
                GameCenter.printSpace();
                PlayerBoard.printOcean(player.getViewOfOpponentsOcean());
                System.out.println();
                if(hitShip){
                    opponent.updateShips(input);
                    opponent.updateGameStatus();
                }
                time.sleep(2);
            }
        }while(!progressFurther);
    } 
    private void continueScreen(PlayerBoard player)throws Exception{
        System.out.printf("%s's turn. Please hit enter to continue:", player.getName());
        br.readLine();
    }
    private void printShipPlacementGameNotes(){
        System.out.println(
            "Game Notes:\n" +
            "Coordinate: made up of a letter, a dash, and a number.\n" +
            "Direction: V or H (for vertical/horizontal placement).\n" + 
            "Example:     A-1 H\n"
        );
    }
    private void printShipPlacementUI(PlayerBoard player, int i){
        PlayerBoard.printOcean(player.getOcean());
        System.out.printf("The %s is %d units long.\n", player.getShipAt(i).getName(), player.getShipAt(i).getLength());
        System.out.printf("%s, please enter a coordinate, followed by a space and direction to place the ship:\n", player.getName());
    }
    private void printCoordSelectionGameNotes(){
        System.out.println(
            "Game Notes:\n" + 
            "Coordinates should be in the following format: Letter-Number\n"
        );
    }
    private void printCoordSelectionUI(PlayerBoard player){
        PlayerBoard.printOcean(player.getViewOfOpponentsOcean());
        System.out.printf("%s, please enter a coordinate:\n", player.getName());
    }
}

class PlayerBoard{
    private static int oceanDimension;
    private static final char oceanMarker = '~';
    private static final char opponentOceanMarker = '#';
    private static final char hitMarker = '!';
    private static final char missMarker = '-';
    private String name;
    private char[][] ocean;
    private char[][] viewOfOpponentOcean;
    private Ship[] ships;
    private HashSet<String> occupiedSpaces;
    private HashSet<String> usedCoordinates;
    private boolean allShipsSunk;
    
    public PlayerBoard(String playerName){
        setName(playerName);
        ships = new Ship[]{
            new Ship(2, "Destroyer"),
            new Ship(3, "Submarine"),
            new Ship(3, "Cruiser"),
            new Ship(4, "Battleship"),
            new Ship(5, "Carrier")
        };
        setOcean(oceanMarker);
        setViewOfOpponentsOcean(opponentOceanMarker);
        setOccupiedSpaces(new HashSet<String>());
        setUsedCoordinates(new HashSet<String>());
        setAllShipsSunk(false);
    }

    //Encapsulating Methods - Getters:
    public String getName(){
        return name;
    }
    public char[][] getOcean(){
        return ocean;
    }
    public char[][] getViewOfOpponentsOcean(){
        return viewOfOpponentOcean;
    }
    public HashSet<String> getOccupiedSpaces(){
        return occupiedSpaces;
    }
    public HashSet<String> getUsedCoordinates(){
        return usedCoordinates;
    }
    public Ship[] getShips(){
        return ships;
    }
    public Ship getShipAt(int i){
        if(i >= ships.length-1){
            return ships[ships.length-1];
        }else if(i <= 0){
            return ships[0];
        }else{
            return ships[i];
        }
    }
    public int getNumShips(){
        return ships.length;
    }
    public boolean getAllShipsSunk(){
        return allShipsSunk;
    }

    //Encapsulating Methods - Setters:
    private void setName(String name){
        if(name.matches("Player One") || name.matches("Player Two")){
            this.name = name;
        }else{
            this.name = "DEFAULT%NAME";
        }
    }
    private void setOcean(char c){
        ocean = fillOcean(c);
    }
    private void setViewOfOpponentsOcean(char c){
        viewOfOpponentOcean = fillOcean(c);
    }
    private void setOccupiedSpaces(HashSet<String> occupiedSpaces){
        this.occupiedSpaces = occupiedSpaces;
    }
    private void setUsedCoordinates(HashSet<String> usedCoordinates){
        this.usedCoordinates = usedCoordinates;
    }
    private void setAllShipsSunk(boolean allShipsSunk){
        this.allShipsSunk = allShipsSunk;
    }

    //Encapsulating Methods - Static:
    public static int getOceanDimension(){
        return oceanDimension;
    }
    public static void setOceanDimension(int dimension){
        if(dimension > 8){
            oceanDimension = 8;
        }else if(dimension < 6){
            oceanDimension = 6;
        }else{
            oceanDimension = dimension;
        }
    }
    public static boolean isValidCoord(String coord){
        char maxLetter = (char)(64 + oceanDimension);
        char maxNumber = (char)(47 + oceanDimension);
        String pattern = "[A-" + Character.toString(maxLetter) + "]{1}-[0-" + Character.toString(maxNumber) + "]{1}";
        return coord.matches(pattern);
    }
    
    //Encapsulating Methods - Utility:
    private static char[][] fillOcean(char c){
        char[][] temp = new char[oceanDimension][oceanDimension];
        for(int i = 0; i < oceanDimension; i++){
            for(int j = 0; j < oceanDimension; j++){
                temp[i][j] = c;
            }
        }
        return temp;
    }
    public boolean canPlaceShipHorizontally(Ship s, String coord){
        String[] coords = generateHorizontalCoords(s, coord);
        for(String c: coords){
            if(!isValidCoord(c) || occupiedSpaces.contains(c))
                return false;
        }
        return true;
    }
    public boolean canPlaceShipVertically(Ship s, String coord){
        String[] coords = generateVerticalCoords(s, coord);
        for(String c: coords){
            if(!isValidCoord(c) || occupiedSpaces.contains(c))
                return false;
        }
        return true;
    }
    public void placeShipHorizontally(Ship s, String coord){
        String[] coords = generateHorizontalCoords(s, coord);
        s.setCoords(coords);
        addCoordsToOccupiedList(coords);
        for(int i = 0; i < coords.length; i++){
            int row = (int)(coords[i].charAt(0)) - 65;
            int column = (int)(coords[i].charAt(2)) - 48;
            ocean[row][column] = 'O';
        }
    }
    public void placeShipVertically(Ship s, String coord){
        String[] coords = generateVerticalCoords(s, coord);
        s.setCoords(coords);
        addCoordsToOccupiedList(coords);
        for(int i = 0; i < coords.length; i++){
            int row = (int)(coords[i].charAt(0)) - 65;
            int column = (int)(coords[i].charAt(2)) - 48;
            ocean[row][column] = 'O';
        }
    }
    private String[] generateHorizontalCoords(Ship s, String coord){
        String[] coords = new String[s.getLength()];
        for(int i = 0; i < coords.length; i++){
            String row = coord.substring(0,1);
            String column =  Character.toString((char)((int)coord.charAt(2) + i));
            coords[i] = row + "-" + column;
        }
        return coords;
    }
    private String[] generateVerticalCoords(Ship s, String coord){
        String[] coords = new String[s.getLength()];
        for(int i = 0; i < coords.length; i++){
            String row = Character.toString((char)((int)coord.charAt(0) + i));
            String column = coord.substring(2,3);
            coords[i] = row + "-" + column;
        }
        return coords;
    }
    private void addCoordsToOccupiedList(String[] coords){
        for(String c: coords){
            occupiedSpaces.add(c);
        }
    }
    public void addCoordsToUsedList(String coord){
        usedCoordinates.add(coord);
    }
    public boolean alreadyUsedCoord(String coord){
        return usedCoordinates.contains(coord);
    }
    public boolean hitShip(String coord){
        return occupiedSpaces.contains(coord);
    }
    public void updateViewOfOpponentsBoard(PlayerBoard opponent, String coord, boolean hitShip){
        char marker = hitShip? hitMarker:missMarker;
        int r = Character.getNumericValue(coord.charAt(0) - 17);
        int c = Character.getNumericValue(coord.charAt(2));
        viewOfOpponentOcean[r][c] = marker;
    }
    public void updateShips(String coord){
        for(Ship s: ships){
            if(!s.getIsSunk() && s.containsCoord(coord)){
                s.updateSectionAsHit(coord);
                s.updateSinkStatus();
            }
        }
    }
    public static void printOcean(char[][] ocean){
        StringBuffer sb = new StringBuffer();
        sb.append(" ");
        for(int i = 0; i < oceanDimension; i++){
            if(i < oceanDimension - 1){
                sb.append("| " + i + " ");
            }else{
                sb.append("| " + i + " |\n");
            }
        }
        for(int i = 0; i < ocean.length; i++){
            sb.append((char)(65+i));
            for(int j = 0; j < ocean.length; j++){
                if(j < oceanDimension - 1){
                    sb.append("| " + ocean[i][j] + " ");
                }else{
                    sb.append("| " + ocean[i][j] + " |\n");
                }
            }
        }
        System.out.println(sb);
    }
    public void updateGameStatus(){
        boolean allShipsSunk = true;
        for(Ship ship: ships){
            allShipsSunk = (allShipsSunk && ship.getIsSunk());
        }
        setAllShipsSunk(allShipsSunk);
    }
}

class Ship {
    private static final String hitMarker = "-";
    private int length;
    private String name;
    private String[] coords;
    private boolean isSunk;

    public Ship(int length, String name){
        setLength(length);
        setName(name);
        setCoords(new String[this.length]);
        setIsSunk(false);
    }

    //Encapsulating Methods - Getters:
    public int getLength(){
        return length;
    }
    public String[] getCoords(){
        return coords;
    }
    public String getName(){
        return name;
    }
    public boolean getIsSunk(){
        return isSunk;
    }

    //Encapsulating Methods - Setters:
    public void setLength(int length){
        if(length > 5){
            this.length = 5;
        }else if(length < 2){
            this.length = 2;
        }else{
            this.length = length;
        }
    }
    public void setCoords(String[] coords){
        if(coords.length == length){
            this.coords = coords;
        }else{
            this.coords = new String[length];
        }
    }
    public void setName(String name){
        switch(name){
            case "Destroyer": 
                this.name = name;
                break;
            case "Submarine":
                this.name = name;
                break;
            case "Cruiser": 
                this.name = name;
                break;
            case "Battleship":
                this.name = name;
                break;
            case "Carrier":
                this.name = name;
                break;
            default:
                this.name = "DEFAULT%NAME";
                break;   
        }
    }
    public void setIsSunk(boolean isSunk){
        this.isSunk = isSunk;
    }

    //Encapsulating Methods - Static:
    //Encapsulating Methods - Utility:
    public boolean containsCoord(String coord){
        return coord.contains(coord);
    }
    public void updateSectionAsHit(String coord){
        for(int i = 0; i < length; i++){
            if(coords[i].matches(coord)){
                coords[i] = hitMarker;
                break;
            }
        }
    }
    public void updateSinkStatus(){
        boolean isSunk = true;
        for(String coord: coords){
            isSunk = (isSunk && coord.matches(hitMarker));
        }
        setIsSunk(isSunk);
    }
}