package org.ranapat.examples.githubbrowser.observable.organization;

import org.ranapat.examples.githubbrowser.data.ApplicationDatabase;
import org.ranapat.examples.githubbrowser.data.dao.OrganizationDao;
import org.ranapat.examples.githubbrowser.data.entity.Organization;
import org.ranapat.instancefactory.Fi;

import java.util.concurrent.Callable;

import io.reactivex.Maybe;
import io.reactivex.schedulers.Schedulers;

public class DataObservable {
    final private OrganizationDao organizationDao;

    public DataObservable(final OrganizationDao organizationDao) {
        this.organizationDao = organizationDao;
    }

    public DataObservable() {
        this(
                Fi.get(ApplicationDatabase.class).organizationDao()
        );
    }

    public Maybe<Organization> fetchByLogin(final String login) {
        return Maybe.fromCallable(new Callable<Organization>() {
            @Override
            public Organization call() {
                return organizationDao.fetchByLogin(login);
            }
        }).subscribeOn(Schedulers.io());
    }

    public Maybe<Organization> store(final Organization organization) {
        return Maybe.fromCallable(new Callable<Organization>() {
            @Override
            public Organization call() {
                organizationDao.keep(organization);

                return organization;
            }
        }).subscribeOn(Schedulers.io());
    }

}
