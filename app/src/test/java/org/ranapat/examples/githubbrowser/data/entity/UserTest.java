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
                "name",
                date
        );
        assertThat(user.id, is(equalTo(1L)));
        assertThat(user.name, is(equalTo("name")));
        assertThat(user.updatedAt, is(equalTo(date)));
    }


    @Test
    public void shouldGetUpdatedAt() {
        final Date date = new Date();

        final User user = new User(
                1,
                "name",
                date
        );
        assertThat(user.getUpdatedAt(), is(equalTo(user.updatedAt)));
    }

    @Test
    public void shouldBeUpToDate() {
        final Date date = new Date();

        UpToDateChecker.setValues(1, 1);

        final User user = new User(
                1,
                "name",
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
                "name",
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
                "name",
                calendar.getTime()
        );
        assertThat(user.isUpToDate(), is(equalTo(true)));

        UpToDateChecker.resetValues();
    }

}