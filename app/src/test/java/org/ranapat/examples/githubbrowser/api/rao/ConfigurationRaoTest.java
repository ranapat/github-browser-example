package org.ranapat.examples.githubbrowser.api.rao;

import org.json.JSONException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.ranapat.examples.githubbrowser.Settings;
import org.ranapat.examples.githubbrowser.api.exceptions.RequestFailedException;
import org.ranapat.examples.githubbrowser.data.entity.Configuration;
import org.ranapat.examples.githubbrowser.management.NetworkManager;
import org.ranapat.instancefactory.InstanceFactory;

import java.nio.file.Files;
import java.nio.file.Paths;

import gherkin.lexer.Encoding;
import okhttp3.HttpUrl;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;

public class ConfigurationRaoTest {

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
    public void shouldReturnProperRootApi() {
        final ConfigurationRao configurationRao = new ConfigurationRao();

        assertThat(configurationRao.getConfigApi(), is(equalTo(Settings.configApi)));
    }

    @Test
    public void shouldReturnConfiguration() throws Exception {
        final MockWebServer server = new MockWebServer();

        final HttpUrl baseUrl = server.url("");

        server.enqueue(
                new MockResponse()
                        .setBody(
                                new String(Files.readAllBytes(Paths.get(
                                        getClass().getClassLoader().getResource("mocks/configuration/Configuration.json").getPath()
                                )), Encoding.DEFAULT_ENCODING)
                                        .replaceAll("https?://[^/]+/", baseUrl.toString())
                        )
        );

        final ConfigurationRao configurationRao = new ConfigurationRao(baseUrl.toString());
        final Configuration configuration = configurationRao.fetch();

        assertThat(server.takeRequest().getRequestUrl().toString(), is(equalTo(baseUrl.toString())));

        assertThat(configuration.organizationMembers, is(equalTo("example1")));
        assertThat(configuration.userInfo, is(equalTo("example2")));

        server.shutdown();
    }

    @Test(expected = RequestFailedException.class)
    public void shouldThrowOnBadResponse() throws Exception {
        final MockWebServer server = new MockWebServer();

        final HttpUrl baseUrl = server.url("");

        server.enqueue(
                new MockResponse()
                        .setResponseCode(400)
                        .setBody(
                                new String(Files.readAllBytes(Paths.get(
                                        getClass().getClassLoader().getResource("mocks/configuration/Configuration.json").getPath()
                                )), Encoding.DEFAULT_ENCODING)
                                        .replaceAll("https?://[^/]+/", baseUrl.toString())
                        )
        );

        final ConfigurationRao configurationRao = new ConfigurationRao(baseUrl.toString());
        final Configuration configuration = configurationRao.fetch();

        server.shutdown();
    }

    @Test(expected = JSONException.class)
    public void shouldThrowOnBadJson() throws Exception {
        final MockWebServer server = new MockWebServer();

        final HttpUrl baseUrl = server.url("");

        server.enqueue(
                new MockResponse()
                        .setBody(
                                ""
                        )
        );

        final ConfigurationRao configurationRao = new ConfigurationRao(baseUrl.toString());
        final Configuration configuration = configurationRao.fetch();

        server.shutdown();
    }

}