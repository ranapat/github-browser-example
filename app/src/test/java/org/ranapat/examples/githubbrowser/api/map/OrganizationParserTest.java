package org.ranapat.examples.githubbrowser.api.map;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;
import org.ranapat.examples.githubbrowser.data.entity.Organization;

import java.nio.file.Files;
import java.nio.file.Paths;

import gherkin.lexer.Encoding;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class OrganizationParserTest {

    @Test
    public void shouldGetFields() throws Exception {
        final OrganizationParser organizationParser = new OrganizationParser();
        final String data = new String(Files.readAllBytes(Paths.get(
                getClass().getClassLoader().getResource("mocks/organizations/Organization.json").getPath()
        )), Encoding.DEFAULT_ENCODING);
        final JSONObject jsonObject = new JSONObject(data);
        final Organization organization = organizationParser.parse(jsonObject);

        assertEquals(59331769, organization.id);
        assertEquals("ranapat-org", organization.login);
        assertNotNull(organization.updatedAt);
    }

    @Test(expected = JSONException.class)
    public void shouldThrow() throws Exception {
        final OrganizationParser organizationParser = new OrganizationParser();
        final JSONObject jsonObject = new JSONObject("{}");
        final Organization organization = organizationParser.parse(jsonObject);
    }

}