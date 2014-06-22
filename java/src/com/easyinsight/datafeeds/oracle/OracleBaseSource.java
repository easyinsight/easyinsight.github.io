package com.easyinsight.datafeeds.oracle;

import com.easyinsight.core.DateValue;
import com.easyinsight.core.EmptyValue;
import com.easyinsight.core.NumericValue;
import com.easyinsight.core.Value;
import com.easyinsight.datafeeds.ServerDataSourceDefinition;

import javax.xml.bind.JAXBElement;
import javax.xml.datatype.XMLGregorianCalendar;
import java.util.Date;

/**
 * User: jamesboe
 * Date: 6/9/14
 * Time: 9:47 AM
 */
public abstract class OracleBaseSource extends ServerDataSourceDefinition {
    protected Value getDate(JAXBElement<XMLGregorianCalendar> cal) {
        if (cal == null || cal.getValue() == null) {
            return new EmptyValue();
        }
        Date date = cal.getValue().toGregorianCalendar().getTime();
        return new DateValue(date);
    }

    protected Value getMeasureValue(JAXBElement<java.math.BigDecimal> value) {
        if (value.getValue() == null) {
            return new EmptyValue();
        }
        return new NumericValue(value.getValue().doubleValue());
    }

    protected Value getDate(XMLGregorianCalendar cal) {
        if (cal == null) {
            return new EmptyValue();
        }
        Date date = cal.toGregorianCalendar().getTime();
        return new DateValue(date);
    }
}
