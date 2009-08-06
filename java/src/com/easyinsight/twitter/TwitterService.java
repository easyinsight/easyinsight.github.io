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
        return TwitterTimer.getTweets();
    }

    public static void main(String[] args) throws Exception {
        new TwitterService().getTweets();
    }
}
