package util;

public enum OS {
    Windows, Unix, Mac, UNDEFINED;

    private static final String OSName = System.getProperty("os.name").toLowerCase();
    private static OS OS = null;

    public static OS getOS() {
        if (OS != null)
            return OS;

        if (OSName.contains("win"))
            OS = Windows;

        else if (OSName.contains("mac"))
            OS = Mac;

        else if (OSName.contains("unix"))
            OS = Unix;

        else
            OS = UNDEFINED;

        return OS;
    }
}
