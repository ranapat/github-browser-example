package org.ranapat.examples.githubbrowser.observable.exceptions;

public class UsersUndefinedException extends IllegalStateException {
    public UsersUndefinedException() {
        super("Users are undefined");
    }
}