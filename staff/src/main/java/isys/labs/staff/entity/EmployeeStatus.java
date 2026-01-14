package isys.labs.staff.entity;

import lombok.Getter;

/**
 * Статусы сотрудников в системе
 */
@Getter
public enum EmployeeStatus {
    ACTIVE("Работает"),
    ON_LEAVE("В отпуске"),
    SUSPENDED("Отстранен"),
    FIRED("Уволен");

    private final String description;

    EmployeeStatus(String description) {
        this.description = description;
    }

}