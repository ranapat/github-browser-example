package org.ranapat.examples.githubbrowser.ui.organization;

import androidx.annotation.NonNull;

import org.ranapat.examples.githubbrowser.data.entity.User;

public class ListUser {
    @NonNull
    public final User user;
    public boolean incomplete;

    public ListUser(final User user) {
        this.user = user;
        incomplete = false;
    }
}
