package byow.lab12;
import org.junit.Test;
import static org.junit.Assert.*;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.Random;

/**
 * Draws a world consisting of hexagonal regions.
 */
public class HexWorld {

    private static final int SIZE = 3; //size of big hexagon
    private static final int SEED = 42;
    private static final Random RANDOM = new Random(SEED);

    private static class Hex {

        private int _x;
        private int _y;
        private TETile _tile;

        public Hex (int x, int y, TETile tile) {
            _x = x; //where the shape starts
            _y = y; //same ^
            _tile = tile;
        }


        public void addHexagon(TETile[][] tiles) {
            //fill in
            int x = _x;
            int y = _y;

            for (int s = 0; s < SIZE; s += 1) {
                int rowLength = SIZE + 2 * s;
                for (int i = 0; i < rowLength; i += 1) {
                    x += 1;
                    tiles[x][y] = _tile;
                }
                x = _x - 1;
                y = y - 1;
            }

            //do same row twice
            x += 1;

            for (int s = SIZE - 1; s >= 0; s -= 1) {
                int rowLength = SIZE + 2 * s;
                for (int i = 0; i < rowLength; i += 1) {
                    x += 1;
                    tiles[x][y] = _tile;
                }
                x = _x + 1;
                y = y - 1;
            }
        }
    }

    private static TETile randomTile() {
        int tileNum = RANDOM.nextInt(3);
        switch (tileNum) {
            case 0: return Tileset.WALL;
            case 1: return Tileset.FLOWER;
            case 2: return Tileset.WATER;
            default: return Tileset.NOTHING;
        }
    }

    private static void addColumnofHex(int x, int y, int numHexes, TETile[][] world) {
        for (int i = 0; i < numHexes; i += 1) {
            TETile tile = randomTile();
            Hex h = new Hex(x, y, tile);
            h.addHexagon(world);
            y = y - (SIZE * 2);
        }
    }

    private static void tesselateWorld() {
        int width = 10 * SIZE;
        int height = 12 * SIZE;
        TETile[][] world = new TETile[width][height];
        TERenderer ter = new TERenderer();
        ter.initialize(width, height);

        for (int x = 0; x < world.length; x += 1) {
            for (int y = 0; y < world[0].length; y += 1) {
                world[x][y] = Tileset.NOTHING;
            }
        }

        //first column has 3 hexagons
        int x = SIZE - 2;
        int y = (height - 1) - (2 * SIZE);
        addColumnofHex(x, y, 3, world);

        //next has 4
        x += 2 * SIZE - 1;
        y += SIZE;
        addColumnofHex(x, y, 4, world);

        //5
        x += 2 * SIZE - 1;
        y += SIZE;
        addColumnofHex(x, y, 5, world);

        //4
        x += 2 * SIZE - 1;
        y -= SIZE;
        addColumnofHex(x, y, 4, world);

        //3
        x += 2 * SIZE - 1;
        y -= SIZE;
        addColumnofHex(x, y, 3, world);

        ter.renderFrame(world);
    }



    public static void main(String[] unused) {

        tesselateWorld();
    }

}
