package com.easyinsight.datafeeds.basecampnext;

import com.easyinsight.datafeeds.FeedDefinition;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import org.apache.commons.httpclient.HttpClient;
import org.jetbrains.annotations.NotNull;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

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

    public void populate(FeedDefinition parentDefinition)  {
        HttpClient httpClient = new HttpClient();
        DateTimeFormatter format = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ssZ");
        DateTimeFormatter altFormat = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss.SSSZZ");
        projects = new ArrayList<Project>();
        //JSONObject me = runJSONRequestForObject("/people/me.json", (BasecampNextCompositeSource) parentDefinition, httpClient);
        BasecampNextCompositeSource basecampNextCompositeSource = (BasecampNextCompositeSource) parentDefinition;
        JSONArray jsonArray = runJSONRequest("projects.json", basecampNextCompositeSource, httpClient);
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject projectObject = (JSONObject) jsonArray.get(i);
            String projectURL = "https://basecamp.com/"+basecampNextCompositeSource.getEndpoint()+"/projects/" + getValue(projectObject,"id");
            Date updatedAt;
            try {
                updatedAt = format.parseDateTime(getValue(projectObject,"updated_at")).toDate();
            } catch (Exception e) {
                updatedAt = altFormat.parseDateTime(getValue(projectObject,"updated_at")).toDate();
            }
            projects.add(new Project(String.valueOf(getValue(projectObject, "id")),
                    getValue(projectObject,"name"),
                    getValue(projectObject,"description"),
                    projectURL,
                    updatedAt,
                    (Boolean) projectObject.get("archived")));
        }
        jsonArray = runJSONRequest("projects/archived.json", (BasecampNextCompositeSource) parentDefinition, httpClient);
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject projectObject = (JSONObject) jsonArray.get(i);
            String projectURL = "https://basecamp.com/"+basecampNextCompositeSource.getEndpoint()+"/projects/" + getValue(projectObject,"id");
            Date updatedAt;
            try {
                updatedAt = format.parseDateTime(getValue(projectObject,"updated_at")).toDate();
            } catch (Exception e) {
                updatedAt = altFormat.parseDateTime(getValue(projectObject,"updated_at")).toDate();
            }
            projects.add(new Project(String.valueOf(getValue(projectObject, "id")),
                    getValue(projectObject,"name"),
                    getValue(projectObject,"description"),
                    projectURL,
                    updatedAt, (Boolean) projectObject.get("archived")));
        }
    }

    @NotNull
    @Override
    protected List<String> getKeys(FeedDefinition parentDefinition) {
        throw new UnsupportedOperationException();
    }
}
