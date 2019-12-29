package org.ranapat.examples.githubbrowser.api.rao;

import org.json.JSONObject;
import org.ranapat.examples.githubbrowser.api.map.OrganizationParser;
import org.ranapat.examples.githubbrowser.data.entity.Organization;
import org.ranapat.hal.Hal;

import java.util.HashMap;

import okhttp3.Request;
import okhttp3.Response;

public class OrganizationRao extends BaseRao {
    public OrganizationRao() {
        super();
    }

    public Organization fetchByLogin(final String organizationUrl, final String login) throws Exception {
        try {
            return doFetchByLogin(organizationUrl, login);
        } catch (final Exception exception) {
            logException(exception, "OrganizationRao::fetchByLogin");

            throw exception;
        }
    }

    private Organization doFetchByLogin(final String organizationUrl, final String login) throws Exception {
        final Request request = new Request.Builder()
                .url(Hal.safe(organizationUrl, new HashMap<String, Object>() {{
                    put("login", login);
                }}))
                .build();

        final Response response = getResponse(request);
        final String responseString = getResponseString(response);
        final JSONObject json = new JSONObject(responseString);
        final OrganizationParser organizationParser = new OrganizationParser();

        return organizationParser.parse(json);
    }
}
