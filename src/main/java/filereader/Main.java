package filereader;

import files.DefaultDataPathGetter;
import files.DefaultFileReader;
import files.DefaultFileWriter;

import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        String CSV_FILENAME = "people.csv";

        Scanner scanner = new Scanner(System.in);
        String input;
        MenuOptions menuOption;
        boolean shutdown = false, menuOptionNotFound;

        // Load CSV
        DefaultFileReader fileReader = new DefaultFileReader();
        DefaultFileWriter fileWriter = new DefaultFileWriter();
        DefaultDataPathGetter dataPathGetter = null;

        PeopleManager peopleManager = new PeopleManager();
        StringBuilder content = null;

        try {
            dataPathGetter = new DefaultDataPathGetter();

            try {
                content = fileReader.getFileContent(dataPathGetter.getTargetDataPath(CSV_FILENAME).toString());
            } catch (IOException | NullPointerException e) {
                content = fileReader.getFileContent(dataPathGetter.getSrcDataPath(CSV_FILENAME).toString());
            }

        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }

        String[] lines = content.toString().split("\n");

        for (String line : lines) {
            if (line.isEmpty())
                continue;

            String[] fields = line.split(",");

            if (fields.length < 5)
                continue;

            String firstName = fields[0];
            String lastName = fields[1];
            Genders genders = Genders.fromString(fields[2]);
            int id;
            String country = fields[4];

            try {
                id = Integer.parseInt(fields[3]);
            } catch (NumberFormatException e) {
                e.printStackTrace();
                continue;
            }

            peopleManager.addPerson(new Person(firstName, lastName, genders, id, country));
        }

        while (!shutdown) {
            MenuOptions.print();

            System.out.print("Select a menu option: ");
            input = scanner.nextLine();
            menuOptionNotFound = false;

            if (input.isEmpty())
                continue;

            menuOption = MenuOptions.fromString(input);

            try {
                switch (menuOption) {
                    case PRINT_UNSORTED -> peopleManager.printUnsorted();
                    case PRINT_SORTED -> printSorted(peopleManager, scanner);
                    case FIND -> findPerson(peopleManager, scanner);
                    case INSERT -> {
                        int personId = askPersonId("Enter person ID", scanner);

                        // Check if the person ID has already being inserted
                        if (isPersonIdAssigned(personId, peopleManager)) {
                            System.out.println();
                            System.out.println("ID already assigned...");
                            break;
                        }

                        // Get fields
                        String firstName = askFirstName(scanner);
                        String lastName = askLastName(scanner);
                        String country = askCountry(scanner);
                        Genders gender = askGender(scanner);

                        // Add person
                        Person person = new Person(firstName, lastName, gender, personId, country);
                        peopleManager.addPerson(person);

                        // Append person to CSV
                        fileWriter.appendFileContent(CSV_FILENAME, person.toWrite());
                    }
                    case MODIFY -> {
                        int personId = askPersonId("Enter person ID to modify", scanner);

                        // Check if the person ID has already being inserted
                        if (!isPersonIdAssigned(personId, peopleManager)) {
                            System.out.println();
                            System.out.println("ID hasn't been assigned...");
                            break;
                        }

                        // Get person
                        int personIndex = getPersonIndex(personId, peopleManager);
                        Person person = peopleManager.getPerson(personIndex);

                        // Get field to modify
                        PersonFieldOptions option = askFieldToModify(peopleManager, scanner);

                        // Get current field values
                        String firstName = person.firstName();
                        String lastName = person.lastName();
                        Genders gender = person.genders();
                        String country = person.country();

                        boolean isValid = true;

                        switch (option) {
                            case FIRST_NAME -> firstName = askFirstName(scanner);
                            case LAST_NAME -> lastName = askLastName(scanner);
                            case GENDER -> gender = askGender(scanner);
                            case COUNTRY -> country = askCountry(scanner);
                            case ID -> {
                                System.out.println();
                                System.out.println("ID cannot be modified...");
                                isValid = false;
                            }
                            default -> {
                                System.out.println();
                                System.out.println("Invalid field...");
                                isValid = false;
                            }
                        }

                        if (!isValid)
                            break;

                        // Remove person
                        peopleManager.deletePerson(personIndex);

                        // Add person
                        person = new Person(firstName, lastName, gender, personId, country);
                        peopleManager.addPerson(person);

                        // Overwrite CSV
                        content = peopleManager.getContentToWrite();
                        fileWriter.overwriteTargetDataFileContent(dataPathGetter, CSV_FILENAME, content.toString());
                    }
                    case DELETE -> {
                        Integer personIndex = findPerson(peopleManager, scanner);

                        if (personIndex == null)
                            break;

                        if (askBooleanQuestion("Do you want to remove the given person from the list?", scanner))
                            peopleManager.deletePerson(personIndex);
                    }
                    case SAVE -> {
                        content = peopleManager.getContentToWrite();
                        fileWriter.overwriteTargetDataFileContent(dataPathGetter, CSV_FILENAME, content.toString());
                    }
                    case SHUTDOWN -> shutdown = true;
                    default -> menuOptionNotFound = true;
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            if (menuOptionNotFound) {
                System.out.println();
                System.out.println("Invalid option...");
            }

            if (!shutdown) {
                System.out.println();
                System.out.println("Press ENTER to Continue: ");
                scanner.nextLine();
            }
        }

        scanner.close();
    }

    private static void printSorted(PeopleManager peopleManager, Scanner scanner) {
        String input;
        Comparator<Person> comparator = null;
        boolean sortingOptionNotFound;

        System.out.println();
        PersonFieldOptions.print();

        do {
            System.out.println();
            System.out.print("Select sorting: ");

            input = scanner.nextLine();
            sortingOptionNotFound = false;

            switch (PersonFieldOptions.fromString(input)) {
                case FIRST_NAME -> comparator = new FirstNamesSorter();
                case LAST_NAME -> comparator = new LastNamesSorter();
                case ID -> comparator = new IdSorter();
                case GENDER -> comparator = new GendersSorter();
                case COUNTRY -> comparator = new CountriesSorter();
                default -> sortingOptionNotFound = true;
            }
        } while (sortingOptionNotFound);

        boolean reversed = askBooleanQuestion("Do you want to sort it in reversed order?", scanner);
        peopleManager.printSorted(comparator, reversed);
    }

    private static int askPersonId(String message, Scanner scanner) throws NumberFormatException {
        System.out.println();
        System.out.printf("%s: ", message);

        return Integer.parseInt(scanner.nextLine());
    }

    private static Integer getPersonIndex(int id, PeopleManager peopleManager) {
        peopleManager.sortPeople(new IdSorter());
        List<Integer> idList = peopleManager.getIdList();
        int index = Collections.binarySearch(idList, id);

        return (index < 0) ? null : index;
    }

    private static Integer getPersonIndex(PeopleManager peopleManager, Scanner scanner) throws NumberFormatException {
        int id = askPersonId("Enter person ID to search for", scanner);
        return getPersonIndex(id, peopleManager);
    }

    private static boolean isPersonIdAssigned(int id, PeopleManager peopleManager) {
        return getPersonIndex(id, peopleManager) != null;
    }

    private static Integer findPerson(PeopleManager peopleManager, Scanner scanner) {
        Integer personIndex = getPersonIndex(peopleManager, scanner);
        System.out.println();

        if (personIndex == null) {
            System.out.println("ID not found...");
            return null;
        }

        System.out.println("ID found:");
        System.out.println(peopleManager.getPerson(personIndex));
        return personIndex;
    }

    private static Boolean askBooleanQuestion(String message, Scanner scanner) {
        String input;
        Character c;

        while (true) {
            System.out.println();
            System.out.printf("%s [y/N] ", message);
            input = scanner.nextLine();

            if (input.isEmpty())
                continue;

            c = input.charAt(0);

            if (c.equals('y') || c.equals('Y'))
                return true;

            else if (c.equals('n') || c.equals('N'))
                return false;
        }
    }

    private static String askString(String message, Scanner scanner) {
        String input;

        while (true) {
            System.out.println();
            System.out.printf("%s: ", message);
            input = scanner.nextLine();

            if (input.isEmpty())
                continue;

            return input;
        }
    }

    private static String askFirstName(Scanner scanner) {
        return askString("Enter first name", scanner);
    }

    private static String askLastName(Scanner scanner) {
        return askString("Enter last name", scanner);
    }

    private static String askCountry(Scanner scanner) {
        return askString("Enter country", scanner);
    }

    private static Genders askGender(Scanner scanner) {
        String input;

        while (true) {
            System.out.println();
            System.out.println("Enter gender: ");
            input = scanner.nextLine();

            if (input.isEmpty())
                continue;

            return Genders.fromString(input);
        }
    }

    private static PersonFieldOptions askFieldToModify(PeopleManager peopleManager, Scanner scanner) {
        String input;

        System.out.println();
        PersonFieldOptions.print();

        System.out.println();
        System.out.print("Select field to modify: ");
        input = scanner.nextLine();

        return PersonFieldOptions.fromString(input);
    }
}
