package byow.Core;

import edu.princeton.cs.introcs.StdDraw;

import java.awt.*;

public class FinalScore {

    private static double finalScore = 0;
    private static double maxPossible;
    private static int numCoins = Items.getNumCoins();


    public static void finalScore() {
        int numPoints = numCoins * 10;
        int extraCredit = Encounters.getExtraCredit();
        int numMoves = Player.getNumMoves();

        finalScore = ((Math.sqrt(numPoints)) + (2 / Math.pow(numMoves, 2))) * 1000;
        finalScore += extraCredit;
        System.out.println(finalScore);

        maxPossible = calculateMaxPossible();
        System.out.println(maxPossible);
        double winning = maxPossible - 100; // must be within 100pts of maxPossible to win

        if (finalScore >= winning) {
            youWon();
        } else {
            youLost();
        }

    }


    public static double calculateMaxPossible() { //calculates the maximum number of points you can gain in one world
        int coins = numCoins;
        int moves = coins + 4 * 10;
        int points = coins * 10;
        int extraCredit = 200;

        return (((Math.sqrt(points)) + (2 / Math.pow(moves, 2))) * 1000) + extraCredit;

    }



    public static void youWon() {
        StdDraw.clear(StdDraw.BLACK);
        StdDraw.setXscale(0.0, 80);
        StdDraw.setYscale(0.0, 40);
        Font water = new Font("Arial", Font.BOLD, 30);
        StdDraw.setFont(water);
        StdDraw.setPenColor(StdDraw.BOOK_LIGHT_BLUE);
        StdDraw.text(80 / 2, 40 - 5, "Congrats you won the game!!!");
        StdDraw.text(80 / 2, 40 - 10, "You scored within 100 points of the maximum points you could have received.");
        StdDraw.picture(80 / 2, 40 / 2, "byow/Core/smile.jpg"); //too big
        StdDraw.show();

    }


    public static void youLost() {
        StdDraw.clear(StdDraw.BLACK);
        StdDraw.setXscale(0.0, 80);
        StdDraw.setYscale(0.0, 40);
        Font water = new Font("Arial", Font.BOLD, 30);
        StdDraw.setFont(water);
        StdDraw.setPenColor(StdDraw.WHITE);
        StdDraw.text(80 / 2, 40 - 5, "You lost.");
        StdDraw.text(80 / 2, 40 - 10, "You scored over 100 points below the maximum points you could have received.");
        StdDraw.picture(80 / 2, 40 / 2, "byow/Core/frown.png"); //too big
        StdDraw.show();

    }



}
