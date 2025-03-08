package filereader;

public record Person(String firstName, String lastName, Genders genders, int id, String country) {
    public String getFormattedName() {
        return "%s, %s".formatted(lastName, firstName);
    }

    public String toWrite() {
        return "%s,%s,%s,%d,%s".formatted(firstName, lastName, genders.getGender(), id, country);
    }

    @Override
    public String toString() {
        return "%-30s %-15d %-15s %-20s".formatted(getFormattedName(), id, genders.getGender(), country);
    }
}
