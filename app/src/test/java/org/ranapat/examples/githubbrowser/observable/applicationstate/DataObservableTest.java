package org.ranapat.examples.githubbrowser.observable.applicationstate;

import org.junit.Test;
import org.ranapat.examples.githubbrowser.data.ApplicationDatabase;
import org.ranapat.examples.githubbrowser.data.dao.ApplicationStateDao;
import org.ranapat.examples.githubbrowser.data.entity.ApplicationState;
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
        final ApplicationStateDao applicationStateDao = mock(ApplicationStateDao.class);
        InstanceFactory.set(applicationDatabase, ApplicationDatabase.class);
        when(applicationDatabase.applicationStateDao()).thenReturn(applicationStateDao);
        final DataObservable dataObservable = new DataObservable();

        when(applicationStateDao.fetch()).thenReturn(mock(ApplicationState.class));

        dataObservable.fetch().test().awaitTerminalEvent();

        verify(applicationStateDao, times(1)).fetch();

        InstanceFactory.remove(ApplicationDatabase.class);
    }

    @Test
    public void shouldCallFetchOnce() {
        final ApplicationStateDao applicationStateDao = mock(ApplicationStateDao.class);
        final DataObservable dataObservable = new DataObservable(
                applicationStateDao
        );

        when(applicationStateDao.fetch()).thenReturn(mock(ApplicationState.class));

        dataObservable.fetch().test().awaitTerminalEvent();

        verify(applicationStateDao, times(1)).fetch();
    }

    @Test
    public void shouldEmitFromFetchOnce() {
        final ApplicationState applicationState = mock(ApplicationState.class);
        final ApplicationStateDao applicationStateDao = mock(ApplicationStateDao.class);
        when(applicationStateDao.fetch()).thenReturn(applicationState);
        final DataObservable dataObservable = new DataObservable(
                applicationStateDao
        );

        final TestObserver<ApplicationState> testObserver = dataObservable.fetch().test();

        testObserver.awaitTerminalEvent();
        testObserver.assertResult(applicationState);
    }

    @Test
    public void shouldHandleNullFromFetchOnce() {
        final ApplicationStateDao applicationStateDao = mock(ApplicationStateDao.class);
        when(applicationStateDao.fetch()).thenReturn(null);
        final DataObservable dataObservable = new DataObservable(
                applicationStateDao
        );

        final TestObserver<ApplicationState> testObserver = dataObservable.fetch().test();

        testObserver.awaitTerminalEvent();
        testObserver.assertNoValues();
    }

    @Test
    public void shouldCallStoreOnce() {
        final ApplicationStateDao applicationStateDao = mock(ApplicationStateDao.class);
        final DataObservable dataObservable = new DataObservable(
                applicationStateDao
        );

        dataObservable.store(mock(ApplicationState.class)).test().awaitTerminalEvent();

        verify(applicationStateDao, times(1)).store((ApplicationState) any());
    }

    @Test
    public void shouldEmitFromStoreOnce() {
        final ApplicationState applicationState = mock(ApplicationState.class);
        final ApplicationStateDao applicationStateDao = mock(ApplicationStateDao.class);
        final DataObservable dataObservable = new DataObservable(
                applicationStateDao
        );

        final TestObserver<ApplicationState> testObserver = dataObservable.store(applicationState).test();

        testObserver.awaitTerminalEvent();
        testObserver.assertResult(applicationState);
    }

    @Test
    public void shouldThrowFromStoreOnce() {
        final ApplicationState applicationState = mock(ApplicationState.class);
        final ApplicationStateDao applicationStateDao = mock(ApplicationStateDao.class);
        when(applicationStateDao.store(applicationState)).thenReturn(0L);
        final DataObservable dataObservable = new DataObservable(
                applicationStateDao
        );

        final TestObserver<ApplicationState> testObserver = dataObservable.store(applicationState).test();

        testObserver.awaitTerminalEvent();
        testObserver.assertResult(applicationState);
    }

}