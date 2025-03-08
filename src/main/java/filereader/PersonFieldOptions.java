package filereader;

public enum PersonFieldOptions {
    FIRST_NAME('f'), LAST_NAME('l'), ID('i'), GENDER('g'), COUNTRY('c'), UNDEFINED('0');

    private final Character option;

    PersonFieldOptions(Character option) {
        this.option = option;
    }

    public Character getOption() {
        return option;
    }

    public static PersonFieldOptions fromChar(char optionChar) {
        for (PersonFieldOptions option : PersonFieldOptions.values())
            if (option.getOption().equals(optionChar))
                return option;

        return UNDEFINED;
    }

    public static PersonFieldOptions fromString(String optionString) {
        if (optionString == null)
            return UNDEFINED;

        return fromChar(optionString.charAt(0));
    }

    public static void print() {
        System.out.printf("""
                        Available options:
                        %s - First name
                        %s - Last name
                        %s - ID
                        %s - Gender
                        %s - Country%n""", FIRST_NAME.getOption(), LAST_NAME.getOption(), ID.getOption(),
                GENDER.getOption(), COUNTRY.getOption());
    }
}
