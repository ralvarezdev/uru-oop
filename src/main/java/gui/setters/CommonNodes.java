package gui.setters;

import javafx.geometry.Insets;

public final class CommonNodes {
    public static Insets getInset(int top, int right, int bottom, int left) {
        return new Insets(top, right, bottom, left);
    }

    public static Insets getInset(int vertical, int horizontal) {
        return new Insets(vertical, horizontal, vertical, horizontal);
    }

    public static Insets getInset(int spacing) {
        return new Insets(spacing, spacing, spacing, spacing);
    }

    public static Insets getRightInset(int right) {
        return getInset(0, right, 0, 0);
    }

    public static Insets getBottomInset(int bottom) {
        return getInset(0, 0, bottom, 0);
    }
}
