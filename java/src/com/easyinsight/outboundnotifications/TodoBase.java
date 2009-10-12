package com.easyinsight.outboundnotifications;

import javax.persistence.*;

/**
 * Created by IntelliJ IDEA.
 * User: abaldwin
 * Date: Jun 1, 2009
 * Time: 10:02:55 AM
 */
@Entity
@Table(name="todo_base")
@Inheritance(strategy = InheritanceType.JOINED)
public class TodoBase {

    public static final int CONFIGURE_DATA_SOURCE = 1;
    public static final int BUY_OUR_STUFF = 2;

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="todo_id")
    private long id;

    @Column(name="user_id")
    private long userID;

    @Column(name="todo_type")
    private int type;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }


    public long getUserID() {
        return userID;
    }

    public void setUserID(long userID) {
        this.userID = userID;
    }
    
    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
