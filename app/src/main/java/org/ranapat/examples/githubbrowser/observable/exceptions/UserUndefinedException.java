package org.ranapat.examples.githubbrowser.observable.exceptions;

public class UserUndefinedException extends IllegalStateException {
    public UserUndefinedException() {
        super("User is undefined");
    }
}