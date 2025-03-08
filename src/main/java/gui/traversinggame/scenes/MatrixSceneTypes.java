package gui.traversinggame.scenes;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import files.ResourceGetter;
import gui.setters.CommonNodes;
import gui.setters.LayoutSetter;
import gui.setters.NodeSetter;
import gui.traversinggame.commons.Colors;
import gui.traversinggame.commons.Coords;
import gui.traversinggame.commons.KeyDirections;
import gui.traversinggame.commons.Sizes;
import gui.traversinggame.commons.assets.Assets;
import gui.traversinggame.setters.SceneSetter;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Objects;

public enum MatrixSceneTypes {
    NOVICE(5, 5, "NOVICE"), NORMAL(7, 10, "NORMAL"), ADVANCED(10, 20, "ADVANCED"), EXPERT(15, 30, "EXPERT");

    // Matrix scene name
    public final String name;

    // Matrix scene rows and columns
    public final int rows;
    public final int cols;

    // Matrix scene minimum width and height
    public final double minWidth;
    public final double minHeight;

    // Current key direction
    private static KeyDirections currKeyDir;
    private static Integer currKeyDirCounter;

    // Player and glasses coordinates
    private static HashMap<Coords, Integer> playerCoords;
    private static HashMap<Coords, Double> playerCoordsPx;

    private static HashMap<Coords, Integer> glassesCoords;
    private static HashMap<Coords, Double> glassesCoordsPx;

    MatrixSceneTypes(int rows, int cols, String name) {
        this.rows = rows;
        this.cols = cols;
        this.name = name;
        this.minWidth = getWidth(cols) + 2 * Sizes.Matrix.PADDING;
        ;
        this.minHeight = getHeight(rows) + 2 * Sizes.Matrix.PADDING;
        ;
    }

    private double getWidth(int cols) {
        return cols * Sizes.Cell.WIDTH + (cols - 1) * Sizes.Cell.PADDING;
    }

    private double getHeight(int rows) {
        return rows * Sizes.Cell.HEIGHT + (rows - 1) * Sizes.Cell.PADDING;
    }

    private Integer getMatrixOppositeQuarter(Coords.Quarter quarter) {
        return (quarter.getQuarter() + 2) % 4;
    }

    private HashMap<Coords, Integer> getMatrixQuarterCornerCell(Coords.Quarter quarter) {
        HashMap<Coords, Integer> coordsMap = new HashMap<>();

        switch (quarter) {
            case TOP_RIGHT -> {
                coordsMap.put(Coords.X, cols - 1);
                coordsMap.put(Coords.Y, 0);
            }
            case TOP_LEFT -> {
                coordsMap.put(Coords.X, 0);
                coordsMap.put(Coords.Y, 0);
            }
            case BOTTOM_LEFT -> {
                coordsMap.put(Coords.X, 0);
                coordsMap.put(Coords.Y, rows - 1);
            }
            case BOTTOM_RIGHT -> {
                coordsMap.put(Coords.X, cols - 1);
                coordsMap.put(Coords.Y, rows - 1);
            }
        }

        return coordsMap;
    }

    private HashMap<Coords, Double> getCoordsPx(HashMap<Coords, Integer> coords) {
        HashMap<Coords, Double> coordsPx = new HashMap<>();

        // Get cell coordinates
        coordsPx.put(Coords.X, getWidth(coords.get(Coords.X)));
        coordsPx.put(Coords.Y, getHeight(coords.get(Coords.Y)));

        return coordsPx;
    }

    private void getPlayerCoordsPx() {
        playerCoordsPx = getCoordsPx(playerCoords);
    }

    private void getGlassesCoordsPx() {
        glassesCoordsPx = getCoordsPx(glassesCoords);
    }

    private double getCellTranslateX(HashMap<Coords, Double> coordsPx) {
        return coordsPx.get(Coords.X) - minWidth / 2 + Sizes.Cell.WIDTH - Sizes.Cell.PADDING;
    }

    private double getCellTranslateY(HashMap<Coords, Double> coordsPx) {
        return coordsPx.get(Coords.Y) - minHeight / 2 + Sizes.Cell.HEIGHT - Sizes.Cell.PADDING;
    }

    public Scene getScene(ResourceGetter assetsResourceGetter, ResourceGetter stylesResourceGetter) throws IOException {
        // Reset values
        currKeyDir = null;
        currKeyDirCounter = 0;

        // Tale Pane which contains cells
        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setPadding(CommonNodes.getInset(Sizes.Matrix.PADDING));
        gridPane.setVgap(Sizes.Cell.PADDING);
        gridPane.setHgap(Sizes.Cell.PADDING);

        // Player image
        InputStream playerAsset = assetsResourceGetter.getResourceAsStream(Assets.Image.PLAYER);
        Image playerImage = new Image(playerAsset);
        ImageView playerImageView = new ImageView(playerImage);

        playerImageView.setFitWidth(Sizes.Cell.WIDTH);
        playerImageView.setFitHeight(Sizes.Cell.HEIGHT);

        // Add cells to grid pane
        for (int i = 0; i < rows; i++)
            for (int j = 0; j < cols; j++) {
                Rectangle rectCell = new Rectangle();
                rectCell.setWidth(Sizes.Cell.WIDTH);
                rectCell.setHeight(Sizes.Cell.HEIGHT);

                NodeSetter.setRectStyle(rectCell, Sizes.BorderRadius.MEDIUM, Colors.Dark.NODE_BG);
                gridPane.add(rectCell, j, i);
            }

        // Main container
        playerCoords = getMatrixQuarterCornerCell(Coords.Quarter.INITIAL_PLAYER);
        getPlayerCoordsPx();

        playerImageView.setTranslateX(getCellTranslateX(playerCoordsPx));
        playerImageView.setTranslateY(getCellTranslateY(playerCoordsPx));

        // Grid pane container
        StackPane stackPane = new StackPane(gridPane, playerImageView);
        stackPane.setMinSize(minWidth, minHeight);

        LayoutSetter.setBgColor(stackPane, Colors.Dark.STAGE_BG);

        // Glasses cell
        glassesCoords = getMatrixQuarterCornerCell(Coords.Quarter.INITIAL_GLASSES);
        getGlassesCoordsPx();

        // Game loop
        // var gameLoopExecutor = Executors.newSingleThreadExecutor();

        // Scene
        Scene scene = new Scene(stackPane);

        SceneSetter.setDefaultStyles(stylesResourceGetter, scene);

        // Add player image events
        scene.setOnKeyPressed(event -> {
            KeyCode eventCode = event.getCode();
            KeyDirections eventKeyDir = KeyDirections.getKeyDirection(eventCode);

            if (eventKeyDir == null) {
                System.err.println("UNEXPECTED VALUE: " + currKeyDir);
                return;
            }
            System.out.println("RECEIVED MOVE: " + eventKeyDir);

            if (currKeyDir != eventKeyDir) {
                currKeyDir = eventKeyDir;
                currKeyDirCounter = 1;

            } else if (currKeyDirCounter >= KeyDirections.MAX_DIR) {
                System.err.println("INVALID MOVE: Too many " + eventKeyDir.getDirection() + " movements.");
                currKeyDirCounter++;
                return;

            } else
                currKeyDirCounter++;

            // Get player coordinates
            final int x = playerCoords.get(Coords.X);
            final int y = playerCoords.get(Coords.Y);

            switch (currKeyDir) {
                case MOVE_UP -> {
                    if (y > 0)
                        playerCoords.put(Coords.Y, y - 1);
                }
                case MOVE_RIGHT -> {
                    if (x < cols - 1)
                        playerCoords.put(Coords.X, x + 1);
                }
                case MOVE_DOWN -> {
                    if (y < rows - 1)
                        playerCoords.put(Coords.Y, y + 1);
                }
                case MOVE_LEFT -> {
                    if (x > 0)
                        playerCoords.put(Coords.X, x - 1);
                }
            }

            // Update player coordinates
            getPlayerCoordsPx();
            playerImageView.setTranslateX(getCellTranslateX(playerCoordsPx));
            playerImageView.setTranslateY(getCellTranslateY(playerCoordsPx));

            // Check if player reached glasses
            boolean sameRow = Objects.equals(playerCoords.get(Coords.X), glassesCoords.get(Coords.X));
            boolean sameCol = Objects.equals(playerCoords.get(Coords.Y), glassesCoords.get(Coords.Y));

            if (sameRow && sameCol)
                System.out.println("GLASSES REACHED. YOU WINNED");
        });

        return scene;
    }
}
