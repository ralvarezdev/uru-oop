package filereader;

import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

public class PeopleManager {
    private final LinkedList<Person> people;

    public PeopleManager() {
        this.people = new LinkedList<>();
    }

    public PeopleManager(Person... people) {
        if (people.length == 0) {
            this.people = new LinkedList<>();
            return;
        }
        this.people = new LinkedList<>(Arrays.asList(people));
    }

    public void addPerson(Person p) {
        if (p == null)
            return;

        people.add(p);
    }

    public void deletePerson(int index) {
        if (index < 0 || index >= people.size())
            return;

        people.remove(index);
    }

    public Person getPerson(int index) {
        if (index < 0 || index >= people.size())
            return null;

        return people.get(index);
    }

    public List<Integer> getIdList() {
        LinkedList<Integer> idList = new LinkedList<>();

        for (Person p : people)
            idList.add(p.id());

        return idList;
    }

    public void sortPeople(Comparator<Person> comparator, boolean reversed) {
        if (reversed)
            people.sort(comparator.reversed());

        else
            people.sort(comparator);
    }

    public void sortPeople(Comparator<Person> comparator) {
        sortPeople(comparator, false);
    }

    private void printPeople() {
        System.out.printf("%-30s %-15s %-15s %-20s%n", "NAME", "ID", "GENDER", "COUNTRY");

        for (Person p : people)
            System.out.println(p);
    }

    public void printUnsorted() {
        System.out.println();
        System.out.println("UNSORTED PEOPLE");
        System.out.println();

        printPeople();
    }

    public void printSorted(Comparator<Person> comparator, boolean reversed) {
        if (comparator == null) {
            System.err.println("Null comparator...");
            return;
        }

        System.out.println();
        System.out.println("SORTED PEOPLE");
        System.out.println();

        sortPeople(comparator, reversed);
        printPeople();
    }

    public StringBuilder getContentToWrite() {
        StringBuilder content = new StringBuilder();
        int peopleSize = people.size();

        for (int i = 0; i < peopleSize; i++)
            if (i != peopleSize - 1)
                content.append("%s\n".formatted(people.get(i).toWrite()));

            else
                content.append(people.get(i).toWrite());

        return content;
    }
}
