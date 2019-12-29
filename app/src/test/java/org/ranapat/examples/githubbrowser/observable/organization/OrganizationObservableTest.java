package org.ranapat.examples.githubbrowser.observable.organization;

import org.junit.Test;
import org.ranapat.examples.githubbrowser.data.entity.Configuration;
import org.ranapat.examples.githubbrowser.data.entity.Organization;
import org.ranapat.examples.githubbrowser.observable.configuration.ConfigurationObservable;
import org.ranapat.examples.githubbrowser.observable.exceptions.OrganizationUndefinedException;
import org.ranapat.instancefactory.InstanceFactory;

import io.reactivex.Maybe;
import io.reactivex.observers.TestObserver;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class OrganizationObservableTest {

    @Test
    public void shouldHandleDefaultConstructor() {
        final Configuration configuration = new Configuration(1, 1, "url", null, null, null);

        final ApiObservable apiObservable = mock(ApiObservable.class);
        final DataObservable dataObservable = mock(DataObservable.class);
        final ConfigurationObservable configurationObservable = mock(ConfigurationObservable.class);

        InstanceFactory.set(apiObservable, ApiObservable.class);
        InstanceFactory.set(dataObservable, DataObservable.class);
        InstanceFactory.set(configurationObservable, ConfigurationObservable.class);

        when(apiObservable.fetchByLogin("url", "login")).thenReturn(Maybe.<Organization>empty());
        when(dataObservable.fetchByLogin("login")).thenReturn(Maybe.<Organization>empty());
        when(configurationObservable.fetch()).thenReturn(Maybe.just(configuration));

        final OrganizationObservable organizationObservable = new OrganizationObservable(
        );

        final TestObserver<Organization> testObserver = organizationObservable.fetchByLogin("login").test();

        testObserver.awaitTerminalEvent();
        testObserver.assertError(OrganizationUndefinedException.class);

        InstanceFactory.remove(ApiObservable.class);
        InstanceFactory.remove(DataObservable.class);
        InstanceFactory.remove(ConfigurationObservable.class);
    }

    @Test
    public void shouldThrowExceptionOnEmptyResults() {
        final Configuration configuration = new Configuration(1, 1, "url", null, null, null);

        final ApiObservable apiObservable = mock(ApiObservable.class);
        final DataObservable dataObservable = mock(DataObservable.class);
        final ConfigurationObservable configurationObservable = mock(ConfigurationObservable.class);

        when(apiObservable.fetchByLogin("url", "login")).thenReturn(Maybe.<Organization>empty());
        when(dataObservable.fetchByLogin("login")).thenReturn(Maybe.<Organization>empty());
        when(configurationObservable.fetch()).thenReturn(Maybe.just(configuration));

        final OrganizationObservable organizationObservable = new OrganizationObservable(
                apiObservable, dataObservable, configurationObservable
        );

        final TestObserver<Organization> testObserver = organizationObservable.fetchByLogin("login").test();

        testObserver.awaitTerminalEvent();
        testObserver.assertError(OrganizationUndefinedException.class);
    }

    @Test
    public void shouldEmitUpToDateDataFromData() {
        final Configuration configuration = new Configuration(1, 1, "url", null, null, null);
        final Organization organization = mock(Organization.class);

        final ApiObservable apiObservable = mock(ApiObservable.class);
        final DataObservable dataObservable = mock(DataObservable.class);
        final ConfigurationObservable configurationObservable = mock(ConfigurationObservable.class);

        when(organization.isUpToDate()).thenReturn(true);
        when(apiObservable.fetchByLogin("url", "login")).thenReturn(Maybe.<Organization>empty());
        when(dataObservable.fetchByLogin("login")).thenReturn(Maybe.just(organization));
        when(configurationObservable.fetch()).thenReturn(Maybe.just(configuration));

        final OrganizationObservable organizationObservable = new OrganizationObservable(
                apiObservable, dataObservable, configurationObservable
        );

        final TestObserver<Organization> testObserver = organizationObservable.fetchByLogin("login").test();

        testObserver.awaitTerminalEvent();
        testObserver.assertResult(organization);
    }

    @Test
    public void shouldEmitUpToDateDataFromApi() {
        final Configuration configuration = new Configuration(1, 1, "url", null, null, null);
        final Organization organization = mock(Organization.class);

        final ApiObservable apiObservable = mock(ApiObservable.class);
        final DataObservable dataObservable = mock(DataObservable.class);
        final ConfigurationObservable configurationObservable = mock(ConfigurationObservable.class);

        when(organization.isUpToDate()).thenReturn(true);
        when(apiObservable.fetchByLogin("url", "login")).thenReturn(Maybe.just(organization));
        when(dataObservable.fetchByLogin("login")).thenReturn(Maybe.<Organization>empty());
        when(dataObservable.store(organization)).thenReturn(Maybe.just(organization));
        when(configurationObservable.fetch()).thenReturn(Maybe.just(configuration));

        final OrganizationObservable organizationObservable = new OrganizationObservable(
                apiObservable, dataObservable, configurationObservable
        );

        TestObserver<Organization> testObserver = organizationObservable.fetchByLogin("login").test();

        testObserver.awaitTerminalEvent();
        testObserver.assertResult(organization);
    }

    @Test
    public void shouldNotCallStore() {
        final Configuration configuration = new Configuration(1, 1, "url", null, null, null);
        final Organization organization = mock(Organization.class);

        final ApiObservable apiObservable = mock(ApiObservable.class);
        final DataObservable dataObservable = mock(DataObservable.class);
        final ConfigurationObservable configurationObservable = mock(ConfigurationObservable.class);

        when(apiObservable.fetchByLogin("url", "login")).thenReturn(Maybe.<Organization>empty());
        when(dataObservable.fetchByLogin("login")).thenReturn(Maybe.just(organization));
        when(configurationObservable.fetch()).thenReturn(Maybe.just(configuration));

        final OrganizationObservable organizationObservable = new OrganizationObservable(
                apiObservable, dataObservable, configurationObservable
        );

        TestObserver<Organization> testObserver = organizationObservable.fetchByLogin("login").test();

        testObserver.awaitTerminalEvent();
        testObserver.assertResult(organization);
        verify(dataObservable, times(0)).store(organization);
    }

    @Test
    public void shouldCallStoreOnce() {
        final Configuration configuration = new Configuration(1, 1, "url", null, null, null);
        final Organization organization = mock(Organization.class);

        final ApiObservable apiObservable = mock(ApiObservable.class);
        final DataObservable dataObservable = mock(DataObservable.class);
        final ConfigurationObservable configurationObservable = mock(ConfigurationObservable.class);

        when(organization.isUpToDate()).thenReturn(true);
        when(apiObservable.fetchByLogin("url", "login")).thenReturn(Maybe.just(organization));
        when(dataObservable.fetchByLogin("login")).thenReturn(Maybe.<Organization>empty());
        when(dataObservable.store(organization)).thenReturn(Maybe.just(organization));
        when(configurationObservable.fetch()).thenReturn(Maybe.just(configuration));

        final OrganizationObservable organizationObservable = new OrganizationObservable(
                apiObservable, dataObservable, configurationObservable
        );

        TestObserver<Organization> testObserver = organizationObservable.fetchByLogin("login").test();

        testObserver.awaitTerminalEvent();
        testObserver.assertResult(organization);
        verify(dataObservable, times(1)).store(organization);
    }

    @Test
    public void shouldEmitUpToDateDataCase1() {
        final Configuration configuration = new Configuration(1, 1, "url", null, null, null);
        final Organization organization1 = mock(Organization.class);
        final Organization organization2 = mock(Organization.class);

        final ApiObservable apiObservable = mock(ApiObservable.class);
        final DataObservable dataObservable = mock(DataObservable.class);
        final ConfigurationObservable configurationObservable = mock(ConfigurationObservable.class);

        when(organization1.isUpToDate()).thenReturn(false);
        when(organization2.isUpToDate()).thenReturn(true);
        when(apiObservable.fetchByLogin("url", "login")).thenReturn(Maybe.just(organization2));
        when(dataObservable.fetchByLogin("login")).thenReturn(Maybe.just(organization1));
        when(dataObservable.store(organization1)).thenReturn(Maybe.just(organization1));
        when(dataObservable.store(organization2)).thenReturn(Maybe.just(organization2));
        when(configurationObservable.fetch()).thenReturn(Maybe.just(configuration));

        final OrganizationObservable organizationObservable = new OrganizationObservable(
                apiObservable, dataObservable, configurationObservable
        );

        TestObserver<Organization> testObserver = organizationObservable.fetchByLogin("login").test();

        testObserver.awaitTerminalEvent();
        testObserver.assertResult(organization2);
    }

    @Test
    public void shouldEmitUpToDateDataCase2() {
        final Configuration configuration = new Configuration(1, 1, "url", null, null, null);
        final Organization organization1 = mock(Organization.class);
        final Organization organization2 = mock(Organization.class);

        final ApiObservable apiObservable = mock(ApiObservable.class);
        final DataObservable dataObservable = mock(DataObservable.class);
        final ConfigurationObservable configurationObservable = mock(ConfigurationObservable.class);

        when(organization1.isUpToDate()).thenReturn(false);
        when(organization2.isUpToDate()).thenReturn(true);
        when(apiObservable.fetchByLogin("url", "login")).thenReturn(Maybe.just(organization1));
        when(dataObservable.fetchByLogin("login")).thenReturn(Maybe.just(organization2));
        when(dataObservable.store(organization1)).thenReturn(Maybe.just(organization1));
        when(dataObservable.store(organization2)).thenReturn(Maybe.just(organization2));
        when(configurationObservable.fetch()).thenReturn(Maybe.just(configuration));

        final OrganizationObservable organizationObservable = new OrganizationObservable(
                apiObservable, dataObservable, configurationObservable
        );

        TestObserver<Organization> testObserver = organizationObservable.fetchByLogin("login").test();

        testObserver.awaitTerminalEvent();
        testObserver.assertResult(organization2);
    }

    @Test
    public void shouldEmitUpToDateDataCase3() {
        final Configuration configuration = new Configuration(1, 1, "url", null, null, null);
        final Organization organization1 = mock(Organization.class);
        final Organization organization2 = mock(Organization.class);

        final ApiObservable apiObservable = mock(ApiObservable.class);
        final DataObservable dataObservable = mock(DataObservable.class);
        final ConfigurationObservable configurationObservable = mock(ConfigurationObservable.class);

        when(organization1.isUpToDate()).thenReturn(true);
        when(organization2.isUpToDate()).thenReturn(true);
        when(apiObservable.fetchByLogin("url", "login")).thenReturn(Maybe.just(organization1));
        when(dataObservable.fetchByLogin("login")).thenReturn(Maybe.just(organization1));
        when(dataObservable.store(organization1)).thenReturn(Maybe.just(organization1));
        when(dataObservable.store(organization2)).thenReturn(Maybe.just(organization2));
        when(configurationObservable.fetch()).thenReturn(Maybe.just(configuration));

        final OrganizationObservable organizationObservable = new OrganizationObservable(
                apiObservable, dataObservable, configurationObservable
        );

        TestObserver<Organization> testObserver = organizationObservable.fetchByLogin("login").test();

        testObserver.awaitTerminalEvent();
        testObserver.assertResult(organization1);
    }

    @Test
    public void shouldEmitNotUpToDateDataCase1() {
        final Configuration configuration = new Configuration(1, 1, "url", null, null, null);
        final Organization organization1 = mock(Organization.class);
        final Organization organization2 = mock(Organization.class);

        final ApiObservable apiObservable = mock(ApiObservable.class);
        final DataObservable dataObservable = mock(DataObservable.class);
        final ConfigurationObservable configurationObservable = mock(ConfigurationObservable.class);

        when(organization1.isUpToDate()).thenReturn(false);
        when(organization2.isUpToDate()).thenReturn(false);
        when(apiObservable.fetchByLogin("url", "login")).thenReturn(Maybe.just(organization1));
        when(dataObservable.fetchByLogin("login")).thenReturn(Maybe.just(organization2));
        when(dataObservable.store(organization1)).thenReturn(Maybe.just(organization1));
        when(dataObservable.store(organization2)).thenReturn(Maybe.just(organization2));
        when(configurationObservable.fetch()).thenReturn(Maybe.just(configuration));

        final OrganizationObservable organizationObservable = new OrganizationObservable(
                apiObservable, dataObservable, configurationObservable
        );

        TestObserver<Organization> testObserver = organizationObservable.fetchByLogin("login").test();

        testObserver.awaitTerminalEvent();
        testObserver.assertResult(organization2);
    }

    @Test
    public void shouldEmitNotUpToDateDataCase2() {
        final Configuration configuration = new Configuration(1, 1, "url", null, null, null);
        final Organization organization = mock(Organization.class);

        final ApiObservable apiObservable = mock(ApiObservable.class);
        final DataObservable dataObservable = mock(DataObservable.class);
        final ConfigurationObservable configurationObservable = mock(ConfigurationObservable.class);

        when(organization.isUpToDate()).thenReturn(false);
        when(apiObservable.fetchByLogin("url", "login")).thenReturn(Maybe.<Organization>empty());
        when(dataObservable.fetchByLogin("login")).thenReturn(Maybe.just(organization));
        when(dataObservable.store(organization)).thenReturn(Maybe.just(organization));
        when(configurationObservable.fetch()).thenReturn(Maybe.just(configuration));

        final OrganizationObservable organizationObservable = new OrganizationObservable(
                apiObservable, dataObservable, configurationObservable
        );

        TestObserver<Organization> testObserver = organizationObservable.fetchByLogin("login").test();

        testObserver.awaitTerminalEvent();
        testObserver.assertResult(organization);
    }

}