package gui.pencilpi;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import files.DefaultResourceGetter;
import gui.pencilpi.commons.Sizes;
import gui.pencilpi.commons.Texts;
import gui.pencilpi.commons.assets.Assets;
import gui.pencilpi.commons.styles.Styles;
import gui.pencilpi.scenes.MainScene;
import gui.setters.StageSetter;

import java.io.InputStream;

public class PencilPi extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        // Load main scene
        try {
            // Resource getters
            DefaultResourceGetter assetsResourceGetter = new DefaultResourceGetter(Assets.class);
            DefaultResourceGetter stylesResourceGetter = new DefaultResourceGetter(Styles.class);

            // Set window icon
            InputStream icon = assetsResourceGetter.getResourceAsStream(Assets.Image.WIN);
            StageSetter.setWindowIcon(stage, icon);

            // Get the main scene
            Scene scene = MainScene.getScene(assetsResourceGetter, stylesResourceGetter);

            // Set the stage
            stage.setTitle(Texts.TITLE);
            stage.setScene(scene);

            // Set minimum size of the stage
            stage.setMinHeight(Sizes.Stage.MAIN_HEIGHT);
            stage.setMinWidth(Sizes.Stage.MAIN_WIDTH);
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }

    }

    public static void main(String[] args) {
        launch(args);
    }
}
