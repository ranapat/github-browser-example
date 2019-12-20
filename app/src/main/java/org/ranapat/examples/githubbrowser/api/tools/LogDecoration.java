package org.ranapat.examples.githubbrowser.api.tools;

public final class LogDecoration {
    private static LogDecoration instance;
    public static LogDecoration getInstance() {
        if (instance == null) {
            instance = new LogDecoration();
        }

        return instance;
    }

    private LogDecoration() {
        //
    }

    @lombok.Generated
    public String getCurrentMethodName() {
        final StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();

        final int length = stackTraceElements.length;
        boolean startToTrack = false;
        for (int i = 0; i < length; ++i) {
            final StackTraceElement stackTraceElement = stackTraceElements[i];

            if (stackTraceElement.getClassName().contains("BaseRao")) {
                startToTrack = true;
            } else if (startToTrack) {
                return stackTraceElement.getMethodName();
            }
        }

        return "undefined";
    }

    @lombok.Generated
    public String getCurrentClassName(final Class<?> _class) {
        final Class<?> enclosingClass = _class.getEnclosingClass();
        if (enclosingClass != null) {
            return enclosingClass.getSimpleName();
        } else {
            return _class.getSimpleName();
        }
    }

    @lombok.Generated
    public String getCallerClassAndMethod(final Class<?> _class) {
        return getCurrentClassName(_class) + "::" + getCurrentMethodName();
    }

}
