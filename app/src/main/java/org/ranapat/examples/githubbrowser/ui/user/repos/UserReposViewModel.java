package org.ranapat.examples.githubbrowser.ui.user.repos;

import android.content.Context;

import org.ranapat.examples.githubbrowser.data.entity.User;
import org.ranapat.examples.githubbrowser.management.ApplicationContext;
import org.ranapat.examples.githubbrowser.management.NetworkManager;
import org.ranapat.examples.githubbrowser.management.TemporaryDataKeeperManager;
import org.ranapat.examples.githubbrowser.observable.user.UserObservable;
import org.ranapat.examples.githubbrowser.ui.BaseViewModel;
import org.ranapat.instancefactory.Fi;

import java.lang.ref.WeakReference;

import io.reactivex.subjects.PublishSubject;

import static org.ranapat.examples.githubbrowser.ui.common.States.LOADING;
import static org.ranapat.examples.githubbrowser.ui.common.States.READY;

public class UserReposViewModel extends BaseViewModel {
    final public PublishSubject<String> state;
    final public PublishSubject<String> url;

    final private UserObservable userObservable;
    final private TemporaryDataKeeperManager temporaryDataKeeperManager;

    final private WeakReference<Context> context;

    private User currentUser;

    public UserReposViewModel(
            final NetworkManager networkManager,
            final UserObservable userObservable,
            final TemporaryDataKeeperManager temporaryDataKeeperManager,
            final Context context
    ) {
        super(networkManager);

        this.userObservable = userObservable;
        this.temporaryDataKeeperManager = temporaryDataKeeperManager;

        this.context = new WeakReference<>(context);

        state = PublishSubject.create();
        url = PublishSubject.create();
    }

    public UserReposViewModel() {
        this(
                Fi.get(NetworkManager.class),
                Fi.get(UserObservable.class),
                Fi.get(TemporaryDataKeeperManager.class),
                Fi.get(ApplicationContext.class)
        );
    }

    public void initialize() {
        state.onNext(LOADING);

        currentUser = temporaryDataKeeperManager.user;

        url.onNext(currentUser.urls.reposUrl);
        state.onNext(READY);
    }

    public void viewStopped() {
        clearDisposables();
    }

    @Override
    protected void triggerNetworkStatus(final Boolean isOnline) {
        //
    }

}