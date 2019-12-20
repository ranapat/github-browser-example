package org.ranapat.examples.githubbrowser.api.exceptions;

import org.json.JSONException;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public class RequestFailedExceptionTest {

    @Test
    public void shouldGetFields() {
        RequestFailedException.reset();
        final RequestFailedException requestFailedException = new RequestFailedException(1, "body");
        assertEquals(1, requestFailedException.getCode());
        assertEquals("body", requestFailedException.getResponseBody());
        assertEquals("Api Request failed N0", requestFailedException.getMessage());
        assertEquals("org.ranapat.examples.githubbrowser.api.exceptions.RequestFailedException: Api Request failed N0", requestFailedException.toString());
    }

    @Test
    public void shouldGetJSONObject() throws Exception {
        final RequestFailedException requestFailedException = new RequestFailedException(1, "{\"key\":\"value\"}");
        assertEquals("{\"key\":\"value\"}", requestFailedException.getResponseBody());
        assertEquals("value", requestFailedException.getResponseJson().getString("key"));
    }

    @Test(expected = JSONException.class)
    public void shouldThrowsCase1() throws Exception {
        final RequestFailedException requestFailedException = new RequestFailedException(1, "{\"key\":\"value\"}");
        assertEquals("{\"key\":\"value\"}", requestFailedException.getResponseBody());
        assertEquals("value", requestFailedException.getResponseJson().getString("key1"));
    }

    @Test(expected = JSONException.class)
    public void shouldThrowsCase2() throws Exception {
        final RequestFailedException requestFailedException = new RequestFailedException(1, "");
        requestFailedException.getResponseJson();
    }

    @Test
    public void shouldConstructIndexesCorrectly() {
        RequestFailedException.reset();
        final RequestFailedException requestFailedException1 = new RequestFailedException(1, "");
        assertThat(requestFailedException1.getMessage(), is(equalTo("Api Request failed N0")));
        final RequestFailedException requestFailedException2 = new RequestFailedException(1, "");
        assertThat(requestFailedException2.getMessage(), is(equalTo("Api Request failed N1")));
        RequestFailedException.reset();
        final RequestFailedException requestFailedException3 = new RequestFailedException(1, "");
        assertThat(requestFailedException3.getMessage(), is(equalTo("Api Request failed N0")));
        final RequestFailedException requestFailedException4 = new RequestFailedException(1, "");
        assertThat(requestFailedException4.getMessage(), is(equalTo("Api Request failed N1")));
    }

}