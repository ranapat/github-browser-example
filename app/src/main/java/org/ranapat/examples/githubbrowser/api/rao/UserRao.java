package org.ranapat.examples.githubbrowser.api.rao;

import org.json.JSONArray;
import org.ranapat.examples.githubbrowser.api.map.UserMembersListParser;
import org.ranapat.examples.githubbrowser.data.entity.Organization;
import org.ranapat.examples.githubbrowser.data.entity.User;
import org.ranapat.hal.Hal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Request;
import okhttp3.Response;

public class UserRao extends BaseRao {
    public UserRao() {
        super();
    }

    public List<User> fetchMemberList(final String organizationMembersUrl, final Organization organization, final int page) throws Exception {
        try {
            return doFetchMemberList(organizationMembersUrl, organization, page);
        } catch (final Exception exception) {
            logException(exception, "UserRao::fetchMemberList");

            throw exception;
        }
    }

    private List<User> doFetchMemberList(final String organizationMembersUrl, final Organization organization, final int page) throws Exception {
        final Request request = new Request.Builder()
                .url(Hal.safe(organizationMembersUrl, new HashMap<String, Object>() {{
                    put("login", organization.login);
                    put("page", page);
                }}))
                .build();

        final Response response = getResponse(request);
        final String responseString = getResponseString(response);
        final JSONArray json = new JSONArray(responseString);
        final UserMembersListParser userMembersListParser = new UserMembersListParser(organization.id);
        final List<User> users = new ArrayList<>();

        final int length = json.length();
        for (int i = 0; i < length; ++i) {
            users.add(userMembersListParser.parse(
                    json.getJSONObject(i)
            ));
        }

        return users;
    }
}
