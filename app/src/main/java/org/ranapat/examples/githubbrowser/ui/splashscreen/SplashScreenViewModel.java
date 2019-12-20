package org.ranapat.examples.githubbrowser.ui.splashscreen;

import android.content.Context;

import androidx.appcompat.app.AppCompatActivity;

import org.ranapat.examples.githubbrowser.R;
import org.ranapat.examples.githubbrowser.management.ApplicationContext;
import org.ranapat.examples.githubbrowser.management.NetworkManager;
import org.ranapat.examples.githubbrowser.ui.BaseViewModel;
import org.ranapat.examples.githubbrowser.ui.main.MainActivity;
import org.ranapat.examples.githubbrowser.ui.publishable.ParameterizedMessage;
import org.ranapat.instancefactory.Fi;

import java.lang.ref.WeakReference;
import java.util.concurrent.TimeUnit;

import io.reactivex.Maybe;
import io.reactivex.functions.Consumer;
import io.reactivex.subjects.PublishSubject;

import static org.ranapat.examples.githubbrowser.ui.common.States.CLEAN_REDIRECT;
import static org.ranapat.examples.githubbrowser.ui.common.States.ERROR;
import static org.ranapat.examples.githubbrowser.ui.common.States.LOADING;

public class SplashScreenViewModel extends BaseViewModel {
    final public PublishSubject<String> state;
    final public PublishSubject<Class<? extends AppCompatActivity>> next;
    final public PublishSubject<ParameterizedMessage> message;

    final private WeakReference<Context> context;

    public SplashScreenViewModel(
            final NetworkManager networkManager,
            final Context context
    ) {
        super(networkManager);

        this.context = new WeakReference<>(context);

        state = PublishSubject.create();
        next = PublishSubject.create();
        message = PublishSubject.create();
    }

    public SplashScreenViewModel() {
        this(
                Fi.get(NetworkManager.class),
                Fi.get(ApplicationContext.class)
        );
    }

    public void initialize() {
        state.onNext(LOADING);

        subscription(Maybe
                .just(true)
                .delay(5, TimeUnit.SECONDS)
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(final Boolean aBoolean) {
                        next.onNext(MainActivity.class);
                        state.onNext(CLEAN_REDIRECT);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(final Throwable throwable) {
                        message.onNext(new ParameterizedMessage(R.string.unexpected_error));
                        state.onNext(ERROR);
                    }
                }));
    }

    @Override
    protected void triggerNetworkStatus(final Boolean isOnline) {
        if (isOnline) {
            initialize();
        }
    }
}
