package org.ranapat.examples.githubbrowser.data.tools;

import org.junit.Test;

import java.util.Date;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class ConvertersTest {

    @Test
    public void shouldReturnDateFromTimestamp() {
        final Date date = new Date();
        final long timestamp = date.getTime();
        assertThat(date, is(equalTo(Converters.fromTimestamp(timestamp))));
    }

    @Test
    public void shouldReturnNullFromTimestamp() {
        assertThat(null, is(equalTo(Converters.fromTimestamp(null))));
    }

    @Test
    public void shouldReturnLongFromDate() {
        final Date date = new Date();
        final long timestamp = date.getTime();
        assertThat(timestamp, is(equalTo(Converters.dateToTimestamp(date))));
    }

    @Test
    public void shouldReturnNullFromDate() {
        assertThat(null, is(equalTo(Converters.dateToTimestamp(null))));
    }

}