package com.easyinsight.analysis;

import org.hibernate.Session;
import com.easyinsight.database.Database;
import com.easyinsight.userupload.UploadPolicy;
import com.easyinsight.logging.LogClass;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;

/**
 * User: James Boe
 * Date: Jun 14, 2008
 * Time: 12:00:09 AM
 */
public class TagStorage {

    public void saveCloud(TagCloud tagCloud) {
        saveCloud(tagCloud, null);
    }
    
    public void saveCloud(TagCloud tagCloud, Connection conn) {
        if (tagCloud.getTagCloudID() != null && tagCloud.getTagCloudID() == 0) {
            tagCloud.setTagCloudID(null);
        }
        Session session;
        if (conn == null) {
            session = Database.instance().createSession();
        } else {
            session = Database.instance().createSession(conn);
        }
        try {
            if (conn == null) session.beginTransaction();
            for (Tag tag : tagCloud.getTags()) {
                if (tag.getTagID() != null && tag.getTagID() == 0) {
                    tag.setTagID(null);
                }
            }
            session.saveOrUpdate(tagCloud);
            session.flush();
            if (conn == null) session.getTransaction().commit();
        } catch (Exception e) {
            LogClass.error(e);
            if (conn == null) session.getTransaction().rollback();
            throw new RuntimeException(e);
        } finally {
            session.close();
        }
    }

    public TagCloud getCloud(Long tagCloudID) {
        Session session = Database.instance().createSession();
        try {
            session.beginTransaction();
            List results = session.createQuery("from TagCloud as tagCloud where tagCloud.tagCloudID = ?").setLong(0, tagCloudID).list();
            TagCloud tagCloud = (TagCloud) results.get(0);
            tagCloud.setTags(new ArrayList<Tag>(tagCloud.getTags()));
            session.getTransaction().commit();
            return tagCloud;
        } catch (Exception e) {
            LogClass.error(e);
            session.getTransaction().rollback();
            throw new RuntimeException(e);
        } finally {
            session.close();
        }
    }    

    public void addTags(TagCloud tagCloud) {
        Session session = Database.instance().createSession();
        try {
            session.beginTransaction();
            // retrieve the previous set of tags...
            List<Tag> existingTags = getExistingTagsFromCloud(tagCloud.getTagCloudID(), session);
            existingTags.removeAll(tagCloud.getTags());
            for (Tag tag : existingTags) {
                detatchTag(tag, session);
            }
            for (Tag tag : tagCloud.getTags()) {
                addTag(tag, session);
            }
            session.getTransaction().commit();
        } catch (Exception e) {
            LogClass.error(e);
            session.getTransaction().rollback();
            throw new RuntimeException(e);
        } finally {
            session.close();
        }
    }

    private void detatchTag(Tag tag, Session session) {
        tag.setUseCount(tag.getUseCount() - 1);
        session.update(tag);
    }

    private List<Tag> getExistingTagsFromCloud(long cloudID, Session session) {
        List tagList = session.createQuery("from TagCloud as tagCloud left join fetch tagCloud.tags").list();
        LogClass.debug(tagList.toString());
        return null;
    }

    public Tag addTag(Tag tag, Session session) {
        Tag returnTag;
        if (tag.getTagID() == null) {
            List tags = session.createQuery("from Tag where tagName = ?").setString(0, tag.getTagName()).list();
            if (tags.isEmpty()) {
                // it's a new tag
                session.save(tag);
                returnTag = tag;
            } else {
                // existing tag found, increment the use and use it instead
                Tag existingTag = (Tag) tags.get(0);
                existingTag.setUseCount(existingTag.getUseCount() + 1);
                session.update(existingTag);
                returnTag = existingTag;
            }
        } else {
            tag.setUseCount(tag.getUseCount() + 1);
            session.update(tag);
            returnTag = tag;
        }
        return returnTag;
    }

    public int determineFeedTagPopularity(String tagName) {
        return 0;
    }

    public int determineAnalysisTagPopularity(String tagName) {
        return 0;
    }

    public List<Tag> getTags(Integer limit) {
        List<Tag> tags = new ArrayList<Tag>();
        Connection conn = Database.instance().getConnection();
        try {
            PreparedStatement queryTagsStmt = conn.prepareStatement("SELECT TAG, COUNT(TAG) FROM ANALYSIS_TAGS, FEED_TO_TAG, ANALYSIS_TO_TAG, DATA_FEED, ANALYSIS WHERE " +
                    "(ANALYSIS_TAGS.ANALYSIS_TAGS_ID = FEED_TO_TAG.ANALYSIS_TAGS_ID AND FEED_TO_TAG.FEED_ID = DATA_FEED.DATA_FEED_ID AND DATA_FEED.MARKETPLACE_VISIBLE = ?) OR " +
                    "(ANALYSIS_TAGS.ANALYSIS_TAGS_ID = ANALYSIS_TO_TAG.ANALYSIS_TAGS_ID AND ANALYSIS_TO_TAG.ANALYSIS_ID = ANALYSIS.ANALYSIS_ID AND ANALYSIS.MARKETPLACE_VISIBLE = ?) " +
                    "GROUP BY TAG");
            queryTagsStmt.setBoolean(1, true);
            queryTagsStmt.setBoolean(2, true);
            ResultSet rs = queryTagsStmt.executeQuery();
            while (rs.next()) {
                String tagName = rs.getString(1);
                if ("".equals(tagName)) {
                    continue;
                }
                int useCount = rs.getInt(2);
                Tag tag = new Tag(tagName);
                tag.setUseCount(useCount);
                tags.add(tag);
            }
        } catch (SQLException e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        } finally {
            Database.instance().closeConnection(conn);
        }
        if (limit != null) {
            Collections.sort(tags, new Comparator<Tag> () {

                public int compare(Tag o1, Tag o2) {
                    return new Integer(o2.getUseCount()).compareTo(o1.getUseCount());
                }
            });
            tags = tags.subList(0, Math.min(tags.size(), limit));
        }
        Collections.sort(tags, new Comparator<Tag> () {

            public int compare(Tag o1, Tag o2) {
                return o1.getTagName().compareTo(o2.getTagName());
            }
        });
        return tags;
    }
}
