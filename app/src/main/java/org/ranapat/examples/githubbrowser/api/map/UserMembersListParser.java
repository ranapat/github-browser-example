package org.ranapat.examples.githubbrowser.api.map;

import org.json.JSONException;
import org.json.JSONObject;
import org.ranapat.examples.githubbrowser.data.entity.User;
import org.ranapat.examples.githubbrowser.data.entity.UserUrls;

import java.util.Date;

public class UserMembersListParser implements Parseable<User> {
    private final long organizationId;

    public UserMembersListParser(final long organizationId) {
        this.organizationId = organizationId;
    }

    @Override
    public User parse(final JSONObject jsonObject) throws JSONException {
        final User user = new User(
                jsonObject.getLong("id"),
                organizationId,
                jsonObject.getString("login"),
                jsonObject.getString("avatar_url"),
                jsonObject.getString("type"),
                jsonObject.getBoolean("site_admin"),
                new Date()
        );

        final UserUrls userUrls = new UserUrls(
                user.id,
                jsonObject.getString("url"),
                jsonObject.getString("followers_url"),
                jsonObject.getString("following_url"),
                jsonObject.getString("starred_url"),
                jsonObject.getString("subscriptions_url"),
                jsonObject.getString("organizations_url"),
                jsonObject.getString("repos_url"),
                jsonObject.getString("events_url"),
                jsonObject.getString("received_events_url")
        );

        user.urls = userUrls;

        return user;
    }
}
