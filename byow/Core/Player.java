package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import java.io.*;
import java.util.ArrayList;

public class Player implements Serializable {

    public static Position characterPosition;
    public static boolean transported;
    private static int numMoves = 0;
    private static ArrayList<Position> teleport = Items.getTeleportPositions();

    public Player(Position characterPosition) {
        this.characterPosition = characterPosition;
    }

    public boolean canCharMove(TETile[][] world, int x, int y, int width, int height) {
        if (x >= 0 && y >= 0 && x < width && y < height) {
            if (world[x][y].character() != Tileset.WALL.character()) {
                return true;
            }
        }
        return false;
    }

    public void move(TETile[][] world, char input, int width, int height) {
        int x = characterPosition.getX();
        int y = characterPosition.getY();
        boolean moved = false;

        if (input == 'a') {
            if (canCharMove(world, x - 1, y, width, height)) {
                characterPosition = new Position(x - 1, y);
                moved = true;
            }
        } else if (input == 'd') {
            if (canCharMove(world, x + 1, y, width, height)) {
                characterPosition = new Position(x + 1, y);
                moved = true;
            }
        } else if (input == 'w') {
            if (canCharMove(world, x, y + 1, width, height)) {
                characterPosition = new Position(x, y + 1);
                moved = true;
            }
        } else if (input == 's') {
            if (canCharMove(world, x, y - 1, width, height)) {
                characterPosition = new Position(x, y - 1);
                moved = true;
            }
        }
        else if (input == 't' && Engine.getCurrTile().equals("teleport")) {
            characterPosition = teleport.get(0);
            teleport.remove(0);
            moved = true;
            transported = true;

        }

        if (moved) {
            numMoves += 1;
        }
    }


    public static int getNumMoves() {
        return numMoves;
    }
}

