package com.easyinsight.users;

/**
 * User: jamesboe
 * Date: Aug 26, 2009
 * Time: 11:43:38 AM
 */
public class TokenSpecification {
    private String name;
    private int type;
    private boolean defined;
    private String urlToConfigure;

    public String getUrlToConfigure() {
        return urlToConfigure;
    }

    public void setUrlToConfigure(String urlToConfigure) {
        this.urlToConfigure = urlToConfigure;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public boolean isDefined() {
        return defined;
    }

    public void setDefined(boolean defined) {
        this.defined = defined;
    }
}
