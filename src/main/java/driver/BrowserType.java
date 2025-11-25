package driver;

public enum BrowserType {
    CHROME("chrome"),
    CHROME_MOBILE("chromeMobile");

    private final String value;

    BrowserType(String value) {
        this.value = value;
    }

    public static BrowserType from(String raw) {
        if (raw == null) {
            return CHROME;
        }
        String normalized = raw.trim().toLowerCase();
        for (BrowserType type : values()) {
            if (type.value.equals(normalized)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown browser: " + raw);
    }

    @Override
    public String toString() {
        return value;
    }
}
