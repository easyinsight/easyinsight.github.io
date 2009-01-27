package com.easyinsight.core;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.PrimaryKeyJoinColumn;

/**
 * User: James Boe
 * Date: Jul 16, 2008
 * Time: 12:46:48 AM
 */

@Entity
@Table(name="empty_value")
@PrimaryKeyJoinColumn(name="value_id")
public class PersistableEmptyValue extends PersistableValue {
}
