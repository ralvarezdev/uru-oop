package gui.traversinggame.commons;

import javafx.scene.paint.Color;
import gui.commons.ColorPalette;

public enum Colors implements ColorPalette {
    GREEN_LIGHT(195, 250, 232), GREEN_DARK(56, 217, 169), GREY_LIGHT(214, 243, 245), GREY_MEDIUM(73, 80, 87),
    GREY_DARK(33, 37, 41);

    private final double[] color;

    private static final int MAX_ALPHA = 255;

    Colors(double r, double g, double b, double a) {
        this.color = new double[]{r, g, b, a};
    }

    Colors(int r, int g, int b, int a) {
        this(r, g, b, (double) a);
    }

    Colors(double r, double g, double b) {
        this(r, g, b, MAX_ALPHA);
    }

    Colors(int r, int g, int b) {
        this(r, g, b, (double) MAX_ALPHA);
    }

    public Color getColor() {
        return new Color(this.color[0] / 255, this.color[1] / 255, this.color[2] / 255, this.color[3] / 255);
    }

    public String getRGBA() {
        return "rgba(%d,%d,%d,%d)".formatted((int) this.color[0], (int) this.color[1], (int) this.color[2],
                (int) this.color[3]);
    }

    // Dark theme
    public final static class Dark {
        public final static Colors STAGE_BG = Colors.GREY_DARK;

        public final static Colors NODE_FG = Colors.GREEN_LIGHT;
        public final static Colors NODE_BG = Colors.GREY_MEDIUM;

        public final static Colors NODE_HOVER_FG = Colors.GREY_LIGHT;
        public final static Colors NODE_HOVER_BG = Colors.GREEN_DARK;

        public final static Colors FONT = Colors.GREEN_LIGHT;

        public final static Colors FONT_HOVER = Colors.GREY_LIGHT;

        private Dark() {
        }
    }
}