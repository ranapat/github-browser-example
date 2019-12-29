package org.ranapat.examples.githubbrowser.data.entity;

import org.junit.Test;
import org.ranapat.examples.githubbrowser.data.tools.UpToDateChecker;

import java.util.Calendar;
import java.util.Date;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class OrganizationTest {

    @Test
    public void shouldSetAllFields() {
        final Date date = new Date();

        final Organization organization = new Organization(
                1,
                "login",
                "membersUrl",
                date
        );
        assertThat(organization.id, is(equalTo(1L)));
        assertThat(organization.login, is(equalTo("login")));
        assertThat(organization.membersUrl, is(equalTo("membersUrl")));
        assertThat(organization.updatedAt, is(equalTo(date)));
    }

    @Test
    public void shouldGetUpdatedAt() {
        final Organization organization = new Organization(
                1,
                "login",
                "membersUrl",
                new Date()
        );
        assertThat(organization.getUpdatedAt(), is(equalTo(organization.updatedAt)));
    }

    @Test
    public void shouldBeUpToDate() {
        final Date date = new Date();

        UpToDateChecker.setValues(0, 1);

        final Organization organization = new Organization(
                1,
                "login",
                "membersUrl",
                date
        );
        assertThat(organization.isUpToDate(), is(equalTo(true)));

        UpToDateChecker.resetValues();
    }

    @Test
    public void shouldBeOutOfDateCase1() {
        final Date date = new Date();

        UpToDateChecker.setValues(0, 1);

        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MINUTE, -1);

        final Organization organization = new Organization(
                1,
                "login",
                "membersUrl",
                calendar.getTime()
        );
        assertThat(organization.isUpToDate(), is(equalTo(false)));

        UpToDateChecker.resetValues();
    }

    @Test
    public void shouldBeAlwaysUpToDateCase2() {
        final Date date = new Date();

        UpToDateChecker.setValues(0, -1);

        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MINUTE, 100);

        final Organization organization = new Organization(
                1,
                "login",
                "membersUrl",
                calendar.getTime()
        );
        assertThat(organization.isUpToDate(), is(equalTo(true)));

        UpToDateChecker.resetValues();
    }

}