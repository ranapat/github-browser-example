package org.ranapat.examples.githubbrowser.api.map;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;
import org.ranapat.examples.githubbrowser.data.entity.UserDetails;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

import gherkin.lexer.Encoding;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class UserDetailsParserTest {

    @Test
    public void shouldGetFields() throws Exception {
        final UserDetailsParser userDetailsParser = new UserDetailsParser();
        final String data = new String(Files.readAllBytes(Paths.get(
                getClass().getClassLoader().getResource("mocks/users/User.json").getPath()
        )), Encoding.DEFAULT_ENCODING);
        final JSONObject jsonObject = new JSONObject(data);
        final UserDetails userDetails = userDetailsParser.parse(jsonObject);

        final DateFormat formatterUTC = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        formatterUTC.setTimeZone(TimeZone.getTimeZone("UTC"));

        assertEquals(2637602L, userDetails.userId);
        assertEquals("Brian Anglin", userDetails.name);
        assertEquals("@github ", userDetails.company);
        assertEquals("http://briananglin.me", userDetails.blog);
        assertEquals("Los Angeles", userDetails.location);
        assertEquals("some@email.com", userDetails.email);
        assertEquals("\uD83D\uDC4B I love all software & security things. Application Security @GitHub Past: @snapchat CS `18  USC ", userDetails.bio);
        assertTrue(userDetails.hireable);
        assertEquals(98, userDetails.publicRepos);
        assertEquals(71, userDetails.followers);
        assertEquals(38, userDetails.publicGists);
        assertEquals("2012-10-24T03:44:53Z", formatterUTC.format(userDetails.remoteCreatedAt));
        assertEquals("2019-12-23T23:17:36Z", formatterUTC.format(userDetails.remoteUpdatedAt));
    }

    @Test
    public void shouldNotCrashForEmptyTest() throws Exception {
        final UserDetailsParser userDetailsParser = new UserDetailsParser();
        final String data = new String(Files.readAllBytes(Paths.get(
                getClass().getClassLoader().getResource("mocks/users/User.empty.json").getPath()
        )), Encoding.DEFAULT_ENCODING);
        final JSONObject jsonObject = new JSONObject(data);
        final UserDetails userDetails = userDetailsParser.parse(jsonObject);

        assertEquals(4389652L, userDetails.userId);
        assertNull(userDetails.name);
        assertNull(userDetails.company);
        assertNull(userDetails.blog);
        assertNull(userDetails.location);
        assertNull(userDetails.email);
        assertNull(userDetails.bio);
        assertFalse(userDetails.hireable);
        assertEquals(0, userDetails.publicRepos);
        assertEquals(0, userDetails.followers);
        assertEquals(0, userDetails.publicGists);
        assertNull(userDetails.remoteCreatedAt);
        assertNull(userDetails.remoteUpdatedAt);

    }

    @Test(expected = JSONException.class)
    public void shouldThrow() throws Exception {
        final UserDetailsParser userDetailsParser = new UserDetailsParser();
        final JSONObject jsonObject = new JSONObject("{}");
        final UserDetails userDetails = userDetailsParser.parse(jsonObject);
    }

}
