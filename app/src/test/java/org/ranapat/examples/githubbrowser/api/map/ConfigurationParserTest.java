package org.ranapat.examples.githubbrowser.api.map;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;
import org.ranapat.examples.githubbrowser.data.entity.Configuration;

import java.nio.file.Files;
import java.nio.file.Paths;

import gherkin.lexer.Encoding;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ConfigurationParserTest {

    @Test
    public void shouldGetFields() throws Exception {
        final ConfigurationParser configurationParser = new ConfigurationParser();
        final String data = new String(Files.readAllBytes(Paths.get(
                getClass().getClassLoader().getResource("mocks/configuration/Configuration.json").getPath()
        )), Encoding.DEFAULT_ENCODING);
        final JSONObject jsonObject = new JSONObject(data);
        final Configuration configuration = configurationParser.parse(jsonObject);

        assertEquals(1, configuration.id);
        assertEquals(1, configuration.defaultMembersInOrganizationPerPage);
        assertEquals("example1", configuration.organizationMembers);
        assertEquals("example2", configuration.userInfo);
        assertNotNull(configuration.updatedAt);
    }

    @Test(expected = JSONException.class)
    public void shouldThrow() throws Exception {
        final ConfigurationParser configurationParser = new ConfigurationParser();
        final JSONObject jsonObject = new JSONObject("{}");
        final Configuration configuration = configurationParser.parse(jsonObject);
    }

}