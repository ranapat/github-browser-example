package org.ranapat.examples.githubbrowser.observable.exceptions;

public final class ConfigurationUndefinedException extends IllegalStateException {
    public ConfigurationUndefinedException() {
        super("Configuration is undefined");
    }
}
