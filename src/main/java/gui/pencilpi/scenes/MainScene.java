package gui.pencilpi.scenes;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import files.DefaultFileReader;
import files.DefaultFileWriter;
import files.ResourceGetter;
import gui.pencilpi.commons.CaretPosition;
import gui.pencilpi.commons.Colors;
import gui.pencilpi.commons.Sizes;
import gui.pencilpi.commons.assets.Assets;
import gui.setters.CommonNodes;
import gui.setters.LayoutSetter;
import gui.setters.NodeSetter;
import gui.setters.StageSetter;
import gui.traversinggame.setters.SceneSetter;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class MainScene {
    private static File currentFile = null;
    private static CaretPosition caretPosition;

    public static void setCurrentFile(File file) {
        currentFile = file;
    }

    public static void updateCurrentPositionLabel(Label currentPositionLabel, int line, int column) {
        currentPositionLabel.setText("%d:%d".formatted(line, column));
    }

    public static void updateCurrentPositionLabel(Label currentPositionLabel) {
        updateCurrentPositionLabel(currentPositionLabel, caretPosition.currentLine(), caretPosition.currentColumn());
    }

    public static void updateCharactersLabel(Label charactersLabel, int numberCharacters) {
        charactersLabel.setText("Characters: %d".formatted(numberCharacters));
    }

    public static void updateCharactersLabel(Label charactersLabel) {
        updateCharactersLabel(charactersLabel, caretPosition.currentCharacters());
    }

    public static void updateCaretPosition(TextArea textArea, Label currentPositionLabel, Label charactersLabel) {
        caretPosition = new CaretPosition(textArea);

        // Update position current label
        updateCurrentPositionLabel(currentPositionLabel);

        // Update characters label
        updateCharactersLabel(charactersLabel);
    }

    public static Scene getScene(ResourceGetter assetsResourceGetter, ResourceGetter stylesResourceGetter) throws IOException {
        // Border pane
        BorderPane borderPane = new BorderPane();

        // Menu bar
        MenuBar menuBar = new MenuBar();
        NodeSetter.setMenuBarStyle(menuBar, Colors.Dark.MENU_BG);
        borderPane.setTop(menuBar);

        // Menu file
        Menu fileMenu = new Menu("");
        Label fileLabel = new Label("File");
        fileMenu.setGraphic(fileLabel);

        // Menu modes
        Menu modesMenu = new Menu("");
        Label modesLabel = new Label("Modes");
        modesMenu.setGraphic(modesLabel);

        // Set menu styles
        for (Menu menu : new Menu[]{fileMenu, modesMenu})
            NodeSetter.setMenuStyle(menu, Colors.Dark.MENU_BG);

        // Set menu label styles
        for (Label label : new Label[]{fileLabel, modesLabel})
            NodeSetter.setLabelStyle(label, Sizes.Font.MENU, Colors.Dark.FONT, Colors.Dark.MENU_BG);

        // Set menu
        menuBar.getMenus().addAll(fileMenu, modesMenu);

        // Menu items
        MenuItem newMenuItem = new MenuItem("New");
        MenuItem openMenuItem = new MenuItem("Open");
        MenuItem saveMenuItem = new MenuItem("Save");
        MenuItem saveAsMenuItem = new MenuItem("Save As");

        // Menu modes items
        MenuItem calculatorMenuItem = new MenuItem("Calculator");

        // Set menu file items
        fileMenu.getItems().addAll(newMenuItem, openMenuItem, saveMenuItem, saveAsMenuItem);

        // Set menu modes items
        modesMenu.getItems().addAll(calculatorMenuItem);

        // Set menu item styles
        // I couldn't get the styling to work properly
        /*
        for(MenuItem menuItem:new MenuItem[]{newMenuItem, openMenuItem, saveMenuItem, saveAsMenuItem, calculatorMenuItem})
            NodeSetter.setMenuItemStyle(menuItem, Sizes.Font.MENU_ITEM, Colors.Dark.FONT, Colors.Dark.MENU_BG)
         */

        // Menu items actions
        calculatorMenuItem.setOnAction(_ -> {
            try {
                // Create a new stage
                Stage stage = new Stage();
                Scene scene = CalculatorScene.getScene(stylesResourceGetter);

                // Set window icon
                InputStream icon = assetsResourceGetter.getResourceAsStream(Assets.Image.WIN);
                StageSetter.setWindowIcon(stage, icon);

                // Set the stage
                stage.setTitle("Calculator");
                stage.setScene(scene);
                stage.setResizable(false);

                stage.show();

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        // Text area border pane
        BorderPane textAreaBorderPane = new BorderPane();
        LayoutSetter.setBgColor(textAreaBorderPane, Colors.Dark.STAGE_BG);
        textAreaBorderPane.setPadding(CommonNodes.getInset(Sizes.TextArea.PADDING));
        borderPane.setCenter(textAreaBorderPane);

        // Text area
        TextArea textArea = new TextArea();
        NodeSetter.setTextAreaStyle(textArea, Sizes.Font.TEXT, Colors.Dark.FONT, Colors.Dark.TEXT_AREA_BG);
        textAreaBorderPane.setCenter(textArea);

        // Footer border pane
        BorderPane footerBorderPane = new BorderPane();
        footerBorderPane.setPadding(CommonNodes.getInset(Sizes.Footer.PADDING_VERTICAL, Sizes.Footer.PADDING_HORIZONTAL));
        LayoutSetter.setBgColor(footerBorderPane, Colors.Dark.LABEL_BG);

        // Set footer border pane
        borderPane.setBottom(footerBorderPane);

        // Data labels HBox
        HBox dataLabelsHBox = new HBox();
        dataLabelsHBox.setSpacing(Sizes.Footer.PADDING_HORIZONTAL);

        // Set data labels HBox
        footerBorderPane.setLeft(dataLabelsHBox);

        // Data labels
        Label currentPositionLabel = new Label();
        Label charactersLabel = new Label();

        // Update caret position
        updateCaretPosition(textArea, currentPositionLabel, charactersLabel);

        // Set data labels
        dataLabelsHBox.getChildren().addAll(currentPositionLabel, charactersLabel);

        // Made by label
        Label madeByLabel = new Label("Made by: ralvarezdev");

        // Set made by label
        footerBorderPane.setRight(madeByLabel);

        // Set label styles
        for (Label label : new Label[]{currentPositionLabel, charactersLabel, madeByLabel})
            NodeSetter.setLabelStyle(label, Sizes.Font.TEXT, Colors.Dark.FONT);

        // Create open file chooser
        FileChooser openFileChooser = new FileChooser();
        openFileChooser.setTitle("Open File");

        // Create save file chooser
        FileChooser saveFileChooser = new FileChooser();
        saveFileChooser.setTitle("Save");
        saveFileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("All Files", "*.*"));

        // Create file reader and file writer
        DefaultFileReader fileReader = new DefaultFileReader();
        DefaultFileWriter fileWriter = new DefaultFileWriter();

        // Set new menu item action
        newMenuItem.setOnAction(_ -> {
            textArea.clear();

            // Update caret position
            updateCaretPosition(textArea, currentPositionLabel, charactersLabel);
        });

        // Set open menu item action
        openMenuItem.setOnAction(_ -> {
            // Show open dialog
            File file = openFileChooser.showOpenDialog(new Stage());

            // Update current file
            setCurrentFile(file);

            try {
                StringBuilder content = fileReader.getFileContent(file);
                textArea.setText(content.toString());

            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            // Update caret position
            updateCaretPosition(textArea, currentPositionLabel, charactersLabel);
        });

        // Write file lambda expression
        Runnable writeToFile = () -> {
            try {
                fileWriter.writeFileContent(currentFile, textArea.getText(), false);

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        };

        // Set save as menu item action
        saveAsMenuItem.setOnAction(_ -> {
            // Show save dialog
            File file = saveFileChooser.showSaveDialog(new Stage());

            // Update current file
            setCurrentFile(file);
            writeToFile.run();
        });

        // Set save menu item action
        saveMenuItem.setOnAction(_ -> {
            if (currentFile == null) {
                saveAsMenuItem.fire();
                return;
            }

            writeToFile.run();
        });

        // Update data labels based on text area content
        textArea.setOnKeyPressed(ev -> {
            KeyCode code = ev.getCode();

            // Update caret position
            caretPosition.update();

            // Update last key code
            caretPosition.setCurrentKeyCode(code);

            // Debug
            System.out.println(caretPosition);

            switch (code) {
                case LEFT:
                    if (caretPosition.oldPrevious().isEmpty())
                        break;

                    if (!caretPosition.oldPrevious().equals("\n"))
                        caretPosition.decreaseCurrentColumn();

                    else {
                        caretPosition.decreaseCurrentLine();
                        caretPosition.setCurrentColumn(caretPosition.currentLineLength());
                    }
                    break;

                case RIGHT:
                    if (caretPosition.oldNext().isEmpty())
                        break;

                    if (!caretPosition.oldNext().equals("\n"))
                        caretPosition.increaseCurrentColumn();

                    else {
                        caretPosition.increaseCurrentLine();
                        caretPosition.setCurrentColumn(1);
                    }
                    break;

                case ENTER:
                    caretPosition.moveDown();
                    caretPosition.setCurrentColumn(1);
                    caretPosition.increaseCurrentCharacters();
                    break;

                case DOWN:
                    if (caretPosition.currentLine() < caretPosition.currentLines()) {
                        caretPosition.increaseCurrentLine();
                        caretPosition.move();
                    }
                    break;

                case UP:
                    if (caretPosition.currentLine() > 1) {
                        caretPosition.decreaseCurrentLine();
                        caretPosition.move();
                    }
                    break;

                case BACK_SPACE:
                    if (caretPosition.oldPrevious().isEmpty())
                        break;

                    if (!caretPosition.oldPrevious().equals("\n")) {
                        caretPosition.decreaseCurrentLineLength();
                        caretPosition.decreaseCurrentColumn();
                    } else {
                        caretPosition.decreaseCurrentLine();
                        caretPosition.setCurrentColumn(caretPosition.currentLineLength());
                        caretPosition.moveUp();
                    }

                    caretPosition.decreaseCurrentCharacters();
                    break;

                case DELETE:
                    if (caretPosition.oldNext().isEmpty())
                        break;

                    if (caretPosition.oldNext().equals("\n"))
                        caretPosition.moveUp();

                    else
                        caretPosition.decreaseCurrentLineLength();

                    caretPosition.decreaseCurrentCharacters();
                    break;

                default:
                    if (code.isFunctionKey() || code.isMediaKey() || code.isModifierKey())
                        return;

                    caretPosition.increaseCurrentColumn();
                    caretPosition.increaseCurrentLineLength();
                    caretPosition.increaseCurrentCharacters();
            }

            // Update data labels
            updateCurrentPositionLabel(currentPositionLabel);
            updateCharactersLabel(charactersLabel);
        });

        // Scene
        Scene scene = new Scene(borderPane);

        // Set scene
        SceneSetter.setDefaultStyles(stylesResourceGetter, scene);

        return scene;
    }
}
