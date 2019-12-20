package org.ranapat.examples.githubbrowser.api.exceptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class RequestFailedException extends IOException {
    private static int index = 0;
    public static void reset() {
        index = 0;
    }

    private final int code;
    private final String responseBody;

    public RequestFailedException(final int code, final String responseBody) {
        super("Api Request failed N" + index++);

        this.code = code;
        this.responseBody = responseBody;
    }

    public int getCode() {
        return code;
    }

    public String getResponseBody() {
        return responseBody;
    }

    public JSONObject getResponseJson() throws JSONException {
        return new JSONObject(responseBody);
    }
}
