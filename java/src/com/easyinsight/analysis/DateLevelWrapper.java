package com.easyinsight.analysis;

import javax.persistence.*;
import java.io.Serializable;

/**
 * User: jamesboe
 * Date: 8/15/11
 * Time: 9:00 PM
 */
@Entity
@Table(name="date_level_wrapper")
public class DateLevelWrapper implements Serializable, Cloneable
{
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="date_level_wrapper_id")
    private long id;
    @Column(name="date_level")
    private int dateLevel;
    @Column(name="display")
    private String display;
    @Column(name="short_display")
    private String shortDisplay;

    public String getShortDisplay() {
        return shortDisplay;
    }

    public void setShortDisplay(String shortDisplay) {
        this.shortDisplay = shortDisplay;
    }

    public String getDisplay() {
        return display;
    }

    public void setDisplay(String display) {
        this.display = display;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getDateLevel() {
        return dateLevel;
    }

    public void setDateLevel(int dateLevel) {
        this.dateLevel = dateLevel;
    }

    public DateLevelWrapper clone() throws CloneNotSupportedException {
        DateLevelWrapper wrapper = (DateLevelWrapper) super.clone();
        wrapper.setId(0);
        return wrapper;
    }
}
