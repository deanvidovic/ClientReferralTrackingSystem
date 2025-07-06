package hr.clientreferraltrackingsystem.serialization;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ChangeLogHolder implements Serializable {
    private String fieldChanged;
    private String oldValue;
    private String newValue;
    private String role;
    private LocalDateTime changeDateTime;

    public ChangeLogHolder(String fieldChanged, String oldValue, String newValue, String role, LocalDateTime changeDateTime) {
        this.fieldChanged = fieldChanged;
        this.oldValue = oldValue;
        this.newValue = newValue;
        this.role = role;
        this.changeDateTime = changeDateTime;
    }

    public String getFieldChanged() {
        return fieldChanged;
    }

    public String getOldValue() {
        return oldValue;
    }

    public String getNewValue() {
        return newValue;
    }

    public String getRole() {
        return role;
    }

    public LocalDateTime getChangeDateTime() {
        return changeDateTime;
    }

    public void setFieldChanged(String fieldChanged) {
        this.fieldChanged = fieldChanged;
    }

    public void setOldValue(String oldValue) {
        this.oldValue = oldValue;
    }

    public void setNewValue(String newValue) {
        this.newValue = newValue;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setChangeDateTime(LocalDateTime changeDateTime) {
        this.changeDateTime = changeDateTime;
    }

    public String getFormattedDateTime() {
        return changeDateTime.format(DateTimeFormatter.ofPattern("dd.MM.yyyy."));
    }
}
