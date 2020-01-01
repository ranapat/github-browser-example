package org.ranapat.examples.githubbrowser.ui.organization;

import android.content.Context;

import androidx.appcompat.app.AppCompatActivity;

import org.ranapat.examples.githubbrowser.R;
import org.ranapat.examples.githubbrowser.data.entity.Configuration;
import org.ranapat.examples.githubbrowser.data.entity.Organization;
import org.ranapat.examples.githubbrowser.data.entity.User;
import org.ranapat.examples.githubbrowser.management.ApplicationContext;
import org.ranapat.examples.githubbrowser.management.NetworkManager;
import org.ranapat.examples.githubbrowser.observable.ExceptionChecker;
import org.ranapat.examples.githubbrowser.observable.configuration.ConfigurationObservable;
import org.ranapat.examples.githubbrowser.observable.exceptions.ConfigurationUndefinedException;
import org.ranapat.examples.githubbrowser.observable.exceptions.UsersUndefinedException;
import org.ranapat.examples.githubbrowser.observable.organization.OrganizationObservable;
import org.ranapat.examples.githubbrowser.observable.user.UserObservable;
import org.ranapat.examples.githubbrowser.ui.BaseViewModel;
import org.ranapat.examples.githubbrowser.ui.publishable.ParameterizedMessage;
import org.ranapat.instancefactory.Fi;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Maybe;
import io.reactivex.MaybeSource;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.subjects.PublishSubject;
import timber.log.Timber;

import static org.ranapat.examples.githubbrowser.ui.common.States.ERROR;
import static org.ranapat.examples.githubbrowser.ui.common.States.LOADING;
import static org.ranapat.examples.githubbrowser.ui.common.States.READY;

public class OrganizationViewModel extends BaseViewModel {
    final public PublishSubject<String> state;
    final public PublishSubject<Class<? extends AppCompatActivity>> next;
    final public PublishSubject<Configuration> configuration;
    final public PublishSubject<Organization> organization;
    final public PublishSubject<List<User>> users;
    final public PublishSubject<User> user;
    final public PublishSubject<User> incomplete;

    final private ConfigurationObservable configurationObservable;
    final private OrganizationObservable organizationObservable;
    final private UserObservable userObservable;

    final private WeakReference<Context> context;

    private Configuration currentConfiguration;
    private Organization currentOrganization;
    private List<User> usersList;

    public OrganizationViewModel(
            final NetworkManager networkManager,
            final ConfigurationObservable configurationObservable,
            final OrganizationObservable organizationObservable,
            final UserObservable userObservable,
            final Context context
    ) {
        super(networkManager);

        this.configurationObservable = configurationObservable;
        this.organizationObservable = organizationObservable;
        this.userObservable = userObservable;

        this.context = new WeakReference<>(context);

        state = PublishSubject.create();
        next = PublishSubject.create();
        configuration = PublishSubject.create();
        organization = PublishSubject.create();
        users = PublishSubject.create();
        user = PublishSubject.create();
        incomplete = PublishSubject.create();
    }

    public OrganizationViewModel() {
        this(
                Fi.get(NetworkManager.class),
                Fi.get(ConfigurationObservable.class),
                Fi.get(OrganizationObservable.class),
                Fi.get(UserObservable.class),
                Fi.get(ApplicationContext.class)
        );
    }

    public void initialize(final Organization organization) {
        state.onNext(LOADING);

        currentOrganization = organization;

        subscription(configurationObservable
                .fetch()
                .flatMap(new Function<Configuration, MaybeSource<List<User>>>() {
                    @Override
                    public MaybeSource<List<User>> apply(final Configuration _configuration) {
                        currentConfiguration = _configuration;

                        configuration.onNext(_configuration);
                        return userObservable
                                .fetchAllByOrganization(currentOrganization);
                    }
                })
                .subscribe(new Consumer<List<User>>() {
                    @Override
                    public void accept(final List<User> _users) {
                        usersList = _users;

                        users.onNext(_users);
                        state.onNext(READY);

                        subscription(Maybe.just(true)
                                .delay(250, TimeUnit.MILLISECONDS)
                                .subscribe(new Consumer<Boolean>() {
                                    @Override
                                    public void accept(final Boolean aBoolean) {
                                        loadUserDetails();
                                    }
                                }));
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(final Throwable throwable) {
                        if (ExceptionChecker.isLast(throwable, ConfigurationUndefinedException.class)) {
                            messages.success.onNext(new ParameterizedMessage(R.string.error_configuration_undefined));
                        } else if (ExceptionChecker.isLast(throwable, UsersUndefinedException.class)) {
                            messages.error.onNext(new ParameterizedMessage(R.string.error_users_undefined));
                        } else {
                            messages.error.onNext(new ParameterizedMessage(R.string.unexpected_error));
                        }
                        users.onNext(new ArrayList<User>());
                        state.onNext(ERROR);
                    }
                })
        );
    }

    public void onItemClickListener(final int position) {
        Timber.e("### click on " + position);
    }

    @Override
    protected void triggerNetworkStatus(final Boolean isOnline) {
        //
    }

    private void loadUserDetails() {
        final List<Maybe<User>> disposables = new ArrayList<>();

        for (final User _user : usersList) {
            disposables.add(userObservable
                    .fetchDetails(_user)
                    .onErrorResumeNext(new Function<Throwable, MaybeSource<User>>() {
                        @Override
                        public MaybeSource<User> apply(final Throwable throwable) {
                            incomplete.onNext(_user);

                            return Maybe.just(_user);
                        }
                    })
                    .doOnSuccess(new Consumer<User>() {
                        @Override
                        public void accept(final User __user) {
                            user.onNext(__user);
                        }
                    })
            );
        }

        subscription(Maybe.concat(disposables).toList().subscribe(new Consumer<List<User>>() {
            @Override
            public void accept(final List<User> users) {
                //
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(final Throwable throwable) {
                //
            }
        }));
    }
}