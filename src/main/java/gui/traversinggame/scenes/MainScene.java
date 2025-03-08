package gui.traversinggame.scenes;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Pair;
import files.ResourceGetter;
import gui.setters.CommonNodes;
import gui.setters.LayoutSetter;
import gui.setters.NodeSetter;
import gui.setters.StageSetter;
import gui.traversinggame.commons.Colors;
import gui.traversinggame.commons.Sizes;
import gui.traversinggame.commons.Texts;
import gui.traversinggame.commons.assets.Assets;
import gui.traversinggame.setters.SceneSetter;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.LinkedList;

public final class MainScene {
    // Main image
    private final static int MAIN_IMAGE_HEIGHT = 200;

    private static <T extends Event> EventHandler<T> getOnMatrixSceneClicked(ResourceGetter assetsResourceGetter, ResourceGetter stylesResourceGetter, Stage mainStage, MatrixSceneTypes matrixType) {
        return (_) -> {
            try {
                if (matrixType == null)
                    return;

                // Create matrix stage
                Stage matrixStage = new Stage();

                // Set matrix stage title and icon
                matrixStage.setTitle(matrixType.name);
                InputStream icon = assetsResourceGetter.getResourceAsStream(Assets.Image.WIN);
                StageSetter.setWindowIcon(matrixStage, icon);

                // Set matrix scene
                Scene matrixScene = matrixType.getScene(assetsResourceGetter, stylesResourceGetter);
                matrixStage.setScene(matrixScene);

                matrixStage.setOnCloseRequest(_ -> mainStage.show());

                // Change focus stage
                mainStage.hide();
                matrixStage.show();

                // Set matrix stage minimum size
                StageSetter.setDiffMinSize(matrixStage, matrixScene, matrixType.minWidth, matrixType.minHeight);

            } catch (IOException e) {
                System.err.println(e);
                System.exit(-1);
            }
        };
    }

    public static Scene getScene(ResourceGetter assetsResourceGetter, ResourceGetter stylesResourceGetter, Stage stage) throws IOException {
        // Title label
        Label titleLabel = new Label(Texts.TITLE);
        NodeSetter.setLabelStyle(titleLabel, Sizes.Font.BIG, Colors.Dark.FONT);

        // Main image
        Image mainImage = new Image(assetsResourceGetter.getResourceAsStream(Assets.Image.MAIN));
        ImageView mainImageView = new ImageView(mainImage);
        mainImageView.setFitHeight(MAIN_IMAGE_HEIGHT);
        mainImageView.setPreserveRatio(true);

        // Buttons
        Button noviceMode = new Button(MatrixSceneTypes.NOVICE.name);
        Button normalMode = new Button(MatrixSceneTypes.NORMAL.name);
        Button advancedMode = new Button(MatrixSceneTypes.ADVANCED.name);
        Button expertMode = new Button(MatrixSceneTypes.EXPERT.name);
        Button[] btns = new Button[]{noviceMode, normalMode, advancedMode, expertMode};

        // Vertical box aligned at the center
        VBox centerVBox = new VBox(mainImageView, titleLabel, noviceMode, normalMode, advancedMode, expertMode);
        centerVBox.setAlignment(Pos.CENTER);

        // Add title margin
        VBox.setMargin(titleLabel, CommonNodes.getBottomInset(Sizes.Margin.TITLE_BOTTOM));

        // Add buttons style, size and margin
        for (Button btn : btns) {
            String btnStyle = NodeSetter.getBtnStyle(Sizes.Font.MEDIUM, Colors.Dark.FONT, Sizes.BorderRadius.BIG,
                    Colors.Dark.NODE_BG);
            String btnFocusStyle = NodeSetter.getBtnStyle(Sizes.Font.MEDIUM, Colors.Dark.FONT_HOVER, Sizes.BorderRadius.BIG,
                    Colors.Dark.NODE_HOVER_BG);

            btn.setStyle(btnStyle);
            btn.setPrefSize(Sizes.Button.BIG_WIDTH, Sizes.Button.BIG_HEIGHT);
            VBox.setMargin(btn, CommonNodes.getBottomInset(Sizes.Margin.BTN_BOTTOM));

            // Set buttons on mouse entered and exited events
            btn.setOnMouseExited(_ -> btn.setStyle(btnStyle));
            btn.setOnMouseEntered(_ -> btn.setStyle(btnFocusStyle));
        }

        // Footer label
        Label footerLabel = new Label(Texts.FOOTER);
        NodeSetter.setLabelStyle(footerLabel, Sizes.Font.MEDIUM, Colors.Dark.FONT);

        // Horizontal box aligned at the center
        HBox bottomHBox = new HBox(footerLabel);
        bottomHBox.setAlignment(Pos.CENTER_RIGHT);

        // Add footer label margin
        HBox.setMargin(footerLabel, CommonNodes.getInset(0, Sizes.Margin.FOOTER_RIGHT, Sizes.Margin.FOOTER_BOTTOM, 0));

        // Main container
        BorderPane borderPane = new BorderPane();
        borderPane.setPrefSize(Sizes.Stage.MAX_WIDTH, Sizes.Stage.MAX_HEIGHT);
        borderPane.setCenter(centerVBox);
        borderPane.setBottom(bottomHBox);

        LayoutSetter.setBgColor(borderPane, Colors.Dark.STAGE_BG);

        // Scene
        Scene scene = new Scene(borderPane);

        SceneSetter.setDefaultStyles(stylesResourceGetter, scene);

        // Set buttons on mouse clicked events
        LinkedList<Pair<Button, MatrixSceneTypes>> btnsMatrixTypes = new LinkedList<>(Arrays.asList(
                new Pair<>(noviceMode, MatrixSceneTypes.NOVICE),
                new Pair<>(normalMode, MatrixSceneTypes.NORMAL),
                new Pair<>(advancedMode, MatrixSceneTypes.ADVANCED),
                new Pair<>(expertMode, MatrixSceneTypes.EXPERT)
        ));

        for (Pair<Button, MatrixSceneTypes> btnMatrixType : btnsMatrixTypes) {
            Button btn = btnMatrixType.getKey();
            MatrixSceneTypes matrixType = btnMatrixType.getValue();
            btn.setOnMouseClicked(getOnMatrixSceneClicked(assetsResourceGetter, stylesResourceGetter, stage, matrixType));
        }

        return scene;
    }
}
