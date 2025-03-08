package filereader;

public enum Genders {
    FEMALE("Female"), MALE("Male"), BIGENDER("Bigender"), GENDERQUEER("Genderqueer"), NON_BINARY("Non-binary"),
    GENDERFLUID("Genderfluid"), POLYGENDER("Polygender"), AGENDER("Agender"), UNDEFINED("UNDEFINED");

    private final String gender;

    Genders(String gender) {
        this.gender = gender;
    }

    public String getGender() {
        return this.gender;
    }

    public static Genders fromString(String genderString) {
        if (genderString == null)
            return UNDEFINED;

        for (Genders g : Genders.values())
            if (g.gender.equalsIgnoreCase(genderString))
                return g;

        return UNDEFINED;
    }
}
