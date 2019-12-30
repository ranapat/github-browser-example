package org.ranapat.examples.githubbrowser.observable.user;

import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.ranapat.examples.githubbrowser.data.entity.Configuration;
import org.ranapat.examples.githubbrowser.data.entity.Organization;
import org.ranapat.examples.githubbrowser.data.entity.User;
import org.ranapat.examples.githubbrowser.observable.ExceptionChecker;
import org.ranapat.examples.githubbrowser.observable.configuration.ConfigurationObservable;
import org.ranapat.examples.githubbrowser.observable.exceptions.UsersUndefinedException;
import org.ranapat.instancefactory.InstanceFactory;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Maybe;
import io.reactivex.observers.TestObserver;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class UserObservableTest {

    @Test
    public void shouldHandleDefaultConstructor() {
        final Configuration configuration = new Configuration(1, 1, null, "url", null, null);
        final Organization organization = new Organization(1, null, null);

        final ApiObservable apiObservable = mock(ApiObservable.class);
        final DataObservable dataObservable = mock(DataObservable.class);
        final ConfigurationObservable configurationObservable = mock(ConfigurationObservable.class);

        InstanceFactory.set(apiObservable, ApiObservable.class);
        InstanceFactory.set(dataObservable, DataObservable.class);
        InstanceFactory.set(configurationObservable, ConfigurationObservable.class);

        when(apiObservable.fetchMemberList("url", organization, 1)).thenReturn(Maybe.<List<User>>empty());
        when(dataObservable.fetchAllByOrganization(organization)).thenReturn(Maybe.<List<User>>empty());
        when(configurationObservable.fetch()).thenReturn(Maybe.just(configuration));

        final UserObservable userObservable = new UserObservable(
        );

        final TestObserver<List<User>> testObserver = userObservable.fetchAllByOrganization(organization).test();

        testObserver.awaitTerminalEvent();
        assertThat(ExceptionChecker.isLast(testObserver.errors().get(0), UsersUndefinedException.class), is(equalTo(true)));

        InstanceFactory.remove(ApiObservable.class);
        InstanceFactory.remove(DataObservable.class);
        InstanceFactory.remove(ConfigurationObservable.class);
    }

    @Test
    public void shouldThrowExceptionOnEmptyResultsCase1() {
        final Configuration configuration = new Configuration(1, 1, null, "url", null, null);
        final Organization organization = new Organization(1, null, null);

        final ApiObservable apiObservable = mock(ApiObservable.class);
        final DataObservable dataObservable = mock(DataObservable.class);
        final ConfigurationObservable configurationObservable = mock(ConfigurationObservable.class);

        when(apiObservable.fetchMemberList("url", organization, 1)).thenReturn(Maybe.<List<User>>empty());
        when(dataObservable.fetchAllByOrganization(organization)).thenReturn(Maybe.<List<User>>empty());
        when(configurationObservable.fetch()).thenReturn(Maybe.just(configuration));

        final UserObservable userObservable = new UserObservable(
                apiObservable, dataObservable, configurationObservable
        );

        final TestObserver<List<User>> testObserver = userObservable.fetchAllByOrganization(organization).test();

        testObserver.awaitTerminalEvent();
        assertThat(ExceptionChecker.isLast(testObserver.errors().get(0), UsersUndefinedException.class), is(equalTo(true)));
    }

    @Test
    public void shouldThrowExceptionOnEmptyResultsCase2() {
        final Configuration configuration = new Configuration(1, 1, null, "url", null, null);
        final Organization organization = new Organization(1, null, null);
        final List<User> emptyUsers = new ArrayList<>();

        final ApiObservable apiObservable = mock(ApiObservable.class);
        final DataObservable dataObservable = mock(DataObservable.class);
        final ConfigurationObservable configurationObservable = mock(ConfigurationObservable.class);

        when(apiObservable.fetchMemberList("url", organization, 1)).thenReturn(Maybe.just(emptyUsers));
        when(dataObservable.fetchAllByOrganization(organization)).thenReturn(Maybe.just(emptyUsers));
        when(configurationObservable.fetch()).thenReturn(Maybe.just(configuration));

        final UserObservable userObservable = new UserObservable(
                apiObservable, dataObservable, configurationObservable
        );

        final TestObserver<List<User>> testObserver = userObservable.fetchAllByOrganization(organization).test();

        testObserver.awaitTerminalEvent();
        assertThat(ExceptionChecker.isLast(testObserver.errors().get(0), UsersUndefinedException.class), is(equalTo(true)));
    }

    @Test
    public void shouldEmitUpToDateDataFromData() {
        final Configuration configuration = new Configuration(1, 1, null, "url", null, null);
        final Organization organization = new Organization(1, null, null);
        final User user = mock(User.class);
        final List<User> users = asList(user);

        final ApiObservable apiObservable = mock(ApiObservable.class);
        final DataObservable dataObservable = mock(DataObservable.class);
        final ConfigurationObservable configurationObservable = mock(ConfigurationObservable.class);

        when(user.isUpToDate()).thenReturn(true);
        when(apiObservable.fetchMemberList("url", organization, 1)).thenReturn(Maybe.<List<User>>empty());
        when(dataObservable.fetchAllByOrganization(organization)).thenReturn(Maybe.just(users));
        when(configurationObservable.fetch()).thenReturn(Maybe.just(configuration));

        final UserObservable userObservable = new UserObservable(
                apiObservable, dataObservable, configurationObservable
        );

        final TestObserver<List<User>> testObserver = userObservable.fetchAllByOrganization(organization).test();

        testObserver.awaitTerminalEvent();
        assertThat(testObserver.valueCount(), is(equalTo(1)));
        assertThat(testObserver.values().get(0).size(), is(equalTo(1)));
        assertThat(testObserver.values().get(0).get(0), is(equalTo(user)));
    }

    @Test
    public void shouldEmitUpToDateDataFromApiCase1() {
        final Configuration configuration = new Configuration(1, 1, null, "url", null, null);
        final Organization organization = new Organization(1, null, null);
        final User user = mock(User.class);
        final List<User> users = asList(user);

        final ApiObservable apiObservable = mock(ApiObservable.class);
        final DataObservable dataObservable = mock(DataObservable.class);
        final ConfigurationObservable configurationObservable = mock(ConfigurationObservable.class);

        when(user.isUpToDate()).thenReturn(true);
        when(apiObservable.fetchMemberList("url", organization, 1)).thenReturn(Maybe.just(users));
        when(apiObservable.fetchMemberList("url", organization, 2)).thenReturn(Maybe.<List<User>>empty());
        when(dataObservable.fetchAllByOrganization(organization)).thenReturn(Maybe.<List<User>>empty());
        when(dataObservable.store(users)).thenReturn(Maybe.just(users));
        when(configurationObservable.fetch()).thenReturn(Maybe.just(configuration));

        final UserObservable userObservable = new UserObservable(
                apiObservable, dataObservable, configurationObservable
        );

        final TestObserver<List<User>> testObserver = userObservable.fetchAllByOrganization(organization).test();

        testObserver.awaitTerminalEvent();
        assertThat(testObserver.valueCount(), is(equalTo(1)));
        assertThat(testObserver.values().get(0).size(), is(equalTo(1)));
        assertThat(testObserver.values().get(0).get(0), is(equalTo(user)));
    }

    @Test
    public void shouldEmitUpToDateDataFromApiCase2() {
        final Configuration configuration = new Configuration(1, 1, null, "url", null, null);
        final Organization organization = new Organization(1, null, null);
        final User user = mock(User.class);
        final List<User> users = asList(user);
        final List<User> emptyUsers = new ArrayList<>();

        final ApiObservable apiObservable = mock(ApiObservable.class);
        final DataObservable dataObservable = mock(DataObservable.class);
        final ConfigurationObservable configurationObservable = mock(ConfigurationObservable.class);

        when(user.isUpToDate()).thenReturn(true);
        when(apiObservable.fetchMemberList("url", organization, 1)).thenReturn(Maybe.just(users));
        when(apiObservable.fetchMemberList("url", organization, 2)).thenReturn(Maybe.just(emptyUsers));
        when(dataObservable.fetchAllByOrganization(organization)).thenReturn(Maybe.<List<User>>empty());
        when(dataObservable.store(users)).thenReturn(Maybe.just(users));
        when(configurationObservable.fetch()).thenReturn(Maybe.just(configuration));

        final UserObservable userObservable = new UserObservable(
                apiObservable, dataObservable, configurationObservable
        );

        final TestObserver<List<User>> testObserver = userObservable.fetchAllByOrganization(organization).test();

        testObserver.awaitTerminalEvent();
        assertThat(testObserver.valueCount(), is(equalTo(1)));
        assertThat(testObserver.values().get(0).size(), is(equalTo(1)));
        assertThat(testObserver.values().get(0).get(0), is(equalTo(user)));
    }

    @Test
    public void shouldNotCallStoreCase1() {
        final Configuration configuration = new Configuration(1, 1, null, "url", null, null);
        final Organization organization = new Organization(1, null, null);
        final User user = mock(User.class);
        final List<User> users = asList(user);

        final ApiObservable apiObservable = mock(ApiObservable.class);
        final DataObservable dataObservable = mock(DataObservable.class);
        final ConfigurationObservable configurationObservable = mock(ConfigurationObservable.class);

        when(user.isUpToDate()).thenReturn(true);
        when(apiObservable.fetchMemberList("url", organization, 1)).thenReturn(Maybe.<List<User>>empty());
        when(dataObservable.fetchAllByOrganization(organization)).thenReturn(Maybe.just(users));
        when(configurationObservable.fetch()).thenReturn(Maybe.just(configuration));

        final UserObservable userObservable = new UserObservable(
                apiObservable, dataObservable, configurationObservable
        );

        final TestObserver<List<User>> testObserver = userObservable.fetchAllByOrganization(organization).test();

        testObserver.awaitTerminalEvent();
        assertThat(testObserver.valueCount(), is(equalTo(1)));
        assertThat(testObserver.values().get(0).size(), is(equalTo(1)));
        assertThat(testObserver.values().get(0).get(0), is(equalTo(user)));
        verify(dataObservable, times(0)).store(ArgumentMatchers.<User>anyList());
    }

    @Test
    public void shouldNotCallStoreCase2() {
        final Configuration configuration = new Configuration(1, 1, null, "url", null, null);
        final Organization organization = new Organization(1, null, null);
        final User user = mock(User.class);
        final List<User> users = asList(user);

        final ApiObservable apiObservable = mock(ApiObservable.class);
        final DataObservable dataObservable = mock(DataObservable.class);
        final ConfigurationObservable configurationObservable = mock(ConfigurationObservable.class);

        when(user.isUpToDate()).thenReturn(false);
        when(apiObservable.fetchMemberList("url", organization, 1)).thenReturn(Maybe.<List<User>>empty());
        when(dataObservable.fetchAllByOrganization(organization)).thenReturn(Maybe.just(users));
        when(configurationObservable.fetch()).thenReturn(Maybe.just(configuration));

        final UserObservable userObservable = new UserObservable(
                apiObservable, dataObservable, configurationObservable
        );

        final TestObserver<List<User>> testObserver = userObservable.fetchAllByOrganization(organization).test();

        testObserver.awaitTerminalEvent();
        assertThat(testObserver.valueCount(), is(equalTo(1)));
        assertThat(testObserver.values().get(0).size(), is(equalTo(1)));
        assertThat(testObserver.values().get(0).get(0), is(equalTo(user)));
        verify(dataObservable, times(0)).store(ArgumentMatchers.<User>anyList());
    }

    @Test
    public void shouldCallStoreOnce() {
        final Configuration configuration = new Configuration(1, 1, null, "url", null, null);
        final Organization organization = new Organization(1, null, null);
        final User user = mock(User.class);
        final List<User> users = asList(user);

        final ApiObservable apiObservable = mock(ApiObservable.class);
        final DataObservable dataObservable = mock(DataObservable.class);
        final ConfigurationObservable configurationObservable = mock(ConfigurationObservable.class);

        when(user.isUpToDate()).thenReturn(true);
        when(apiObservable.fetchMemberList("url", organization, 1)).thenReturn(Maybe.just(users));
        when(apiObservable.fetchMemberList("url", organization, 2)).thenReturn(Maybe.<List<User>>empty());
        when(dataObservable.fetchAllByOrganization(organization)).thenReturn(Maybe.<List<User>>empty());
        when(dataObservable.store(users)).thenReturn(Maybe.just(users));
        when(configurationObservable.fetch()).thenReturn(Maybe.just(configuration));

        final UserObservable userObservable = new UserObservable(
                apiObservable, dataObservable, configurationObservable
        );

        final TestObserver<List<User>> testObserver = userObservable.fetchAllByOrganization(organization).test();

        testObserver.awaitTerminalEvent();
        assertThat(testObserver.valueCount(), is(equalTo(1)));
        assertThat(testObserver.values().get(0).size(), is(equalTo(1)));
        assertThat(testObserver.values().get(0).get(0), is(equalTo(user)));
        verify(dataObservable, times(1)).store(users);
    }

    @Test
    public void shouldEmitUpToDateDataCase1() {
        final Configuration configuration = new Configuration(1, 1, null, "url", null, null);
        final Organization organization = new Organization(1, null, null);
        final User user1 = mock(User.class);
        final List<User> users1 = asList(user1);
        final User user2 = mock(User.class);
        final List<User> users2 = asList(user2);

        final ApiObservable apiObservable = mock(ApiObservable.class);
        final DataObservable dataObservable = mock(DataObservable.class);
        final ConfigurationObservable configurationObservable = mock(ConfigurationObservable.class);

        when(user1.isUpToDate()).thenReturn(false);
        when(user2.isUpToDate()).thenReturn(true);
        when(apiObservable.fetchMemberList("url", organization, 1)).thenReturn(Maybe.just(users2));
        when(apiObservable.fetchMemberList("url", organization, 2)).thenReturn(Maybe.<List<User>>empty());
        when(dataObservable.fetchAllByOrganization(organization)).thenReturn(Maybe.just(users1));
        when(dataObservable.store(users1)).thenReturn(Maybe.just(users1));
        when(dataObservable.store(users2)).thenReturn(Maybe.just(users2));
        when(configurationObservable.fetch()).thenReturn(Maybe.just(configuration));

        final UserObservable userObservable = new UserObservable(
                apiObservable, dataObservable, configurationObservable
        );

        final TestObserver<List<User>> testObserver = userObservable.fetchAllByOrganization(organization).test();

        testObserver.awaitTerminalEvent();
        assertThat(testObserver.valueCount(), is(equalTo(1)));
        assertThat(testObserver.values().get(0).size(), is(equalTo(1)));
        assertThat(testObserver.values().get(0).get(0), is(equalTo(user2)));
    }

    @Test
    public void shouldEmitUpToDateDataCase2() {
        final Configuration configuration = new Configuration(1, 1, null, "url", null, null);
        final Organization organization = new Organization(1, null, null);
        final User user1 = mock(User.class);
        final List<User> users1 = asList(user1);
        final User user2 = mock(User.class);
        final List<User> users2 = asList(user2);

        final ApiObservable apiObservable = mock(ApiObservable.class);
        final DataObservable dataObservable = mock(DataObservable.class);
        final ConfigurationObservable configurationObservable = mock(ConfigurationObservable.class);

        when(user1.isUpToDate()).thenReturn(false);
        when(user2.isUpToDate()).thenReturn(true);
        when(apiObservable.fetchMemberList("url", organization, 1)).thenReturn(Maybe.just(users1));
        when(apiObservable.fetchMemberList("url", organization, 2)).thenReturn(Maybe.<List<User>>empty());
        when(dataObservable.fetchAllByOrganization(organization)).thenReturn(Maybe.just(users2));
        when(dataObservable.store(users1)).thenReturn(Maybe.just(users1));
        when(dataObservable.store(users2)).thenReturn(Maybe.just(users2));
        when(configurationObservable.fetch()).thenReturn(Maybe.just(configuration));

        final UserObservable userObservable = new UserObservable(
                apiObservable, dataObservable, configurationObservable
        );

        final TestObserver<List<User>> testObserver = userObservable.fetchAllByOrganization(organization).test();

        testObserver.awaitTerminalEvent();
        assertThat(testObserver.valueCount(), is(equalTo(1)));
        assertThat(testObserver.values().get(0).size(), is(equalTo(1)));
        assertThat(testObserver.values().get(0).get(0), is(equalTo(user2)));
    }

    @Test
    public void shouldEmitUpToDateDataCase3() {
        final Configuration configuration = new Configuration(1, 1, null, "url", null, null);
        final Organization organization = new Organization(1, null, null);
        final User user1 = mock(User.class);
        final List<User> users1 = asList(user1);
        final User user2 = mock(User.class);
        final List<User> users2 = asList(user2);

        final ApiObservable apiObservable = mock(ApiObservable.class);
        final DataObservable dataObservable = mock(DataObservable.class);
        final ConfigurationObservable configurationObservable = mock(ConfigurationObservable.class);

        when(user1.isUpToDate()).thenReturn(true);
        when(user2.isUpToDate()).thenReturn(true);
        when(apiObservable.fetchMemberList("url", organization, 1)).thenReturn(Maybe.just(users1));
        when(apiObservable.fetchMemberList("url", organization, 2)).thenReturn(Maybe.<List<User>>empty());
        when(dataObservable.fetchAllByOrganization(organization)).thenReturn(Maybe.just(users2));
        when(dataObservable.store(users1)).thenReturn(Maybe.just(users1));
        when(dataObservable.store(users2)).thenReturn(Maybe.just(users2));
        when(configurationObservable.fetch()).thenReturn(Maybe.just(configuration));

        final UserObservable userObservable = new UserObservable(
                apiObservable, dataObservable, configurationObservable
        );

        final TestObserver<List<User>> testObserver = userObservable.fetchAllByOrganization(organization).test();

        testObserver.awaitTerminalEvent();
        assertThat(testObserver.valueCount(), is(equalTo(1)));
        assertThat(testObserver.values().get(0).size(), is(equalTo(1)));
        assertThat(testObserver.values().get(0).get(0), is(equalTo(user2)));
    }

    @Test
    public void shouldEmitNotUpToDateDataCase1() {
        final Configuration configuration = new Configuration(1, 1, null, "url", null, null);
        final Organization organization = new Organization(1, null, null);
        final User user1 = mock(User.class);
        final List<User> users1 = asList(user1);
        final User user2 = mock(User.class);
        final List<User> users2 = asList(user2);

        final ApiObservable apiObservable = mock(ApiObservable.class);
        final DataObservable dataObservable = mock(DataObservable.class);
        final ConfigurationObservable configurationObservable = mock(ConfigurationObservable.class);

        when(user1.isUpToDate()).thenReturn(false);
        when(user2.isUpToDate()).thenReturn(false);
        when(apiObservable.fetchMemberList("url", organization, 1)).thenReturn(Maybe.just(users1));
        when(apiObservable.fetchMemberList("url", organization, 2)).thenReturn(Maybe.<List<User>>empty());
        when(dataObservable.fetchAllByOrganization(organization)).thenReturn(Maybe.just(users2));
        when(dataObservable.store(users1)).thenReturn(Maybe.just(users1));
        when(dataObservable.store(users2)).thenReturn(Maybe.just(users2));
        when(configurationObservable.fetch()).thenReturn(Maybe.just(configuration));

        final UserObservable userObservable = new UserObservable(
                apiObservable, dataObservable, configurationObservable
        );

        final TestObserver<List<User>> testObserver = userObservable.fetchAllByOrganization(organization).test();

        testObserver.awaitTerminalEvent();
        assertThat(testObserver.valueCount(), is(equalTo(1)));
        assertThat(testObserver.values().get(0).size(), is(equalTo(1)));
        assertThat(testObserver.values().get(0).get(0), is(equalTo(user2)));
    }

    @Test
    public void shouldEmitNotUpToDateDataCase2() {
        final Configuration configuration = new Configuration(1, 1, null, "url", null, null);
        final Organization organization = new Organization(1, null, null);
        final User user = mock(User.class);
        final List<User> users = asList(user);

        final ApiObservable apiObservable = mock(ApiObservable.class);
        final DataObservable dataObservable = mock(DataObservable.class);
        final ConfigurationObservable configurationObservable = mock(ConfigurationObservable.class);

        when(user.isUpToDate()).thenReturn(false);
        when(apiObservable.fetchMemberList("url", organization, 1)).thenReturn(Maybe.<List<User>>empty());
        when(dataObservable.fetchAllByOrganization(organization)).thenReturn(Maybe.just(users));
        when(dataObservable.store(users)).thenReturn(Maybe.just(users));
        when(configurationObservable.fetch()).thenReturn(Maybe.just(configuration));

        final UserObservable userObservable = new UserObservable(
                apiObservable, dataObservable, configurationObservable
        );

        final TestObserver<List<User>> testObserver = userObservable.fetchAllByOrganization(organization).test();

        testObserver.awaitTerminalEvent();
        assertThat(testObserver.valueCount(), is(equalTo(1)));
        assertThat(testObserver.values().get(0).size(), is(equalTo(1)));
        assertThat(testObserver.values().get(0).get(0), is(equalTo(user)));
    }

    @Test
    public void shouldCombineAllPagesFromApiCalls() {
        final Configuration configuration = new Configuration(1, 1, null, "url", null, null);
        final Organization organization = new Organization(1, null, null);
        final User user1 = mock(User.class);
        final User user2 = mock(User.class);
        final List<User> users1 = asList(user1);
        final List<User> users2 = asList(user2);

        final ApiObservable apiObservable = mock(ApiObservable.class);
        final DataObservable dataObservable = mock(DataObservable.class);
        final ConfigurationObservable configurationObservable = mock(ConfigurationObservable.class);

        when(user1.isUpToDate()).thenReturn(true);
        when(user2.isUpToDate()).thenReturn(true);
        when(apiObservable.fetchMemberList("url", organization, 1)).thenReturn(Maybe.just(users1));
        when(apiObservable.fetchMemberList("url", organization, 2)).thenReturn(Maybe.just(users2));
        when(apiObservable.fetchMemberList("url", organization, 3)).thenReturn(Maybe.<List<User>>empty());
        when(dataObservable.fetchAllByOrganization(organization)).thenReturn(Maybe.<List<User>>empty());
        when(dataObservable.store(ArgumentMatchers.<User>anyList())).thenAnswer(new Answer<Maybe<List<User>>>() {
            @Override
            public Maybe<List<User>> answer(final InvocationOnMock invocation) {
                final Object[] args = invocation.getArguments();
                final List<User> users = (List<User>) args[0];
                return Maybe.just(users);
            }
        });
        when(configurationObservable.fetch()).thenReturn(Maybe.just(configuration));

        final UserObservable userObservable = new UserObservable(
                apiObservable, dataObservable, configurationObservable
        );

        final TestObserver<List<User>> testObserver = userObservable.fetchAllByOrganization(organization).test();

        testObserver.awaitTerminalEvent();
        assertThat(testObserver.valueCount(), is(equalTo(1)));
        assertThat(testObserver.values().get(0).size(), is(equalTo(2)));
        assertThat(testObserver.values().get(0).get(0), is(equalTo(user1)));
        assertThat(testObserver.values().get(0).get(1), is(equalTo(user2)));
    }

}