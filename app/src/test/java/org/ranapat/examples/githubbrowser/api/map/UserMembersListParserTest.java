package org.ranapat.examples.githubbrowser.api.map;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;
import org.ranapat.examples.githubbrowser.data.entity.User;

import java.nio.file.Files;
import java.nio.file.Paths;

import gherkin.lexer.Encoding;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class UserMembersListParserTest {

    @Test
    public void shouldGetFields() throws Exception {
        final UserMembersListParser userMembersListParser = new UserMembersListParser(1);
        final String data = new String(Files.readAllBytes(Paths.get(
                getClass().getClassLoader().getResource("mocks/users/Organization.member.json").getPath()
        )), Encoding.DEFAULT_ENCODING);
        final JSONObject jsonObject = new JSONObject(data);
        final User user = userMembersListParser.parse(jsonObject);

        assertEquals(4389652, user.id);
        assertEquals(1, user.organizationId);
        assertEquals("ranapat", user.login);
        assertEquals("https://avatars0.githubusercontent.com/u/4389652?v=4", user.avatarUrl);
        assertEquals("User", user.type);
        assertFalse(user.siteAdmin);
        assertEquals(4389652, user.urls.userId);
        assertEquals("https://api.github.com/users/ranapat", user.urls.url);
        assertEquals("https://api.github.com/users/ranapat/followers", user.urls.followersUrl);
        assertEquals("https://api.github.com/users/ranapat/following{/other_user}", user.urls.followingUrl);
        assertEquals("https://api.github.com/users/ranapat/starred{/owner}{/repo}", user.urls.starredUrl);
        assertEquals("https://api.github.com/users/ranapat/subscriptions", user.urls.subscriptionsUrl);
        assertEquals("https://api.github.com/users/ranapat/orgs", user.urls.organizationsUrl);
        assertEquals("https://api.github.com/users/ranapat/repos", user.urls.reposUrl);
        assertEquals("https://api.github.com/users/ranapat/events{/privacy}", user.urls.eventsUrl);
        assertEquals("https://api.github.com/users/ranapat/received_events", user.urls.receivedEventsUrl);

        assertNotNull(user.updatedAt);
        assertNull(user.details);
    }

    @Test(expected = JSONException.class)
    public void shouldThrow() throws Exception {
        final UserMembersListParser userMembersListParser = new UserMembersListParser(1);
        final JSONObject jsonObject = new JSONObject("{}");
        final User user = userMembersListParser.parse(jsonObject);
    }

}
