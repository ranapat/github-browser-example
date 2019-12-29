package org.ranapat.examples.githubbrowser.observable.organization;

import org.ranapat.examples.githubbrowser.api.rao.OrganizationRao;
import org.ranapat.examples.githubbrowser.data.entity.Organization;
import org.ranapat.instancefactory.Fi;

import java.util.concurrent.Callable;

import io.reactivex.Maybe;
import io.reactivex.schedulers.Schedulers;

public class ApiObservable {
    final private OrganizationRao organizationRao;

    public ApiObservable(final OrganizationRao organizationRao) {
        this.organizationRao = organizationRao;
    }

    public ApiObservable() {
        this(
                Fi.get(OrganizationRao.class)
        );
    }

    public Maybe<Organization> fetchByLogin(final String organizationUrl, final String login) {
        return Maybe.fromCallable(new Callable<Organization>() {
            @Override
            public Organization call() {
                try {
                    return organizationRao.fetchByLogin(organizationUrl, login);
                } catch (final Exception e) {
                    return null;
                }
            }
        }).subscribeOn(Schedulers.io());
    }

}
