package org.ranapat.examples.githubbrowser.observable.user;

import org.ranapat.examples.githubbrowser.api.rao.UserRao;
import org.ranapat.examples.githubbrowser.data.entity.Organization;
import org.ranapat.examples.githubbrowser.data.entity.User;
import org.ranapat.instancefactory.Fi;

import java.util.List;
import java.util.concurrent.Callable;

import io.reactivex.Maybe;
import io.reactivex.schedulers.Schedulers;

public class ApiObservable {
    final private UserRao userRao;

    public ApiObservable(final UserRao userRao) {
        this.userRao = userRao;
    }

    public ApiObservable() {
        this(
                Fi.get(UserRao.class)
        );
    }

    public Maybe<List<User>> fetchMemberList(final String organizationMembersUrl, final Organization organization, final int page) {
        return Maybe.fromCallable(new Callable<List<User>>() {
            @Override
            public List<User> call() {
                try {
                    return userRao.fetchMemberList(organizationMembersUrl, organization, page);
                } catch (final Exception e) {
                    return null;
                }
            }
        }).subscribeOn(Schedulers.io());
    }

}
