package driver;

public enum RunMode {
    LOCAL,
    SELENOID,
    GGR;

    public static RunMode fromProperty() {
        String value = System.getProperty("runMode", "local").toLowerCase();
        return switch (value) {
            case "selenoid" -> SELENOID;
            case "ggr" -> GGR;
            default -> LOCAL;
        };
    }
}
