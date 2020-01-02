package org.ranapat.examples.githubbrowser.management;

import org.junit.Test;
import org.ranapat.examples.githubbrowser.data.entity.User;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

public class TemporaryDataKeeperManagerTest {

    @Test
    public void shouldSetAllFields() {
        final TemporaryDataKeeperManager temporaryDataKeeperManager = new TemporaryDataKeeperManager();

        assertThat(temporaryDataKeeperManager.user, is(equalTo(null)));

        final User user = mock(User.class);
        temporaryDataKeeperManager.user = user;
        assertThat(temporaryDataKeeperManager.user, is(equalTo(user)));
    }


}