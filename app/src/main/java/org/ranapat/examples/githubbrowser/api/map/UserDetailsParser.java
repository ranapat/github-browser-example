package org.ranapat.examples.githubbrowser.api.map;

import org.json.JSONException;
import org.json.JSONObject;
import org.ranapat.examples.githubbrowser.data.entity.UserDetails;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class UserDetailsParser implements Parseable<UserDetails> {
    @Override
    public UserDetails parse(final JSONObject jsonObject) throws JSONException {
        Date remoteCreatedAt;
        try {
            final DateFormat isoUtcFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault());
            isoUtcFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

            remoteCreatedAt = isoUtcFormat.parse(jsonObject.getString("created_at"));
        } catch (final ParseException | JSONException exception) {
            remoteCreatedAt = null;
        }

        Date remoteUpdatedAt;
        try {
            final DateFormat isoUtcFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault());
            isoUtcFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

            remoteUpdatedAt = isoUtcFormat.parse(jsonObject.getString("updated_at"));
        } catch (final ParseException | JSONException exception) {
            remoteUpdatedAt = null;
        }

        return new UserDetails(
                jsonObject.getLong("id"),
                jsonObject.optString("name", null),
                jsonObject.optString("company", null),
                jsonObject.optString("blog", null),
                jsonObject.optString("location", null),
                jsonObject.optString("email", null),
                jsonObject.optString("bio", null),
                jsonObject.optBoolean("hireable"),
                jsonObject.optInt("public_repos", 0),
                jsonObject.optInt("followers", 0),
                jsonObject.optInt("public_gists", 0),
                remoteCreatedAt, remoteUpdatedAt
        );
    }
}
