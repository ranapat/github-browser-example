package org.ranapat.examples.githubbrowser.observable.user;

import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.ranapat.examples.githubbrowser.api.rao.UserRao;
import org.ranapat.examples.githubbrowser.data.entity.Organization;
import org.ranapat.examples.githubbrowser.data.entity.User;
import org.ranapat.examples.githubbrowser.data.entity.UserDetails;
import org.ranapat.instancefactory.InstanceFactory;

import java.util.List;

import io.reactivex.observers.TestObserver;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ApiObservableTest {

    @Test
    public void shouldHandleDefaultConstructor() throws Exception {
        final Organization organization = mock(Organization.class);
        final UserRao userRao = mock(UserRao.class);

        InstanceFactory.set(userRao, UserRao.class);

        final ApiObservable apiObservable = new ApiObservable(
        );

        apiObservable.fetchMemberList("url", organization, 1).test().awaitTerminalEvent();

        verify(userRao, times(1)).fetchMemberList("url", organization, 1);

        InstanceFactory.remove(UserRao.class);
    }

    @Test
    public void shouldCallFetchMemberListOnce() throws Exception {
        final Organization organization = mock(Organization.class);
        final UserRao userRao = mock(UserRao.class);
        final ApiObservable apiObservable = new ApiObservable(
                userRao
        );

        apiObservable.fetchMemberList("url", organization, 1).test().awaitTerminalEvent();

        verify(userRao, times(1)).fetchMemberList("url", organization, 1);
    }

    @Test
    public void shouldEmitOnceOnFetchMemberList() throws Exception {
        final User user = mock(User.class);
        final List<User> users = asList(user);
        final Organization organization = mock(Organization.class);
        final UserRao userRao = mock(UserRao.class);
        when(userRao.fetchMemberList("url", organization, 1)).thenReturn(
                users
        );
        ApiObservable apiObservable = new ApiObservable(
                userRao
        );

        final TestObserver<List<User>> testObserver = apiObservable.fetchMemberList("url", organization, 1).test();

        testObserver.awaitTerminalEvent();
        assertThat(testObserver.valueCount(), is(equalTo(1)));
        assertThat(testObserver.values().get(0).size(), is(equalTo(1)));
        assertThat(testObserver.values().get(0).get(0), is(equalTo(user)));
    }

    @Test
    public void shouldNotThrowOnFetchMemberList() throws Exception {
        final Organization organization = mock(Organization.class);
        final UserRao userRao = mock(UserRao.class);
        when(userRao.fetchMemberList("url", organization, 1)).thenThrow(Exception.class);
        final ApiObservable apiObservable = new ApiObservable(
                userRao
        );

        final TestObserver<List<User>> testObserver = apiObservable.fetchMemberList("url", organization, 1).test();

        testObserver.awaitTerminalEvent();
        testObserver.assertNoValues();
    }

    @Test
    public void shouldCallFetchDetailsOnce() throws Exception {
        final User user = mock(User.class);
        final UserRao userRao = mock(UserRao.class);
        final ApiObservable apiObservable = new ApiObservable(
                userRao
        );

        apiObservable.fetchDetails("url", user).test().awaitTerminalEvent();

        verify(userRao, times(1)).fetchDetails("url", user);
    }

    @Test
    public void shouldEmitOnceOnFetchDetails() throws Exception {
        final User user = mock(User.class);
        final UserDetails userDetails = mock(UserDetails.class);
        final UserRao userRao = mock(UserRao.class);
        when(userRao.fetchDetails("url", user)).thenAnswer(new Answer<User>() {
            @Override
            public User answer(InvocationOnMock invocation) {
                user.details = userDetails;
                return user;
            }
        });
        ApiObservable apiObservable = new ApiObservable(
                userRao
        );

        final TestObserver<User> testObserver = apiObservable.fetchDetails("url", user).test();

        testObserver.awaitTerminalEvent();
        assertThat(testObserver.valueCount(), is(equalTo(1)));
        assertThat(testObserver.values().get(0), is(equalTo(user)));
        assertThat(testObserver.values().get(0).details, is(equalTo(userDetails)));
    }

    @Test
    public void shouldNotThrowOnFetchDetails() throws Exception {
        final User user = mock(User.class);
        final UserRao userRao = mock(UserRao.class);
        when(userRao.fetchDetails("url", user)).thenThrow(Exception.class);
        final ApiObservable apiObservable = new ApiObservable(
                userRao
        );

        final TestObserver<User> testObserver = apiObservable.fetchDetails("url", user).test();

        testObserver.awaitTerminalEvent();
        testObserver.assertNoValues();
    }

}