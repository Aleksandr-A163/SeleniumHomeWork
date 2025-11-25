package driver;

public enum RunMode {
    LOCAL("local"),
    SELENOID("selenoid"),
    GGR("ggr");

    private final String value;

    RunMode(String value) {
        this.value = value;
    }

    public static RunMode from(String raw) {
        if (raw == null) {
            return LOCAL;
        }
        String normalized = raw.trim().toLowerCase();
        for (RunMode mode : values()) {
            if (mode.value.equals(normalized)) {
                return mode;
            }
        }
        throw new IllegalArgumentException("Unknown runMode: " + raw);
    }

    @Override
    public String toString() {
        return value;
    }
}
