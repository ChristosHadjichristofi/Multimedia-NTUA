package src;

import java.util.ArrayList;
import java.util.Random;

public class Enemy extends Player {

    public Enemy(String name) {
        super(name);
    }

    public void move(Player p, Enemy e) {

        final int MAX = 10;
        final int MIN = 0;
        Random x, y;
        Triplet<Integer, Integer, Integer> shootCoords;

        if (availableShoots == 40 || successLastShot == 0) {

            do {
                x = new Random();
                y = new Random();
                shootCoords = new Triplet<Integer, Integer, Integer>(x.nextInt(MAX - MIN) + MIN, y.nextInt(MAX - MIN) + MIN, 0);
            } while (!e.shoot(e, p, shootCoords.getX(), shootCoords.getY()));
            
            if (successLastShot > 0) 
                e.nextShots = nextValidShoot(shootCoords.getX(), shootCoords.getY(), shootCoords.getOrientation());

        }

        else if (successLastShot == 1){
            
            shootCoords = e.nextShots.remove(0);
            while (!e.shoot(e, p, shootCoords.getX(), shootCoords.getY())){

                if (e.nextShots.size() == 0){
                    successLastShot = 0;
                    break;
                }

                shootCoords = e.nextShots.remove(0);

            }

            if (successLastShot > 1)
                e.nextShots = nextValidShoot(shootCoords.getX(), shootCoords.getY(), shootCoords.getOrientation());
        }

        else {
            int tempSuccessShots = successLastShot;

            shootCoords = e.nextShots.remove(0);
            while (!e.shoot(e, p, shootCoords.getX(), shootCoords.getY())){

                if (e.nextShots.size() == 0){
                    successLastShot = 0;
                    break;
                }

                shootCoords = e.nextShots.remove(0);

            }

            if (successLastShot > tempSuccessShots) 
                e.nextShots = nextValidShoot(shootCoords.getX(), shootCoords.getY(), shootCoords.getOrientation());
            else
                successLastShot = 0;    
        }

    }

    private ArrayList<Triplet<Integer, Integer, Integer>> nextValidShoot(Integer x, Integer y, Integer orientation) {
        int cordX, cordY;
     
        ArrayList<Triplet<Integer, Integer, Integer>> shots = new ArrayList<Triplet<Integer, Integer, Integer>>();

        if (orientation == 1 || orientation == 0){
            shots.add(new Triplet<Integer, Integer, Integer>(x, y - 1, 1)); // l
            shots.add(new Triplet<Integer, Integer, Integer>(x, y + 1, 1)); // r
        }
        if (orientation == 2 || orientation == 0){
            shots.add(new Triplet<Integer, Integer, Integer>(x - 1, y, 2)); // u
            shots.add(new Triplet<Integer, Integer, Integer>(x + 1, y, 2)); // d
        }

        for (int i = 0; i < shots.size(); i++) {
            Triplet<Integer, Integer, Integer> coordinates = shots.get(i);
            cordX = coordinates.getX();
            cordY = coordinates.getY();

            if (cordX < 0 || cordX >= 10 || cordY < 0 || cordY >= 10) shots.remove(i);
        }

        return shots;
    }

    
}
