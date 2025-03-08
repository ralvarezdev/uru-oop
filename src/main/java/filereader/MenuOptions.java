package filereader;

public enum MenuOptions {
    PRINT_UNSORTED('p'), PRINT_SORTED('P'), INSERT('i'), MODIFY('m'), DELETE('d'), FIND('f'), SAVE('s'), SHUTDOWN('e'), UNDEFINED('0');

    private final Character option;

    MenuOptions(Character option) {
        this.option = option;
    }

    public Character getOption() {
        return option;
    }

    public static MenuOptions fromChar(char optionChar) {
        for (MenuOptions option : MenuOptions.values())
            if (option.getOption().equals(optionChar))
                return option;

        return UNDEFINED;
    }

    public static MenuOptions fromString(String optionString) {
        if (optionString == null)
            return UNDEFINED;

        return fromChar(optionString.charAt(0));
    }

    public static void print() {
        System.out.printf("""
                        Available options:
                        %s - Print people (unsorted)
                        %s - Print people (sorted)
                        %s - Find person
                        %s - Insert person
                        %s - Modify person
                        %s - Delete person
                        %s - Save
                        %s - Shutdown
                        %n""", PRINT_UNSORTED.getOption(), PRINT_SORTED.getOption(), FIND.getOption(),
                INSERT.getOption(), MODIFY.getOption(), DELETE.getOption(), SAVE.getOption(), SHUTDOWN.getOption());
    }
}