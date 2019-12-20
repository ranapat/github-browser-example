package org.ranapat.examples.githubbrowser.observable;

import java.util.List;

import io.reactivex.exceptions.CompositeException;

public final class ExceptionChecker {
    private ExceptionChecker() {}

    public static boolean exists(final Throwable throwable, final Class needle) {
        if (isInstanceOf(throwable, needle)) {
            return true;
        } else if (CompositeException.class.isInstance(throwable)) {
            final CompositeException compositeException = (CompositeException) throwable;
            final List<Throwable> throwableList = compositeException.getExceptions();
            for (final Throwable _throwable : throwableList) {
                if (exists(_throwable, needle)) {
                    return true;
                }
            }
        }

        return false;
    }

    public static boolean isLast(final Throwable throwable, final Class needle) {
        if (isInstanceOf(throwable, needle)) {
            return true;
        } else if (CompositeException.class.isInstance(throwable)) {
            final CompositeException compositeException = (CompositeException) throwable;
            final List<Throwable> throwableList = compositeException.getExceptions();
            final Throwable last = throwableList.get(throwableList.size() - 1);

            return isInstanceOf(last, needle);
        }

        return false;
    }

    @SuppressWarnings("unchecked")
    public static <T> T getFirst(final Throwable throwable, final Class<T> needle) {
        if (isInstanceOf(throwable, needle)) {
            return (T) throwable;
        } else if (CompositeException.class.isInstance(throwable)) {
            final CompositeException compositeException = (CompositeException) throwable;
            final List<Throwable> throwableList = compositeException.getExceptions();
            for (final Throwable _throwable : throwableList) {
                final T __throwable = getFirst(_throwable, needle);
                if (__throwable != null) {
                    return __throwable;
                }
            }
        }

        return null;
    }

    @SuppressWarnings("unchecked")
    public static <T> T getLast(final Throwable throwable, final Class<T> needle) {
        Throwable latest = null;

        if (isInstanceOf(throwable, needle)) {
            latest = throwable;
        } else if (CompositeException.class.isInstance(throwable)) {
            final CompositeException compositeException = (CompositeException) throwable;
            final List<Throwable> throwableList = compositeException.getExceptions();
            for (final Throwable _throwable : throwableList) {
                final T __throwable = getLast(_throwable, needle);
                if (__throwable != null) {
                    latest = (Throwable) __throwable;
                }
            }
        }

        return (T) latest;
    }

    private static boolean isInstanceOf(final Throwable instance, final Class of) {
        return instance != null && of.getName().equals(instance.getClass().getName());
    }
}
