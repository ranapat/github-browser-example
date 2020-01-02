package org.ranapat.examples.githubbrowser.ui.organization;

import android.content.Context;

import androidx.appcompat.app.AppCompatActivity;

import org.ranapat.examples.githubbrowser.R;
import org.ranapat.examples.githubbrowser.data.entity.ApplicationState;
import org.ranapat.examples.githubbrowser.data.entity.Configuration;
import org.ranapat.examples.githubbrowser.data.entity.Organization;
import org.ranapat.examples.githubbrowser.data.entity.User;
import org.ranapat.examples.githubbrowser.management.ApplicationContext;
import org.ranapat.examples.githubbrowser.management.NetworkManager;
import org.ranapat.examples.githubbrowser.observable.ExceptionChecker;
import org.ranapat.examples.githubbrowser.observable.applicationstate.ApplicationStateObservable;
import org.ranapat.examples.githubbrowser.observable.configuration.ConfigurationObservable;
import org.ranapat.examples.githubbrowser.observable.exceptions.ConfigurationUndefinedException;
import org.ranapat.examples.githubbrowser.observable.exceptions.UsersUndefinedException;
import org.ranapat.examples.githubbrowser.observable.user.UserObservable;
import org.ranapat.examples.githubbrowser.ui.BaseViewModel;
import org.ranapat.examples.githubbrowser.ui.publishable.ParameterizedMessage;
import org.ranapat.instancefactory.Fi;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

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
    public static final String SORT_BY_NAME = "name";
    public static final String SORT_BY_PUBLIC_REPOS = "publicRepos";
    public static final String SORT_BY_FOLLOWERS = "followers";
    public static final String SORT_BY_PUBLIC_GISTS = "publicGists";

    public static final String SORT_DIRECTION_ASC = "asc";
    public static final String SORT_DIRECTION_DESC = "desc";

    public static final String UP_TO_LIMIT = "upToLimit";
    public static final String NO_LIMIT = "noLimit";

    final public PublishSubject<String> state;
    final public PublishSubject<Class<? extends AppCompatActivity>> next;
    final public PublishSubject<Configuration> configuration;
    final public PublishSubject<Organization> organization;
    final public PublishSubject<List<ListUser>> users;
    final public PublishSubject<ListUser> user;
    final public PublishSubject<String> sortBy;
    final public PublishSubject<String> sortDirection;
    final public PublishSubject<String> limit;
    final public PublishSubject<LoadingProgress> loading;

    final private ApplicationStateObservable applicationStateObservable;
    final private ConfigurationObservable configurationObservable;
    final private UserObservable userObservable;

    final private ComparatorFactory comparatorFactory;

    final private WeakReference<Context> context;

    private ApplicationState currentApplicationState;
    private Configuration currentConfiguration;
    private Organization currentOrganization;
    private String currentSortBy;
    private String currentSortDirection;
    private String currentLimit;
    private List<User> usersList;
    private List<ListUser> normalizedUsers;
    private List<ListUser> sortedUsers;

    public OrganizationViewModel(
            final NetworkManager networkManager,
            final ApplicationStateObservable applicationStateObservable,
            final ConfigurationObservable configurationObservable,
            final UserObservable userObservable,
            final ComparatorFactory comparatorFactory,
            final Context context
    ) {
        super(networkManager);

        this.applicationStateObservable = applicationStateObservable;
        this.configurationObservable = configurationObservable;
        this.userObservable = userObservable;

        this.comparatorFactory = comparatorFactory;

        this.context = new WeakReference<>(context);

        state = PublishSubject.create();
        next = PublishSubject.create();
        configuration = PublishSubject.create();
        organization = PublishSubject.create();
        users = PublishSubject.create();
        user = PublishSubject.create();
        sortBy = PublishSubject.create();
        sortDirection = PublishSubject.create();
        limit = PublishSubject.create();
        loading = PublishSubject.create();
    }

    public OrganizationViewModel() {
        this(
                Fi.get(NetworkManager.class),
                Fi.get(ApplicationStateObservable.class),
                Fi.get(ConfigurationObservable.class),
                Fi.get(UserObservable.class),
                Fi.get(ComparatorFactory.class),
                Fi.get(ApplicationContext.class)
        );
    }

    public void initialize(final Organization currentOrganization) {
        state.onNext(LOADING);

        this.currentOrganization = currentOrganization;
        organization.onNext(currentOrganization);

        subscription(applicationStateObservable
                .fetch()
                .flatMap(new Function<ApplicationState, MaybeSource<Configuration>>() {
                    @Override
                    public MaybeSource<Configuration> apply(final ApplicationState _applicationState) {
                        currentApplicationState = _applicationState;

                        resetSortAndLimit();

                        return configurationObservable
                                .fetch();
                    }
                })
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
                        loadUsers();
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
                        state.onNext(ERROR);
                    }
                })
        );
    }

    public void sortBy(final String currentSortBy) {
        this.currentSortBy = currentSortBy;
        sortBy.onNext(currentSortBy);

        currentApplicationState.sortBy = currentSortBy;
        subscription(applicationStateObservable
                .store(currentApplicationState)
                .subscribe());

        normalizeUsers();
    }

    public void sortDirection(final String currentSortDirection) {
        this.currentSortDirection = currentSortDirection;
        sortDirection.onNext(currentSortDirection);

        currentApplicationState.sortDirection = currentSortDirection;
        subscription(applicationStateObservable
                .store(currentApplicationState)
                .subscribe());

        normalizeUsers();
    }

    public void showUpToLimit() {
        currentLimit = UP_TO_LIMIT;
        limit.onNext(currentLimit);

        currentApplicationState.limit = currentLimit;
        subscription(applicationStateObservable
                .store(currentApplicationState)
                .subscribe());

        normalizeUsers();
    }

    public void showNoLimit() {
        currentLimit = NO_LIMIT;
        limit.onNext(currentLimit);

        currentApplicationState.limit = currentLimit;
        subscription(applicationStateObservable
                .store(currentApplicationState)
                .subscribe());

        normalizeUsers();
    }

    public void onItemClickListener(final int position) {
        Timber.e("### click on " + position);
    }

    public void viewDestroyed() {
        currentApplicationState.reset();
        subscription(applicationStateObservable
                .store(currentApplicationState)
                .subscribe());
    }

    @Override
    protected void triggerNetworkStatus(final Boolean isOnline) {
        //
    }

    private void resetSortAndLimit() {
        currentSortBy = currentApplicationState.sortBy == null ? SORT_BY_NAME : currentApplicationState.sortBy;
        currentSortDirection = currentApplicationState.sortDirection == null ? SORT_DIRECTION_ASC : currentApplicationState.sortDirection;
        currentLimit = currentApplicationState.limit == null ? UP_TO_LIMIT : currentApplicationState.limit;

        sortBy.onNext(currentSortBy);
        sortDirection.onNext(currentSortDirection);
        limit.onNext(currentLimit);
    }

    private void normalizeUsers() {
        sortedUsers = new ArrayList<>();
        for (final ListUser _user : normalizedUsers) {
            if (!_user.incomplete) {
                sortedUsers.add(_user);
            }
        }

        Collections.sort(sortedUsers, comparatorFactory.get(currentSortBy, currentSortDirection));

        if (currentLimit.equals(UP_TO_LIMIT)) {
            sortedUsers = sortedUsers.subList(
                    0,
                    Math.min(sortedUsers.size(), currentConfiguration.defaultMembersInOrganizationPerPage)
            );
        }

        users.onNext(sortedUsers);
    }

    private void loadUsers() {
        clearDisposables();

        subscription(Maybe.just(true)
                .delay(250, TimeUnit.MILLISECONDS)
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(final Boolean aBoolean) {
                        initializeNormalizedUsers();

                        loadUserDetails();
                    }
                }));
    }

    private void initializeNormalizedUsers() {
        normalizedUsers = new ArrayList<>();
        for (final User user : usersList) {
            normalizedUsers.add(new ListUser(user));
        }
    }

    private void loadUserDetails() {
        final AtomicReference<LoadingProgress> loadingProgressAtomicReference = new AtomicReference<>();
        final List<Maybe<User>> disposables = new ArrayList<>();

        loadingProgressAtomicReference.set(new LoadingProgress());
        loadingProgressAtomicReference.get().total = normalizedUsers.size();

        for (final ListUser _user : normalizedUsers) {
            disposables.add(userObservable
                    .fetchDetails(_user.user)
                    .onErrorResumeNext(new Function<Throwable, MaybeSource<User>>() {
                        @Override
                        public MaybeSource<User> apply(final Throwable throwable) {
                            loadingProgressAtomicReference.get().failed++;
                            loading.onNext(loadingProgressAtomicReference.get());

                            _user.incomplete = true;

                            return Maybe.just(_user.user);
                        }
                    })
                    .doOnSuccess(new Consumer<User>() {
                        @Override
                        public void accept(final User __user) {
                            loadingProgressAtomicReference.get().loaded++;
                            loading.onNext(loadingProgressAtomicReference.get());
                        }
                    })
            );
        }

        subscription(Maybe.concat(disposables).toList().subscribe(new Consumer<List<User>>() {
            @Override
            public void accept(final List<User> users) {
                loadingProgressAtomicReference.get().complete = true;
                loading.onNext(loadingProgressAtomicReference.get());

                normalizeUsers();
                state.onNext(READY);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(final Throwable throwable) {
                loadingProgressAtomicReference.get().complete = true;
                loading.onNext(loadingProgressAtomicReference.get());

                normalizeUsers();
                state.onNext(READY);
            }
        }));
    }
}