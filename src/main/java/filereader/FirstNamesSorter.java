package filereader;

import java.util.Comparator;

public class FirstNamesSorter implements Comparator<Person> {

    @Override
    public int compare(Person p1, Person p2) {
        return p1.firstName().compareTo(p2.firstName());
    }
}
