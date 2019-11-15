package util;

/**
 * Helper class for medEvent.
 */
public class Room {

    private int roomId;
    private String name;

    public Room(int roomId, String name) {
        this.roomId = roomId;
        this.name = name;
    }

    public int getRoomId() {
        return roomId;
    }

    public String getName() {
        return name;
    }
    @Override
    public String toString() {
        return name;
    }
}
