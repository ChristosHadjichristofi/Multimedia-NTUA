package src;

public class Grid {
    
    public static final int GRID_SIZE = 10;
    public int [][] grid;

    public Grid() {
        grid = new int [GRID_SIZE][GRID_SIZE];
        initGrid(); 
    }

    public void initGrid() {
        for (int i = 0; i < GRID_SIZE; i++){
            for (int j = 0 ; j < GRID_SIZE; j++){
                grid[i][j] = 0;
            }
        }
    }

    public void changeGrid(int x, int y, int val) {
        grid[x][y] = val;
    }

    public void printMyGridView(){
        for (int i = 0; i < GRID_SIZE; ++i){
            for (int j = 0; j < GRID_SIZE; ++j){
                System.out.print(grid[i][j] + " ");
            }
               System.out.println("");
        }
    }

    // -1 --> hit sea
    //  0 --> sea
    //  1 --> carrier
    //  2 --> battleship
    //  3 --> cruiser
    //  4 --> submarine
    //  5 --> destroyer
    //  6 --> hit ship
    public void printEnemyGridView(){
    }
}
