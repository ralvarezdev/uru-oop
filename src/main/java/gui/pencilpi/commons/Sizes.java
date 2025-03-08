package gui.pencilpi.commons;

public final class Sizes {
    // Calculator sizes
    public final static class Calculator {
        public final static int BUTTONS_PER_ROW = 4;

        public final static int PADDING = 10;

        public final static int MAIN_GAP = 15;

        public final static int BUTTONS_GAP = 5;

        public final static int RESULT_WIDTH = BUTTONS_PER_ROW * Button.OPERATOR_WIDTH + (BUTTONS_PER_ROW - 1) * BUTTONS_GAP;
        public final static int HISTORY_WIDTH = RESULT_WIDTH;

        private Calculator() {
        }
    }

    // Stage sizes
    public final static class Stage {
        public final static int MAIN_WIDTH = 800;
        public final static int MAIN_HEIGHT = 600;

        //public final static int CALCULATOR_WIDTH = 400;
        //public final static int CALCULATOR_HEIGHT = 600;

        private Stage() {
        }
    }

    // Button sizes
    public static final class Button {
        public final static int BIG_WIDTH = 400;
        public final static int BIG_HEIGHT = 30;

        public final static int MEDIUM_WIDTH = 200;
        public final static int MEDIUM_HEIGHT = 30;

        public final static int SMALL_WIDTH = 100;
        public final static int SMALL_HEIGHT = 30;

        public final static int OPERATOR_WIDTH = 50;
        public final static int OPERATOR_HEIGHT = 40;

        public final static int DELETE_WIDTH = OPERATOR_WIDTH * 2 + Calculator.BUTTONS_GAP;
        public final static int DELETE_HEIGHT = 35;

        public final static int BORDER_RADIUS = 10;

        private Button() {
        }
    }

    // Font sizes
    public static final class Font {
        public final static int MENU = 18;
        public final static int MENU_ITEM = 16;

        public final static int TEXT = 16;

        public final static int CALCULATOR_HISTORY = 16;

        public final static int CALCULATOR_RESULT = 24;

        public final static int CALCULATOR_BUTTONS = 16;

        private Font() {
        }
    }

    // Text area sizes
    public static final class TextArea {

        public final static int PADDING = 10;

        private TextArea() {
        }

    }

    // Footer sizes
    public static final class Footer {
        public final static int PADDING_VERTICAL = 5;
        public final static int PADDING_HORIZONTAL = 15;

        private Footer() {
        }
    }

    private Sizes() {
    }
}
