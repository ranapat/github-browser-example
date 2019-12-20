package org.ranapat.examples.githubbrowser.api.transformer;

import org.ranapat.examples.githubbrowser.data.entity.DataEntity;

interface Transformable {
    String getJsonString(final DataEntity dataEntity) throws ClassCastException;
}
