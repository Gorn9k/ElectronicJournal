package by.vstu.electronicjournal.entity.common;

public enum Status {
    ACTIVE("ACTIVE"), NOACTIVE("NOACTIVE"), DELETED("DELETED");

    String status;

    Status(String status) {
        this.status = status;
    }
}
