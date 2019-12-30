package org.ranapat.examples.githubbrowser.observable.user;

import org.ranapat.examples.githubbrowser.data.ApplicationDatabase;
import org.ranapat.examples.githubbrowser.data.dao.UserDao;
import org.ranapat.examples.githubbrowser.data.dao.UserDetailsDao;
import org.ranapat.examples.githubbrowser.data.dao.UserUrlsDao;
import org.ranapat.examples.githubbrowser.data.entity.Organization;
import org.ranapat.examples.githubbrowser.data.entity.User;
import org.ranapat.instancefactory.Fi;

import java.util.List;
import java.util.concurrent.Callable;

import io.reactivex.Maybe;
import io.reactivex.schedulers.Schedulers;

public class DataObservable {
    final private UserDao userDao;
    final private UserDetailsDao userDetailsDao;
    final private UserUrlsDao userUrlsDao;

    public DataObservable(
            final UserDao userDao,
            final UserDetailsDao userDetailsDao,
            final UserUrlsDao userUrlsDao
    ) {
        this.userDao = userDao;
        this.userDetailsDao = userDetailsDao;
        this.userUrlsDao = userUrlsDao;
    }

    public DataObservable() {
        this(
                Fi.get(ApplicationDatabase.class).userDao(),
                Fi.get(ApplicationDatabase.class).userDetailsDao(),
                Fi.get(ApplicationDatabase.class).userUrlsDao()
        );
    }

    public Maybe<List<User>> fetchAllByOrganization(final Organization organization) {
        return Maybe.fromCallable(new Callable<List<User>>() {
            @Override
            public List<User> call() {
                final List<User> users = userDao.fetchByOrganizationId(organization.id);

                for (final User user : users) {
                    user.details = userDetailsDao.fetchByUserId(user.id);
                    user.urls = userUrlsDao.fetchByUserId(user.id);
                }

                return users;

            }
        }).subscribeOn(Schedulers.io());
    }

    public Maybe<User> store(final User user) {
        return Maybe.fromCallable(new Callable<User>() {
            @Override
            public User call() {
                userDao.keep(user);
                if (user.details != null) {
                    userDetailsDao.store(user.details);
                }
                if (user.urls != null) {
                    userUrlsDao.store(user.urls);
                }

                return user;
            }
        }).subscribeOn(Schedulers.io());
    }

    public Maybe<List<User>> store(final List<User> users) {
        return Maybe.fromCallable(new Callable<List<User>>() {
            @Override
            public List<User> call() {
                for (final User user : users) {
                    userDao.keep(user);
                    if (user.details != null) {
                        userDetailsDao.store(user.details);
                    }
                    if (user.urls != null) {
                        userUrlsDao.store(user.urls);
                    }
                }

                return users;
            }
        }).subscribeOn(Schedulers.io());
    }

}
