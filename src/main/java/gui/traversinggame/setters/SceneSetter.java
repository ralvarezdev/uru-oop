package gui.traversinggame.setters;

import javafx.scene.Scene;
import files.ResourceGetter;
import gui.traversinggame.commons.styles.Styles;

import java.io.IOException;

public final class SceneSetter {
    public static void setDefaultStyles(ResourceGetter stylesResourceGetter, Scene scene) throws IOException {
        String fontStyle = stylesResourceGetter.getResourceToExternalForm(Styles.FONT);
        String generalStyle = stylesResourceGetter.getResourceToExternalForm(Styles.GENERAL);

        for (String style : new String[]{fontStyle, generalStyle})
            scene.getStylesheets().add(style);
    }
}
