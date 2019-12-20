package org.ranapat.examples.githubbrowser.api.map;

import org.json.JSONException;
import org.json.JSONObject;
import org.ranapat.examples.githubbrowser.data.entity.Configuration;

public class ConfigurationParser implements Parseable<Configuration> {
    @Override
    public Configuration parse(final JSONObject jsonObject) throws JSONException {
        final Configuration configuration = new Configuration(
                jsonObject.getJSONObject("links").getString("organizationMembers"),
                jsonObject.getJSONObject("links").getString("userInfo")
        );

        return configuration;
    }
}
