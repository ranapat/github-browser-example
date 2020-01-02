package org.ranapat.examples.githubbrowser.observable.applicationstate;

import org.ranapat.examples.githubbrowser.data.ApplicationDatabase;
import org.ranapat.examples.githubbrowser.data.dao.ApplicationStateDao;
import org.ranapat.examples.githubbrowser.data.entity.ApplicationState;
import org.ranapat.instancefactory.Fi;

import java.util.concurrent.Callable;

import io.reactivex.Maybe;
import io.reactivex.schedulers.Schedulers;

public class DataObservable {
    final private ApplicationStateDao applicationStateDao;

    public DataObservable(final ApplicationStateDao applicationStateDao) {
        this.applicationStateDao = applicationStateDao;
    }

    public DataObservable() {
        this(
                Fi.get(ApplicationDatabase.class).applicationStateDao()
        );
    }

    public Maybe<ApplicationState> fetch() {
        return Maybe.fromCallable(new Callable<ApplicationState>() {
            @Override
            public ApplicationState call() {
                return applicationStateDao.fetch();
            }
        }).subscribeOn(Schedulers.io());
    }

    public Maybe<ApplicationState> store(final ApplicationState applicationState) {
        return Maybe.fromCallable(new Callable<ApplicationState>() {
            @Override
            public ApplicationState call() {
                applicationStateDao.store(applicationState);

                return applicationState;
            }
        }).subscribeOn(Schedulers.io());
    }

}