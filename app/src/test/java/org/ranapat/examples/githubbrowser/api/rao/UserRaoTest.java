package org.ranapat.examples.githubbrowser.api.rao;

import org.json.JSONException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.ranapat.examples.githubbrowser.api.exceptions.RequestFailedException;
import org.ranapat.examples.githubbrowser.data.entity.Organization;
import org.ranapat.examples.githubbrowser.data.entity.User;
import org.ranapat.examples.githubbrowser.management.NetworkManager;
import org.ranapat.instancefactory.InstanceFactory;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import gherkin.lexer.Encoding;
import okhttp3.HttpUrl;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;

public class UserRaoTest {

    @Before
    public void beforeTest() {
        final NetworkManager networkManager = mock(NetworkManager.class);

        InstanceFactory.set(networkManager, NetworkManager.class);
    }

    @After
    public void afterTest() {
        InstanceFactory.remove(NetworkManager.class);
    }

    @Test
    public void shouldReturnMembersList() throws Exception {
        final MockWebServer server = new MockWebServer();

        final HttpUrl baseUrl = server.url("");

        server.enqueue(
                new MockResponse()
                        .setBody(
                                new String(Files.readAllBytes(Paths.get(
                                        getClass().getClassLoader().getResource("mocks/users/Organization.members.json").getPath()
                                )), Encoding.DEFAULT_ENCODING)
                        )
        );

        final Organization organization = new Organization(1, "login", null);
        final UserRao userRao = new UserRao();

        final List<User> users = userRao.fetchMemberList(baseUrl.toString(), organization, 1);

        assertThat(server.takeRequest().getRequestUrl().toString(), is(equalTo(baseUrl.toString())));

        assertThat(users.size(), is(equalTo(1)));
        assertThat(users.get(0).id, is(equalTo(4389652L)));
        assertThat(users.get(0).organizationId, is(equalTo(1L)));
        assertThat(users.get(0).login, is(equalTo("ranapat")));
        assertThat(users.get(0).avatarUrl, is(equalTo("https://avatars0.githubusercontent.com/u/4389652?v=4")));
        assertThat(users.get(0).type, is(equalTo("User")));
        assertThat(users.get(0).siteAdmin, is(equalTo(false)));
        assertThat(users.get(0).updatedAt, is(not(equalTo(null))));
        assertThat(users.get(0).details, is(equalTo(null)));
        assertThat(users.get(0).urls.userId, is(equalTo(4389652L)));
        assertThat(users.get(0).urls.url, is(equalTo("https://api.github.com/users/ranapat")));
        assertThat(users.get(0).urls.followersUrl, is(equalTo("https://api.github.com/users/ranapat/followers")));
        assertThat(users.get(0).urls.followingUrl, is(equalTo("https://api.github.com/users/ranapat/following{/other_user}")));
        assertThat(users.get(0).urls.starredUrl, is(equalTo("https://api.github.com/users/ranapat/starred{/owner}{/repo}")));
        assertThat(users.get(0).urls.subscriptionsUrl, is(equalTo("https://api.github.com/users/ranapat/subscriptions")));
        assertThat(users.get(0).urls.organizationsUrl, is(equalTo("https://api.github.com/users/ranapat/orgs")));
        assertThat(users.get(0).urls.reposUrl, is(equalTo("https://api.github.com/users/ranapat/repos")));
        assertThat(users.get(0).urls.eventsUrl, is(equalTo("https://api.github.com/users/ranapat/events{/privacy}")));
        assertThat(users.get(0).urls.receivedEventsUrl, is(equalTo("https://api.github.com/users/ranapat/received_events")));

        server.shutdown();
    }

    @Test(expected = RequestFailedException.class)
    public void shouldThrowOnBadResponseFromFetchMemberList() throws Exception {
        final MockWebServer server = new MockWebServer();

        final HttpUrl baseUrl = server.url("");

        server.enqueue(
                new MockResponse()
                        .setResponseCode(400)
                        .setBody(
                                new String(Files.readAllBytes(Paths.get(
                                        getClass().getClassLoader().getResource("mocks/users/Organization.members.json").getPath()
                                )), Encoding.DEFAULT_ENCODING)
                        )
        );

        final Organization organization = new Organization(1, "login", null);
        final UserRao userRao = new UserRao();

        final List<User> users = userRao.fetchMemberList(baseUrl.toString(), organization, 1);

        server.shutdown();
    }

    @Test(expected = JSONException.class)
    public void shouldThrowOnBadJsonFromFetchMemberList() throws Exception {
        final MockWebServer server = new MockWebServer();

        final HttpUrl baseUrl = server.url("");

        server.enqueue(
                new MockResponse()
                        .setBody(
                                ""
                        )
        );

        final Organization organization = new Organization(1, "login", null);
        final UserRao userRao = new UserRao();

        final List<User> users = userRao.fetchMemberList(baseUrl.toString(), organization, 1);

        server.shutdown();
    }

    @Test
    public void shouldReturnUser() throws Exception {
        final MockWebServer server = new MockWebServer();

        final HttpUrl baseUrl = server.url("");

        server.enqueue(
                new MockResponse()
                        .setBody(
                                new String(Files.readAllBytes(Paths.get(
                                        getClass().getClassLoader().getResource("mocks/users/User.json").getPath()
                                )), Encoding.DEFAULT_ENCODING)
                        )
        );

        final User user = new User(1, 2, "login", null, null, false, null);
        final UserRao userRao = new UserRao();

        assertNull(user.details);

        final User detailedUser = userRao.fetchDetails(baseUrl.toString(), user);

        assertThat(server.takeRequest().getRequestUrl().toString(), is(equalTo(baseUrl.toString())));

        assertEquals(user, detailedUser);
        assertNotNull(user.details);
        assertThat(user.details.userId, is(equalTo(2637602L)));
        assertThat(user.details.name, is(equalTo("Brian Anglin")));
        assertThat(user.details.company, is(equalTo("@github ")));
        assertThat(user.details.blog, is(equalTo("http://briananglin.me")));
        assertThat(user.details.location, is(equalTo("Los Angeles")));
        assertThat(user.details.email, is(equalTo("some@email.com")));
        assertThat(user.details.bio, is(equalTo("\uD83D\uDC4B I love all software & security things. Application Security @GitHub Past: @snapchat CS `18  USC ")));
        assertThat(user.details.hireable, is(equalTo(true)));
        assertThat(user.details.publicRepos, is(equalTo(98)));
        assertThat(user.details.followers, is(equalTo(71)));
        assertThat(user.details.publicGists, is(equalTo(38)));
        assertNotNull(user.details.remoteCreatedAt);
        assertNotNull(user.details.remoteUpdatedAt);

        server.shutdown();
    }

    @Test(expected = RequestFailedException.class)
    public void shouldThrowOnBadResponseFromFetchDetails() throws Exception {
        final MockWebServer server = new MockWebServer();

        final HttpUrl baseUrl = server.url("");

        server.enqueue(
                new MockResponse()
                        .setResponseCode(400)
                        .setBody(
                                new String(Files.readAllBytes(Paths.get(
                                        getClass().getClassLoader().getResource("mocks/users/Organization.members.json").getPath()
                                )), Encoding.DEFAULT_ENCODING)
                        )
        );

        final User user = new User(1, 2, "login", null, null, false, null);
        final UserRao userRao = new UserRao();

        final User detailedUser = userRao.fetchDetails(baseUrl.toString(), user);

        server.shutdown();
    }

    @Test(expected = JSONException.class)
    public void shouldThrowOnBadJsonFromFetchDetails() throws Exception {
        final MockWebServer server = new MockWebServer();

        final HttpUrl baseUrl = server.url("");

        server.enqueue(
                new MockResponse()
                        .setBody(
                                ""
                        )
        );

        final User user = new User(1, 2, "login", null, null, false, null);
        final UserRao userRao = new UserRao();

        final User detailedUser = userRao.fetchDetails(baseUrl.toString(), user);

        server.shutdown();
    }

}