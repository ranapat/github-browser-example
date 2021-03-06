package org.ranapat.examples.githubbrowser.data.entity;

import org.junit.Test;
import org.ranapat.examples.githubbrowser.data.tools.UpToDateChecker;

import java.util.Calendar;
import java.util.Date;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class UserTest {

    @Test
    public void shouldSetAllFields() {
        final Date date = new Date();

        final User user = new User(
                1,
                2,
                "login",
                "avatarUrl",
                "type",
                true,
                date
        );
        assertThat(user.id, is(equalTo(1L)));
        assertThat(user.organizationId, is(equalTo(2L)));
        assertThat(user.login, is(equalTo("login")));
        assertThat(user.avatarUrl, is(equalTo("avatarUrl")));
        assertThat(user.siteAdmin, is(equalTo(true)));
        assertThat(user.updatedAt, is(equalTo(date)));
        assertThat(user.details, is(equalTo(null)));
        assertThat(user.urls, is(equalTo(null)));
    }

    @Test
    public void shouldGetUpdatedAt() {
        final User user = new User(
                1,
                2,
                "login",
                "avatarUrl",
                "type",
                true,
                new Date()
        );
        assertThat(user.getUpdatedAt(), is(equalTo(user.updatedAt)));
    }

    @Test
    public void shouldBeUpToDate() {
        final Date date = new Date();

        UpToDateChecker.setValues(0, 1);

        final User user = new User(
                1,
                2,
                "login",
                "avatarUrl",
                "type",
                true,
                date
        );
        assertThat(user.isUpToDate(), is(equalTo(true)));

        UpToDateChecker.resetValues();
    }

    @Test
    public void shouldBeOutOfDateCase1() {
        final Date date = new Date();

        UpToDateChecker.setValues(0, 1);

        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MINUTE, -1);

        final User user = new User(
                1,
                2,
                "login",
                "avatarUrl",
                "type",
                true,
                calendar.getTime()
        );
        assertThat(user.isUpToDate(), is(equalTo(false)));

        UpToDateChecker.resetValues();
    }

    @Test
    public void shouldBeAlwaysUpToDateCase2() {
        final Date date = new Date();

        UpToDateChecker.setValues(0, -1);

        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MINUTE, 100);

        final User user = new User(
                1,
                2,
                "login",
                "avatarUrl",
                "type",
                true,
                calendar.getTime()
        );
        assertThat(user.isUpToDate(), is(equalTo(true)));

        UpToDateChecker.resetValues();
    }

}