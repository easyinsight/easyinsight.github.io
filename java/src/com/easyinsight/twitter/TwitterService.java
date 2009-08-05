package com.easyinsight.twitter;

import java.util.*;
import java.text.SimpleDateFormat;
import java.text.DateFormat;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.GetMethod;

import nu.xom.Node;
import nu.xom.Nodes;
import nu.xom.Builder;
import com.easyinsight.logging.LogClass;

/**
 * User: jamesboe
 * Date: Jul 31, 2009
 * Time: 1:56:25 PM
 */
public class TwitterService {
    public List<Tweet> getTweets() throws Exception {
        try {
            List<Tweet> tweets = new ArrayList<Tweet>();
            HttpClient httpClient = new HttpClient();
            HttpMethod getMethod = new GetMethod("http://twitter.com/statuses/user_timeline/61808445.rss");
            httpClient.executeMethod(getMethod);
            Builder builder = new Builder();
            nu.xom.Document doc;
            doc = builder.build(getMethod.getResponseBodyAsStream());
            Nodes items = doc.query("/rss/channel/item");
            TimeZone timeZone = TimeZone.getTimeZone("gmt");
            Calendar cal = Calendar.getInstance(timeZone);
            DateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy HH:mm:ss");
            dateFormat.setCalendar(cal);
            for (int i = 0; i < items.size(); i++) {
                Node node = items.get(i);
                String title = node.query("title").get(0).getValue().substring(13);
                String dateString = node.query("pubDate").get(0).getValue();
                String dateSubString = dateString.substring(5, 25);
                Date date = dateFormat.parse(dateSubString);
                Tweet tweet = new Tweet();
                tweet.setStatus(title);
                tweets.add(tweet);
            }
            tweets = tweets.subList(0, 3);
            return tweets;
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) throws Exception {
        new TwitterService().getTweets();
    }
}
