package com.easyinsight.api;

import org.apache.cxf.phase.AbstractPhaseInterceptor;
import org.apache.cxf.phase.Phase;
import org.apache.cxf.message.Message;
import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.binding.soap.interceptor.AbstractSoapInterceptor;
import org.apache.cxf.binding.soap.SoapMessage;
import org.apache.cxf.configuration.security.AuthorizationPolicy;
import org.w3c.dom.bootstrap.DOMImplementationRegistry;
import static org.w3c.dom.bootstrap.DOMImplementationRegistry.*;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSSerializer;
import org.w3c.dom.Node;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.servlet.ServletRequest;
import javax.wsdl.Input;
import java.io.InputStream;
import java.io.IOException;
import java.util.List;
import java.util.Date;

import com.easyinsight.security.SecurityUtil;
import com.easyinsight.security.PasswordService;
import com.easyinsight.database.Database;
import com.easyinsight.users.BandwidthUsage;
import com.easyinsight.users.UserServiceResponse;
import com.easyinsight.users.Account;

/**
 * Created by IntelliJ IDEA.
 * User: abaldwin
 * Date: Mar 31, 2009
 * Time: 4:13:53 PM
 */
public class MessageThrottlingInterceptor extends AbstractPhaseInterceptor {

    public static final long INDIVIDUAL_MAX = 50000000;
    public static final long PROFESSIONAL_MAX = 200000000;
    public static final long ENTERPRISE_MAX = 1000000000;
    public static final long ADMINISTRATOR_MAX = Long.MAX_VALUE;

    public MessageThrottlingInterceptor() {
        super(Phase.RECEIVE);
    }

    public void handleMessage(Message message) throws Fault {
        InputStream is = message.getContent(InputStream.class);
        message.setContent(InputStream.class, new CountingInputStream(is));
        message.getInterceptorChain().add(new MessageThrottlingInterceptorEnd());
    }

    public class MessageThrottlingInterceptorEnd extends AbstractPhaseInterceptor {

        public MessageThrottlingInterceptorEnd() {
            super(Phase.PRE_INVOKE);
        }

        public void handleMessage(Message message) throws Fault {
            CountingInputStream cs = (CountingInputStream) message.getContent(InputStream.class);

            //query="SELECT bu FROM BandwidthUsage bu WHERE bu.accountId = :accountId AND bu.bandwidthDate = current_date()") })

            BandwidthUsage bu;
            Session s = Database.instance().createSession();
            try {
                UserServiceResponse account = getAccount(message);
                s.beginTransaction();
                List buResults = s.createQuery("FROM BandwidthUsage where accountId = ? AND bandwidthDate = current_date()").setLong(0, account.getAccountID()).list();
                if(buResults.size() == 0) {
                    bu = new BandwidthUsage();
                    bu.setBandwidthDate(new Date());
                    bu.setAccountId(account.getAccountID());
                }
                else if(buResults.size() == 1)
                    bu = (BandwidthUsage) buResults.get(0);
                else
                    throw new RuntimeException("Multiple bandwidth results for " + new Date() + " and account ID " + SecurityUtil.getAccountID());

                long bandwidthUsage = bu.getUsedBandwidth();
                bu.setUsedBandwidth(bu.getUsedBandwidth() + cs.getCount());

                s.save(bu);
                s.getTransaction().commit();
                if(bandwidthUsage > getMaxCount(account.getAccountType())) {
                    throw new Fault(new Exception("Exceeded your limit."));
                }
            }
            catch(Fault f) {
                throw f;
            }
            catch(Exception e) {
                s.getTransaction().rollback();
                throw new RuntimeException(e);
            }
            finally {
                s.close();
            }

        }

        public UserServiceResponse getAccount(Message message) {
            AuthorizationPolicy policy = message.get(AuthorizationPolicy.class);
            return SecurityUtil.authenticateToResponse(policy.getUserName(), PasswordService.getInstance().encrypt(policy.getPassword()));
        }

        public long getMaxCount(int tier) {
            switch(tier) {
                case Account.INDIVIDUAL:
                    return INDIVIDUAL_MAX;
                case Account.PROFESSIONAL:
                    return PROFESSIONAL_MAX;
                case Account.ENTERPRISE:
                    return ENTERPRISE_MAX;
                case Account.ADMINISTRATOR:
                    return ADMINISTRATOR_MAX;
                default:
                    throw new RuntimeException("Users that aren't Individual, Professional, Enterprise, or Administrator tier should not be accessing the API.");
            }
        }
    }
}
