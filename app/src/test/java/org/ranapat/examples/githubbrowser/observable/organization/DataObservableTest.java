package org.ranapat.examples.githubbrowser.observable.organization;

import org.junit.Test;
import org.ranapat.examples.githubbrowser.data.ApplicationDatabase;
import org.ranapat.examples.githubbrowser.data.dao.OrganizationDao;
import org.ranapat.examples.githubbrowser.data.entity.Organization;
import org.ranapat.instancefactory.InstanceFactory;

import io.reactivex.observers.TestObserver;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class DataObservableTest {

    @Test
    public void shouldDefaultConstructor() {
        final ApplicationDatabase applicationDatabase = mock(ApplicationDatabase.class);
        final OrganizationDao organizationDao = mock(OrganizationDao.class);
        InstanceFactory.set(applicationDatabase, ApplicationDatabase.class);
        when(applicationDatabase.organizationDao()).thenReturn(organizationDao);
        final DataObservable dataObservable = new DataObservable();

        when(organizationDao.fetchByLogin("login")).thenReturn(mock(Organization.class));

        dataObservable.fetchByLogin("login").test().awaitTerminalEvent();

        verify(organizationDao, times(1)).fetchByLogin("login");

        InstanceFactory.remove(ApplicationDatabase.class);
    }

    @Test
    public void shouldCallFetchByLoginOnce() {
        final OrganizationDao organizationDao = mock(OrganizationDao.class);
        final DataObservable dataObservable = new DataObservable(
                organizationDao
        );

        when(organizationDao.fetchByLogin("login")).thenReturn(mock(Organization.class));

        dataObservable.fetchByLogin("login").test().awaitTerminalEvent();

        verify(organizationDao, times(1)).fetchByLogin("login");
    }

    @Test
    public void shouldEmitFromFetchByLoginOnce() {
        final Organization organization = mock(Organization.class);
        final OrganizationDao organizationDao = mock(OrganizationDao.class);
        when(organizationDao.fetchByLogin("login")).thenReturn(organization);
        final DataObservable dataObservable = new DataObservable(
                organizationDao
        );

        final TestObserver<Organization> testObserver = dataObservable.fetchByLogin("login").test();

        testObserver.awaitTerminalEvent();
        testObserver.assertResult(organization);
    }

    @Test
    public void shouldHandleNullFromFetchByLoginOnce() {
        final OrganizationDao organizationDao = mock(OrganizationDao.class);
        when(organizationDao.fetchByLogin("login")).thenReturn(null);
        final DataObservable dataObservable = new DataObservable(
                organizationDao
        );

        final TestObserver<Organization> testObserver = dataObservable.fetchByLogin("login").test();

        testObserver.awaitTerminalEvent();
        testObserver.assertNoValues();
    }

    @Test
    public void shouldCallKeepOnceOnStore() {
        final Organization organization = mock(Organization.class);
        final OrganizationDao organizationDao = mock(OrganizationDao.class);
        final DataObservable dataObservable = new DataObservable(
                organizationDao
        );

        dataObservable.store(organization).test().awaitTerminalEvent();

        verify(organizationDao, times(1)).keep(organization);
    }

    @Test
    public void shouldEmitFromKeepOnceOnStore() {
        final Organization organization = mock(Organization.class);
        final OrganizationDao organizationDao = mock(OrganizationDao.class);
        final DataObservable dataObservable = new DataObservable(
                organizationDao
        );

        final TestObserver<Organization> testObserver = dataObservable.store(organization).test();

        testObserver.awaitTerminalEvent();
        testObserver.assertResult(organization);
    }

}