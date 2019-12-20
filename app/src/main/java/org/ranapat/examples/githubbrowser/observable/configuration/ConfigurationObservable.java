package org.ranapat.examples.githubbrowser.observable.configuration;

import org.ranapat.examples.githubbrowser.data.entity.Configuration;
import org.ranapat.examples.githubbrowser.observable.exceptions.ConfigurationUndefinedException;
import org.ranapat.instancefactory.Fi;

import io.reactivex.Maybe;
import io.reactivex.functions.BiConsumer;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Predicate;

public class ConfigurationObservable {
    final private ApiObservable apiObservable;
    final private DataObservable dataObservable;

    private Configuration inMemory;

    public ConfigurationObservable(
            final ApiObservable apiObservable,
            final DataObservable dataObservable
    ) {
        this.apiObservable = apiObservable;
        this.dataObservable = dataObservable;
    }

    public ConfigurationObservable() {
        this(
                Fi.get(ApiObservable.class),
                Fi.get(DataObservable.class)
        );
    }

    public Maybe<Configuration> fetch() {
        final Maybe<Configuration> memory = inMemory != null ? Maybe.just(inMemory) : Maybe.<Configuration>empty();
        final Maybe<Configuration> data = dataObservable
                .fetch()
                .doOnSuccess(new Consumer<Configuration>() {
                    @Override
                    public void accept(final Configuration configuration) {
                        inMemory = configuration;
                    }
                });
        final Maybe<Configuration> api = apiObservable
                .fetch()
                .doOnSuccess(new Consumer<Configuration>() {
                    @Override
                    public void accept(final Configuration configuration) {
                        inMemory = configuration;

                        dataObservable
                                .store(configuration)
                                .subscribe();
                    }
                });

        return Maybe
                .concat(memory, data, api)
                .filter(new Predicate<Configuration>() {
                    @Override
                    public boolean test(final Configuration configuration) {
                        return configuration.isUpToDate();
                    }
                })
                .concatWith(memory)
                .concatWith(data)
                .firstElement()
                .doOnEvent(new BiConsumer<Configuration, Throwable>() {
                    @Override
                    public void accept(final Configuration configuration, final Throwable throwable) throws ConfigurationUndefinedException {
                        if (configuration == null) {
                            throw new ConfigurationUndefinedException();
                        }
                    }
                });
    }

    public Maybe<Boolean> resetUpdatedAt() {
        return dataObservable.resetUpdatedAt();
    }

}
