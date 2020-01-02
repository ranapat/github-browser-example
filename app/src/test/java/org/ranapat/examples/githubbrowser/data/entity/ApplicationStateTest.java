package org.ranapat.examples.githubbrowser.data.entity;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class ApplicationStateTest {

    @Test
    public void shouldSetAllFields() {
        final ApplicationState applicationState = new ApplicationState(
                1,
                "currentOrganization",
                "sortBy",
                "sortDirection",
                "limit"
        );
        assertThat(applicationState.id, is(equalTo(1L)));
        assertThat(applicationState.currentOrganization, is(equalTo("currentOrganization")));
        assertThat(applicationState.sortBy, is(equalTo("sortBy")));
        assertThat(applicationState.sortDirection, is(equalTo("sortDirection")));
        assertThat(applicationState.limit, is(equalTo("limit")));
    }

    @Test
    public void shouldSetDefaultParameters() {
        final ApplicationState applicationState = new ApplicationState(
                "currentOrganization",
                "sortBy",
                "sortDirection",
                "limit"
        );
        assertThat(applicationState.id, is(equalTo(1L)));
        assertThat(applicationState.currentOrganization, is(equalTo("currentOrganization")));
        assertThat(applicationState.sortBy, is(equalTo("sortBy")));
        assertThat(applicationState.sortDirection, is(equalTo("sortDirection")));
        assertThat(applicationState.limit, is(equalTo("limit")));
    }

    @Test
    public void shouldReset() {
        final ApplicationState applicationState = new ApplicationState(
                "currentOrganization",
                "sortBy",
                "sortDirection",
                "limit"
        );
        assertThat(applicationState.id, is(equalTo(1L)));
        assertThat(applicationState.currentOrganization, is(equalTo("currentOrganization")));
        assertThat(applicationState.sortBy, is(equalTo("sortBy")));
        assertThat(applicationState.sortDirection, is(equalTo("sortDirection")));
        assertThat(applicationState.limit, is(equalTo("limit")));

        applicationState.reset();

        assertThat(applicationState.id, is(equalTo(1L)));
        assertThat(applicationState.currentOrganization, is(equalTo(null)));
        assertThat(applicationState.sortBy, is(equalTo(null)));
        assertThat(applicationState.sortDirection, is(equalTo(null)));
        assertThat(applicationState.limit, is(equalTo(null)));
    }

    @Test
    public void shouldCheckIfSet() {
        final ApplicationState applicationState = new ApplicationState(
                "currentOrganization",
                "sortBy",
                "sortDirection",
                "limit"
        );
        assertThat(applicationState.isSet(), is(equalTo(true)));

        applicationState.currentOrganization = null;
        assertThat(applicationState.isSet(), is(equalTo(false)));

        applicationState.currentOrganization = "";
        assertThat(applicationState.isSet(), is(equalTo(false)));

        applicationState.currentOrganization = ".";
        assertThat(applicationState.isSet(), is(equalTo(true)));
    }

}