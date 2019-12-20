package org.ranapat.examples.githubbrowser.api.rao;

import org.ranapat.examples.githubbrowser.api.exceptions.RequestFailedException;
import org.ranapat.examples.githubbrowser.api.tools.LogDecoration;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.Buffer;
import timber.log.Timber;

public abstract class BaseRao {
    protected final OkHttpClient okHttpClient;

    public BaseRao() {
        okHttpClient = getOkHttpClient();
    }

    protected OkHttpClient getOkHttpClient() {
        return new OkHttpClient();
    }

    protected Response getResponse(final Request request) throws IOException {
        try {
            return okHttpClient.newCall(request).execute();
        } catch (final IOException exception) {
            logException(
                    exception,
                    request.toString(),
                    LogDecoration.getInstance().getCallerClassAndMethod(getClass())
            );

            throw exception;
        }
    }

    protected String getResponseString(final Response response) throws IOException {
        final String responseString = response.body().string();
        if (!response.isSuccessful()) {
            final RequestFailedException requestFailedException = new RequestFailedException(response.code(), responseString);

            final Request request = response.request();
            logException(
                    requestFailedException,
                    "Request & Response Details:\n"
                            + "Request: " + (request != null ? request.toString() : "undefined") + "\n"
                            + "Request Body: " + (request != null ? filterBodyForTrace(requestBodyToString(request.body())) : "undefined") + "\n"
                            + "Response: " + response.toString() + "\n"
                            + "Response Body: " + filterBodyForTrace(responseString),
                    LogDecoration.getInstance().getCallerClassAndMethod(getClass())
            );

            throw requestFailedException;
        }

        return responseString;
    }

    protected void logException(final Throwable throwable, final String... message) {
        final StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("--- exception ---" + "\n");

        for (int i = 0; i < message.length - 1; ++i) {
            Timber.e(message[i]);
        }
        Timber.e(throwable, message[message.length - 1]);
    }

    protected String requestBodyToString(final RequestBody requestBody) {
        try {
            final Buffer buffer = new Buffer();
            requestBody.writeTo(buffer);
            return buffer.readUtf8();
        } catch (final Exception exception) {
            return "";
        }
    }

    protected String filterBodyForTrace(final String body) {
        return body
                .replaceAll("\"userId\"\\W*:\\W*\"([^\"]+)\"", "\"userId\":\"********\"")
                .replaceAll("\"username\"\\W*:\\W*\"([^\"]+)\"", "\"username\":\"********\"")
                .replaceAll("\"password\"\\W*:\\W*\"([^\"]+)\"", "\"password\":\"********\"")
                .replaceAll("\"token\"\\W*:\\W*\"([^\"]+)\"", "\"token\":\"********\"")
                .replaceAll("\"refreshToken\"\\W*:\\W*\"([^\"]+)\"", "\"refreshToken\":\"********\"");
    }
}
