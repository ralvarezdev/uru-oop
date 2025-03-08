package gui.setters;

import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import gui.commons.ColorPalette;

public final class NodeSetter {
    private static String getFontStyle(int fontSize, ColorPalette textColor) {
        if (fontSize <= 0 || textColor == null)
            return "";

        return """
                -fx-font-size: %1$dpx;
                -fx-text-fill: %2$s;
                """.formatted(fontSize, textColor.getRGBA());
    }

    private static String getBorderRadiusStyle(int borderRadiusSize) {
        if (borderRadiusSize <= 0)
            return "";

        return """
                -fx-background-radius: %dpx;
                -fx-border-radius: %s;
                """.formatted(borderRadiusSize, borderRadiusSize);
    }

    private static String getBgColorStyle(ColorPalette color) {
        if (color == null)
            return "";

        return """
                -fx-background-color: %s;
                """.formatted(color.getRGBA());
    }

    private static String getTextAreaBgColorStyle(ColorPalette color) {
        if (color == null)
            return "";

        return """
                -fx-background-color: %1$s;
                -fx-control-inner-background: %1$s;
                -fx-faint-focus-color: transparent;
                """.formatted(color.getRGBA());
    }

    private static String getBorderStyle(String color, int width) {
        if (width < 0 || color == null || color.isEmpty())
            return "";

        return """
                -fx-border-color: %s;
                -fx-border-width: %d;
                """.formatted(color, width);
    }

    private static String getBorderStyle(ColorPalette color, int width) {
        return getBorderStyle(color.getRGBA(), width);
    }

    public static String getTransparentBorderColorStyle() {
        return getBorderStyle("transparent", 0);
    }

    public static String getLabelStyle(int fontSize, ColorPalette textColor, ColorPalette bgColor) {
        String fontStyle = getFontStyle(fontSize, textColor);
        String bgStyle = getBgColorStyle(bgColor);

        return "%s%s".formatted(fontStyle, bgStyle);
    }

    public static String getMenuBarStyle(ColorPalette bgColor) {
        return getBgColorStyle(bgColor);
    }

    public static String getMenuStyle(ColorPalette bgColor) {
        return getBgColorStyle(bgColor);
    }

    public static String getMenuItemStyle(int fontSize, ColorPalette textColor, ColorPalette bgColor) {
        String fontStyle = getFontStyle(fontSize, textColor);
        String bgStyle = getBgColorStyle(bgColor);

        return "%s%s".formatted(fontStyle, bgStyle);
    }

    public static String getBtnStyle(int fontSize, ColorPalette textColor, int borderRadiusSize,
                                     ColorPalette btnColor) {
        String bgStyle = getBgColorStyle(btnColor);
        String fontStyle = getFontStyle(fontSize, textColor);
        String borderRadiusStyle = getBorderRadiusStyle(borderRadiusSize);

        return "%s%s%s".formatted(bgStyle, fontStyle, borderRadiusStyle);
    }

    public static String getTextAreaStyle(int fontSize, ColorPalette textColor, ColorPalette bgColor) {
        String fontStyle = getFontStyle(fontSize, textColor);
        String bgStyle = getTextAreaBgColorStyle(bgColor);

        return "%s%s".formatted(bgStyle, fontStyle);
    }

    public static void setMenuBarStyle(MenuBar menuBar, ColorPalette bgColor) {
        String menuBarStyle = getMenuBarStyle(bgColor);

        if (!menuBarStyle.isEmpty())
            menuBar.setStyle(menuBarStyle);
    }

    public static void setMenuStyle(Menu menu, ColorPalette bgColor) {
        String menuStyle = getMenuStyle(bgColor);

        if (!menuStyle.isEmpty())
            menu.setStyle(menuStyle);
    }

    public static void setMenuItemStyle(MenuItem menuItem, int fontSize, ColorPalette textColor, ColorPalette bgColor) {
        String menuItemStyle = getMenuItemStyle(fontSize, textColor, bgColor);

        if (!menuItemStyle.isEmpty())
            menuItem.setStyle(menuItemStyle);
    }

    public static void setLabelStyle(Label label, int fontSize, ColorPalette textColor, ColorPalette bgColor) {
        String fontStyle = getLabelStyle(fontSize, textColor, bgColor);

        if (!fontStyle.isEmpty())
            label.setStyle(fontStyle);
    }

    public static void setLabelStyle(Label label, int fontSize, ColorPalette textColor) {
        NodeSetter.setLabelStyle(label, fontSize, textColor, null);
    }

    public static void setBtnStyle(Button btn, int fontSize, ColorPalette textColor, int borderRadiusSize,
                                   ColorPalette btnColor) {
        String btnStyle = getBtnStyle(fontSize, textColor, borderRadiusSize, btnColor);

        if (!btnStyle.isEmpty())
            btn.setStyle(btnStyle);
    }

    public static void setRectStyle(Rectangle rect, int rectBorderRadiusSize, Color rectColor) {
        rect.setArcHeight(rectBorderRadiusSize);
        rect.setArcWidth(rectBorderRadiusSize);

        if (rectColor != null)
            rect.setFill(rectColor);
    }

    public static void setRectStyle(Rectangle rect, int rectBorderRadiusSize, ColorPalette rectColor) {
        NodeSetter.setRectStyle(rect, rectBorderRadiusSize, rectColor.getColor());
    }

    public static void setTextAreaStyle(TextArea textArea, int fontSize, ColorPalette textColor, ColorPalette bgColor) {
        String textAreaStyle = getTextAreaStyle(fontSize, textColor, bgColor);

        if (!textAreaStyle.isEmpty())
            textArea.setStyle(textAreaStyle);
    }
}
