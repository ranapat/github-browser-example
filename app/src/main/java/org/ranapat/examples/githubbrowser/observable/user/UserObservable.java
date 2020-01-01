package org.ranapat.examples.githubbrowser.observable.user;

import org.ranapat.examples.githubbrowser.data.entity.Configuration;
import org.ranapat.examples.githubbrowser.data.entity.Organization;
import org.ranapat.examples.githubbrowser.data.entity.User;
import org.ranapat.examples.githubbrowser.observable.configuration.ConfigurationObservable;
import org.ranapat.examples.githubbrowser.observable.exceptions.UserUndefinedException;
import org.ranapat.examples.githubbrowser.observable.exceptions.UsersUndefinedException;
import org.ranapat.instancefactory.Fi;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Maybe;
import io.reactivex.MaybeSource;
import io.reactivex.functions.BiConsumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;

public class UserObservable {
    final private ApiObservable apiObservable;
    final private DataObservable dataObservable;
    final private ConfigurationObservable configurationObservable;

    public UserObservable(
            final ApiObservable apiObservable,
            final DataObservable dataObservable,
            final ConfigurationObservable configurationObservable
    ) {
        this.apiObservable = apiObservable;
        this.dataObservable = dataObservable;
        this.configurationObservable = configurationObservable;
    }

    public UserObservable() {
        this(
                Fi.get(ApiObservable.class),
                Fi.get(DataObservable.class),
                Fi.get(ConfigurationObservable.class)
        );
    }

    public Maybe<List<User>> fetchAllByOrganization(final Organization organization) {
        return configurationObservable
                .fetch()
                .flatMap(new Function<Configuration, MaybeSource<List<User>>>() {
                    @Override
                    public MaybeSource<List<User>> apply(final Configuration configuration) {
                        return fetchAllByOrganization(configuration.organizationMembers, organization);
                    }
                });
    }

    private Maybe<List<User>> fetchAllByOrganization(final String organizationMembersUrl, final Organization organization) {
        final Maybe<List<User>> data = dataObservable
                .fetchAllByOrganization(organization);
        final Maybe<List<User>> api = fetchMemberList(organizationMembersUrl, organization, 1, new ArrayList<User>())
                .flatMap(new Function<List<User>, MaybeSource<List<User>>>() {
                    @Override
                    public MaybeSource<List<User>> apply(final List<User> users) {
                        if (users.size() > 0) {
                            return dataObservable
                                    .store(users);
                        } else {
                            return Maybe.just(users);
                        }
                    }
                });

        return Maybe
                .concat(data, api)
                .filter(new Predicate<List<User>>() {
                    @Override
                    public boolean test(final List<User> users) {
                        return users.size() > 0 && users.get(0).isUpToDate();
                    }
                })
                .concatWith(data)
                .firstElement()
                .doOnEvent(new BiConsumer<List<User>, Throwable>() {
                    @Override
                    public void accept(final List<User> users, final Throwable throwable) throws UsersUndefinedException {
                        if (users == null || users.size() == 0) {
                            throw new UsersUndefinedException();
                        }
                    }
                });
    }

    public Maybe<User> fetchDetails(final User user) {
        return configurationObservable
                .fetch()
                .flatMap(new Function<Configuration, MaybeSource<User>>() {
                    @Override
                    public MaybeSource<User> apply(final Configuration configuration) {
                        return fetchDetails(configuration.userInfo, user);
                    }
                });
    }

    private Maybe<User> fetchDetails(final String userInfoUrl, final User user) {
        final Maybe<User> direct = user.details != null ? Maybe.just(user) : Maybe.<User>empty();
        final Maybe<User> data = dataObservable
                .fetchById(user.id);
        final Maybe<User> api = apiObservable
                .fetchDetails(userInfoUrl, user)
                .flatMap(new Function<User, MaybeSource<User>>() {
                    @Override
                    public MaybeSource<User> apply(final User user) {
                        return dataObservable
                                .store(user);
                    }
                });

        return Maybe
                .concat(direct, data, api)
                .filter(new Predicate<User>() {
                    @Override
                    public boolean test(final User user) {
                        return user.isUpToDate() && user.details != null;
                    }
                })
                .concatWith(direct)
                .concatWith(data)
                .firstElement()
                .doOnEvent(new BiConsumer<User, Throwable>() {
                    @Override
                    public void accept(final User user, final Throwable throwable) throws UsersUndefinedException {
                        if (user == null || user.details == null) {
                            throw new UserUndefinedException();
                        }
                    }
                });
    }

    private Maybe<List<User>> fetchMemberList(final String organizationMembersUrl, final Organization organization, final int page, final List<User> users) {
        return apiObservable
                .fetchMemberList(organizationMembersUrl, organization, page)
                .defaultIfEmpty(new ArrayList<User>())
                .flatMap(new Function<List<User>, MaybeSource<List<User>>>() {
                    @Override
                    public MaybeSource<List<User>> apply(final List<User> _users) {
                        if (_users.size() > 0) {
                            users.addAll(_users);
                            return fetchMemberList(organizationMembersUrl, organization, page + 1, users);
                        } else {
                            return Maybe.just(users);
                        }
                    }
                });
    }

}