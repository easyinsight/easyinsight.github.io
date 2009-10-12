package com.easyinsight.outboundnotifications;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.PrimaryKeyJoinColumn;

/**
 * Created by IntelliJ IDEA.
 * User: abaldwin
 * Date: Sep 14, 2009
 * Time: 2:18:20 PM
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(name="buy_our_stuff_todo")
@PrimaryKeyJoinColumn(name = "todo_id")
public class BuyOurStuffTodo extends TodoBase {
    public BuyOurStuffTodo() {
        setType(BUY_OUR_STUFF);
    }
}
