package org.ranapat.examples.githubbrowser.observable.configuration;

import org.junit.Test;
import org.ranapat.examples.githubbrowser.data.entity.Configuration;
import org.ranapat.examples.githubbrowser.observable.exceptions.ConfigurationUndefinedException;
import org.ranapat.instancefactory.InstanceFactory;

import io.reactivex.Maybe;
import io.reactivex.observers.TestObserver;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ConfigurationObservableTest {

    @Test
    public void shouldHandleDefaultConstructor() {
        final ApiObservable apiObservable = mock(ApiObservable.class);
        final DataObservable dataObservable = mock(DataObservable.class);

        InstanceFactory.set(apiObservable, ApiObservable.class);
        InstanceFactory.set(dataObservable, DataObservable.class);

        when(apiObservable.fetch()).thenReturn(Maybe.<Configuration>empty());
        when(dataObservable.fetch()).thenReturn(Maybe.<Configuration>empty());

        final ConfigurationObservable configurationObservable = new ConfigurationObservable(
        );

        final TestObserver<Configuration> testObserver = configurationObservable.fetch().test();

        testObserver.awaitTerminalEvent();
        testObserver.assertError(ConfigurationUndefinedException.class);

        InstanceFactory.remove(ApiObservable.class);
        InstanceFactory.remove(DataObservable.class);
    }

    @Test
    public void shouldThrowExceptionOnEmptyResults() {
        final ApiObservable apiObservable = mock(ApiObservable.class);
        final DataObservable dataObservable = mock(DataObservable.class);

        when(apiObservable.fetch()).thenReturn(Maybe.<Configuration>empty());
        when(dataObservable.fetch()).thenReturn(Maybe.<Configuration>empty());

        final ConfigurationObservable configurationObservable = new ConfigurationObservable(
                apiObservable,
                dataObservable
        );

        final TestObserver<Configuration> testObserver = configurationObservable.fetch().test();

        testObserver.awaitTerminalEvent();
        testObserver.assertError(ConfigurationUndefinedException.class);
    }

    @Test
    public void shouldEmitUpToDateDataFromData() {
        final Configuration configuration = mock(Configuration.class);
        final ApiObservable apiObservable = mock(ApiObservable.class);
        final DataObservable dataObservable = mock(DataObservable.class);

        when(configuration.isUpToDate()).thenReturn(true);
        when(apiObservable.fetch()).thenReturn(Maybe.<Configuration>empty());
        when(dataObservable.fetch()).thenReturn(Maybe.just(configuration));

        final ConfigurationObservable configurationObservable = new ConfigurationObservable(
                apiObservable,
                dataObservable
        );

        final TestObserver<Configuration> testObserver = configurationObservable.fetch().test();

        testObserver.awaitTerminalEvent();
        testObserver.assertResult(configuration);
    }

    @Test
    public void shouldEmitUpToDateDataFromApi() {
        final Configuration configuration = mock(Configuration.class);
        final ApiObservable apiObservable = mock(ApiObservable.class);
        final DataObservable dataObservable = mock(DataObservable.class);

        when(configuration.isUpToDate()).thenReturn(true);
        when(apiObservable.fetch()).thenReturn(Maybe.just(configuration));
        when(dataObservable.fetch()).thenReturn(Maybe.<Configuration>empty());
        when(dataObservable.store((Configuration) any())).thenReturn(Maybe.<Configuration>empty());

        final ConfigurationObservable configurationObservable = new ConfigurationObservable(
                apiObservable,
                dataObservable
        );

        TestObserver<Configuration> testObserver = configurationObservable.fetch().test();

        testObserver.awaitTerminalEvent();
        testObserver.assertResult(configuration);
    }

    @Test
    public void shouldNotCallStore() {
        final Configuration configuration = mock(Configuration.class);
        final ApiObservable apiObservable = mock(ApiObservable.class);
        final DataObservable dataObservable = mock(DataObservable.class);

        when(apiObservable.fetch()).thenReturn(Maybe.<Configuration>empty());
        when(dataObservable.fetch()).thenReturn(Maybe.just(configuration));

        final ConfigurationObservable configurationObservable = new ConfigurationObservable(
                apiObservable,
                dataObservable
        );

        TestObserver<Configuration> testObserver = configurationObservable.fetch().test();

        testObserver.awaitTerminalEvent();
        testObserver.assertResult(configuration);
        verify(dataObservable, times(0)).store((Configuration) any());
    }

    @Test
    public void shouldCallStoreOnce() {
        final Configuration configuration = mock(Configuration.class);
        final ApiObservable apiObservable = mock(ApiObservable.class);
        final DataObservable dataObservable = mock(DataObservable.class);

        when(configuration.isUpToDate()).thenReturn(true);
        when(apiObservable.fetch()).thenReturn(Maybe.just(configuration));
        when(dataObservable.fetch()).thenReturn(Maybe.<Configuration>empty());
        when(dataObservable.store((Configuration) any())).thenReturn(Maybe.<Configuration>empty());

        final ConfigurationObservable configurationObservable = new ConfigurationObservable(
                apiObservable,
                dataObservable
        );

        TestObserver<Configuration> testObserver = configurationObservable.fetch().test();

        testObserver.awaitTerminalEvent();
        testObserver.assertResult(configuration);
        verify(dataObservable, times(1)).store((Configuration) any());
    }

    @Test
    public void shouldEmitUpToDateDataCase1() {
        final Configuration configuration1 = mock(Configuration.class);
        final Configuration configuration2 = mock(Configuration.class);
        final ApiObservable apiObservable = mock(ApiObservable.class);
        final DataObservable dataObservable = mock(DataObservable.class);

        when(configuration1.isUpToDate()).thenReturn(false);
        when(configuration2.isUpToDate()).thenReturn(true);
        when(apiObservable.fetch()).thenReturn(Maybe.just(configuration2));
        when(dataObservable.fetch()).thenReturn(Maybe.just(configuration1));
        when(dataObservable.store((Configuration) any())).thenReturn(Maybe.<Configuration>empty());

        ConfigurationObservable configurationObservable = new ConfigurationObservable(
                apiObservable,
                dataObservable
        );

        TestObserver<Configuration> testObserver = configurationObservable.fetch().test();

        testObserver.awaitTerminalEvent();
        testObserver.assertResult(configuration2);
    }

    @Test
    public void shouldEmitUpToDateDataCase2() {
        final Configuration configuration1 = mock(Configuration.class);
        final Configuration configuration2 = mock(Configuration.class);
        final ApiObservable apiObservable = mock(ApiObservable.class);
        final DataObservable dataObservable = mock(DataObservable.class);

        when(configuration1.isUpToDate()).thenReturn(false);
        when(configuration2.isUpToDate()).thenReturn(true);
        when(apiObservable.fetch()).thenReturn(Maybe.just(configuration1));
        when(dataObservable.fetch()).thenReturn(Maybe.just(configuration2));
        when(dataObservable.store((Configuration) any())).thenReturn(Maybe.<Configuration>empty());

        ConfigurationObservable configurationObservable = new ConfigurationObservable(
                apiObservable,
                dataObservable
        );

        TestObserver<Configuration> testObserver = configurationObservable.fetch().test();

        testObserver.awaitTerminalEvent();
        testObserver.assertResult(configuration2);
    }

    @Test
    public void shouldEmitUpToDateDataCase3() {
        final Configuration configuration1 = mock(Configuration.class);
        final Configuration configuration2 = mock(Configuration.class);
        final ApiObservable apiObservable = mock(ApiObservable.class);
        final DataObservable dataObservable = mock(DataObservable.class);

        when(configuration1.isUpToDate()).thenReturn(true);
        when(configuration2.isUpToDate()).thenReturn(true);
        when(apiObservable.fetch()).thenReturn(Maybe.just(configuration1));
        when(dataObservable.fetch()).thenReturn(Maybe.just(configuration2));
        when(dataObservable.store((Configuration) any())).thenReturn(Maybe.<Configuration>empty());

        ConfigurationObservable configurationObservable = new ConfigurationObservable(
                apiObservable,
                dataObservable
        );

        TestObserver<Configuration> testObserver = configurationObservable.fetch().test();

        testObserver.awaitTerminalEvent();
    }

    @Test
    public void shouldEmitNotUpToDateDataCase1() {
        final Configuration configuration1 = mock(Configuration.class);
        final Configuration configuration2 = mock(Configuration.class);
        final ApiObservable apiObservable = mock(ApiObservable.class);
        final DataObservable dataObservable = mock(DataObservable.class);

        when(configuration1.isUpToDate()).thenReturn(false);
        when(configuration2.isUpToDate()).thenReturn(false);
        when(apiObservable.fetch()).thenReturn(Maybe.just(configuration1));
        when(dataObservable.fetch()).thenReturn(Maybe.just(configuration2));
        when(dataObservable.store((Configuration) any())).thenReturn(Maybe.<Configuration>empty());

        ConfigurationObservable configurationObservable = new ConfigurationObservable(
                apiObservable,
                dataObservable
        );

        TestObserver<Configuration> testObserver = configurationObservable.fetch().test();

        testObserver.awaitTerminalEvent();
        testObserver.assertResult(configuration2);
    }

    @Test
    public void shouldEmitNotUpToDateDataCase2() {
        final Configuration configuration = mock(Configuration.class);
        final ApiObservable apiObservable = mock(ApiObservable.class);
        final DataObservable dataObservable = mock(DataObservable.class);

        when(configuration.isUpToDate()).thenReturn(false);
        when(apiObservable.fetch()).thenReturn(Maybe.<Configuration>empty());
        when(dataObservable.fetch()).thenReturn(Maybe.just(configuration));
        when(dataObservable.store((Configuration) any())).thenReturn(Maybe.<Configuration>empty());

        ConfigurationObservable configurationObservable = new ConfigurationObservable(
                apiObservable,
                dataObservable
        );

        TestObserver<Configuration> testObserver = configurationObservable.fetch().test();

        testObserver.awaitTerminalEvent();
        testObserver.assertResult(configuration);
    }

    @Test
    public void shouldEmitFromMemoryCase1() {
        final Configuration configuration = mock(Configuration.class);
        final ApiObservable apiObservable = mock(ApiObservable.class);
        final DataObservable dataObservable = mock(DataObservable.class);

        when(configuration.isUpToDate()).thenReturn(true);
        when(apiObservable.fetch()).thenReturn(Maybe.just(configuration));
        when(dataObservable.fetch()).thenReturn(Maybe.<Configuration>empty());
        when(dataObservable.store((Configuration) any())).thenReturn(Maybe.<Configuration>empty());

        ConfigurationObservable configurationObservable = new ConfigurationObservable(
                apiObservable,
                dataObservable
        );

        TestObserver<Configuration> testObserver1 = configurationObservable.fetch().test();

        testObserver1.awaitTerminalEvent();
        testObserver1.assertResult(configuration);

        when(apiObservable.fetch()).thenReturn(Maybe.<Configuration>empty());

        TestObserver<Configuration> testObserver2 = configurationObservable.fetch().test();

        testObserver2.awaitTerminalEvent();
        testObserver2.assertResult(configuration);
    }

    @Test
    public void shouldEmitFromMemoryCase2() {
        final Configuration configuration = mock(Configuration.class);
        final ApiObservable apiObservable = mock(ApiObservable.class);
        final DataObservable dataObservable = mock(DataObservable.class);

        when(configuration.isUpToDate()).thenReturn(true);
        when(apiObservable.fetch()).thenReturn(Maybe.<Configuration>empty());
        when(dataObservable.fetch()).thenReturn(Maybe.just(configuration));
        when(dataObservable.store((Configuration) any())).thenReturn(Maybe.<Configuration>empty());

        ConfigurationObservable configurationObservable = new ConfigurationObservable(
                apiObservable,
                dataObservable
        );

        TestObserver<Configuration> testObserver1 = configurationObservable.fetch().test();

        testObserver1.awaitTerminalEvent();
        testObserver1.assertResult(configuration);

        when(dataObservable.fetch()).thenReturn(Maybe.<Configuration>empty());

        TestObserver<Configuration> testObserver2 = configurationObservable.fetch().test();

        testObserver2.awaitTerminalEvent();
        testObserver2.assertResult(configuration);
    }

    @Test
    public void shouldEmitFromMemoryCase3() {
        final Configuration configuration = mock(Configuration.class);
        final ApiObservable apiObservable = mock(ApiObservable.class);
        final DataObservable dataObservable = mock(DataObservable.class);

        when(configuration.isUpToDate()).thenReturn(false);
        when(apiObservable.fetch()).thenReturn(Maybe.<Configuration>empty());
        when(dataObservable.fetch()).thenReturn(Maybe.just(configuration));
        when(dataObservable.store((Configuration) any())).thenReturn(Maybe.<Configuration>empty());

        ConfigurationObservable configurationObservable = new ConfigurationObservable(
                apiObservable,
                dataObservable
        );

        TestObserver<Configuration> testObserver1 = configurationObservable.fetch().test();

        testObserver1.awaitTerminalEvent();
        testObserver1.assertResult(configuration);

        when(dataObservable.fetch()).thenReturn(Maybe.<Configuration>empty());

        TestObserver<Configuration> testObserver2 = configurationObservable.fetch().test();

        testObserver2.awaitTerminalEvent();
        testObserver2.assertResult(configuration);
    }

    @Test
    public void shouldEmitFromResetUpdatedAt() {
        final ApiObservable apiObservable = mock(ApiObservable.class);
        final DataObservable dataObservable = mock(DataObservable.class);

        when(dataObservable.resetUpdatedAt()).thenReturn(Maybe.just(true));

        ConfigurationObservable configurationObservable = new ConfigurationObservable(
                apiObservable, dataObservable
        );

        TestObserver<Boolean> testObserver = configurationObservable.resetUpdatedAt().test();

        testObserver.awaitTerminalEvent();
        assertThat(testObserver.values().size(), is(equalTo(1)));
        assertThat(testObserver.values().get(0), is(equalTo(true)));
    }

}