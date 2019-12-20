package org.ranapat.examples.githubbrowser.api.map;

import org.json.JSONException;
import org.json.JSONObject;
import org.ranapat.examples.githubbrowser.data.entity.DataEntity;

public interface Parseable<T extends DataEntity> {
    T parse(final JSONObject jsonObject) throws JSONException;
}
