package filereader;

import java.util.Comparator;

public class GendersSorter implements Comparator<Person> {

    @Override
    public int compare(Person p1, Person p2) {
        return p1.genders().toString().compareTo(p2.genders().toString());
    }
}
