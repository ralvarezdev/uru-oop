package gui.setters;

import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.InputStream;

public final class StageSetter {
    public static void setWindowIcon(Stage stage, InputStream icon) throws IOException {
        stage.getIcons().add(new Image(icon));
    }

    public static double getDiffWidth(Stage stage, Scene scene) {
        return stage.getWidth() - scene.getWidth();
    }

    public static double getDiffHeight(Stage stage, Scene scene) {
        return stage.getHeight() - scene.getHeight();
    }

    public static void setDiffMinSize(Stage stage, Scene scene, double minWidth, double MinHeight) {
        double diffWidth = getDiffWidth(stage, scene);
        double diffHeight = getDiffHeight(stage, scene);

        stage.setMinWidth(minWidth + diffWidth);
        stage.setMinHeight(MinHeight + diffHeight);
    }
}
