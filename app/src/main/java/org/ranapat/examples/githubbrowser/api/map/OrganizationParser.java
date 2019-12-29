package org.ranapat.examples.githubbrowser.api.map;

import org.json.JSONException;
import org.json.JSONObject;
import org.ranapat.examples.githubbrowser.data.entity.Organization;

import java.util.Date;

public class OrganizationParser implements Parseable<Organization> {
    @Override
    public Organization parse(final JSONObject jsonObject) throws JSONException {
        return new Organization(
                jsonObject.getLong("id"),
                jsonObject.getString("login"),
                new Date()
        );
    }
}
