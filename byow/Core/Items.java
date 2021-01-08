package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.ArrayList;
import java.util.Random;

public class Items {
    private Position item;
    private String[] collectionItems = {"ice block", "mountain", "sand quick"};
    private static int numCoins = 0;
    private static ArrayList<Position> teleport = new ArrayList<>(4);

    public Items(Position p) {
        item = p;
    }

    public static void setItems(TETile[][] world, int width, int height, Random random) {
        boolean placeMoreItems = true;
        int itemCount = 0;
        while (placeMoreItems) {
            int xCoord = random.nextInt(width);
            int yCoord = random.nextInt(height);
            if (world[xCoord][yCoord].equals(Tileset.FLOOR)) {
                if (itemCount == 0) {
                    world[xCoord][yCoord] = Tileset.WATER;
                    itemCount += 1;
                } else if (itemCount == 1) {
                    world[xCoord][yCoord] = Tileset.MOUNTAIN;
                    itemCount += 1;
                } else if (itemCount == 2) {
                    world[xCoord][yCoord] = Tileset.SAND;
                    itemCount += 1;
                } else if (itemCount > 2) {
                    placeMoreItems = false;
                }
            }
        }

    }

    public static void toCollect(TETile[][] world, int width, int height, Random random) {
        int itemCount = RandomUtils.uniform(random, 3, 15);
        numCoins = itemCount;
        while (itemCount > 0) {
            int xCoord = random.nextInt(width);
            int yCoord = random.nextInt(height);
            if (world[xCoord][yCoord].equals(Tileset.FLOOR)) {
                world[xCoord][yCoord] = Tileset.COIN;
                itemCount -= 1;
            }
        }
    }

    public static int getNumCoins() { //to allow other class to access number of coins
        return numCoins;
    }


    public static void negativeItems(TETile[][] world, int width, int height, Random random) {
        int itemCount = RandomUtils.uniform(random, 3, 7);
        while (itemCount > 0) {
            int xCoord = random.nextInt(width);
            int yCoord = random.nextInt(height);
            if (world[xCoord][yCoord].equals(Tileset.FLOOR)) {
                world[xCoord][yCoord] = Tileset.BAD;
                itemCount -= 1;
            }
        }
    }

    public static void teleportItems(TETile[][] world, int width, int height, Random random) {
        int itemCount = 4;
        while (itemCount > 0) {
            int xCoord = random.nextInt(width);
            int yCoord = random.nextInt(height);
            if (world[xCoord][yCoord].equals(Tileset.FLOOR)) {
                world[xCoord][yCoord] = Tileset.TELEPORT;
                teleport.add(new Position(xCoord, yCoord));
                itemCount -= 1;
            }
        }
    }

    public static ArrayList<Position> getTeleportPositions() {
        return teleport;
    }


}
