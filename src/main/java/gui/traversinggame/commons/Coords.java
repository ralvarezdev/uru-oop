package gui.traversinggame.commons;

public enum Coords {
    X, Y;

    // Quarters
    public enum Quarter {
        TOP_LEFT(0), TOP_RIGHT(1), BOTTOM_LEFT(2), BOTTOM_RIGHT(3);

        private final int quarter;

        Quarter(int quarter) {
            this.quarter = quarter;
        }

        public int getQuarter() {
            return this.quarter;
        }

        // Initial quarter
        public static final Quarter INITIAL_PLAYER = TOP_LEFT;
        public static final Quarter INITIAL_GLASSES = BOTTOM_RIGHT;
    }
}
