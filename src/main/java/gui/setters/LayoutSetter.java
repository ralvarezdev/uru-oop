package gui.setters;

import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import gui.commons.ColorPalette;

public final class LayoutSetter {
    public static void setBgColor(Pane pane, ColorPalette stageBgColor) throws NullPointerException {
        pane.setBackground(new Background(new BackgroundFill(stageBgColor.getColor(), null, null)));
    }
}
