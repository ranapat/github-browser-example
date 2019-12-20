package org.ranapat.examples.githubbrowser.observable.configuration;

import org.junit.Test;
import org.ranapat.examples.githubbrowser.api.rao.ConfigurationRao;
import org.ranapat.examples.githubbrowser.data.entity.Configuration;
import org.ranapat.instancefactory.InstanceFactory;

import io.reactivex.observers.TestObserver;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ApiObservableTest {

    @Test
    public void shouldHandleDefaultConstructor() throws Exception {
        final ConfigurationRao configurationRao = mock(ConfigurationRao.class);

        InstanceFactory.set(configurationRao, ConfigurationRao.class);

        final ApiObservable apiObservable = new ApiObservable(
        );

        apiObservable.fetch().test().awaitTerminalEvent();

        verify(configurationRao, times(1)).fetch();

        InstanceFactory.remove(ConfigurationRao.class);
    }

    @Test
    public void shouldCallFetchOnce() throws Exception {
        final ConfigurationRao configurationRao = mock(ConfigurationRao.class);
        final ApiObservable apiObservable = new ApiObservable(
                configurationRao
        );

        apiObservable.fetch().test().awaitTerminalEvent();

        verify(configurationRao, times(1)).fetch();
    }

    @Test
    public void shouldEmitOnce() throws Exception {
        final Configuration configuration = mock(Configuration.class);
        final ConfigurationRao configurationRao = mock(ConfigurationRao.class);
        when(configurationRao.fetch()).thenReturn(
                configuration
        );
        ApiObservable apiObservable = new ApiObservable(
                configurationRao
        );

        final TestObserver<Configuration> testObserver = apiObservable.fetch().test();

        testObserver.awaitTerminalEvent();
        testObserver.assertResult(configuration);
    }

    @Test
    public void shouldNotThrowOnFetch() throws Exception {
        final ConfigurationRao configurationRao = mock(ConfigurationRao.class);
        when(configurationRao.fetch()).thenThrow(Exception.class);
        final ApiObservable apiObservable = new ApiObservable(
                configurationRao
        );

        final TestObserver<Configuration> testObserver = apiObservable.fetch().test();

        testObserver.awaitTerminalEvent();
        testObserver.assertNoValues();
    }

}