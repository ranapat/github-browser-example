package org.ranapat.examples.githubbrowser.observable.user;

import org.junit.Test;
import org.ranapat.examples.githubbrowser.api.rao.UserRao;
import org.ranapat.examples.githubbrowser.data.entity.Organization;
import org.ranapat.examples.githubbrowser.data.entity.User;
import org.ranapat.instancefactory.InstanceFactory;

import java.util.List;

import io.reactivex.observers.TestObserver;

import static java.util.Arrays.asList;
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
        testObserver.assertResult(users);
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

}