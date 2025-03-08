package gui.traversinggame.commons;

public final class Sizes {
    // Stage sizes
    public static final class Stage {
        public final static int MAX_WIDTH = 1280;
        public final static int MAX_HEIGHT = 720;

        public final static int MAIN_MIN_WIDTH = 720;
        public final static int MAIN_MIN_HEIGHT = 600;

        private Stage() {
        }
    }

    // Cell sizes
    public static final class Cell {
        public final static int WIDTH = 40;
        public final static int HEIGHT = 40;
        public static final int PADDING = 5;

        private Cell() {
        }

    }

    // Matrix sizes
    public static final class Matrix {
        public static final int PADDING = 10;

        private Matrix() {
        }
    }

    // Button sizes
    public static final class Button {
        public final static int BIG_WIDTH = 400;
        public final static int BIG_HEIGHT = 30;

        private Button() {
        }
    }

    // Border radius sizes
    public static final class BorderRadius {
        public final static int BIG = 10;
        public final static int MEDIUM = 8;
        public final static int SMALL = 6;

        private BorderRadius() {
        }
    }

    // Font sizes
    public static final class Font {
        public final static int BIG = 50;
        public final static int MEDIUM = 30;
        public final static int SMALL = 20;

        private Font() {
        }
    }

    // Margin sizes
    public static final class Margin {
        public final static int TITLE_TOP = 20;
        public final static int TITLE_BOTTOM = 20;
        public final static int BTN_BOTTOM = 10;
        public final static int FOOTER_RIGHT = 40;
        public final static int FOOTER_BOTTOM = 20;

        private Margin() {
        }
    }

    private Sizes() {
    }
}