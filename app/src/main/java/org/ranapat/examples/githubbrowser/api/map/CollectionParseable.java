package org.ranapat.examples.githubbrowser.api.map;

import org.json.JSONArray;
import org.json.JSONException;
import org.ranapat.examples.githubbrowser.data.entity.DataEntity;

import java.util.List;

public interface CollectionParseable<T extends DataEntity> {
    List<T> parse(final JSONArray jsonArray) throws JSONException;
}