package org.ranapat.examples.githubbrowser.ui.common;

import java.util.List;

import static java.util.Arrays.asList;

public final class States {
    public static final String LOADING = "loading";
    public static final String ERROR = "error";
    public static final String READY = "ready";
    public static final String REFRESH = "refresh";

    public static final String BLOCKED = "blocked";
    public static final String UNBLOCKED = "unblocked";

    public static final String COMPLETE = "complete";
    public static final String REDIRECT = "redirect";
    public static final String CLEAN_REDIRECT = "cleanRedirect";
    public static final String RESTART = "restart";
    public static final String NAVIGATE = "navigate";

    public static final List<String> toThrottle = asList(COMPLETE, REDIRECT, CLEAN_REDIRECT, RESTART, NAVIGATE);

    private States() {}

    public static boolean shallThrottle(final String state) {
        return toThrottle.contains(state);
    }
}
