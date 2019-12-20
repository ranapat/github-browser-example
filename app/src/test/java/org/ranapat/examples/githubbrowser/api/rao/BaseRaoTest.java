package org.ranapat.examples.githubbrowser.api.rao;

import androidx.annotation.Nullable;

import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.ranapat.examples.githubbrowser.api.exceptions.RequestFailedException;
import org.ranapat.examples.githubbrowser.management.NetworkManager;
import org.ranapat.instancefactory.InstanceFactory;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.BufferedSink;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class BaseRaoTest {
    static class BaseRaoExtendedTest extends BaseRao {
        public BaseRaoExtendedTest() {
            super();
        }

        @Override
        protected OkHttpClient getOkHttpClient() {
            return mock(OkHttpClient.class);
        }

        public OkHttpClient getOkHttpClientVariable() {
            return okHttpClient;
        }

        public Response getResponse(final Request request) throws IOException {
            return super.getResponse(request);
        }

        public String getResponseString(final Response response) throws IOException {
            return super.getResponseString(response);
        }

        public String filterBodyForTrace(final String body) {
            return super.filterBodyForTrace(body);
        }
    }

    static class BaseRaoPureExtendedTest extends BaseRao {
        public BaseRaoPureExtendedTest() {
            super();
        }
    }

    @Test
    public void shouldConstruct() {
        final NetworkManager networkManager = mock(NetworkManager.class);

        InstanceFactory.set(networkManager, NetworkManager.class);

        final BaseRaoExtendedTest baseRaoExtendedTest = new BaseRaoExtendedTest();

        InstanceFactory.remove(NetworkManager.class);
    }

    @Test
    public void shouldGetNewOkHttpClient() {
        final NetworkManager networkManager = mock(NetworkManager.class);

        InstanceFactory.set(networkManager, NetworkManager.class);

        final BaseRaoPureExtendedTest baseRaoPureExtendedTest = new BaseRaoPureExtendedTest();
        assertThat(baseRaoPureExtendedTest.getOkHttpClient(), is(not(equalTo(null))));

        InstanceFactory.remove(NetworkManager.class);
    }

    @Test
    public void shouldGetResponseWithSuccess() throws IOException {
        final NetworkManager networkManager = mock(NetworkManager.class);

        InstanceFactory.set(networkManager, NetworkManager.class);

        final BaseRaoExtendedTest baseRaoExtendedTest = new BaseRaoExtendedTest();
        final Response response = mock(Response.class);
        final Call call = mock(Call.class);

        when(baseRaoExtendedTest.getOkHttpClientVariable().newCall((Request) any())).thenReturn(call);
        when(call.execute()).thenReturn(response);

        assertThat(baseRaoExtendedTest.getResponse(mock(Request.class)), is(equalTo(response)));

        InstanceFactory.remove(NetworkManager.class);
    }

    @Test(expected = IOException.class)
    public void shouldGetResponseWithFailAndLog() throws IOException {
        final NetworkManager networkManager = mock(NetworkManager.class);

        InstanceFactory.set(networkManager, NetworkManager.class);

        final BaseRaoExtendedTest baseRaoExtendedTest = new BaseRaoExtendedTest();
        final Response response = mock(Response.class);
        final Call call = mock(Call.class);

        when(networkManager.isOnline()).thenReturn(true);
        when(baseRaoExtendedTest.getOkHttpClientVariable().newCall((Request) any())).thenAnswer(new Answer<Call>() {
            @Override
            public Call answer(InvocationOnMock invocation) throws IOException {
                throw new IOException();
            }
        });
        when(call.execute()).thenReturn(response);

        assertThat(baseRaoExtendedTest.getResponse(mock(Request.class)), is(equalTo(response)));
        verify(networkManager, times(1)).isOnline();

        InstanceFactory.remove(NetworkManager.class);
    }

    @Test(expected = IOException.class)
    public void shouldGetResponseWithFailAndNotLog() throws IOException {
        final NetworkManager networkManager = mock(NetworkManager.class);

        InstanceFactory.set(networkManager, NetworkManager.class);

        final BaseRaoExtendedTest baseRaoExtendedTest = new BaseRaoExtendedTest();
        final Response response = mock(Response.class);
        final Call call = mock(Call.class);

        when(networkManager.isOnline()).thenReturn(false);
        when(baseRaoExtendedTest.getOkHttpClientVariable().newCall((Request) any())).thenAnswer(new Answer<Call>() {
            @Override
            public Call answer(InvocationOnMock invocation) throws IOException {
                throw new IOException();
            }
        });
        when(call.execute()).thenReturn(response);

        assertThat(baseRaoExtendedTest.getResponse(mock(Request.class)), is(equalTo(response)));
        verify(networkManager, times(1)).isOnline();

        InstanceFactory.remove(NetworkManager.class);
    }

    @Test(expected = IOException.class)
    public void shouldGetResponseWithFail() throws IOException {
        final NetworkManager networkManager = mock(NetworkManager.class);

        InstanceFactory.set(networkManager, NetworkManager.class);

        final BaseRaoExtendedTest baseRaoExtendedTest = new BaseRaoExtendedTest();
        final Response response = mock(Response.class);
        final Call call = mock(Call.class);

        when(baseRaoExtendedTest.getOkHttpClientVariable().newCall((Request) any())).thenAnswer(new Answer<Call>() {
            @Override
            public Call answer(InvocationOnMock invocation) throws IOException {
                throw new IOException();
            }
        });
        when(call.execute()).thenReturn(response);

        assertThat(baseRaoExtendedTest.getResponse(mock(Request.class)), is(equalTo(response)));

        InstanceFactory.remove(NetworkManager.class);
    }

    @Test
    public void shouldGetResponseStringWithSuccess() throws IOException {
        final NetworkManager networkManager = mock(NetworkManager.class);

        InstanceFactory.set(networkManager, NetworkManager.class);

        final BaseRaoExtendedTest baseRaoExtendedTest = new BaseRaoExtendedTest();
        final Response response = mock(Response.class);
        final ResponseBody responseBody = mock(ResponseBody.class);

        when(response.isSuccessful()).thenReturn(true);
        when(response.body()).thenReturn(responseBody);
        when(responseBody.string()).thenReturn("result");

        assertThat(baseRaoExtendedTest.getResponseString(response), is(equalTo("result")));

        InstanceFactory.remove(NetworkManager.class);
    }

    @Test(expected = RequestFailedException.class)
    public void shouldGetResponseStringWithFailAndLogCase1() throws IOException {
        final NetworkManager networkManager = mock(NetworkManager.class);

        InstanceFactory.set(networkManager, NetworkManager.class);

        final BaseRaoExtendedTest baseRaoExtendedTest = new BaseRaoExtendedTest();
        final Response response = mock(Response.class);
        final Request request = mock(Request.class);
        final ResponseBody responseBody = mock(ResponseBody.class);
        final RequestBody requestBody = new RequestBody() {
            @Nullable
            @Override
            public MediaType contentType() {
                return mock(MediaType.class);
            }

            @Override
            public void writeTo(final BufferedSink sink) throws IOException {
                sink.write("something".getBytes());
            }
        };

        when(response.isSuccessful()).thenReturn(false);
        when(response.body()).thenReturn(responseBody);
        when(response.request()).thenReturn(request);
        when(request.body()).thenReturn(requestBody);
        when(responseBody.string()).thenReturn("result");

        assertThat(baseRaoExtendedTest.getResponseString(response), is(equalTo("result")));

        InstanceFactory.remove(NetworkManager.class);
    }

    @Test(expected = RequestFailedException.class)
    public void shouldGetResponseStringWithFailAndLogCase2() throws IOException {
        final NetworkManager networkManager = mock(NetworkManager.class);

        InstanceFactory.set(networkManager, NetworkManager.class);

        final BaseRaoExtendedTest baseRaoExtendedTest = new BaseRaoExtendedTest();
        final Response response = mock(Response.class);
        final Request request = mock(Request.class);
        final ResponseBody responseBody = mock(ResponseBody.class);
        final RequestBody requestBody = new RequestBody() {
            @Nullable
            @Override
            public MediaType contentType() {
                return mock(MediaType.class);
            }

            @Override
            public void writeTo(final BufferedSink sink) throws IOException {
                throw new IOException();
            }
        };

        when(response.isSuccessful()).thenReturn(false);
        when(response.body()).thenReturn(responseBody);
        when(response.request()).thenReturn(request);
        when(request.body()).thenReturn(requestBody);
        when(responseBody.string()).thenReturn("result");

        assertThat(baseRaoExtendedTest.getResponseString(response), is(equalTo("result")));

        InstanceFactory.remove(NetworkManager.class);
    }

    @Test
    public void shouldFilterBodyCase1() {
        final NetworkManager networkManager = mock(NetworkManager.class);

        InstanceFactory.set(networkManager, NetworkManager.class);

        final String body = "{}";
        final String result = "{}";

        final BaseRaoExtendedTest baseRaoExtendedTest = new BaseRaoExtendedTest();

        assertThat(baseRaoExtendedTest.filterBodyForTrace(body), is(equalTo(result)));

        InstanceFactory.remove(NetworkManager.class);
    }

    @Test
    public void shouldFilterBodyCase2() {
        final NetworkManager networkManager = mock(NetworkManager.class);

        InstanceFactory.set(networkManager, NetworkManager.class);

        final String body = "{\"key1\":\"value1\",\"key2\":value2}";
        final String result = "{\"key1\":\"value1\",\"key2\":value2}";

        final BaseRaoExtendedTest baseRaoExtendedTest = new BaseRaoExtendedTest();

        assertThat(baseRaoExtendedTest.filterBodyForTrace(body), is(equalTo(result)));

        InstanceFactory.remove(NetworkManager.class);
    }

    @Test
    public void shouldFilterBodyCase3() {
        final NetworkManager networkManager = mock(NetworkManager.class);

        InstanceFactory.set(networkManager, NetworkManager.class);

        final String body = "{\"userId\":\"userId\",\"username\":\"username\",\"password\":\"password\",\"token\":\"token\",\"refreshToken\":\"refreshToken\"}";
        final String result = "{\"userId\":\"********\",\"username\":\"********\",\"password\":\"********\",\"token\":\"********\",\"refreshToken\":\"********\"}";

        final BaseRaoExtendedTest baseRaoExtendedTest = new BaseRaoExtendedTest();

        assertThat(baseRaoExtendedTest.filterBodyForTrace(body), is(equalTo(result)));

        InstanceFactory.remove(NetworkManager.class);
    }

    @Test
    public void shouldFilterBodyCase4() {
        final NetworkManager networkManager = mock(NetworkManager.class);

        InstanceFactory.set(networkManager, NetworkManager.class);

        final String body = "{\"another\":\"another\",\"userId\":\"userId\",\"username\":\"username\",\"password\":\"password\",\"token\":\"token\",\"refreshToken\":\"refreshToken\",\"another1\":\"another1\"}";
        final String result = "{\"another\":\"another\",\"userId\":\"********\",\"username\":\"********\",\"password\":\"********\",\"token\":\"********\",\"refreshToken\":\"********\",\"another1\":\"another1\"}";

        final BaseRaoExtendedTest baseRaoExtendedTest = new BaseRaoExtendedTest();

        assertThat(baseRaoExtendedTest.filterBodyForTrace(body), is(equalTo(result)));

        InstanceFactory.remove(NetworkManager.class);
    }

    @Test(expected = RequestFailedException.class)
    public void shouldGetResponseStringWithFail() throws IOException {
        final NetworkManager networkManager = mock(NetworkManager.class);

        InstanceFactory.set(networkManager, NetworkManager.class);

        final BaseRaoExtendedTest baseRaoExtendedTest = new BaseRaoExtendedTest();
        final Response response = mock(Response.class);
        final ResponseBody responseBody = mock(ResponseBody.class);

        when(response.isSuccessful()).thenReturn(false);
        when(response.code()).thenReturn(0);
        when(response.body()).thenReturn(responseBody);
        when(responseBody.string()).thenReturn("result");

        assertThat(baseRaoExtendedTest.getResponseString(response), is(equalTo("result")));

        InstanceFactory.remove(NetworkManager.class);
    }

    @Test(expected = IOException.class)
    public void shouldGetResponseStringWithException() throws IOException {
        final NetworkManager networkManager = mock(NetworkManager.class);

        InstanceFactory.set(networkManager, NetworkManager.class);

        final BaseRaoExtendedTest baseRaoExtendedTest = new BaseRaoExtendedTest();
        final Response response = mock(Response.class);
        final ResponseBody responseBody = mock(ResponseBody.class);

        when(response.isSuccessful()).thenReturn(false);
        when(response.body()).thenReturn(responseBody);
        when(responseBody.string()).thenAnswer(new Answer<String>() {
            @Override
            public String answer(InvocationOnMock invocation) throws IOException {
                throw  new IOException();
            }
        });

        assertThat(baseRaoExtendedTest.getResponseString(response), is(equalTo("result")));

        InstanceFactory.remove(NetworkManager.class);
    }



}