package src;

public class Grid {
    
    public static final int GRID_SIZE = 10;
    public int [][] grid;

    public Grid() {
        grid = new int [GRID_SIZE][GRID_SIZE];
        initGrid(); 
    }

    public void initGrid() {
        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0 ; j < GRID_SIZE; j++) {
                grid[i][j] = 0;
            }
        }
    }

    public void changeGrid(int x, int y, int val) {
        grid[x][y] = val;
    }

    public void printMyGridView() {
        for (int i = 0; i < GRID_SIZE; ++i) {
            for (int j = 0; j < GRID_SIZE; ++j) {
                System.out.print(grid[i][j] + " ");
            }
               System.out.println("");
        }
    }

    public void printMyGridViewRow(int i) {
        for (int j = 0; j < GRID_SIZE; j++)
            System.out.print(grid[i][j] + " ");
        System.out.print("   |   ");
    }

    public void printEnemyGridViewRow(int i) {
        for (int j = 0; j < GRID_SIZE; j++) {
            if (grid[i][j] == 7) System.out.print("O" + " ");
            else if (grid[i][j] == 0 || grid[i][j] == 1 || grid[i][j] == 2 || grid[i][j] == 3 || grid[i][j] == 4 || grid[i][j] == 5 ) System.out.print("-" + " ");
            else System.out.print("X" + " ");
        }
        System.out.println("");
    }

    // 7 --> hit sea
    //  0 --> sea
    //  1 --> carrier
    //  2 --> battleship
    //  3 --> cruiser
    //  4 --> submarine
    //  5 --> destroyer
    //  6 --> hit ship
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

    public int shipTypeAtPos(int x, int y, Player p) {
        return p.grid.grid[x][y]; 
    }

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
