package org.ranapat.examples.githubbrowser.observable.organization;

import org.ranapat.examples.githubbrowser.data.entity.Configuration;
import org.ranapat.examples.githubbrowser.data.entity.Organization;
import org.ranapat.examples.githubbrowser.observable.configuration.ConfigurationObservable;
import org.ranapat.examples.githubbrowser.observable.exceptions.OrganizationUndefinedException;
import org.ranapat.instancefactory.Fi;

import io.reactivex.Maybe;
import io.reactivex.MaybeSource;
import io.reactivex.functions.BiConsumer;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;

public class OrganizationObservable {
    final private ApiObservable apiObservable;
    final private DataObservable dataObservable;
    final private ConfigurationObservable configurationObservable;

    public OrganizationObservable(
            final ApiObservable apiObservable,
            final DataObservable dataObservable,
            final ConfigurationObservable configurationObservable
    ) {
        this.apiObservable = apiObservable;
        this.dataObservable = dataObservable;
        this.configurationObservable = configurationObservable;
    }

    public OrganizationObservable() {
        this(
                Fi.get(ApiObservable.class),
                Fi.get(DataObservable.class),
                Fi.get(ConfigurationObservable.class)
        );
    }

    public Maybe<Organization> fetchByLogin(final String login) {
        return configurationObservable
                .fetch()
                .flatMap(new Function<Configuration, MaybeSource<Organization>>() {
                    @Override
                    public MaybeSource<Organization> apply(final Configuration configuration) {
                        return fetchByLogin(configuration.organization, login);
                    }
                });
    }

    private Maybe<Organization> fetchByLogin(final String organizationUrl, final String login) {
        final Maybe<Organization> data = dataObservable
                .fetchByLogin(login);
        final Maybe<Organization> api = apiObservable
                .fetchByLogin(organizationUrl, login)
                .flatMap(new Function<Organization, MaybeSource<Organization>>() {
                    @Override
                    public MaybeSource<Organization> apply(final Organization organization) {
                        return dataObservable
                                .store(organization);
                    }
                });

        return Maybe
                .concat(data, api)
                .filter(new Predicate<Organization>() {
                    @Override
                    public boolean test(final Organization organization) {
                        return organization.isUpToDate();
                    }
                })
                .concatWith(data)
                .firstElement()
                .doOnEvent(new BiConsumer<Organization, Throwable>() {
                    @Override
                    public void accept(final Organization organization, final Throwable throwable) throws OrganizationUndefinedException {
                        if (organization == null) {
                            throw new OrganizationUndefinedException();
                        }
                    }
                });
    }

}