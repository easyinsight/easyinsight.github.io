package com.easyinsight.twitter;

import java.util.List;
import java.util.Timer;
import java.util.Date;
import java.util.ArrayList;

/**
 * User: jamesboe
 * Date: Aug 6, 2009
 * Time: 11:41:22 AM
 */
public class TwitterTimer {

    private static List<Tweet> tweets = new ArrayList<Tweet>();
    private static Timer timer;

    public static void updateTweets(List<Tweet> tweets) {
        TwitterTimer.tweets = tweets;
    }

    public static List<Tweet> getTweets() {
        return tweets;
    }

    public static void start() {
        timer = new Timer();
        timer.scheduleAtFixedRate(new TwitterTimerTask(), new Date(), 1000 * 60 * 60);
    }

    public static void stop() {
        timer.cancel();
    }
}
