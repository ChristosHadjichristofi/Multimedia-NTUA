package src;

import java.util.ArrayList;

public class Ship {

    public int type;
    public int size;
    public int shotPoints;
    public int bonusPoints;
    public int status;
    public ArrayList<Pair<Integer,Integer>> positions;

    public Ship(int type, int size, int shotPoints, int bonusPoints){
        
        this.type = type;
        this.size = size;
        this.shotPoints = shotPoints;
        this.bonusPoints = bonusPoints;
        status = 0;
        positions = new ArrayList<Pair<Integer,Integer>>();
    }

    public String checkStatus() {
        if (positions.size() == size){
            return "intact";
        }
        else if (positions.size() == 0){
            return "sank";
        }
        return "hit";
    }

}
