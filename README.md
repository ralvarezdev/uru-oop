# uru-oop

**Note:** Archived and read-only. Kept for reference from the OOP college course.

My projects from the Object-Oriented Programming college course, written in Java (JavaFX 22, Maven, Java 22 source/target). Maven artifact: `com.example:uru-oop:1.0-SNAPSHOT`.

## What's in this repository

- **`filereader`** — console app that loads `data/src/people.csv` and lets the user sort/filter people by first name, last name, country, gender or ID.
- **`files`** — reusable file/property reading-writing utilities (`FileReader`, `FileWriter`, `PropertiesReader`, `ResourceGetter`).
- **`exceptions`** — `ConnectionException`, `MissingPropertyException`.
- **`gui/pencilpi`** — JavaFX desktop app ("PencilPi") with a calculator scene.
- **`gui/traversinggame`** — JavaFX desktop traversal/maze-style game.
- **`gui/setters`** / **`gui/commons`** — shared JavaFX scene/stage/node setup helpers and a shared `ColorPalette`.
- **`util`** — shared utility helpers.

## Prerequisites

- JDK 22
- Maven (the repo ships the Maven Wrapper: `mvnw` / `mvnw.cmd`)
- JavaFX 22-ea dependencies, fetched automatically by Maven

## Running

```bash
./mvnw compile
./mvnw exec:java -Dexec.mainClass="filereader.Main"      # CSV file-reader console app
./mvnw javafx:run -Djavafx.mainClass="gui.pencilpi.PencilPi"
```

Exact Maven exec/javafx goals depend on `pom.xml`; check it if the commands above don't match. The CSV used by `filereader.Main` is expected at `data/src/people.csv` relative to the working directory.

## License

GNU General Public License v3.0 (see `LICENSE`).
