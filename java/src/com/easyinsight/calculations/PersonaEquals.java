package com.easyinsight.calculations;

import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.analysis.FilterDefinition;
import com.easyinsight.analysis.FilterValueDefinition;
import com.easyinsight.analysis.IRow;
import com.easyinsight.core.EmptyValue;
import com.easyinsight.core.Value;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.security.SecurityUtil;

import java.util.HashSet;
import java.util.Set;

/**
 * User: jamesboe
 * Date: 9/17/11
 * Time: 5:18 PM
 */
public class PersonaEquals extends Function {
    public Value evaluate() {
        String persona = SecurityUtil.getPersonaName();
        if (persona == null) {
            return new EmptyValue();
        }
        /*for (int i = 0; i < paramCount(); i++) {
            System.out.println(i + " = " + getParameter(i));
        }*/
        for (int i = 0; i < paramCount() - 1; i++) {
            String personaToTry = minusQuotes(getParameter(i)).toString();
            if (persona.equals(personaToTry)) {
                return getParameter(paramCount() - 1);
            }
        }
        return new EmptyValue();
    }

    @Override
    public boolean onDemand() {
        return true;
    }

    public int getParameterCount() {
        return -1;
    }
}
