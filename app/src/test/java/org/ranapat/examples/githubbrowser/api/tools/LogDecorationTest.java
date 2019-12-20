package org.ranapat.examples.githubbrowser.api.tools;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class LogDecorationTest {

    @Test
    public void shouldGetInstance() {
        assertThat(LogDecoration.getInstance(), is(equalTo(LogDecoration.getInstance())));
    }

}
