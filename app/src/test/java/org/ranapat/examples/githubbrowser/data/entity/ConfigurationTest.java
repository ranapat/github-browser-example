package org.ranapat.examples.githubbrowser.data.entity;

import org.junit.Test;
import org.ranapat.examples.githubbrowser.data.tools.UpToDateChecker;

import java.util.Calendar;
import java.util.Date;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

public class ConfigurationTest {

    @Test
    public void shouldSetAllFields() {
        final Date date = new Date();

        final Configuration configuration = new Configuration(
                1,
                15,
                "organizationMembers",
                "userInfo",
                date
        );
        assertThat(configuration.id, is(equalTo(1L)));
        assertThat(configuration.defaultMembersInOrganizationPerPage, is(equalTo(15)));
        assertThat(configuration.organizationMembers, is(equalTo("organizationMembers")));
        assertThat(configuration.userInfo, is(equalTo("userInfo")));
        assertThat(configuration.updatedAt, is(equalTo(date)));
    }

    @Test
    public void shouldSetDefaultParameters() {
        final Configuration configuration = new Configuration(
                15,
                "organizationMembers",
                "userInfo"
        );
        assertThat(configuration.id, is(equalTo(1L)));
        assertThat(configuration.defaultMembersInOrganizationPerPage, is(equalTo(15)));
        assertThat(configuration.organizationMembers, is(equalTo("organizationMembers")));
        assertThat(configuration.userInfo, is(equalTo("userInfo")));
        assertThat(configuration.updatedAt, is(not(equalTo(null))));
    }

    @Test
    public void shouldGetUpdatedAt() {
        final Configuration configuration = new Configuration(
                15,
                "organizationMembers",
                "userInfo"
        );
        assertThat(configuration.getUpdatedAt(), is(equalTo(configuration.updatedAt)));
    }

    @Test
    public void shouldBeUpToDate() {
        final Date date = new Date();

        UpToDateChecker.setValues(1, 0);

        final Configuration configuration = new Configuration(
                1,
                15,
                "organizationMembers",
                "userInfo",
                date
        );
        assertThat(configuration.isUpToDate(), is(equalTo(true)));

        UpToDateChecker.resetValues();
    }

    @Test
    public void shouldBeOutOfDateCase1() {
        final Date date = new Date();

        UpToDateChecker.setValues(1, 0);

        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MINUTE, -1);

        final Configuration configuration = new Configuration(
                1,
                15,
                "organizationMembers",
                "userInfo",
                calendar.getTime()
        );
        assertThat(configuration.isUpToDate(), is(equalTo(false)));

        UpToDateChecker.resetValues();
    }

    @Test
    public void shouldBeAlwaysUpToDateCase2() {
        final Date date = new Date();

        UpToDateChecker.setValues(-1, 0);

        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MINUTE, 100);

        final Configuration configuration = new Configuration(
                1,
                15,
                "organizationMembers",
                "userInfo",
                calendar.getTime()
        );
        assertThat(configuration.isUpToDate(), is(equalTo(true)));

        UpToDateChecker.resetValues();
    }

}