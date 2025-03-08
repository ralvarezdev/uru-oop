package gui.traversinggame.commons;

import javafx.scene.input.KeyCode;

public enum KeyDirections {
    MOVE_UP("UP"), MOVE_RIGHT("RIGHT"), MOVE_DOWN("DOWN"), MOVE_LEFT("LEFT");

    public static final int MAX_DIR = 3;

    private final String dir;

    KeyDirections(String dir) {
        this.dir = dir;
    }

    public static KeyDirections getKeyDirection(KeyCode keyCode) {
        return switch (keyCode) {
            case W, UP -> MOVE_UP;
            case D, RIGHT -> MOVE_RIGHT;
            case S, DOWN -> MOVE_DOWN;
            case A, LEFT -> MOVE_LEFT;
            default -> null;
        };
    }

    public String getDirection() {
        return this.dir;
    }
}
