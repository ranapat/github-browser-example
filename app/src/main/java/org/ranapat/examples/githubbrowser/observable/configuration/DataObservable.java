package org.ranapat.examples.githubbrowser.observable.configuration;

import org.ranapat.examples.githubbrowser.data.ApplicationDatabase;
import org.ranapat.examples.githubbrowser.data.dao.ConfigurationDao;
import org.ranapat.examples.githubbrowser.data.entity.Configuration;
import org.ranapat.instancefactory.Fi;

import java.util.concurrent.Callable;

import io.reactivex.Maybe;
import io.reactivex.schedulers.Schedulers;

public class DataObservable {
    final private ConfigurationDao configurationDao;

    public DataObservable(final ConfigurationDao configurationDao) {
        this.configurationDao = configurationDao;
    }

    public DataObservable() {
        this(
                Fi.get(ApplicationDatabase.class).configurationDao()
        );
    }

    public Maybe<Configuration> fetch() {
        return Maybe.fromCallable(new Callable<Configuration>() {
            @Override
            public Configuration call() {
                return configurationDao.fetch();
            }
        }).subscribeOn(Schedulers.io());
    }

    public Maybe<Configuration> store(final Configuration configuration) {
        return Maybe.fromCallable(new Callable<Configuration>() {
            @Override
            public Configuration call() {
                configurationDao.store(configuration);

                return configuration;
            }
        }).subscribeOn(Schedulers.io());
    }

    public Maybe<Boolean> resetUpdatedAt() {
        return Maybe.fromCallable(new Callable<Boolean>() {
            @Override
            public Boolean call() {
                configurationDao.resetUpdatedAt();

                return true;
            }
        }).subscribeOn(Schedulers.io());
    }

}
