package byow.Core;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import edu.princeton.cs.introcs.StdDraw;

import java.awt.*;
import java.util.ArrayList;

public class Encounters {

    private static Tileset specialTile;
    private static TERenderer ter = RoomCreation.getRenderer();
    private static int extraCredit = 0;
    private static int numPoints = 0; //just need for the HUD score display
    private static int coins = Items.getNumCoins();
    private static int totalPoints = 0;
    private static ArrayList<Position> teleport = Items.getTeleportPositions();

    public Encounters(Tileset specialTile) {
        this.specialTile = specialTile;
    }


    public static void checkTile(String description, TETile[][] tiles) {
        if (description.equals("water")) {
            encounterWaterDisplay(tiles);
            extraCredit += 100;
            totalPoints += 100;

        } else if (description.equals("mountain")) {
            encounterMountainDisplay(tiles);
            extraCredit += 25;
            totalPoints += 25;

        } else if (description.equals("sand")) {
            encounterSandDisplay(tiles);
            extraCredit += 50;
            totalPoints += 50;

        } else if (description.equals("coin")) {
            numPoints += 20;
            totalPoints += 20;
            coins -= 1;
            Font tileTitle = new Font(Font.MONOSPACED, Font.BOLD, 17);
            StdDraw.setFont(tileTitle);
            StdDraw.setPenColor(StdDraw.BLACK);
            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.text(40, 2, "You gained 20 points!");
            StdDraw.show();

        } else if (description.equals("bad")) {
            encounterBadTile(tiles);
            numPoints -= 10;
            totalPoints -= 10;
        }
        else if (description.equals("teleport") && !Player.transported) {
            Font tileTitle = new Font(Font.MONOSPACED, Font.BOLD, 17);
            StdDraw.setFont(tileTitle);
            StdDraw.setPenColor(StdDraw.BLACK);
            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.text(40, 2, "Press ’T’ to teleport to another side of the world (This is your only chance!)");
            StdDraw.show();
            for (int i = 0; i < teleport.size(); i += 1) {
                if (Player.characterPosition.equals(teleport.get(i))) {
                    teleport.remove(i);
                }
            }
        }
        Player.transported = false;

    }

    public static int getExtraCredit() {
        return extraCredit;
    }

    public static int getNumPoints() {
        return numPoints;
    }
    public static int getTotalPoints() {
        return totalPoints;
    }

    public static int coinsLeft() {
        return coins;
    }

    public static void encounterWaterDisplay(TETile[][] tiles) {
        StdDraw.clear(StdDraw.BLACK);
        StdDraw.setXscale(0.0, 80);
        StdDraw.setYscale(0.0, 40);
        Font water = new Font("Arial", Font.BOLD, 35);
        StdDraw.setFont(water);
        StdDraw.setPenColor(StdDraw.BOOK_LIGHT_BLUE);
        StdDraw.text(80 / 2, 40 - 5, "You have encountered the Water Waves!!!");
        StdDraw.picture(80 / 2, 40 / 2, "byow/Core/waterdrop.jpg");
        StdDraw.text(80 / 2, 20 - 15, "You've gained 100 extra points");
        StdDraw.show();
        StdDraw.pause(3000);
        ter.initialize(80, 40);
        ter.renderFrame(tiles);
    }

    public static void encounterMountainDisplay(TETile[][] tiles) {
        StdDraw.clear(StdDraw.BLACK);
        StdDraw.setXscale(0.0, 80);
        StdDraw.setYscale(0.0, 40);
        Font mountain = new Font("Arial", Font.BOLD, 35);
        StdDraw.setFont(mountain);
        StdDraw.setPenColor(StdDraw.WHITE);
        StdDraw.text(80 / 2, 40 - 5, "You have encountered the Rocky Mountains!!!");
        StdDraw.picture(80 / 2, 40 / 2, "byow/Core/mountain.jpg");
        StdDraw.text(80 / 2, 20 - 10, "You've gained 25 extra points");

        StdDraw.show();
        StdDraw.pause(3000);
        ter.initialize(80, 40);
        ter.renderFrame(tiles);

    }


    public static void encounterSandDisplay(TETile[][] tiles) {
        StdDraw.clear(StdDraw.BLACK);
        StdDraw.setXscale(0.0, 80);
        StdDraw.setYscale(0.0, 40);
        Font sand = new Font("Arial", Font.BOLD, 35);
        StdDraw.setFont(sand);
        StdDraw.setPenColor(StdDraw.YELLOW);
        StdDraw.text(80 / 2, 40 - 5, "You have encountered the Sand Dunes!!!");
        StdDraw.picture(80 / 2, 40 / 2, "byow/Core/sand.jpg");
        StdDraw.text(80 / 2, 20 - 10, "You've gained 50 extra points");
        StdDraw.show();
        StdDraw.pause(3000);
        ter.initialize(80, 40);
        ter.renderFrame(tiles);

    }

    public static void encounterBadTile(TETile[][] tiles) {
        StdDraw.clear(StdDraw.BLACK);
        StdDraw.setXscale(0.0, 80);
        StdDraw.setYscale(0.0, 40);
        Font bad = new Font("Arial", Font.BOLD, 35);
        StdDraw.setFont(bad);
        StdDraw.setPenColor(StdDraw.RED);
        StdDraw.text(80 / 2, 40 - 5, "Uh oh! You encountered a bad tile!");
        StdDraw.picture(80 / 2, 40 / 2 + 5, "byow/Core/minus.png");
        StdDraw.text(80 / 2, 20 - 5, "You lost 20 points");
        StdDraw.show();
        StdDraw.pause(3000);
        ter.initialize(80, 40);
        ter.renderFrame(tiles);

    }


}
