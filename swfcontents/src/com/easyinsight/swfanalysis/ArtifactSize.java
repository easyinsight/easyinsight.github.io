package com.easyinsight.swfanalysis;

import org.apache.tools.ant.Task;
import org.apache.tools.ant.BuildException;
import com.easyinsight.validated.*;

import java.util.Calendar;
import java.io.File;

/**
 * User: James Boe
 * Date: Apr 22, 2009
 * Time: 1:30:17 PM
 */
public class ArtifactSize extends Task {
    private String artifactPath;
    private String key;
    private String secretKey;
    private String component;
    private String sizeAPIKey;

    public void setArtifactPath(String artifactPath) {
        this.artifactPath = artifactPath;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public void setComponent(String component) {
        this.component = component;
    }

    public void setSizeAPIKey(String sizeAPIKey) {
        this.sizeAPIKey = sizeAPIKey;
    }

    @Override
    public void execute() throws BuildException {
        validateProperties();
        try {
            BasicAuthValidatedPublish service = new BasicAuthValidatingPublishServiceServiceLocator().getBasicAuthValidatingPublishServicePort();
            ((BasicAuthValidatingPublishServiceServiceSoapBindingStub)service).setUsername(key);
            ((BasicAuthValidatingPublishServiceServiceSoapBindingStub)service).setPassword(secretKey);
            DayWhere dayWhere = new DayWhere();
            Calendar cal = Calendar.getInstance();
            dayWhere.setKey("Date");
            dayWhere.setDayOfYear(cal.get(Calendar.DAY_OF_YEAR));
            dayWhere.setYear(cal.get(Calendar.YEAR));
            StringWhere componentWhere = new StringWhere();
            componentWhere.setKey("Component");
            componentWhere.setValue(component);
            Where where = new Where();
            where.setDayWheres(new DayWhere[] { dayWhere });
            where.setStringWheres(new StringWhere[] { componentWhere });
            Row row = new Row();
            StringPair componentPair = new StringPair("Component", component);
            DatePair datePair = new DatePair("Date", cal);
            File baseDir = getProject().getBaseDir();
            File file = new File(baseDir, artifactPath);
            if (!file.exists()) {
                throw new BuildException("Could not find artifact " + file.getAbsolutePath());
            }
            NumberPair sizePair = new NumberPair("Size", file.length());
            row.setStringPairs( new StringPair[] { componentPair });
            row.setDatePairs(new DatePair[] { datePair });
            row.setNumberPairs(new NumberPair[] { sizePair });
            service.updateRow(sizeAPIKey, row, where);
        } catch (Exception e) {
            throw new BuildException(e);
        }
    }

    private void validateProperties() throws BuildException {
        if (sizeAPIKey == null) {
            throw new BuildException("sizeAPIKey must be set to the API key of the data source you are publishing into.");
        }
        if (component == null) {
            throw new BuildException("You must set a component for the publish--for example, the Application or Module you are publishing results from.");
        }
        if (key == null) {
            throw new BuildException("You must set a key for the publish. You can find the key under Account/API Administration.");
        }
        if (secretKey == null) {
            throw new BuildException("You must set a secretKey for the publish. You can find the key under Account/API Administration.");
        }
        if (artifactPath == null) {
            throw new BuildException("You must set an artifactPath property. This property will match with the value used in the mxmlc ant task.");
        }
    }
}
