package byow.Core;

import byow.InputDemo.InputSource;
import byow.InputDemo.KeyboardInputSource;
import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import edu.princeton.cs.introcs.StdDraw;

import java.io.*;
import java.awt.*;
import java.util.Random;
/**
 * @Source https://docs.oracle.com/javase/7/docs/api/javax/swing/text/Position.html
 * @Source https://stackoverflow.com/questions/28948315/save-game-data-java
 * @Source https://www.geeksforgeeks.org/stringbuilder-class-in-java-with-examples/
 * @Source https://stackoverflow.com/questions/1521921/splitting-words-into-letters-in-java
 * @Source https://beginnersbook.com/2013/12/how-to-convert-string-to-int-in-java/
 * @Source https://introcs.cs.princeton.edu/java/stdlib/javadoc/StdDraw.html#isKeyPressed-int-
 * @Source https://beginnersbook.com (how-to-convert-char-to-string-and-a-string-to-char-in-java)
 */

public class Engine {
    //TERenderer ter = new TERenderer();
    /* Feel free to change the width and height. */
    public static final int WIDTH = 80;
    public static final int HEIGHT = 40;
    public static final String SAVED_FILE = "load_game.txt";
    private static boolean finishedTypingSeed = false;
    private static Player p;
    private static TETile[][] world;
    private static TERenderer ter = RoomCreation.getRenderer();
    private static boolean finishedTypingName = false;
    private static String avatarName = "";
    private static long userSeed;
    private static String currTileStep;
    private static TETile chosenAvatar = Tileset.AVATAR;
    private static TETile worldTheme = Tileset.NOTHING;
    private static int coinsLeft = 0;

    /**
     * Method used for exploring a fresh world. This method should handle all inputs,
     * including inputs from the main menu.
     */
    public static void interactWithKeyboard() {
        InputSource inputSource;
        inputSource = new KeyboardInputSource();
        while (inputSource.possibleNextInput()) {
            char c = inputSource.getNextKey();

            if (c == 'I') {
                showInstructions();
            }

            if (c == 'N') {
                enterSeed();
                Random random = new Random(userSeed);
                if (finishedTypingSeed) {
                    world = createWorld(random);
                    p = playerInitiation(world);
                    keyboardStartGame(world, p);
                }

            }
            if (c == 'L') {
                keyboardLoadWorld();
            }
            if (c == 'Q') {
                System.exit(0);

            }
            if (c == 'A') {
                enterName();
                displayMainMenuHead();

            }
            if (c == 'C') {
                chooseAvatar();
                displayMainMenuHead();
            }
            if (c == 'T') {
                chooseTheme();
                displayMainMenuHead();
            }

        }

    }


    public TETile[][] interactWithInputString(String input) {
        input = input.toLowerCase();
        char characterSelection = menuSelection(input);
        if (characterSelection == 'n') {
            long userSeed = readSeed(input);
            String userMovements = readUserMovements(input);
            Random random = new Random(userSeed);
            TETile[][] finalWorldFrame = createWorld(random);
            startGame(finalWorldFrame, userMovements);
            return finalWorldFrame;
        } else if (characterSelection == 'q') {
            return null;
        } else if (characterSelection == 'l') {
            String userMovements = input.substring(1);
            return loadWorld(userMovements);
        }
        throw new RuntimeException();
    }

    public static void main(String[] args) {
        Engine engine = new Engine();
        engine.interactWithInputString("n1234s");

    }

    //selects n, l or q on menu based on input (used interactWithKeyboard() instead)
    public char menuSelection(String input) {
        return input.charAt(0);
    }

    public static long readSeed(String input) { //returns seed
        String[] inputs = input.split("(?<=\\D)(?=\\d)|(?<=\\d)(?=\\D)");
        return Long.parseLong(inputs[1]);
    }

    public static String readUserMovements(String input) { //returns key movement letters
        String[] inputs = input.split("(?<=\\D)(?=\\d)|(?<=\\d)(?=\\D)");
        return inputs[2];
    }


    public static void displayMainMenuHead() {
        StdDraw.clear(Color.BLACK);
        StdDraw.setXscale(0.0, WIDTH);
        StdDraw.setYscale(0.0, HEIGHT);
        Font gameTitle = new Font(Font.MONOSPACED, Font.BOLD, 30);
        StdDraw.setFont(gameTitle);
        StdDraw.setPenColor(StdDraw.WHITE);
        StdDraw.text(WIDTH / 2, HEIGHT - 10, "CS1B: THE GAME");

        Font instructions = new Font(Font.MONOSPACED, Font.BOLD, 20);
        StdDraw.setFont(instructions);
        StdDraw.text(WIDTH / 2, HEIGHT / 2 + 5, "Instructions (Press I)");

        Font menuTitle = new Font("Arial", Font.BOLD, 15);
        StdDraw.setFont(menuTitle);
        StdDraw.text(WIDTH / 2, HEIGHT / 2, "New Game (Press N)");
        StdDraw.text(WIDTH / 2, HEIGHT / 2 - 2, "Load Game (Press L)");
        StdDraw.text(WIDTH / 2, HEIGHT / 2 - 4, "Quit Game (Press Q)");
        StdDraw.text(WIDTH / 2, HEIGHT / 2 - 6, "Avatar Name (Press A)");
        StdDraw.text(WIDTH / 2, HEIGHT / 2 - 8, "Choose an Avatar (Press C)");
        StdDraw.text(WIDTH / 2, HEIGHT / 2 - 10, "World Theme (Press T)");
        StdDraw.setPenColor(StdDraw.WHITE);
        StdDraw.show();
    }

    public static void enterSeed() { //Allows you to type in seed value
        StdDraw.clear(Color.BLACK);
        StdDraw.setXscale(0.0, WIDTH);
        StdDraw.setYscale(0.0, HEIGHT);
        Font description = new Font(Font.MONOSPACED, Font.BOLD, 20);
        StdDraw.setFont(description);
        StdDraw.text(40, 30, "Enter your desired seed, then press 'S'!");
        description = new Font(Font.MONOSPACED, Font.BOLD, 15);
        StdDraw.setFont(description);
        StdDraw.text(40, 25, "(Press 'M' to go back to the Main Menu)");
        StdDraw.setPenColor(StdDraw.WHITE);
        StdDraw.show();
        String seed = "";
        while (true) {
            if (StdDraw.hasNextKeyTyped()) {
                char currentChar = StdDraw.nextKeyTyped();
                currentChar = Character.toLowerCase(currentChar);
                if (currentChar == 's') {
                    finishedTypingSeed = true;
                    userSeed = Long.parseLong(seed);
                    break;
                }
                if (Character.isDigit(currentChar)) {
                    seed += currentChar;
                }
                if (currentChar == 'm') {
                    displayMainMenuHead();
                    break;
                }
            }
        }
    }

    public static void enterName() { //Allows you to type in avatar name
        StdDraw.clear(Color.BLACK);
        StdDraw.setXscale(0.0, WIDTH);
        StdDraw.setYscale(0.0, HEIGHT);
        Font description = new Font(Font.MONOSPACED, Font.BOLD, 15);
        StdDraw.setFont(description);
        StdDraw.text(40, 30, "Enter your desired name, then press '1'!");
        StdDraw.setPenColor(StdDraw.WHITE);
        StdDraw.show();
        String name = "";
        while (true) {
            if (StdDraw.hasNextKeyTyped()) {
                char currentChar = StdDraw.nextKeyTyped();
                currentChar = Character.toLowerCase(currentChar);
                if (currentChar == '1') {
                    if (name.length() > 0) {
                        finishedTypingName = true;
                        avatarName = name;
                    }
                    break;
                } else {
                    name += currentChar;
                }
            }
        }
    }

    public static void chooseAvatar() { //Allows you to choose an avatar
        StdDraw.clear(Color.BLACK);
        StdDraw.setXscale(0.0, WIDTH);
        StdDraw.setYscale(0.0, HEIGHT);
        Font description = new Font(Font.MONOSPACED, Font.BOLD, 15);
        StdDraw.setFont(description);
        StdDraw.setPenColor(StdDraw.WHITE);
        StdDraw.text(40, 30, "Enter the letter corresponding to the avatar you want!");


        StdDraw.text(20, 15, "Press 'T'");
        StdDraw.text(40, 15, "Press 'F'");
        StdDraw.text(60, 15, "Press 'A'");
        StdDraw.text(60, 13, "(default)");

        description = new Font(Font.MONOSPACED, Font.BOLD, 35);
        StdDraw.setFont(description);
        StdDraw.text(20, 20, "♠");
        StdDraw.text(40, 20, "❀");
        StdDraw.text(60, 20, "@");


        StdDraw.show();
        while (true) {
            if (StdDraw.hasNextKeyTyped()) {
                char currentChar = StdDraw.nextKeyTyped();
                currentChar = Character.toLowerCase(currentChar);
                if (currentChar == 't') {
                    chosenAvatar = Tileset.TREE;
                    break;
                } else if (currentChar == 'f') {
                    chosenAvatar = Tileset.FLOWER;
                    break;
                } else if (currentChar == 'a') {
                    break;
                }
            }
        }
    }

    public static void chooseTheme() { //Allows you to choose a theme
        StdDraw.clear(Color.BLACK);
        StdDraw.setXscale(0.0, WIDTH);
        StdDraw.setYscale(0.0, HEIGHT);
        Font description = new Font(Font.MONOSPACED, Font.BOLD, 15);
        StdDraw.setFont(description);
        StdDraw.setPenColor(StdDraw.WHITE);
        StdDraw.text(40, 30, "Enter the letter corresponding to the theme you want!");


        StdDraw.text(20, 15, "Press 'G'");
        StdDraw.text(40, 15, "Press 'W'");
        StdDraw.text(60, 15, "Press 'N'");
        StdDraw.text(60, 13, "(default)");

        description = new Font(Font.MONOSPACED, Font.BOLD, 35);
        StdDraw.setFont(description);
        StdDraw.text(20, 20, "\"");
        StdDraw.text(40, 20, "≈");
        StdDraw.text(60, 20, " ");


        StdDraw.show();
        while (true) {
            if (StdDraw.hasNextKeyTyped()) {
                char currentChar = StdDraw.nextKeyTyped();
                currentChar = Character.toLowerCase(currentChar);
                if (currentChar == 'g') {
                    worldTheme = Tileset.GRASS;
                    break;
                } else if (currentChar == 'w') {
                    worldTheme = Tileset.WATER;
                    break;
                } else if (currentChar == 'n') {
                    break;
                }
            }
        }
    }

    public static void showInstructions() {
        StdDraw.clear(Color.BLACK);
        StdDraw.setXscale(0.0, WIDTH);
        StdDraw.setYscale(0.0, HEIGHT);

        Font gameTitle = new Font(Font.MONOSPACED, Font.BOLD, 20);
        StdDraw.setFont(gameTitle);
        StdDraw.setPenColor(StdDraw.WHITE);
        StdDraw.text(WIDTH / 2, HEIGHT - 5, "Goal:");
        StdDraw.text(WIDTH / 2, HEIGHT - 10, "Points:");
        StdDraw.text(WIDTH / 2, HEIGHT - 17, "Other Features:");
        StdDraw.text(WIDTH / 2, HEIGHT - 27, "Press 'M' to go back to the Main Menu.");

        Font menuTitle = new Font("Arial", Font.BOLD, 13);
        StdDraw.setFont(menuTitle);
        StdDraw.text(WIDTH / 2, HEIGHT - 7, "Try to collect all of the coins in the world using the fewest moves possible");
        StdDraw.text(WIDTH / 2, HEIGHT - 12, "Collecting coins increase your points by 20");
        StdDraw.text(WIDTH / 2, HEIGHT - 14, "Encountering the water, sand or mountain tiles add 100, 50, or 25 points");
        StdDraw.text(WIDTH / 2, HEIGHT - 19, "Encountering the bad tiles (-) decreases your points by 20");
        StdDraw.text(WIDTH / 2, HEIGHT - 21, "Encountering teleportation tiles lets you move across the world using 1 move");

        StdDraw.setPenColor(StdDraw.WHITE);
        StdDraw.show();

        while (true) {
            if (StdDraw.hasNextKeyTyped()) {
                char currentChar = StdDraw.nextKeyTyped();
                currentChar = Character.toLowerCase(currentChar);
                if (currentChar == 'm') {
                    displayMainMenuHead();
                    break;
                }
            }
        }

    }

    public void startGame(TETile[][] tiles, String userInput) {
        for (int i = 0; i < userInput.length(); i++) {
            char curr = userInput.charAt(i);
            if (curr == ':') {
                char next = userInput.charAt(i + 1);
                next = Character.toLowerCase(next);
                if (next == 'q') {
                    saveWorld(tiles);
                    System.exit(0); //need to quit and save
                }
            }
        }
        saveWorld(tiles);
    }

    //for load world
    public static Memory readObjectFromFile(String filepath)  {

        try {
            FileInputStream fileIn = new FileInputStream(filepath);
            ObjectInputStream objectIn = new ObjectInputStream(fileIn);

            Object obj = objectIn.readObject();

            objectIn.close();
            return (Memory) obj;

        } catch (IOException ex) {
            ex.printStackTrace();
            return null;

        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
            return null;
        }

    }

    //loading function
    public static void keyboardLoadWorld() {
        Memory loadGame = readObjectFromFile(SAVED_FILE);
        world = loadGame.world;
        avatarName = loadGame.avatarName;
        p = loadGame.avatar;
        finishedTypingName = loadGame.finishedTypingName;
        chosenAvatar = loadGame.userAvatar;
        keyboardStartGame(loadGame.world, loadGame.avatar);
    }

    //for string world
    public TETile[][] loadWorld(String userInput) {
        Memory loadGame = readObjectFromFile(SAVED_FILE);
        startGame(loadGame.world, userInput);
        return loadGame.world;
    }


    public static void saveWorld(TETile[][] tiles) {

        boolean saved = false;
        while (!saved) {
            try {
                FileOutputStream fileOut = new FileOutputStream(SAVED_FILE);
                ObjectOutputStream oos = new ObjectOutputStream(fileOut);

                Memory game = new Memory(tiles, p, avatarName, finishedTypingName, chosenAvatar);
                oos.writeObject(game);
                oos.close();
                saved = true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public static TETile[][] createWorld(Random random) {
        RoomCreation room = new RoomCreation();
        return room.tesselateWorld(random, worldTheme);
    }


    public static Player playerInitiation(TETile[][] tiles) {
        Position pos = RoomCreation.getAvatar(); //get new position
        Player pl = new Player(pos); //create player at that pos
        tiles[pos.getX()][pos.getY()] = chosenAvatar;
        ter.renderFrame(tiles);
        return pl;
    }

    public static void keyboardStartGame(TETile[][] tiles, Player player) {
        ter.initialize(WIDTH, HEIGHT);
        ter.renderFrame(tiles);
        addNameToHUD();
        boolean quit = false;
        coinsLeft = Items.getNumCoins();
        while (coinsLeft > 0) {
            checkMouse();
            if (StdDraw.hasNextKeyTyped()) {
                char currentChar = StdDraw.nextKeyTyped();
                currentChar = Character.toLowerCase(currentChar);
                if (currentChar == ':') {
                    quit = true;
                } else if (currentChar == 'q' && quit) {
                    saveWorld(tiles);
                    System.exit(0);
                    break;
                } else {
                    int x = player.characterPosition.getX();
                    int y = player.characterPosition.getY();
                    tiles[x][y] = Tileset.FLOOR;
                    player.move(tiles, currentChar, WIDTH, HEIGHT);
                    x = player.characterPosition.getX();
                    y = player.characterPosition.getY();
                    currTileStep = tiles[x][y].description();
                    tiles[x][y] = chosenAvatar;
                    ter.renderFrame(tiles);
                    Encounters.checkTile(currTileStep, tiles);
                    addNameToHUD();
                    quit = false;
                }

            }
            coinsLeft = Encounters.coinsLeft();
        }
        FinalScore.finalScore();
    }

    public static String getCurrTile() {
        return currTileStep;
    }

    public static void checkMouse() {
        int x = (int) StdDraw.mouseX();
        int y = (int) StdDraw.mouseY();

        if (x < 80 && y < 40 && y > 0 && x > 0 && !world[x][y].equals(worldTheme)) {

            Font tileTitle = new Font(Font.MONOSPACED, Font.BOLD, 20);
            StdDraw.setFont(tileTitle);
            StdDraw.setPenColor(StdDraw.BLACK);
            StdDraw.filledRectangle(5, 39, 3, 1);
            StdDraw.setPenColor(StdDraw.WHITE);
            StdDraw.text(5, 39, world[x][y].description());
            StdDraw.show();
            StdDraw.pause(500);
            ter.renderFrame(world);
            addNameToHUD();
        }

    }

    public static void addNameToHUD() {
        Font tileTitle = new Font(Font.MONOSPACED, Font.BOLD, 17);
        StdDraw.setFont(tileTitle);
        StdDraw.setPenColor(StdDraw.BLACK);
        //StdDraw.filledRectangle(40, 39, 9, 1);
        StdDraw.setPenColor(StdDraw.RED);
        StdDraw.text(40, 39, "Collect all the coins in the least moves possible! Points: " + Encounters.getTotalPoints());
        StdDraw.show();

        if (finishedTypingName) {
            StdDraw.setPenColor(StdDraw.BLACK);
            StdDraw.filledRectangle(70, 39, 6, 1);
            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.text(70, 39, "Avatar: " + avatarName);
            StdDraw.show();
        }
    }
}
