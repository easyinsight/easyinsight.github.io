package com.easyinsight.publisher;

import com.easyinsight.rowutil.RowMethod;
import com.easyinsight.rowutil.TransactionResults;
import com.easyinsight.rowutil.TransactionalLoad;
import com.easyinsight.rowutil.transactional.BasicAuthTransactionalLoadAPIService;
import com.easyinsight.rowutil.transactional.EITransactionalLoad;
import org.snaplogic.cc.*;
import org.snaplogic.cc.prop.SimpleProp;
import org.snaplogic.cc.prop.SnapProp;
import org.snaplogic.common.ComponentResourceErr;
import org.snaplogic.common.Field;
import org.snaplogic.common.Record;
import org.snaplogic.common.exceptions.SnapComponentException;

import javax.xml.ws.BindingProvider;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

/**
 * User: abaldwin
 * Date: Jun 2, 2010
 * Time: 11:59:16 AM
 */
public class EasyInsightWriter extends ComponentAPI {
    private static final String API_KEY = "API Key";
    private static final String API_SECRET_KEY = "API Secret Key";
    private static final String DATA_SOURCE_NAME = "Data Source Name";
    private static final String REPLACE_DATA = "Replace Data";
    private static final String CHANGE_DATA_SOURCE = "Change Data Source to Match Fields";

    public String getAPIVersion() {
        return "1.0";
    }

    public String getComponentVersion() {
        return "1.00";
    }

    @Override
    public String getLabel() {
        return "Easy Insight Data Writer";
    }

    @Override
    public String getDescription() {
        return "This component sends your data to a data source in your Easy Insight account so that you can easily report on the data provided.";
    }

    @Override
    public Capabilities getCapabilities() {
        Capabilities capabilities = new Capabilities();
        capabilities.put(Capability.OUTPUT_VIEW_LOWER_LIMIT, 0);
        capabilities.put(Capability.OUTPUT_VIEW_UPPER_LIMIT, 0);
        capabilities.put(Capability.INPUT_VIEW_LOWER_LIMIT, 1);
        capabilities.put(Capability.INPUT_VIEW_UPPER_LIMIT, 1);
        return capabilities;
    }

    

    public void execute(Map<String, InputView> stringInputViewMap, Map<String, OutputView> stringOutputViewMap) {
        if (stringInputViewMap == null || stringInputViewMap.isEmpty()) {
            throw new SnapComponentException("No input view is connected.");
        }
        InputView iv = stringInputViewMap.values().iterator().next();
        Record rec;
        ArrayList<String> fieldNames = new ArrayList<String>();
        for(Field f: iv.getFields()) {
            fieldNames.add(f.getName());
        }
        TransactionalLoad addData = new TransactionalLoad(((Boolean)this.getPropertyValue(REPLACE_DATA)) ? RowMethod.REPLACE : RowMethod.ADD, (String) this.getPropertyValue(API_KEY),
                (String) this.getPropertyValue(API_SECRET_KEY), (String) this.getPropertyValue(DATA_SOURCE_NAME),
                (Boolean) this.getPropertyValue(CHANGE_DATA_SOURCE), 250, fieldNames.toArray(new String[fieldNames.size()]));
        try {
            addData.startData();
            while((rec = iv.readRecord()) != null) {
                ArrayList<Object> fields = new ArrayList<Object>();
                for(Field f : iv.getFields()) {
                    if(f.getType().equals(Field.SnapFieldType.SnapString)) {
                        fields.add(rec.getString(f.getName()));
                    } else if (f.getType().equals(Field.SnapFieldType.SnapDateTime)) {
                        fields.add(new Date(rec.getDatetime(f.getName()).getTime()));
                    } else if (f.getType().equals(Field.SnapFieldType.SnapNumber)) {
                        fields.add(rec.getNumber(f.getName()));
                    }
                }
                addData.newRow(fields.toArray());
            }
            addData.flush();
        } catch (RemoteException e) {
            throw new SnapComponentException(e.getMessage());
        }
        TransactionResults transactionResults = addData.generateResults();
        if (!transactionResults.isSuccessful()) {
            error(transactionResults.getFailureMessage());
        }
    }

    @Override
    public void validate(ComponentResourceErr componentResourceErr) {
        super.validate(componentResourceErr);
        BasicAuthTransactionalLoadAPIService service = new BasicAuthTransactionalLoadAPIService();
        EITransactionalLoad port = service.getBasicAuthTransactionalLoadAPIPort();
        ((BindingProvider)port).getRequestContext().put(BindingProvider.USERNAME_PROPERTY, this.getPropertyValue(API_KEY));
        ((BindingProvider)port).getRequestContext().put(BindingProvider.PASSWORD_PROPERTY, this.getPropertyValue(API_SECRET_KEY));
        try {
            port.validateCredentials();
        } catch(Exception e) {
            componentResourceErr.setMessage("Invalid Easy Insight Credentials.");
        }
    }

    @Override
    public void createResourceTemplate() {
        super.createResourceTemplate();
        SnapProp apiKey = new SimpleProp(API_KEY, SimpleProp.SimplePropType.SnapString, true);
        this.setPropertyDef(API_KEY, apiKey);
        SnapProp apiSecretKey = new SimpleProp(API_SECRET_KEY, SimpleProp.SimplePropType.SnapString, true);
        this.setPropertyDef(API_SECRET_KEY, apiSecretKey);
        SnapProp dataSourceName = new SimpleProp(DATA_SOURCE_NAME, SimpleProp.SimplePropType.SnapString, true);
        this.setPropertyDef(DATA_SOURCE_NAME, dataSourceName);
        SnapProp replace = new SimpleProp(REPLACE_DATA, SimpleProp.SimplePropType.SnapBoolean, true);
        this.setPropertyDef(REPLACE_DATA, replace);
        SnapProp changeDataSource = new SimpleProp(CHANGE_DATA_SOURCE, SimpleProp.SimplePropType.SnapBoolean, true);
        this.setPropertyDef(CHANGE_DATA_SOURCE, changeDataSource);
    }
}
