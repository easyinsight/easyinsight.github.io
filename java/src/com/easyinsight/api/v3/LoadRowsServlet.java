package com.easyinsight.api.v3;


import com.easyinsight.analysis.IRow;
import com.easyinsight.api.ServiceRuntimeException;
import com.easyinsight.core.NamedKey;
import com.easyinsight.database.Database;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.FeedDefinition;
import com.easyinsight.datafeeds.FeedStorage;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.logging.LogClass;
import com.easyinsight.security.SecurityUtil;
import com.easyinsight.storage.DataStorage;
import nu.xom.Document;
import nu.xom.Element;
import nu.xom.Node;
import nu.xom.Nodes;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Map;

/**
 * User: jamesboe
 * Date: 1/3/11
 * Time: 1:35 PM
 */
public class LoadRowsServlet extends APIServlet {

    @Override
    protected ResponseInfo processXML(Document document, EIConnection conn) {
        throw new UnsupportedOperationException();
    }
}
