package org.ranapat.examples.githubbrowser.observable.exceptions;

public class OrganizationUndefinedException extends IllegalStateException {
    public OrganizationUndefinedException() {
        super("Organization is undefined");
    }
}
