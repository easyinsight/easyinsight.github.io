package com.easyinsight.api;

import com.easyinsight.users.UserService;
import org.apache.cxf.phase.AbstractPhaseInterceptor;
import org.apache.cxf.phase.Phase;
import org.apache.cxf.message.Message;
import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.configuration.security.AuthorizationPolicy;
import org.hibernate.Session;

import java.io.InputStream;
import java.util.List;
import java.util.Date;

import com.easyinsight.security.SecurityUtil;
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
                if(cs.getCount() > 10485760) {
                    throw new Fault(new Exception("You cannot send a message larger than 10mb at a time."));
                }
                if(bandwidthUsage > Account.getMaxCount(account.getAccountType())) {
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
            try {
                return SecurityUtil.authenticateKeys(policy.getUserName(), policy.getPassword());
            } catch (com.easyinsight.security.SecurityException e) {
                return new UserService().authenticate(policy.getUserName(), policy.getPassword(), false);
            }

        }
    }
}
