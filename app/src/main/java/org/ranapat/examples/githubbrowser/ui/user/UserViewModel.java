package org.ranapat.examples.githubbrowser.ui.user;

import android.content.Context;

import androidx.appcompat.app.AppCompatActivity;

import org.ranapat.examples.githubbrowser.R;
import org.ranapat.examples.githubbrowser.data.entity.User;
import org.ranapat.examples.githubbrowser.management.ApplicationContext;
import org.ranapat.examples.githubbrowser.management.NetworkManager;
import org.ranapat.examples.githubbrowser.observable.ExceptionChecker;
import org.ranapat.examples.githubbrowser.observable.exceptions.UserUndefinedException;
import org.ranapat.examples.githubbrowser.observable.user.UserObservable;
import org.ranapat.examples.githubbrowser.ui.BaseViewModel;
import org.ranapat.examples.githubbrowser.ui.publishable.ParameterizedMessage;
import org.ranapat.instancefactory.Fi;

import java.lang.ref.WeakReference;

import io.reactivex.functions.Consumer;
import io.reactivex.subjects.PublishSubject;

import static org.ranapat.examples.githubbrowser.ui.common.States.ERROR;
import static org.ranapat.examples.githubbrowser.ui.common.States.LOADING;
import static org.ranapat.examples.githubbrowser.ui.common.States.READY;

public class UserViewModel extends BaseViewModel {
    final public PublishSubject<String> state;
    final public PublishSubject<Class<? extends AppCompatActivity>> next;
    final public PublishSubject<User> user;

    final private UserObservable userObservable;

    final private WeakReference<Context> context;

    private User currentUser;

    public UserViewModel(
            final NetworkManager networkManager,
            final UserObservable userObservable,
            final Context context
    ) {
        super(networkManager);

        this.userObservable = userObservable;

        this.context = new WeakReference<>(context);

        state = PublishSubject.create();
        next = PublishSubject.create();
        user = PublishSubject.create();
    }

    public UserViewModel() {
        this(
                Fi.get(NetworkManager.class),
                Fi.get(UserObservable.class),
                Fi.get(ApplicationContext.class)
        );
    }

    public void initialize(final User currentUser) {
        state.onNext(LOADING);

        this.currentUser = currentUser;
        user.onNext(currentUser);

        subscription(userObservable
                .fetchDetails(currentUser)
                .subscribe(new Consumer<User>() {
                    @Override
                    public void accept(final User _user) {
                        state.onNext(READY);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(final Throwable throwable) {
                        if (ExceptionChecker.isLast(throwable, UserUndefinedException.class)) {
                            messages.error.onNext(new ParameterizedMessage(R.string.error_user_undefined));
                        } else {
                            messages.error.onNext(new ParameterizedMessage(R.string.unexpected_error));
                        }
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