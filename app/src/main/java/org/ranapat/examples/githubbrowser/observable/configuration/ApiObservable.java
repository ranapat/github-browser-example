package org.ranapat.examples.githubbrowser.observable.configuration;

import org.ranapat.examples.githubbrowser.api.rao.ConfigurationRao;
import org.ranapat.examples.githubbrowser.data.entity.Configuration;
import org.ranapat.instancefactory.Fi;

import java.util.concurrent.Callable;

import io.reactivex.Maybe;
import io.reactivex.schedulers.Schedulers;

public class ApiObservable {
    final private ConfigurationRao configurationRao;

    public ApiObservable(final ConfigurationRao configurationRao) {
        this.configurationRao = configurationRao;
    }

    public ApiObservable() {
        this(
                Fi.get(ConfigurationRao.class)
        );
    }

    public Maybe<Configuration> fetch() {
        return Maybe.fromCallable(new Callable<Configuration>() {
            @Override
            public Configuration call() {
                try {
                    return configurationRao.fetch();
                } catch (final Exception e) {
                    return null;
                }
            }
        }).subscribeOn(Schedulers.io());
    }

}
