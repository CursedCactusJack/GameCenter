import java.io.BufferedReader;

public class Battleship{
    private BufferedReader br;
    private PlayerBoard player1;
    private PlayerBoard player2;
    private boolean gameOver;
    private int turn;
    //private final TimeUnit time = TimeUnit.SECONDS;

    public Battleship(BufferedReader br){
        this.br = br;
        gameOver = false;
        turn = 1;
    }

    public void startGame()throws Exception{
        setBoardSize();
        this.player1 = new PlayerBoard("Player One");
        this.player2 = new PlayerBoard("Player Two");
        setupShips(player1);
        setupShips(player2);
        while(!gameOver){
            if(turn%2 == 1){
                play(player1, player2);
            }else{
                play(player2, player1);
            }
        }
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
            }while(!progressFurther);
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
    public void play(PlayerBoard player, PlayerBoard oppBoard) throws Exception{
        String input = "";
        boolean progressFurther = false;
        System.out.println("Coordinates should be in the following format: Letter-Number");
        do{
            System.out.printf("%s, please enter a valid coordinate:\n", player.getName());
            input = br.readLine();
            progressFurther = PlayerBoard.isValidCoord(input);
        }while(!progressFurther);
        
    }
}
