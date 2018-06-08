package org.activiti.cloud.connectors.twitter.model;

import java.util.HashMap;
import java.util.Map;

public class Home {

    private String welcome = "Hello From the Trending Topic Campaigns: Dummy Twitter Connector Service";
    private Map<String, String> properties = new HashMap<>();

    public Home() {
    }

    public String getWelcome() {
        return welcome;
    }

    public void setWelcome(String welcome) {
        this.welcome = welcome;
    }

    public Map<String, String> getProperties() {
        return properties;
    }

    public void setProperty(String key, String value){
        properties.put(key, value);
    }

    public void setProperties(Map<String, String> properties) {
        this.properties = properties;
    }
}
