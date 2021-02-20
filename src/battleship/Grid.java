package battleship;

/*
    For the grids, there is a list that shows which number represents what:
    0 --> sea
    1 --> carrier
    2 --> battleship
    3 --> cruiser
    4 --> submarine
    5 --> destroyer
    6 --> hit ship
    7 --> hit sea
*/
public class Grid {
    
    public static final int GRID_SIZE = 10;
    public int [][] grid;

    public Grid() {
        grid = new int [GRID_SIZE][GRID_SIZE];
        initGrid(); 
    }

    // method to initialize grid to zero (which is sea)
    public void initGrid() {
        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0 ; j < GRID_SIZE; j++) {
                grid[i][j] = 0;
            }
        }
    }

    // method to change a value at the grid
    public void changeGrid(int x, int y, int val) {
        grid[x][y] = val;
    }

    // method to print either player's or enemy's grid.
    public void printMyGridView() {
        for (int i = 0; i < GRID_SIZE; ++i) {
            for (int j = 0; j < GRID_SIZE; ++j) {
                System.out.print(grid[i][j] + " ");
            }
               System.out.println("");
        }
    }

    // method to print player's/enemy's grid specific row.
    public void printMyGridViewRow(int i) {
        for (int j = 0; j < GRID_SIZE; j++)
            System.out.print(grid[i][j] + " ");
        System.out.print("   |   ");
    }
    
    // method to print opponent's board to see only the tiles that he shot.
    // so if this method is used for player object, it will show the grid of the enemy but
    // only the tiles that player shot will be shown (with O the failed shots, with X the successful shots)
    // same goes for enemy, if this method is called for enemy object.    
    public void printEnemyGridView(){
        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; ++j) {
                if (grid[i][j] == 7) System.out.print("O" + " ");
                else if (grid[i][j] == 0 || grid[i][j] == 1 || grid[i][j] == 2 || grid[i][j] == 3 || grid[i][j] == 4 || grid[i][j] == 5 ) System.out.print("-" + " ");
                else System.out.print("X" + " ");
            }
            System.out.println("");
        }
    }
    
    // does the same thing as printEnemyGridView() but instead it prints a specific row        
    public void printEnemyGridViewRow(int i) {
        for (int j = 0; j < GRID_SIZE; j++) {
            if (grid[i][j] == 7) System.out.print("O" + " ");
            else if (grid[i][j] == 0 || grid[i][j] == 1 || grid[i][j] == 2 || grid[i][j] == 3 || grid[i][j] == 4 || grid[i][j] == 5 ) System.out.print("-" + " ");
            else System.out.print("X" + " ");
        }
        System.out.println("");
    }
    
    // returns the type of ship at position x,y
    public int shipTypeAtPos(int x, int y, Player p) {
        return p.grid.grid[x][y]; 
    }

    // combines printMyGridView and printEnemyGridView, in order to show to the player
    // his grid, but also his opponent's grid (only with the shots player did, everything else is hidden)
	public void printGrids(Player p, Player e) {
        System.out.print("Player's Points: " + String.format("%-" + 6 + "s", p.points) + "|");
        System.out.println("   Enemy's Points: " + e.points);
        System.out.println();
        for (int i = 0; i < GRID_SIZE; i++) {
            p.grid.printMyGridViewRow(i);
            e.grid.printEnemyGridViewRow(i);
        }
        System.out.println();
    }
}
