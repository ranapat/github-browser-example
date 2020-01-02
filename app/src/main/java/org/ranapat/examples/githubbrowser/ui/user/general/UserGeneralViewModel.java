package org.ranapat.examples.githubbrowser.ui.user.general;

import android.content.Context;

import org.ranapat.examples.githubbrowser.data.entity.User;
import org.ranapat.examples.githubbrowser.management.ApplicationContext;
import org.ranapat.examples.githubbrowser.management.NetworkManager;
import org.ranapat.examples.githubbrowser.management.TemporaryDataKeeperManager;
import org.ranapat.examples.githubbrowser.observable.user.UserObservable;
import org.ranapat.examples.githubbrowser.ui.BaseViewModel;
import org.ranapat.instancefactory.Fi;

import java.lang.ref.WeakReference;
import java.util.concurrent.TimeUnit;

import io.reactivex.Maybe;
import io.reactivex.functions.Consumer;
import io.reactivex.subjects.PublishSubject;

import static org.ranapat.examples.githubbrowser.ui.common.States.ERROR;
import static org.ranapat.examples.githubbrowser.ui.common.States.LOADING;
import static org.ranapat.examples.githubbrowser.ui.common.States.READY;

public class UserGeneralViewModel extends BaseViewModel {
    final public PublishSubject<String> state;

    final private UserObservable userObservable;
    final private TemporaryDataKeeperManager temporaryDataKeeperManager;

    final private WeakReference<Context> context;

    private User currentUser;

    public UserGeneralViewModel(
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
    }

    public UserGeneralViewModel() {
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

        subscription(Maybe.just(true)
                .delay(1, TimeUnit.SECONDS)
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(final Boolean _aBoolean) {
                        state.onNext(READY);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(final Throwable throwable) {
                        state.onNext(ERROR);
                    }
                })
        );
    }

    @Override
    protected void triggerNetworkStatus(final Boolean isOnline) {
        //
    }

}