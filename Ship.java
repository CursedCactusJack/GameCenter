public class Ship {
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