package com.easyinsight.twitter;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.GetMethod;

import java.util.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import nu.xom.Builder;
import nu.xom.Nodes;
import nu.xom.Node;
import com.easyinsight.logging.LogClass;

/**
 * User: jamesboe
 * Date: Aug 6, 2009
 * Time: 11:41:30 AM
 */
public class TwitterTimerTask extends TimerTask {
    public void run() {
        try {
            List<Tweet> tweets = new ArrayList<Tweet>();
            HttpClient httpClient = new HttpClient();
            HttpMethod getMethod = new GetMethod("http://twitter.com:80/statuses/user_timeline/61808445.rss");
            getMethod.setFollowRedirects(false);
            httpClient.executeMethod(getMethod);
            Builder builder = new Builder();
            nu.xom.Document doc;
            doc = builder.build(getMethod.getResponseBodyAsStream());
            Nodes items = doc.query("/rss/channel/item");
            TimeZone timeZone = TimeZone.getTimeZone("gmt");
            Calendar cal = Calendar.getInstance(timeZone);
            DateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy HH:mm:ss");
            dateFormat.setCalendar(cal);
            long now = cal.getTimeInMillis();
            for (int i = 0; i < items.size(); i++) {
                Node node = items.get(i);
                String title = node.query("title").get(0).getValue().substring(13);
                String dateString = node.query("pubDate").get(0).getValue();
                String dateSubString = dateString.substring(5, 25);
                Date date = dateFormat.parse(dateSubString);
                long dateMS = date.getTime();
                long delta = now - dateMS;
                String dateVal;
                if (delta < (60 * 60 * 1000)) {
                    dateVal = (delta / (60 * 1000)) + " minutes ago";
                } else if (delta < (60 * 60 * 1000 * 24)) {
                    dateVal = (delta / (60 * 60 * 1000)) + " hours ago";
                } else {
                    dateVal = (delta / (60 * 60 * 1000 * 24)) + " days ago";
                }
                int urlIndex = title.indexOf("http://");
                if (urlIndex != -1) {
                    int endIndex = title.indexOf(" ", urlIndex);
                    String postTitle;
                    if (endIndex == -1) {
                        endIndex = title.length();
                        postTitle = "";
                    } else {
                        postTitle = title.substring(endIndex);
                    }
                    String url = title.substring(urlIndex, endIndex);
                    title = title.substring(0, urlIndex) + "<a href=\"" + url + "\" target=\"blank\"><u>" + url + "</u></a>" + postTitle; 
                }
                title = title + "   <font size=\"10\" color=\"#111111\">" + dateVal + "</font>";
                Tweet tweet = new Tweet();
                tweet.setStatus(title);
                tweet.setTimeString(dateVal);
                tweets.add(tweet);
            }
            tweets = tweets.subList(0, 3);
            TwitterTimer.updateTweets(tweets);
        } catch (Exception e) {
            LogClass.info("Couldn't get info from twitter");
        }
    }
}
