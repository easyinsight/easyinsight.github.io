package com.easyinsight.analysis;

import com.easyinsight.analysis.definitions.WSForm;
import com.easyinsight.core.Key;

import javax.persistence.*;
import java.util.List;
import java.util.Map;

/**
 * User: jamesboe
 * Date: Oct 28, 2010
 * Time: 10:44:03 PM
 */
@Entity
@Table(name="form")
public class FormDefinitionState extends AnalysisDefinitionState {

    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="form_id")
    private long formID;

    @Override
    public WSAnalysisDefinition createWSDefinition() {
        WSForm form = new WSForm();
        form.setFormID(formID);
        return form;
    }

    public long getFormID() {
        return formID;
    }

    public void setFormID(long formID) {
        this.formID = formID;
    }

    @Override
    public AnalysisDefinitionState clone(Map<Key, Key> keyMap, List<AnalysisItem> allFields) throws CloneNotSupportedException {
        FormDefinitionState formDefinitionState = (FormDefinitionState) super.clone(keyMap, allFields);
        formDefinitionState.setFormID(0);
        return formDefinitionState;
    }
}
