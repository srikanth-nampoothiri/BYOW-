package byow.Core;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.*;
import java.util.stream.IntStream;

import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.Edge;
import edu.princeton.cs.algs4.EdgeWeightedGraph;


/**
 * @Source https://docs.oracle.com/javase/8/docs/api/java/util/stream/IntStream.html
 * @Source https://docs.oracle.com/javase/8/docs/api/java/util/Random.html#ints--
 * @Source https://www.geeksforgeeks.org/java-util-random-class-java/
 */


public class RoomCreation {

    public static final int WIDTH = 80;
    public static final int HEIGHT = 40;
    private int index = 0;
    private  ArrayList<Integer> listofDoors = new ArrayList<>();
    private EdgeWeightedGraph worldPositions = new EdgeWeightedGraph(3200);
    private HashMap<Integer, Position> indexToPosition = new HashMap<>();
    private HashMap<Position, Integer> positionToIndex = new HashMap<>();
    private ArrayList<Integer> toConnect = new ArrayList<>();
    private static Position avatar;
    private static TETile[][] world = new TETile[WIDTH][HEIGHT];
    private static TERenderer ter = new TERenderer();
    private static TETile userTheme;

    private class Room {

        private int _x;
        private int _y;
        private TETile _tile;
        private int _xSize;
        private int _ySize;
        private Random _random;

        Room(int x, int y, TETile tile, int xSize, int ySize, Random random) {
            _x = x;
            _y = y;
            _tile = tile;
            _xSize = xSize;
            _ySize = ySize;
            _random = random;
        }

        public boolean checkForOverlap(int x, int y, TETile[][] tiles, int xSize, int ySize) {

            boolean overlaps = false;

            for (int s = 0; s < ySize; s += 1) { // number of y rows
                for (int i = 0; i < xSize; i += 1) { //number of x columns
                    if (!tiles[x][y].description().equals(userTheme.description())) {
                        overlaps = true;
                        break;
                    }
                    x += 1;
                }
                x = _x;
                y = y + 1;
            }

            return overlaps;

        }

        public void addRectangle(TETile[][] tiles) {

            int x = _x;
            int y = _y;
            int xSize = _xSize;
            int ySize = _ySize;
            Random random = _random;

            for (int s = 0; s < ySize; s += 1) { // number of y rows
                for (int i = 0; i < xSize; i += 1) { //number of x columns
                    tiles[x][y] = _tile;
                    x += 1;
                }
                x = _x;
                y = y + 1;
            }

            //add x wall

            x = _x - 1;
            y = _y - 1;

            int xWallLength = xSize + 2;
            int yWallLength = ySize;

            for (int s = 0; s < 2; s += 1) { //y rows
                for (int i = 1; i <= xWallLength; i += 1) {
                    tiles[x][y] = Tileset.WALL;
                    x += 1;
                }
                x = _x - 1;
                y += (ySize + 1);

            }

            //add y wall
            x = _x - 1;
            y = _y;

            for (int s = 0; s < 2; s += 1) { //y rows
                for (int i = 1; i <= yWallLength; i += 1) {
                    tiles[x][y] = Tileset.WALL;
                    y += 1;
                }
                x = _x + xSize;
                y = _y;
            }

            //add openings
            int wallX = _x - 1;
            int wallY = _y;

            roomOpenings(wallX, wallY, tiles, _xSize, _ySize, random);

        }

    }

    private void roomOpenings(int x, int y, TETile[][] tiles, int xSize, int ySize, Random random) {
        //int numOpenings = randomUtils.uniform(random, 1, 3);
        int i = 0;
        while (i < 1) {
            int xVal = RandomUtils.uniform(random, x, (x + xSize));
            int yVal = RandomUtils.uniform(random, y, (y + ySize));
            if (tiles[xVal][yVal].description().equals("floor")) {
                i += 1;
                tiles[xVal][yVal] = Tileset.FLOOR;
                Position pos = new Position(xVal, yVal);
                //doorStatus.put(pos, true);
                listofDoors.add(positionToIndex.get(pos));
            }

        }
    }

    private int xRandomNumber(Random random) {
        IntStream intstream = random.ints(30, 6, WIDTH - 10);
        int[] randomNumbers = intstream.toArray();
        int randomNum = randomNumbers[index];
        index += 1;
        if (index >= 30) {
            index = 0;
        }
        return randomNum;
    }


    private int yRandomNumber(Random random) {
        IntStream intstream = random.ints(20, 6, HEIGHT - 10);
        int[] randomNumbers = intstream.toArray();
        int randomNum = randomNumbers[index];
        index += 1;
        if (index >= 20) {
            index = 0;
        }
        return randomNum;
    }


    private void generateRooms(TETile[][] tiles, Random random) {
        int numRooms = RandomUtils.uniform(random, 10, 30);

        for (int i = 0; i < numRooms; i += 1) {
            int xSize = RandomUtils.uniform(random, 2, 10); //width
            int ySize = RandomUtils.uniform(random, 2, 10); //height
            int x = xRandomNumber(random);
            int y = yRandomNumber(random);

            Room room = new Room(x, y, Tileset.FLOOR, xSize, ySize, random);
            if (!room.checkForOverlap(x, y, tiles, xSize, ySize)) {
                room.addRectangle(tiles);
            }
        }
    }


    //connects edges
    private void createGraph() { //connect all edges
        for (int v = 80; v < 3200; v += 1) {
            ArrayList<Integer> adj = getAdjacentVertices(v);
            for (int vertex: adj) {
                Edge edge = new Edge(v, vertex, v + vertex);
                worldPositions.addEdge(edge);
            }
        }

    }

    //for createGraph (to help connect edges)
    public static ArrayList<Integer> getAdjacentVertices(int v) {
        ArrayList<Integer> adj = new ArrayList<>();
        if ((v + 1) % 40 != 0) { //top (making sure it exists)
            adj.add(v + 1);
        }
        if (v < 3160) { //right
            adj.add(v + 40);
        }
        return adj;
    }

    //creates wall around hallways
    public static void createWall(ArrayList<Position> positions, TETile[][] tiles) {
        Position next;
        for (int i = 0; i < positions.size(); i += 1) {
            for (Position adj: getAdj(positions.get(i))) {
                int x = adj.getX();
                int y = adj.getY();
                if (tiles[x][y].description().equals(userTheme.description())) {
                    tiles[x][y] = Tileset.WALL;
                }
            }
        }
    }

    //for create wall to generate hallway wall
    public static ArrayList<Position> getAdj(Position position) {
        ArrayList<Position> adjacent = new ArrayList<>();

        int x = position.getX();
        int y = position.getY();

        if (x + 1 < 80) {
            Position right = new Position(x + 1, y);
            adjacent.add(right);
        }
        if (x - 1 > 0) {
            Position left = new Position(x - 1, y);
            adjacent.add(left);
        }
        if (y + 1 < 40) {
            Position top = new Position(x, y + 1);
            adjacent.add(top);
        }
        if (y - 1 > 0) {
            Position bottom = new Position(x, y - 1);
            adjacent.add(bottom);
        }

        return adjacent;
    }

    private int generateHallways(int start, int end, TETile[][] tiles) {
        FindPath dijkstra = new FindPath(worldPositions, start);
        Iterable<Edge> path = dijkstra.pathTo(end);
        int j = 0;

        ArrayList<Position> positions = new ArrayList<>();
        positions.add(indexToPosition.get(start));

        Iterator<Edge> iterator = path.iterator();

        while (iterator.hasNext()) {
            int element = iterator.next().either();
            Position pos = indexToPosition.get(element);
            positions.add(pos);
            tiles[pos.getX()][pos.getY()] = Tileset.FLOOR;
            j += 1;
        }

        int k = positions.size() / 2;
        Position positionToAdd = positions.get(k);
        int hallPosition = positionToIndex.get(positionToAdd);

        createWall(positions, world);

        return hallPosition;
    }


    //use to connect remaining isolated rooms
    public void connectHalls(ArrayList<Integer> positions, TETile[][] tiles) {

        //connect first two points, then remove
        int start = positions.get(0);
        int end = positions.get(1);
        int hall = generateHallways(start, end, tiles);
        positions.remove(0);
        positions.remove(0);

        //connect new hallway to rest
        for (int i = 0; i < positions.size(); i += 1) {
            int next = positions.get(i);
            hall = generateHallways(next, hall, tiles);
        }


    }

    public static Position getAvatar() {
        return avatar;
    }

    public static TETile[][] getWorld() {
        return world;
    }

    public static TERenderer getRenderer() {
        return ter;
    }


    public TETile[][] tesselateWorld(Random random, TETile theme) {
        int width = WIDTH;
        int height = HEIGHT;
        int k = 0;
        userTheme = theme;

        createGraph();

        for (int x = 0; x < world.length; x += 1) {
            for (int y = 0; y < world[0].length; y += 1) {
                world[x][y] = userTheme;
                indexToPosition.put(k, new Position(x, y));
                positionToIndex.put(new Position(x, y), k);
                k += 1;
            }
        }

        //add rooms
        generateRooms(world, random);


        //add hallways
        int numDoors = listofDoors.size();

        for (int i = 0; i < numDoors; i += 2) {
            if (listofDoors.size() == 1) {
                break;
            }

            int start = listofDoors.get(0);
            int end = listofDoors.get(1);

            listofDoors.remove(0);
            listofDoors.remove(0);

            toConnect.add(generateHallways(start, end, world));

        }

        if (listofDoors.size() == 1) {
            int door = listofDoors.get(0);
            toConnect.add(door);
        }

        int avatarPos = toConnect.get(0);
        avatar = indexToPosition.get(avatarPos);

        if (toConnect.size() > 1) {
            connectHalls(toConnect, world);
        }

        Items.setItems(world, width, height, random);
        Items.toCollect(world, width, height, random);
        Items.negativeItems(world, width, height, random);
        Items.teleportItems(world, width, height, random);

        return world;
    }

    public static void main(String[] unused) {

    }



}
