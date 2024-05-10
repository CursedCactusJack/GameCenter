import java.util.HashSet;

public class PlayerBoard{
    private static int oceanDimension;
    private static char oceanMarker = '∽';
    private static char opponentOceanMarker = '∽';
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
    public void setName(String name){
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
    public void addCoordsToOccupiedList(String[] coords){
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
        char marker = hitShip? '!':'-';
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
        System.out.print(" ");
        for(int i = 0; i < oceanDimension; i++){
            System.out.print(i);
        }
        System.out.println();
        for(int i = 0; i < ocean.length; i++){
            System.out.print((char)(65+i));
            for(int j = 0; j < ocean.length; j++){
                System.out.print(ocean[i][j]);
            }
            System.out.println();
        }
    }
    public void updateGameStatus(){
        boolean allShipsSunk = true;
        for(Ship ship: ships){
            allShipsSunk = (allShipsSunk && ship.getIsSunk());
        }
        setAllShipsSunk(allShipsSunk);
    }
}
