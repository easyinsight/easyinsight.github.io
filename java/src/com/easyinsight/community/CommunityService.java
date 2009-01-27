package com.easyinsight.community;

import com.easyinsight.datafeeds.FeedDescriptor;

import java.util.List;

/**
 * User: James Boe
 * Date: Jun 23, 2008
 * Time: 1:05:35 PM
 */
public class CommunityService {
    public List<FeedDescriptor> getFeeds(long communityID) {
        return null;
    }

    // core community page needs to show the various public communities
    // most popular communities?
    // need to be able to create a new community from it
    // that's the community list page, effectively

    // Community.mxml then has the set of available feeds and saved analyses
    // along with some more social aspects...
    // so if I create a private community, you're going to need to set up billing...
    // if I set up salesforce...how's this interaction going to work?
    // rather than pricing on the community...
    // what's the best way to position pricing here...
    // okay, I come in as new user, upload some data
    // best way to model this...
    // if I set up as a professional user
    // it's too complex as it stands
    // what if I instead set up
    // Free User
    

    // get the communities accessible to a user
    
    public List<Community> getCommunitiesForUser(long accountID) {
        
        return null;
    }
}
