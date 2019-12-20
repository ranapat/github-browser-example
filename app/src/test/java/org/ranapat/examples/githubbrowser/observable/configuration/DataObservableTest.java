package org.ranapat.examples.githubbrowser.observable.configuration;

import org.junit.Test;
import org.ranapat.examples.githubbrowser.data.ApplicationDatabase;
import org.ranapat.examples.githubbrowser.data.dao.ConfigurationDao;
import org.ranapat.examples.githubbrowser.data.entity.Configuration;
import org.ranapat.instancefactory.InstanceFactory;

import io.reactivex.observers.TestObserver;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class DataObservableTest {

    @Test
    public void shouldDefaultConstructor() {
        final ApplicationDatabase applicationDatabase = mock(ApplicationDatabase.class);
        final ConfigurationDao configurationDao = mock(ConfigurationDao.class);
        InstanceFactory.set(applicationDatabase, ApplicationDatabase.class);
        when(applicationDatabase.configurationDao()).thenReturn(configurationDao);
        final DataObservable dataObservable = new DataObservable();

        when(configurationDao.fetch()).thenReturn(mock(Configuration.class));

        dataObservable.fetch().test().awaitTerminalEvent();

        verify(configurationDao, times(1)).fetch();

        InstanceFactory.remove(ApplicationDatabase.class);
    }

    @Test
    public void shouldCallFetchOnce() {
        final ConfigurationDao configurationDao = mock(ConfigurationDao.class);
        final DataObservable dataObservable = new DataObservable(
                configurationDao
        );

        when(configurationDao.fetch()).thenReturn(mock(Configuration.class));

        dataObservable.fetch().test().awaitTerminalEvent();

        verify(configurationDao, times(1)).fetch();
    }

    @Test
    public void shouldEmitFromFetchOnce() {
        final Configuration configuration = mock(Configuration.class);
        final ConfigurationDao configurationDao = mock(ConfigurationDao.class);
        when(configurationDao.fetch()).thenReturn(configuration);
        final DataObservable dataObservable = new DataObservable(
                configurationDao
        );

        final TestObserver<Configuration> testObserver = dataObservable.fetch().test();

        testObserver.awaitTerminalEvent();
        testObserver.assertResult(configuration);
    }

    @Test
    public void shouldHandleNullFromFetchOnce() {
        final ConfigurationDao configurationDao = mock(ConfigurationDao.class);
        when(configurationDao.fetch()).thenReturn(null);
        final DataObservable dataObservable = new DataObservable(
                configurationDao
        );

        final TestObserver<Configuration> testObserver = dataObservable.fetch().test();

        testObserver.awaitTerminalEvent();
        testObserver.assertNoValues();
    }

    @Test
    public void shouldCallStoreOnce() {
        final ConfigurationDao configurationDao = mock(ConfigurationDao.class);
        final DataObservable dataObservable = new DataObservable(
                configurationDao
        );

        dataObservable.store(mock(Configuration.class)).test().awaitTerminalEvent();

        verify(configurationDao, times(1)).store((Configuration) any());
    }

    @Test
    public void shouldEmitFromStoreOnce() {
        final Configuration configuration = mock(Configuration.class);
        final ConfigurationDao configurationDao = mock(ConfigurationDao.class);
        final DataObservable dataObservable = new DataObservable(
                configurationDao
        );

        final TestObserver<Configuration> testObserver = dataObservable.store(configuration).test();

        testObserver.awaitTerminalEvent();
        testObserver.assertResult(configuration);
    }

    @Test
    public void shouldThrowFromStoreOnce() {
        final Configuration configuration = mock(Configuration.class);
        final ConfigurationDao configurationDao = mock(ConfigurationDao.class);
        when(configurationDao.store(configuration)).thenReturn(0L);
        final DataObservable dataObservable = new DataObservable(
                configurationDao
        );

        final TestObserver<Configuration> testObserver = dataObservable.store(configuration).test();

        testObserver.awaitTerminalEvent();
        testObserver.assertResult(configuration);
    }

    @Test
    public void shouldCallResetUpdatedAtOnce() {
        final ConfigurationDao configurationDao = mock(ConfigurationDao.class);
        final DataObservable dataObservable = new DataObservable(
                configurationDao
        );

        dataObservable.resetUpdatedAt().test().awaitTerminalEvent();

        verify(configurationDao, times(1)).resetUpdatedAt();
    }

    @Test
    public void shouldEmitBooleanFromResetUpdatedAtOnce() {
        final ConfigurationDao configurationDao = mock(ConfigurationDao.class);
        final DataObservable dataObservable = new DataObservable(
                configurationDao
        );

        final TestObserver<Boolean> testObserver = dataObservable.resetUpdatedAt().test();

        testObserver.awaitTerminalEvent();
        testObserver.assertResult(true);
    }

}