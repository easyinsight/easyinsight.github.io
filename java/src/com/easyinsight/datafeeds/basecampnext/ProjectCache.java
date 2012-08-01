package com.easyinsight.datafeeds.basecampnext;

import com.easyinsight.analysis.IRow;
import com.easyinsight.datafeeds.FeedDefinition;
import org.jetbrains.annotations.NotNull;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
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
        DateTimeFormatter format = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ssZ");
        projects = new ArrayList<Project>();
        JSONArray jsonArray = runJSONRequest("projects.json", (BasecampNextCompositeSource) parentDefinition);
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject projectObject = jsonArray.getJSONObject(i);
            projects.add(new Project(String.valueOf(projectObject.getInt("id")),
                    projectObject.getString("name"),
                    projectObject.getString("description"),
                    projectObject.getString("url"),
                    format.parseDateTime(projectObject.getString("updated_at")).toDate()));
        }
        jsonArray = runJSONRequest("projects/archived.json", (BasecampNextCompositeSource) parentDefinition);
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject projectObject = jsonArray.getJSONObject(i);
            projects.add(new Project(String.valueOf(projectObject.getInt("id")),
                    projectObject.getString("name"),
                    projectObject.getString("description"),
                    projectObject.getString("url"),
                    format.parseDateTime(projectObject.getString("updated_at")).toDate()));
        }
    }

    @NotNull
    @Override
    protected List<String> getKeys(FeedDefinition parentDefinition) {
        throw new UnsupportedOperationException();
    }
}
