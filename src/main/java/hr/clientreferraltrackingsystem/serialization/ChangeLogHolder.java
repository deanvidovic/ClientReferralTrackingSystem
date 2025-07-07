package hr.clientreferraltrackingsystem.serialization;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * A serializable holder class that keeps track of changes made to a specific field,
 * including old and new values, the role responsible for the change, and the timestamp of the change.
 */
public class ChangeLogHolder implements Serializable {
    private String fieldChanged;
    private String oldValue;
    private String newValue;
    private String role;
    private LocalDateTime changeDateTime;

    /**
     * Constructs a new ChangeLogHolder instance.
     *
     * @param fieldChanged  the name of the field that was changed
     * @param oldValue      the old value of the field before the change
     * @param newValue      the new value of the field after the change
     * @param role          the role responsible for the change
     * @param changeDateTime the timestamp when the change occurred
     */
    public ChangeLogHolder(String fieldChanged, String oldValue, String newValue, String role, LocalDateTime changeDateTime) {
        this.fieldChanged = fieldChanged;
        this.oldValue = oldValue;
        this.newValue = newValue;
        this.role = role;
        this.changeDateTime = changeDateTime;
    }

    /**
     * Returns the name of the changed field.
     *
     * @return the fieldChanged
     */
    public String getFieldChanged() {
        return fieldChanged;
    }

    /**
     * Returns the old value of the field before the change.
     *
     * @return the oldValue
     */
    public String getOldValue() {
        return oldValue;
    }

    /**
     * Returns the new value of the field after the change.
     *
     * @return the newValue
     */
    public String getNewValue() {
        return newValue;
    }

    /**
     * Returns the role responsible for the change.
     *
     * @return the role
     */
    public String getRole() {
        return role;
    }

    /**
     * Returns the timestamp when the change occurred.
     *
     * @return the changeDateTime
     */
    public LocalDateTime getChangeDateTime() {
        return changeDateTime;
    }

    /**
     * Sets the name of the changed field.
     *
     * @param fieldChanged the fieldChanged to set
     */
    public void setFieldChanged(String fieldChanged) {
        this.fieldChanged = fieldChanged;
    }

    /**
     * Sets the old value of the field before the change.
     *
     * @param oldValue the oldValue to set
     */
    public void setOldValue(String oldValue) {
        this.oldValue = oldValue;
    }

    /**
     * Sets the new value of the field after the change.
     *
     * @param newValue the newValue to set
     */
    public void setNewValue(String newValue) {
        this.newValue = newValue;
    }

    /**
     * Sets the role responsible for the change.
     *
     * @param role the role to set
     */
    public void setRole(String role) {
        this.role = role;
    }

    /**
     * Sets the timestamp when the change occurred.
     *
     * @param changeDateTime the changeDateTime to set
     */
    public void setChangeDateTime(LocalDateTime changeDateTime) {
        this.changeDateTime = changeDateTime;
    }

    /**
     * Returns the formatted date of the change in the pattern "dd.MM.yyyy."
     *
     * @return formatted date as String
     */
    public String getFormattedDateTime() {
        return changeDateTime.format(DateTimeFormatter.ofPattern("dd.MM.yyyy."));
    }
}
