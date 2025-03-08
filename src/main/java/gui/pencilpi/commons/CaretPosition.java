package gui.pencilpi.commons;

import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;

import java.util.Arrays;
import java.util.HashMap;

public final class CaretPosition {
    private final Old old;
    private final Current current;
    private final TextArea textArea;
    private final HashMap<Integer, Integer> linesLength;

    private final class Old {
        private String previous;
        private String next;

        private int column;
        private boolean isColumnValid;

        private KeyCode keyCode;

        private Old() {
            this.previous = "";
            this.next = "";
            this.isColumnValid = false;
        }
    }

    private final class Current {
        private String previous;
        private String next;

        private int column;
        private int characters;
        private int line;
        private int lines;

        private KeyCode keyCode;

        private Current() {
            this.previous = "";
            this.next = "";
            this.column = 1;
            this.characters = 0;
            this.line = 1;
            this.lines = 1;
        }
    }

    public CaretPosition(TextArea textArea) {
        // Set cursor at start
        textArea.positionCaret(0);

        this.textArea = textArea;
        this.old = new Old();
        this.current = new Current();
        this.linesLength = new HashMap<>();

        // Set current characters
        setCurrentCharacters(textArea.getText().length());

        // Set current lines
        String[] lines = textArea.getText().split("\n");
        this.setCurrentLines(lines.length);

        // Set line length
        for (int i = 1; i <= lines.length; i++)
            linesLength.put(i, lines[i - 1].length());
    }

    // - Getters and Setters
    public String oldPrevious() {
        return this.old.previous;
    }

    public String oldNext() {
        return this.old.next;
    }

    public String currentPrevious() {
        return this.current.previous;
    }

    public String currentNext() {
        return this.current.next;
    }

    public int previousColumn() {
        return this.old.column;
    }

    public void setPreviousColumn(int column) {
        this.old.column = column;
    }

    public int currentColumn() {
        return this.current.column;
    }

    public void setCurrentColumn(int columns) {
        this.current.column = columns;
    }

    public void setCurrentColumnAtEndOfCurrentLine() {
        int diff = this.currentLine() == this.currentLines() ? 1 : 0;
        this.setCurrentColumn(linesLength.get(this.currentLine()) + diff);
    }

    public void increaseCurrentColumn(int diff) {
        setCurrentColumn(this.current.column + diff);
    }

    public void increaseCurrentColumn() {
        this.increaseCurrentColumn(1);
    }

    public void decreaseCurrentColumn(int diff) {
        setCurrentColumn(this.current.column - diff);
    }

    public void decreaseCurrentColumn() {
        this.decreaseCurrentColumn(1);
    }

    public int currentCharacters() {
        return this.current.characters;
    }

    public void setCurrentCharacters(int characters) {
        this.current.characters = characters;
    }

    public void increaseCurrentCharacters() {
        this.setCurrentCharacters(this.current.characters + 1);
    }

    public void decreaseCurrentCharacters() {
        this.setCurrentCharacters(this.current.characters - 1);
    }

    public int lineLength(int line) {
        return linesLength.get(line);
    }

    public void setLineLength(int line, int length) {
        linesLength.put(line, length);
    }

    public int currentLineLength() {
        return this.lineLength(this.currentLine());
    }

    public void setCurrentLineLength(int length) {
        this.setLineLength(this.currentLine(), length);
    }

    public void decreaseCurrentLineLength() {
        this.setCurrentLineLength(this.currentLineLength() - 1);
    }

    public void increaseCurrentLineLength() {
        this.setCurrentLineLength(this.currentLineLength() + 1);
    }

    public int currentLine() {
        return this.current.line;
    }

    public void setCurrentLine(int line) {
        this.current.line = line;
    }

    public void increaseCurrentLine() {
        this.setCurrentLine(this.current.line + 1);
    }

    public void decreaseCurrentLine() {
        this.setCurrentLine(this.current.line - 1);
    }

    public int currentLines() {
        return this.current.lines;
    }

    public void setCurrentLines(int lines) {
        this.current.lines = lines;
    }

    public void increaseCurrentLines() {
        this.setCurrentLines(this.current.lines + 1);
    }

    public void decreaseCurrentLines() {
        this.setCurrentLines(this.current.lines - 1);
    }

    public KeyCode oldKeyCode() {
        return this.old.keyCode;
    }

    public KeyCode currentKeyCode() {
        return this.current.keyCode;
    }

    public void setCurrentKeyCode(KeyCode keyCode) {
        this.old.keyCode = this.current.keyCode;
        this.current.keyCode = keyCode;

        if (this.old.isColumnValid)
            this.old.isColumnValid = this.oldKeyCode() == KeyCode.UP || this.oldKeyCode() == KeyCode.DOWN;
    }

    // - Update
    public void update() {
        this.old.previous = this.currentPrevious();
        this.old.next = this.currentNext();

        int caretPosition = textArea.getCaretPosition();
        String text = this.textArea.getText();
        this.current.next = "";
        this.current.previous = "";

        if (text.isEmpty())
            return;

        if (caretPosition == 0) {
            this.current.next = text.substring(0, 1);
            this.current.previous = "";
        } else if (caretPosition == text.length()) {
            this.current.next = "";
            this.current.previous = text.substring(caretPosition - 1, caretPosition);
        } else {
            this.current.next = text.substring(caretPosition, caretPosition + 1);
            this.current.previous = text.substring(caretPosition - 1, caretPosition);
        }
    }

    // - Move through lines
    public void moveUp() {
        int nextLineLength = this.lineLength(this.currentLine() + 1);
        this.setCurrentLineLength(this.currentColumn() + nextLineLength - 1);

        for (int i = this.currentLine() + 1; i < this.currentLines(); i++)
            linesLength.put(i, linesLength.get(i + 1));

        linesLength.remove(this.currentLines());
        this.decreaseCurrentLines();
    }

    public void moveDown() {
        int diff = this.currentLineLength() - this.currentColumn();
        this.setCurrentLineLength(this.currentColumn());

        this.increaseCurrentLine();
        this.increaseCurrentLines();

        for (int i = this.currentLine(); i < this.currentLines(); i++)
            linesLength.put(i + 1, linesLength.get(i));

        this.setCurrentLineLength(diff);
    }

    public void move() {
        if (this.old.isColumnValid) {
            if (this.previousColumn() > linesLength.get(this.currentLine()))
                this.setCurrentColumnAtEndOfCurrentLine();

            else
                this.setCurrentColumn(this.previousColumn());
        } else if (this.currentColumn() > linesLength.get(this.currentLine())) {
            this.setPreviousColumn(this.currentColumn());
            this.old.isColumnValid = true;
            this.setCurrentColumnAtEndOfCurrentLine();
        }
    }

    public String toString() {
        return Arrays.toString(new String[]{
                this.oldPrevious(),
                this.currentPrevious(),
                this.currentNext(),
                this.oldNext(),
                this.textArea.getText(),
                String.valueOf(this.currentLines()),
                String.valueOf(this.currentLine()),
                String.valueOf(this.currentColumn()),
                linesLength.toString()
        });
    }
}
