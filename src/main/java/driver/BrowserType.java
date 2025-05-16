package driver;

public enum BrowserType {
    CHROME,
    FIREFOX,
    EDGE;

    public static BrowserType from(String name) {
        if (name == null || name.isBlank()) {
            return CHROME; // По умолчанию
        }

        try {
            return BrowserType.valueOf(name.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Неподдерживаемый браузер: " + name +
                ". Допустимые значения: chrome, firefox, edge.");
        }
    }
}