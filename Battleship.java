import java.io.BufferedReader;
import java.util.HashSet;

public class Battleship{
    private BufferedReader br;
    private PlayerBoard player1;
    private PlayerBoard player2;
    //private boolean p1Won;
    //private boolean p2Won;
    //private boolean p1turn;
    //private final TimeUnit time = TimeUnit.SECONDS;

    public Battleship(BufferedReader br){
        this.br = br;
        //p1Won = false;
        //p2Won = false;
        //p1turn = true;
    }

    public void startGame()throws Exception{
        System.out.println("1. Setting up board:");
        setBoardSize();
        this.player1 = new PlayerBoard();
        this.player2 = new PlayerBoard();
        System.out.println("2. Setting up P1's ships:");
        setupShips(player1);
        System.out.println("3. Setting up P2's ships:");
        setupShips(player2);
    }

    public void setBoardSize()throws Exception{
        String input = "";
        boolean validDimension = false;
        do{
            System.out.println("Enter a value between 6 and 8 for the size of the board:");
            input = br.readLine();
            validDimension = input.matches("[6-8]{1}");
        }while(!validDimension);

        int dimension = Integer.parseInt(input);
        PlayerBoard.setOceanDimension(dimension);
    }
    public void setupShips(PlayerBoard player)throws Exception{
        String input = "";
        boolean progressFurther = false;
        System.out.println("Coordinates should be in the following format: Letter-Number");
        System.out.println("Enter a space followed by a V to position your ship vertically.");
        System.out.println("Enter a space followed by a H to position your ship horizontally.");
        System.out.println("Ex:   A-1 H");
        for(int i = 0; i < 5; i++){
            do{
                do{
                    PlayerBoard.printOcean(player.getOcean());
                    System.out.printf("Currently placing %s. This ship is %d units in length.\n", player.getShipAt(i).getName(), player.getShipAt(i).getLength());
                    System.out.println("Please enter a valid coordinate, followed by a V or an H, to place a ship:");
                    input = br.readLine();
                    progressFurther = isValidCoordOrientation(input);
                }while(!progressFurther);
                
                String coord = input.substring(0, input.indexOf(" ")); 
                String orientation = input.substring(input.indexOf(" ") + 1, input.indexOf(" ") + 2);
                
                if(orientation.equals("H") && player.canPlaceShipHorizontally(player.getShipAt(i), coord)){
                    player.placeShipHorizontally(player.getShipAt(i), coord);
                    progressFurther = true;
                }else if(orientation.equals("V") && player.canPlaceShipVertically(player.getShipAt(i), coord)){
                    player.placeShipVertically(player.getShipAt(i), coord);
                    progressFurther = true;
                }
            }while(!progressFurther);
            
            progressFurther = false;
        }
    }
    public boolean isValidCoordOrientation(String s){
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
}

class PlayerBoard{
    private static int oceanDimension;
    private char[][] ocean;
    private char[][] viewOfOpponentOcean;
    private Ship[] ships;
    private HashSet<String> occupiedSpaces;
    private HashSet<String> usedCoordinates;
    
    public PlayerBoard(){
        ships = new Ship[]{
            new Ship(2, "Destroyer"),
            new Ship(3, "Submarine"),
            new Ship(3, "Cruiser"),
            new Ship(4, "Battleship"),
            new Ship(5, "Carrier")
        };
        setOcean('∽');
        setViewOfOpponentsOcean('∽');
        setOccupiedSpaces(new HashSet<String>());
        setUsedCoordinates(new HashSet<String>());
    }

    //Encapsulating Methods - Getters:
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
        if(i > ships.length){
            return ships[ships.length];
        }else if(i < 0){
            return ships[0];
        }else{
            return ships[i];
        }
    }

    //Encapsulating Methods - Setters:
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
            if(!isValidCoord(c))
                return false;
        }
        return !spacesOccupied(coords);
    }
    public boolean canPlaceShipVertically(Ship s, String coord){
        String[] coords = generateVerticalCoords(s, coord);
        for(String c: coords){
            if(!isValidCoord(c))
                return false;
        }
        return !spacesOccupied(coords);
    }
    public boolean spacesOccupied(String[] coords){
        for(String coord: coords){
            if(occupiedSpaces.contains(coord))
                return true;
        }
        return false;
    }
    public void placeShipHorizontally(Ship s, String coord){
        String[] coords = generateHorizontalCoords(s, coord);
        s.setCoords(coords);
        for(int i = 0; i < coords.length; i++){
            int row = (int)(coords[i].charAt(0)) - 65;
            int column = (int)(coords[i].charAt(2)) - 48;
            ocean[row][column] = 'O';
        }
    }
    public void placeShipVertically(Ship s, String coord){
        String[] coords = generateVerticalCoords(s, coord);
        s.setCoords(coords);
        for(int i = 0; i < coords.length; i++){
            int row = (int)(coords[i].charAt(0)) - 65;
            int column = (int)(coords[i].charAt(2)) - 48;
            ocean[row][column] = 'O';
        }
    }
    public String[] generateHorizontalCoords(Ship s, String coord){
        String[] coords = new String[s.getLength()];
        for(int i = 0; i < coords.length; i++){
            String row = coord.substring(0,1);
            String column =  Character.toString((char)((int)coord.charAt(2) + i));
            coords[i] = row + "-" + column;
        }
        return coords;
    }
    public String[] generateVerticalCoords(Ship s, String coord){
        String[] coords = new String[s.getLength()];
        for(int i = 0; i < coords.length; i++){
            String row = Character.toString((char)((int)coord.charAt(0) + i));
            String column = coord.substring(2,3);
            coords[i] = row + "-" + column;
        }
        return coords;
    }
    public boolean hitShip(String coord){
        return occupiedSpaces.contains(coord);
    }
    public void updateViewOfOpponentsBoard(PlayerBoard opponent, String coordinate){
        char marker = opponent.hitShip(coordinate)? '!':'-';
        int r = Character.getNumericValue(coordinate.charAt(0) - 65);
        int c = Character.getNumericValue(coordinate.charAt(2) - 48);
        
        viewOfOpponentOcean[r][c] = marker;
    }
    public static void printOcean(char[][] ocean){
        for(int i = 0; i < ocean.length; i++){
            for(int j = 0; j < ocean.length; j++){
                System.out.print(ocean[i][j]);
            }
            System.out.println();
        }
    }
}

class Ship {
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
}
