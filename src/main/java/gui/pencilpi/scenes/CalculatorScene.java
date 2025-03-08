package gui.pencilpi.scenes;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import files.ResourceGetter;
import gui.commons.ColorPalette;
import gui.pencilpi.commons.Colors;
import gui.pencilpi.commons.Sizes;
import gui.pencilpi.setters.SceneSetter;
import gui.setters.CommonNodes;
import gui.setters.LayoutSetter;
import gui.setters.NodeSetter;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.function.Consumer;
import java.util.function.Function;

public class CalculatorScene {
    private static double currentResult;

    private static void updateCurrentResult(double result) {
        currentResult = result;
    }

    public static Scene getScene(ResourceGetter stylesResourceGetter) throws IOException {
        // Update current result
        updateCurrentResult(0);

        // Number and operators stacks
        final String error = "ERROR";
        LinkedList<String> historyStack = new LinkedList<>();
        LinkedList<String> resultStack = new LinkedList<>();
        LinkedList<Double> numbersStack = new LinkedList<>();
        LinkedList<String> operatorsStack = new LinkedList<>();
        LinkedList<String> operatorsPostfixStack = new LinkedList<>();

        // Grid pane
        GridPane gridPane = new GridPane();
        gridPane.setPadding(CommonNodes.getInset(Sizes.Calculator.PADDING));
        gridPane.setVgap(Sizes.Calculator.MAIN_GAP);
        gridPane.setHgap(Sizes.Calculator.MAIN_GAP);

        // Set grid pane
        LayoutSetter.setBgColor(gridPane, Colors.Dark.STAGE_BG);

        // Buttons container
        GridPane buttonsGridPane = new GridPane();
        buttonsGridPane.setHgap(Sizes.Calculator.BUTTONS_GAP);
        buttonsGridPane.setVgap(Sizes.Calculator.BUTTONS_GAP);
        gridPane.add(buttonsGridPane, 0, 2);

        // Delete and CE buttons
        Button deleteButton = new Button();
        deleteButton.setText("←");

        Button ceButton = new Button();
        ceButton.setText("CE");

        // Set delete and CE buttons
        buttonsGridPane.add(deleteButton, 0, 0, 2, 1);
        buttonsGridPane.add(ceButton, 2, 0, 2, 1);

        // Set delete and CE buttons style and size
        for (Button button : new Button[]{deleteButton, ceButton}) {
            NodeSetter.setBtnStyle(button, Sizes.Font.CALCULATOR_BUTTONS, Colors.Dark.FONT, Sizes.Button.BORDER_RADIUS, Colors.Dark.BUTTON_DELETE_BG);
            button.setMinWidth(Sizes.Button.DELETE_WIDTH);
            button.setMinHeight(Sizes.Button.DELETE_HEIGHT);
        }

        // Buttons text
        String[] buttonsText = new String[]{
                "7", "8", "9", "/",
                "4", "5", "6", "*",
                "1", "2", "3", "-",
                "0", ".", "=", "+"
        };
        String buttonText;

        // Buttons map
        HashMap<String, Button> buttonsMap = new HashMap<>();

        // Buttons lists
        LinkedList<Button> numberButtons = new LinkedList<>();
        LinkedList<Button> operatorButtons = new LinkedList<>();

        // Set buttons
        for (int i = 0; i < buttonsText.length; i++) {
            // Button
            buttonText = buttonsText[i];
            Button button = new Button(buttonText);
            boolean isNumber = buttonText.matches("[0-9]|\\.");

            // Set buttons style
            ColorPalette bgColor = isNumber ? Colors.Dark.BUTTON_NUMBER_BG : Colors.Dark.BUTTON_OPERATOR_BG;
            NodeSetter.setBtnStyle(button, Sizes.Font.CALCULATOR_BUTTONS, Colors.Dark.FONT, Sizes.Button.BORDER_RADIUS, bgColor);

            // Map button
            buttonsMap.put(buttonText, button);

            // Push to stack
            if (isNumber)
                numberButtons.push(button);
            else
                operatorButtons.push(button);

            // Set buttons size
            button.setMinWidth(Sizes.Button.OPERATOR_WIDTH);
            button.setMinHeight(Sizes.Button.OPERATOR_HEIGHT);

            // Set buttons
            buttonsGridPane.add(button, i % 4, i / 4 + 1);
        }

        // History label
        Label history = new Label("");

        // Set history label style and size
        NodeSetter.setLabelStyle(history, Sizes.Font.CALCULATOR_HISTORY, Colors.Dark.FONT, Colors.Dark.RESULT_BG);
        history.setMinWidth(Sizes.Calculator.HISTORY_WIDTH);
        history.setAlignment(Pos.CENTER_RIGHT);
        gridPane.add(history, 0, 0);

        // Result label
        Label result = new Label("0");

        // Set result label style and size
        NodeSetter.setLabelStyle(result, Sizes.Font.CALCULATOR_RESULT, Colors.Dark.FONT, Colors.Dark.RESULT_BG);
        result.setMinWidth(Sizes.Calculator.RESULT_WIDTH);
        result.setAlignment(Pos.CENTER_RIGHT);
        gridPane.add(result, 0, 1);

        // Get formatted double
        Function<Double, String> getFormattedDouble = "%.3f"::formatted;

        // Update history label lambda expression
        Runnable updateHistory = () -> history.setText(String.join("", historyStack));

        // Update result label lambda expression
        Runnable updateResult = () -> {
            if (currentResult != 0)
                result.setText(getFormattedDouble.apply(currentResult) + String.join("", resultStack));

            else if (!resultStack.isEmpty())
                result.setText(String.join("", resultStack));

            else
                result.setText("0");
        };

        // Set delete button on click event
        deleteButton.setOnAction(_ -> {
            if (result.getText().equals(error) || resultStack.isEmpty())
                return;

            resultStack.removeLast();
            updateResult.run();
        });

        // Set CE button on click event
        ceButton.setOnAction(_ -> {
            resultStack.clear();
            result.setText("0");
            history.setText("");

            // Clear history and numbers stacks, and current result
            historyStack.clear();
            numbersStack.clear();
            updateCurrentResult(0);
        });

        // Operator precedence
        Function<String, Integer> precedence = operator -> switch (operator) {
            case "+", "-" -> 1;
            case "*", "/", "%" -> 2;
            default -> 0;
        };

        // Set number buttons on click event
        for (Button button : numberButtons) {
            button.setOnAction(_ -> {
                if (result.getText().equals(error))
                    return;

                resultStack.addLast(button.getText());
                updateResult.run();
            });
        }

        // Add parsed number lambda expression
        Consumer<String> addParsedNumber = number -> {
            double parsedNumber = Double.parseDouble(number);

            // Add number to its stack, and to the history stack
            numbersStack.push(parsedNumber);
            historyStack.addLast(getFormattedDouble.apply(parsedNumber));
        };

        // Set operator buttons on click event
        for (Button button : operatorButtons) {
            button.setOnAction(_ -> {
                if (result.getText().equals(error))
                    return;

                if (!button.getText().equals("=")) {
                    resultStack.addLast(button.getText());
                    updateResult.run();
                    return;
                }

                // Check if the result stack is empty
                if (resultStack.isEmpty())
                    return;

                // Convert to postfix notation
                StringBuilder tempNumber = new StringBuilder();
                boolean isTempNumberDecimal = false;

                // Clear history
                historyStack.clear();

                // Parse first number
                if (currentResult != 0) {
                    numbersStack.push(currentResult);
                    historyStack.push(getFormattedDouble.apply(currentResult));
                }

                for (String token : resultStack) {
                    // Check if the token is a number
                    if (token.matches("[0-9]")) {
                        tempNumber.append(token);
                        continue;
                    }

                    // Check if the token is a decimal
                    if (token.matches("\\.")) {
                        if (isTempNumberDecimal) {
                            result.setText(error);
                            return;
                        }

                        isTempNumberDecimal = true;
                        tempNumber.append(token);
                        continue;
                    }

                    // Store temp number, if exists
                    if (!tempNumber.isEmpty()) {
                        addParsedNumber.accept(tempNumber.toString());

                        tempNumber.setLength(0);
                        isTempNumberDecimal = false;
                    }

                    // Store operator
                    while (!operatorsStack.isEmpty() && precedence.apply(operatorsStack.peek()) >= precedence.apply(token))
                        operatorsPostfixStack.addLast(operatorsStack.pop());

                    // Add operator to its postfix stack, and to the history stack
                    operatorsPostfixStack.push(token);
                    historyStack.addLast(token);
                }

                if (!tempNumber.isEmpty())
                    addParsedNumber.accept(tempNumber.toString());

                //System.out.println(operatorsPostfixStack);
                //System.out.println(numbersStack);

                // Calculate the result
                double operationResult;

                while (!operatorsStack.isEmpty())
                    operatorsPostfixStack.addLast(operatorsStack.pop());

                while (!operatorsPostfixStack.isEmpty()) {
                    String operator = operatorsPostfixStack.pop();

                    if (numbersStack.size() < 2) {
                        result.setText(error);
                        return;
                    }

                    double number1 = numbersStack.pop();
                    double number2 = numbersStack.pop();

                    operationResult = switch (operator) {
                        case "+" -> number2 + number1;
                        case "-" -> number2 - number1;
                        case "*" -> number2 * number1;
                        case "/" -> number2 / number1;
                        default -> 0;
                    };

                    numbersStack.push(operationResult);
                }

                // Update history label
                updateHistory.run();

                // Set result
                resultStack.clear();
                updateCurrentResult(numbersStack.pop());
                updateResult.run();
            });
        }

        // Scene
        Scene scene = new Scene(gridPane);

        // Set default event property to buttons
        // buttonsMap.get("=").setDefaultButton(true);

        // Event Handler
        // NOTE: ENTER key isn't working as it's expected to
        scene.addEventHandler(KeyEvent.KEY_PRESSED, ev -> {
            //System.out.println(ev);

            switch (ev.getCode()) {
                case BACK_SPACE -> deleteButton.fire();
                case DELETE -> ceButton.fire();
                case ENTER, SPACE, EQUALS -> buttonsMap.get("=").fire();
                case ADD, PLUS -> buttonsMap.get("+").fire();
                case SUBTRACT, MINUS -> buttonsMap.get("-").fire();
                case MULTIPLY, ASTERISK -> buttonsMap.get("*").fire();
                case DIVIDE, SLASH -> buttonsMap.get("/").fire();
                case DECIMAL, PERIOD, COMMA -> buttonsMap.get(".").fire();
                case DIGIT0, NUMPAD0 -> buttonsMap.get("0").fire();
                case DIGIT1, NUMPAD1 -> buttonsMap.get("1").fire();
                case DIGIT2, NUMPAD2 -> buttonsMap.get("2").fire();
                case DIGIT3, NUMPAD3 -> buttonsMap.get("3").fire();
                case DIGIT4, NUMPAD4 -> buttonsMap.get("4").fire();
                case DIGIT5, NUMPAD5 -> buttonsMap.get("5").fire();
                case DIGIT6, NUMPAD6 -> buttonsMap.get("6").fire();
                case DIGIT7, NUMPAD7 -> buttonsMap.get("7").fire();
                case DIGIT8, NUMPAD8 -> buttonsMap.get("8").fire();
                case DIGIT9, NUMPAD9 -> buttonsMap.get("9").fire();
            }
            ev.consume();
        });

        // Set scene
        SceneSetter.setDefaultStyles(stylesResourceGetter, scene);

        return scene;
    }
}
