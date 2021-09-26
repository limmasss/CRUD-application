package crud.models;

public enum Status {
    ACTIVE("ACTIVE"),
    DISABLED("DISABLED");

    private final String displayValue;

    private Status(String displayValue) {
        this.displayValue = displayValue;
    }

    public String getDisplayValue() {
        return displayValue;
    }
}
