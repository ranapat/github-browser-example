package org.ranapat.examples.githubbrowser.ui.organization;

import org.ranapat.examples.githubbrowser.data.entity.User;

public class ListUser {
    public final User user;
    public boolean incomplete;

    public ListUser(final User user) {
        this.user = user;
        incomplete = false;
    }
}
