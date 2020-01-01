package org.ranapat.examples.githubbrowser.data.entity;

import org.junit.Test;

import java.util.Date;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class UserDetailsTest {

    @Test
    public void shouldSetAllFields() {
        final Date date1 = new Date();
        final Date date2 = new Date();

        final UserDetails userDetails = new UserDetails(
                1,
                "name",
                "company",
                "blog",
                "location",
                "email",
                "bio",
                true,
                date1,
                date2
        );
        assertThat(userDetails.userId, is(equalTo(1L)));
        assertThat(userDetails.name, is(equalTo("name")));
        assertThat(userDetails.company, is(equalTo("company")));
        assertThat(userDetails.blog, is(equalTo("blog")));
        assertThat(userDetails.location, is(equalTo("location")));
        assertThat(userDetails.email, is(equalTo("email")));
        assertThat(userDetails.bio, is(equalTo("bio")));
        assertThat(userDetails.hireable, is(equalTo(true)));
        assertThat(userDetails.remoteCreatedAt, is(equalTo(date1)));
        assertThat(userDetails.remoteUpdatedAt, is(equalTo(date2)));
    }

}