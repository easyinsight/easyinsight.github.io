package com.easyinsight.servlet;

import com.easyinsight.admin.HealthListener;
import com.easyinsight.analysis.CurrencyRetrieval;
import com.easyinsight.analysis.ReportCache;
import com.easyinsight.cache.MemCachedManager;
import com.easyinsight.config.ConfigLoader;
import com.easyinsight.database.Database;
import com.easyinsight.database.migration.Migrations;
import com.easyinsight.datafeeds.DataSourceTypeRegistry;
import com.easyinsight.datafeeds.FeedRegistry;
import com.easyinsight.datafeeds.database.DatabaseListener;
import com.easyinsight.datafeeds.migration.MigrationManager;
import com.easyinsight.export.HtmlResultCache;
import com.easyinsight.security.SecurityUtil;
import com.easyinsight.security.DefaultSecurityProvider;
import com.easyinsight.logging.LogClass;
import com.easyinsight.storage.DatabaseManager;
import com.easyinsight.scheduler.Scheduler;
import com.easyinsight.eventing.*;
import com.easyinsight.scorecard.LongKPIRefreshEvent;
import com.easyinsight.scorecard.LongKPIRefreshListener;
import com.easyinsight.util.ServiceUtil;
import net.spy.memcached.MemcachedClient;
import org.apache.jcs.JCS;

import javax.servlet.http.HttpServlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import java.util.Arrays;
import java.util.List;

/**
 * User: James Boe
 * Date: Jan 11, 2008
 * Time: 2:53:14 PM
 */
public class DMSServlet extends HttpServlet {
}
