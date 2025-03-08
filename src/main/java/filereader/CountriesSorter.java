package filereader;

import java.util.Comparator;

public class CountriesSorter implements Comparator<Person> {

    @Override
    public int compare(Person p1, Person p2) {
        return p1.country().compareTo(p2.country());
    }
}
