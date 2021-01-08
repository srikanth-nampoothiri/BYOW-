package byow.Core;
import byow.TileEngine.TETile;
import java.io.*;

public class Memory implements Serializable {

    TETile[][] world;
    Player avatar;
    String avatarName;
    boolean finishedTypingName;
    TETile userAvatar;

    public Memory(TETile[][] world, Player avatar, String avatarName, boolean finishedTypingName, TETile userAvatar) {
        this.world = world;
        this.avatar = avatar;
        this.avatarName = avatarName;
        this.finishedTypingName = finishedTypingName;
        this.userAvatar = userAvatar;
    }

}
