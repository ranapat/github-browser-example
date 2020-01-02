package org.ranapat.examples.githubbrowser.observable.applicationstate;

import org.junit.Test;
import org.ranapat.examples.githubbrowser.data.entity.ApplicationState;
import org.ranapat.instancefactory.InstanceFactory;

import io.reactivex.Maybe;
import io.reactivex.observers.TestObserver;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ApplicationStateObservableTest {

    @Test
    public void shouldHandleDefaultConstructor() {
        final ApplicationState applicationState = mock(ApplicationState.class);
        final DataObservable dataObservable = mock(DataObservable.class);

        InstanceFactory.set(dataObservable, DataObservable.class);

        when(dataObservable.fetch()).thenReturn(Maybe.just(applicationState));

        final ApplicationStateObservable applicationStateObservable = new ApplicationStateObservable(
        );

        final TestObserver<ApplicationState> testObserver = applicationStateObservable.fetch().test();

        testObserver.awaitTerminalEvent();
        testObserver.assertValue(applicationState);

        InstanceFactory.remove(DataObservable.class);
    }

    @Test
    public void shouldEmit() {
        final ApplicationState applicationState = mock(ApplicationState.class);
        final DataObservable dataObservable = mock(DataObservable.class);

        when(dataObservable.fetch()).thenReturn(Maybe.just(applicationState));

        final ApplicationStateObservable applicationStateObservable = new ApplicationStateObservable(
                dataObservable
        );

        final TestObserver<ApplicationState> testObserver = applicationStateObservable.fetch().test();

        testObserver.awaitTerminalEvent();
        testObserver.assertValue(applicationState);
    }

    @Test
    public void shouldStore() {
        final ApplicationState applicationState = mock(ApplicationState.class);
        final DataObservable dataObservable = mock(DataObservable.class);

        when(dataObservable.store(applicationState)).thenReturn(Maybe.just(applicationState));

        final ApplicationStateObservable applicationStateObservable = new ApplicationStateObservable(
                dataObservable
        );

        final TestObserver<ApplicationState> testObserver = applicationStateObservable.store(applicationState).test();

        testObserver.awaitTerminalEvent();
        testObserver.assertValue(applicationState);
    }

}