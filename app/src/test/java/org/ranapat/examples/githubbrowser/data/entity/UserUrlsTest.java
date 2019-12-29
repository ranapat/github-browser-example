package org.ranapat.examples.githubbrowser.data.entity;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class UserUrlsTest {

    @Test
    public void shouldSetAllFields() {
        final UserUrls userUrls = new UserUrls(
                1,
                "url",
                "followersUrl",
                "followingUrl",
                "starredUrl",
                "subscriptionsUrl",
                "organizationsUrl",
                "reposUrl",
                "eventsUrl",
                "receivedEventsUrl"
        );
        assertThat(userUrls.userId, is(equalTo(1L)));
        assertThat(userUrls.url, is(equalTo("url")));
        assertThat(userUrls.followersUrl, is(equalTo("followersUrl")));
        assertThat(userUrls.followingUrl, is(equalTo("followingUrl")));
        assertThat(userUrls.starredUrl, is(equalTo("starredUrl")));
        assertThat(userUrls.subscriptionsUrl, is(equalTo("subscriptionsUrl")));
        assertThat(userUrls.organizationsUrl, is(equalTo("organizationsUrl")));
        assertThat(userUrls.reposUrl, is(equalTo("reposUrl")));
        assertThat(userUrls.eventsUrl, is(equalTo("eventsUrl")));
        assertThat(userUrls.receivedEventsUrl, is(equalTo("receivedEventsUrl")));
    }

}