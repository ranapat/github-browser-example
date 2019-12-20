package org.ranapat.examples.githubbrowser.api.rao;

import org.json.JSONObject;
import org.ranapat.examples.githubbrowser.Settings;
import org.ranapat.examples.githubbrowser.api.map.ConfigurationParser;
import org.ranapat.examples.githubbrowser.data.entity.Configuration;

import okhttp3.Request;
import okhttp3.Response;

public class ConfigurationRao extends BaseRao {
    private final String configApi;

    public ConfigurationRao(final String configApi) {
        super();

        this.configApi = configApi;
    }

    public ConfigurationRao() {
        this(
                Settings.configApi
        );
    }

    public String getConfigApi() {
        return configApi;
    }

    public Configuration fetch() throws Exception {
        try {
            return doFetch();
        } catch (final Exception exception) {
            logException(exception, "ConfigurationRao::fetch");

            throw exception;
        }
    }

    private Configuration doFetch() throws Exception {
        final Request request = new Request.Builder()
                .url(configApi)
                .build();

        final Response response = getResponse(request);
        final String responseString = getResponseString(response);
        final JSONObject json = new JSONObject(responseString);
        final ConfigurationParser configurationParser = new ConfigurationParser();

        return configurationParser.parse(json);
    }
}
