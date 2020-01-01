package org.ranapat.examples.githubbrowser.ui.main;

import android.content.Context;

import androidx.appcompat.app.AppCompatActivity;

import org.ranapat.examples.githubbrowser.R;
import org.ranapat.examples.githubbrowser.data.entity.Configuration;
import org.ranapat.examples.githubbrowser.data.entity.Organization;
import org.ranapat.examples.githubbrowser.data.entity.User;
import org.ranapat.examples.githubbrowser.data.entity.UserDetails;
import org.ranapat.examples.githubbrowser.management.ApplicationContext;
import org.ranapat.examples.githubbrowser.management.NetworkManager;
import org.ranapat.examples.githubbrowser.observable.ExceptionChecker;
import org.ranapat.examples.githubbrowser.observable.configuration.ConfigurationObservable;
import org.ranapat.examples.githubbrowser.observable.exceptions.OrganizationUndefinedException;
import org.ranapat.examples.githubbrowser.observable.exceptions.UsersUndefinedException;
import org.ranapat.examples.githubbrowser.observable.organization.OrganizationObservable;
import org.ranapat.examples.githubbrowser.observable.user.UserObservable;
import org.ranapat.examples.githubbrowser.ui.BaseViewModel;
import org.ranapat.examples.githubbrowser.ui.publishable.ParameterizedMessage;
import org.ranapat.instancefactory.Fi;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Date;
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

public class MainViewModel extends BaseViewModel {
    final public PublishSubject<String> state;
    final public PublishSubject<Class<? extends AppCompatActivity>> next;
    final public PublishSubject<Configuration> configuration;
    final public PublishSubject<Organization> organization;
    final public PublishSubject<List<User>> users;
    final public PublishSubject<User> user;

    final private ConfigurationObservable configurationObservable;
    final private OrganizationObservable organizationObservable;
    final private UserObservable userObservable;

    final private WeakReference<Context> context;

    private List<User> usersList;

    public MainViewModel(
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
    }

    public MainViewModel() {
        this(
                Fi.get(NetworkManager.class),
                Fi.get(ConfigurationObservable.class),
                Fi.get(OrganizationObservable.class),
                Fi.get(UserObservable.class),
                Fi.get(ApplicationContext.class)
        );
    }

    public void initialize() {
        state.onNext(LOADING);

        subscription(configurationObservable
                .fetch()
                .subscribe(new Consumer<Configuration>() {
                    @Override
                    public void accept(final Configuration _configuration) {
                        state.onNext(READY);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(final Throwable throwable) {
                        messages.success.onNext(new ParameterizedMessage(R.string.error_configuration_undefined));
                        state.onNext(ERROR);
                    }
                })
        );
    }

    public void searchForOrganization(final String login) {
        state.onNext(LOADING);

        subscription(organizationObservable
                .fetchByLogin(login)
                .flatMap(new Function<Organization, MaybeSource<List<User>>>() {
                    @Override
                    public MaybeSource<List<User>> apply(final Organization _organization) {
                        organization.onNext(_organization);

                        return userObservable
                                .fetchAllByOrganization(_organization);
                    }
                })
                .subscribe(new Consumer<List<User>>() {
                    @Override
                    public void accept(final List<User> _users) {
                        usersList = _users;

                        users.onNext(_users);
                        state.onNext(READY);

                        loadUserDetails();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(final Throwable throwable) {
                        if (ExceptionChecker.isLast(throwable, OrganizationUndefinedException.class)) {
                            messages.error.onNext(new ParameterizedMessage(R.string.error_organization_undefined));
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

    public void clearOrganization() {
        users.onNext(new ArrayList<User>());
    }

    public void onItemClickListener(final int position) {
        Timber.e("### click on " + position);
    }

    @Override
    protected void triggerNetworkStatus(final Boolean isOnline) {
        //
    }

    private void loadUserDetails() {
        subscription(Maybe.just(true)
                .delay(5, TimeUnit.SECONDS)
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(final Boolean aBoolean) {
                        final User _user = usersList.get(0);
                        _user.details = new UserDetails(_user.id, "", "", "", "", "", "", "", new Date(), new Date());

                        user.onNext(_user);
                    }
                }));
    }
}