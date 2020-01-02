package org.ranapat.examples.githubbrowser.observable.applicationstate;

import org.ranapat.examples.githubbrowser.data.entity.ApplicationState;
import org.ranapat.instancefactory.Fi;

import io.reactivex.Maybe;

public class ApplicationStateObservable {
    final private DataObservable dataObservable;

    public ApplicationStateObservable(
            final DataObservable dataObservable
    ) {
        this.dataObservable = dataObservable;
    }

    public ApplicationStateObservable() {
        this(
                Fi.get(DataObservable.class)
        );
    }

    public Maybe<ApplicationState> fetch() {
        return dataObservable
                .fetch();
    }

    public Maybe<ApplicationState> store(final ApplicationState applicationState) {
        return dataObservable
                .store(applicationState);
    }

}