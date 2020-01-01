package org.ranapat.examples.githubbrowser.observable.user;

import org.junit.Test;
import org.ranapat.examples.githubbrowser.data.ApplicationDatabase;
import org.ranapat.examples.githubbrowser.data.dao.UserDao;
import org.ranapat.examples.githubbrowser.data.dao.UserDetailsDao;
import org.ranapat.examples.githubbrowser.data.dao.UserUrlsDao;
import org.ranapat.examples.githubbrowser.data.entity.Organization;
import org.ranapat.examples.githubbrowser.data.entity.User;
import org.ranapat.examples.githubbrowser.data.entity.UserDetails;
import org.ranapat.examples.githubbrowser.data.entity.UserUrls;
import org.ranapat.instancefactory.InstanceFactory;

import java.util.List;

import io.reactivex.observers.TestObserver;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class DataObservableTest {

    @Test
    public void shouldDefaultConstructor() {
        final Organization organization = new Organization(1, null, null);
        final ApplicationDatabase applicationDatabase = mock(ApplicationDatabase.class);
        final UserDao userDao = mock(UserDao.class);
        final UserDetailsDao userDetailsDao = mock(UserDetailsDao.class);
        final UserUrlsDao userUrlsDao = mock(UserUrlsDao.class);
        InstanceFactory.set(applicationDatabase, ApplicationDatabase.class);
        when(applicationDatabase.userDao()).thenReturn(userDao);
        when(applicationDatabase.userDetailsDao()).thenReturn(userDetailsDao);
        when(applicationDatabase.userUrlsDao()).thenReturn(userUrlsDao);
        final DataObservable dataObservable = new DataObservable();

        when(userDao.fetchByOrganizationId(1)).thenReturn(asList(mock(User.class)));

        dataObservable.fetchAllByOrganization(organization).test().awaitTerminalEvent();

        verify(userDao, times(1)).fetchByOrganizationId(1);

        InstanceFactory.remove(ApplicationDatabase.class);
    }

    @Test
    public void shouldCallFetchAllByOrganization() {
        final Organization organization = new Organization(1, null, null);
        final UserDao userDao = mock(UserDao.class);
        final UserDetailsDao userDetailsDao = mock(UserDetailsDao.class);
        final UserUrlsDao userUrlsDao = mock(UserUrlsDao.class);
        final DataObservable dataObservable = new DataObservable(
                userDao, userDetailsDao, userUrlsDao
        );

        when(userDao.fetchByOrganizationId(1)).thenReturn(asList(mock(User.class)));

        dataObservable.fetchAllByOrganization(organization).test().awaitTerminalEvent();

        verify(userDao, times(1)).fetchByOrganizationId(1);
        verify(userDetailsDao, times(1)).fetchByUserId(anyLong());
        verify(userUrlsDao, times(1)).fetchByUserId(anyLong());
    }

    @Test
    public void shouldEmitFromFetchAllByOrganizationOnce() {
        final User user = mock(User.class);
        final List<User> users = asList(user);
        final Organization organization = new Organization(1, null, null);
        final UserDao userDao = mock(UserDao.class);
        final UserDetailsDao userDetailsDao = mock(UserDetailsDao.class);
        final UserUrlsDao userUrlsDao = mock(UserUrlsDao.class);
        when(userDao.fetchByOrganizationId(1)).thenReturn(users);
        final DataObservable dataObservable = new DataObservable(
                userDao, userDetailsDao, userUrlsDao
        );

        final TestObserver<List<User>> testObserver = dataObservable.fetchAllByOrganization(organization).test();

        testObserver.awaitTerminalEvent();
        assertThat(testObserver.valueCount(), is(equalTo(1)));
        assertThat(testObserver.values().get(0).size(), is(equalTo(1)));
        assertThat(testObserver.values().get(0).get(0), is(equalTo(user)));
    }

    @Test
    public void shouldHandleNullFromFetchAllByOrganization() {
        final Organization organization = new Organization(1, null, null);
        final UserDao userDao = mock(UserDao.class);
        final UserDetailsDao userDetailsDao = mock(UserDetailsDao.class);
        final UserUrlsDao userUrlsDao = mock(UserUrlsDao.class);
        when(userDao.fetchByOrganizationId(1)).thenReturn(null);
        final DataObservable dataObservable = new DataObservable(
                userDao, userDetailsDao, userUrlsDao
        );

        final TestObserver<List<User>> testObserver = dataObservable.fetchAllByOrganization(organization).test();

        testObserver.awaitTerminalEvent();
        testObserver.assertNoValues();
    }

    @Test
    public void shouldCallFetchById() {
        final UserDao userDao = mock(UserDao.class);
        final UserDetailsDao userDetailsDao = mock(UserDetailsDao.class);
        final UserUrlsDao userUrlsDao = mock(UserUrlsDao.class);
        final DataObservable dataObservable = new DataObservable(
                userDao, userDetailsDao, userUrlsDao
        );

        when(userDao.fetchById(1)).thenReturn(mock(User.class));

        dataObservable.fetchById(1).test().awaitTerminalEvent();

        verify(userDao, times(1)).fetchById(1);
        verify(userDetailsDao, times(1)).fetchByUserId(anyLong());
        verify(userUrlsDao, times(1)).fetchByUserId(anyLong());
    }

    @Test
    public void shouldEmitFromFetchByIdOnce() {
        final User user = mock(User.class);
        final UserDao userDao = mock(UserDao.class);
        final UserDetailsDao userDetailsDao = mock(UserDetailsDao.class);
        final UserUrlsDao userUrlsDao = mock(UserUrlsDao.class);
        when(userDao.fetchById(1)).thenReturn(user);
        final DataObservable dataObservable = new DataObservable(
                userDao, userDetailsDao, userUrlsDao
        );

        final TestObserver<User> testObserver = dataObservable.fetchById(1).test();

        testObserver.awaitTerminalEvent();
        assertThat(testObserver.valueCount(), is(equalTo(1)));
        assertThat(testObserver.values().get(0), is(equalTo(user)));
    }

    @Test
    public void shouldHandleNullFromFetchById() {
        final UserDao userDao = mock(UserDao.class);
        final UserDetailsDao userDetailsDao = mock(UserDetailsDao.class);
        final UserUrlsDao userUrlsDao = mock(UserUrlsDao.class);
        when(userDao.fetchById(1)).thenReturn(null);
        final DataObservable dataObservable = new DataObservable(
                userDao, userDetailsDao, userUrlsDao
        );

        final TestObserver<User> testObserver = dataObservable.fetchById(1).test();

        testObserver.awaitTerminalEvent();
        testObserver.assertNoValues();
    }

    @Test
    public void shouldCallKeepSingleOnceOnStoreCase1() {
        final User user = mock(User.class);
        final UserDao userDao = mock(UserDao.class);
        final UserDetailsDao userDetailsDao = mock(UserDetailsDao.class);
        final UserUrlsDao userUrlsDao = mock(UserUrlsDao.class);
        final DataObservable dataObservable = new DataObservable(
                userDao, userDetailsDao, userUrlsDao
        );

        dataObservable.store(user).test().awaitTerminalEvent();

        verify(userDao, times(1)).keep(user);
        verify(userDetailsDao, times(0)).store((UserDetails) any());
        verify(userUrlsDao, times(0)).store((UserUrls) any());
    }

    @Test
    public void shouldCallKeepSingleOnceOnStoreCase2() {
        final User user = mock(User.class);
        user.details = mock(UserDetails.class);
        user.urls = mock(UserUrls.class);
        final UserDao userDao = mock(UserDao.class);
        final UserDetailsDao userDetailsDao = mock(UserDetailsDao.class);
        final UserUrlsDao userUrlsDao = mock(UserUrlsDao.class);
        final DataObservable dataObservable = new DataObservable(
                userDao, userDetailsDao, userUrlsDao
        );

        dataObservable.store(user).test().awaitTerminalEvent();

        verify(userDao, times(1)).keep(user);
        verify(userDetailsDao, times(1)).store(user.details);
        verify(userUrlsDao, times(1)).store(user.urls);
    }

    @Test
    public void shouldEmitFromKeepSingleOnceOnStore() {
        final User user = mock(User.class);
        final UserDao userDao = mock(UserDao.class);
        final UserDetailsDao userDetailsDao = mock(UserDetailsDao.class);
        final UserUrlsDao userUrlsDao = mock(UserUrlsDao.class);
        final DataObservable dataObservable = new DataObservable(
                userDao, userDetailsDao, userUrlsDao
        );

        final TestObserver<User> testObserver = dataObservable.store(user).test();

        testObserver.awaitTerminalEvent();
        testObserver.assertResult(user);
    }

    @Test
    public void shouldCallKeepListOnceOnStoreCase1() {
        final User user = mock(User.class);
        final List<User> users = asList(user);
        final UserDao userDao = mock(UserDao.class);
        final UserDetailsDao userDetailsDao = mock(UserDetailsDao.class);
        final UserUrlsDao userUrlsDao = mock(UserUrlsDao.class);
        final DataObservable dataObservable = new DataObservable(
                userDao, userDetailsDao, userUrlsDao
        );

        dataObservable.store(users).test().awaitTerminalEvent();

        verify(userDao, times(1)).keep(user);
        verify(userDetailsDao, times(0)).store((UserDetails) any());
        verify(userUrlsDao, times(0)).store((UserUrls) any());
    }

    @Test
    public void shouldCallKeepListOnceOnStoreCase2() {
        final User user = mock(User.class);
        user.details = mock(UserDetails.class);
        user.urls = mock(UserUrls.class);
        final List<User> users = asList(user);
        final UserDao userDao = mock(UserDao.class);
        final UserDetailsDao userDetailsDao = mock(UserDetailsDao.class);
        final UserUrlsDao userUrlsDao = mock(UserUrlsDao.class);
        final DataObservable dataObservable = new DataObservable(
                userDao, userDetailsDao, userUrlsDao
        );

        dataObservable.store(users).test().awaitTerminalEvent();

        verify(userDao, times(1)).keep(user);
        verify(userDetailsDao, times(1)).store(user.details);
        verify(userUrlsDao, times(1)).store(user.urls);
    }

    @Test
    public void shouldEmitFromKeepListOnceOnStore() {
        final User user = mock(User.class);
        final List<User> users = asList(user);
        final UserDao userDao = mock(UserDao.class);
        final UserDetailsDao userDetailsDao = mock(UserDetailsDao.class);
        final UserUrlsDao userUrlsDao = mock(UserUrlsDao.class);
        final DataObservable dataObservable = new DataObservable(
                userDao, userDetailsDao, userUrlsDao
        );

        final TestObserver<List<User>> testObserver = dataObservable.store(users).test();

        testObserver.awaitTerminalEvent();
        assertThat(testObserver.valueCount(), is(equalTo(1)));
        assertThat(testObserver.values().get(0).size(), is(equalTo(1)));
        assertThat(testObserver.values().get(0).get(0), is(equalTo(user)));
    }

}