package org.ranapat.examples.githubbrowser.observable.organization;

import org.junit.Test;
import org.ranapat.examples.githubbrowser.api.rao.OrganizationRao;
import org.ranapat.examples.githubbrowser.data.entity.Organization;
import org.ranapat.instancefactory.InstanceFactory;

import io.reactivex.observers.TestObserver;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ApiObservableTest {

    @Test
    public void shouldHandleDefaultConstructor() throws Exception {
        final OrganizationRao organizationRao = mock(OrganizationRao.class);

        InstanceFactory.set(organizationRao, OrganizationRao.class);

        final ApiObservable apiObservable = new ApiObservable(
        );

        apiObservable.fetchByLogin("url", "login").test().awaitTerminalEvent();

        verify(organizationRao, times(1)).fetchByLogin("url", "login");

        InstanceFactory.remove(OrganizationRao.class);
    }

    @Test
    public void shouldCallFetchByLoginOnce() throws Exception {
        final OrganizationRao organizationRao = mock(OrganizationRao.class);
        final ApiObservable apiObservable = new ApiObservable(
                organizationRao
        );

        apiObservable.fetchByLogin("url", "login").test().awaitTerminalEvent();

        verify(organizationRao, times(1)).fetchByLogin("url", "login");
    }

    @Test
    public void shouldEmitOnceOnFetchFromLogin() throws Exception {
        final Organization organization = mock(Organization.class);
        final OrganizationRao organizationRao = mock(OrganizationRao.class);
        when(organizationRao.fetchByLogin("url", "login")).thenReturn(
                organization
        );
        ApiObservable apiObservable = new ApiObservable(
                organizationRao
        );

        final TestObserver<Organization> testObserver = apiObservable.fetchByLogin("url", "login").test();

        testObserver.awaitTerminalEvent();
        testObserver.assertResult(organization);
    }

    @Test
    public void shouldNotThrowOnFetchByLogin() throws Exception {
        final OrganizationRao organizationRao = mock(OrganizationRao.class);
        when(organizationRao.fetchByLogin("url", "login")).thenThrow(Exception.class);
        final ApiObservable apiObservable = new ApiObservable(
                organizationRao
        );

        final TestObserver<Organization> testObserver = apiObservable.fetchByLogin("url", "login").test();

        testObserver.awaitTerminalEvent();
        testObserver.assertNoValues();
    }

}