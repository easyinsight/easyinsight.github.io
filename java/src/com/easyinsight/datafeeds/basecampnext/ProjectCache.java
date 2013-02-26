package com.easyinsight.datafeeds.basecampnext;

import com.easyinsight.datafeeds.FeedDefinition;
import org.apache.commons.httpclient.HttpClient;
import org.jetbrains.annotations.NotNull;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * User: jamesboe
 * Date: 8/1/12
 * Time: 10:07 AM
 */
public class ProjectCache extends BasecampNextBaseSource {
    private List<Project> projects;

    public List<Project> getProjects() {
        return projects;
    }

    public void populate(FeedDefinition parentDefinition) throws JSONException {
        HttpClient httpClient = new HttpClient();
        DateTimeFormatter format = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ssZ");
        DateTimeFormatter altFormat = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss.SSSZZ");
        projects = new ArrayList<Project>();
        //JSONObject me = runJSONRequestForObject("/people/me.json", (BasecampNextCompositeSource) parentDefinition, httpClient);
        BasecampNextCompositeSource basecampNextCompositeSource = (BasecampNextCompositeSource) parentDefinition;
        JSONArray jsonArray = runJSONRequest("projects.json", basecampNextCompositeSource, httpClient);
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject projectObject = jsonArray.getJSONObject(i);
            String projectURL = "https://basecamp.com/"+basecampNextCompositeSource.getEndpoint()+"/projects/" + projectObject.getString("id");
            Date updatedAt;
            try {
                updatedAt = format.parseDateTime(projectObject.getString("updated_at")).toDate();
            } catch (Exception e) {
                updatedAt = altFormat.parseDateTime(projectObject.getString("updated_at")).toDate();
            }
            projects.add(new Project(String.valueOf(projectObject.getInt("id")),
                    projectObject.getString("name"),
                    projectObject.getString("description"),
                    projectURL,
                    updatedAt,
                    projectObject.getBoolean("archived")));
        }
        jsonArray = runJSONRequest("projects/archived.json", (BasecampNextCompositeSource) parentDefinition, httpClient);
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject projectObject = jsonArray.getJSONObject(i);
            String projectURL = "https://basecamp.com/"+basecampNextCompositeSource.getEndpoint()+"/projects/" + projectObject.getString("id");
            Date updatedAt;
            try {
                updatedAt = format.parseDateTime(projectObject.getString("updated_at")).toDate();
            } catch (Exception e) {
                updatedAt = altFormat.parseDateTime(projectObject.getString("updated_at")).toDate();
            }
            projects.add(new Project(String.valueOf(projectObject.getInt("id")),
                    projectObject.getString("name"),
                    projectObject.getString("description"),
                    projectURL,
                    updatedAt, projectObject.getBoolean("archived")));
        }
    }

    @NotNull
    @Override
    protected List<String> getKeys(FeedDefinition parentDefinition) {
        throw new UnsupportedOperationException();
    }
}
