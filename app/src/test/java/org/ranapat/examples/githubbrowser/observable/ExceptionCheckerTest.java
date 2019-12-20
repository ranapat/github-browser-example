package org.ranapat.examples.githubbrowser.observable;

import org.json.JSONException;
import org.junit.Test;

import java.io.IOException;

import io.reactivex.exceptions.CompositeException;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class ExceptionCheckerTest {

    @Test
    public void shouldCheckNullException() {
        assertThat(ExceptionChecker.exists(null, Exception.class), is(equalTo(false)));
    }

    @Test
    public void shouldCheckSingleException() {
        final Exception exception = new Exception();

        assertThat(ExceptionChecker.exists(exception, Exception.class), is(equalTo(true)));
    }

    @Test
    public void shouldCheckForExactExceptionsAndIgnoreParentingCase1() {
        final Exception exception = new IOException();

        assertThat(ExceptionChecker.exists(exception, Exception.class), is(equalTo(false)));
    }

    @Test
    public void shouldCheckForExactExceptionsAndIgnoreParentingCase2() {
        final Exception exception = new Exception();

        assertThat(ExceptionChecker.exists(exception, IOException.class), is(equalTo(false)));
    }

    @Test
    public void shouldCheckCompositeExceptionCase1() {
        final CompositeException compositeException = new CompositeException(
                new JSONException("error")
        );

        assertThat(ExceptionChecker.exists(compositeException, JSONException.class), is(equalTo(true)));
    }

    @Test
    public void shouldCheckCompositeExceptionCase2() {
        final CompositeException compositeException = new CompositeException(
                new JSONException("error"),
                new IOException()
        );

        assertThat(ExceptionChecker.exists(compositeException, JSONException.class), is(equalTo(true)));
    }

    @Test
    public void shouldCheckCompositeExceptionCase3() {
        final CompositeException compositeException = new CompositeException(
                new JSONException("error"),
                new IOException()
        );

        assertThat(ExceptionChecker.exists(compositeException, Exception.class), is(equalTo(false)));
    }

    @Test
    public void shouldCheckIsLastNullException() {
        assertThat(ExceptionChecker.isLast(null, Exception.class), is(equalTo(false)));
    }

    @Test
    public void shouldCheckIsLastExceptionCase1() {
        final Exception exception = new Exception();

        assertThat(ExceptionChecker.isLast(exception, Exception.class), is(equalTo(true)));
    }

    @Test
    public void shouldCheckIsLastExceptionCase2() {
        final Exception exception = new Exception();

        assertThat(ExceptionChecker.isLast(exception, JSONException.class), is(equalTo(false)));
    }

    @Test
    public void shouldCheckIsLastExceptionCase3() {
        final JSONException jsonException = new JSONException("error");

        assertThat(ExceptionChecker.isLast(jsonException, Exception.class), is(equalTo(false)));
    }

    @Test
    public void shouldCheckIsLastExceptionCase4() {
        final CompositeException compositeException = new CompositeException(
                new JSONException("error")
        );

        assertThat(ExceptionChecker.isLast(compositeException, JSONException.class), is(equalTo(true)));
    }

    @Test
    public void shouldCheckIsLastExceptionCase5() {
        final CompositeException compositeException = new CompositeException(
                new Exception(),
                new JSONException("error")
        );

        assertThat(ExceptionChecker.isLast(compositeException, JSONException.class), is(equalTo(true)));
    }

    @Test
    public void shouldCheckIsLastExceptionCase6() {
        final CompositeException compositeException = new CompositeException(
                new Exception(),
                new JSONException("error")
        );

        assertThat(ExceptionChecker.isLast(compositeException, Exception.class), is(equalTo(false)));
    }

    @Test
    public void shouldCheckIsLastExceptionCase7() {
        final CompositeException compositeException = new CompositeException(
                new Exception(),
                new CompositeException(
                        new JSONException("error")
                )
        );

        assertThat(ExceptionChecker.isLast(compositeException, JSONException.class), is(equalTo(true)));
    }

    @Test
    public void shouldCheckIsLastExceptionCase8() {
        final CompositeException compositeException = new CompositeException(
                new Exception(),
                new CompositeException(
                        new JSONException("error"),
                        new Exception()
                )
        );

        assertThat(ExceptionChecker.isLast(compositeException, Exception.class), is(equalTo(true)));
    }

    @Test
    public void shouldCheckIsLastExceptionCase9() {
        final CompositeException compositeException = new CompositeException(
                new Exception(),
                new CompositeException(
                        new JSONException("error"),
                        new Exception()
                )
        );

        assertThat(ExceptionChecker.isLast(compositeException, JSONException.class), is(equalTo(false)));
    }

    @Test
    public void shouldGetFirstFromNull() {
        assertThat(ExceptionChecker.getFirst(null, Exception.class), is(equalTo(null)));
    }

    @Test
    public void shouldGetFirstAndReturnCase1() {
        final Exception exception = new Exception();
        assertThat(ExceptionChecker.getFirst(exception, Exception.class), is(equalTo(exception)));
    }

    @Test
    public void shouldGetFirstAndReturnCase2() {
        final Exception exception = new Exception();
        assertThat(ExceptionChecker.getFirst(exception, JSONException.class), is(equalTo(null)));
    }

    @Test
    public void shouldGetFirstAndReturnCase3() {
        final JSONException jsonException = new JSONException("error");
        assertThat(ExceptionChecker.getFirst(jsonException, Exception.class), is(equalTo(null)));
    }

    @Test
    public void shouldGetFirstAndReturnCase4() {
        final Exception exception = new Exception();
        final CompositeException compositeException = new CompositeException(
                exception
        );

        assertThat(ExceptionChecker.getFirst(compositeException, Exception.class), is(equalTo(exception)));
    }

    @Test
    public void shouldGetFirstAndReturnCase5() {
        final Exception exception = new Exception();
        final CompositeException compositeException = new CompositeException(
                exception,
                new JSONException("")
        );

        assertThat(ExceptionChecker.getFirst(compositeException, Exception.class), is(equalTo(exception)));
    }

    @Test
    public void shouldGetFirstAndReturnCase6() {
        final Exception exception = new Exception();
        final CompositeException compositeException = new CompositeException(
                exception,
                new JSONException("")
        );

        assertThat(ExceptionChecker.getFirst(compositeException, IOException.class), is(equalTo(null)));
    }

    @Test
    public void shouldGetFirstAndReturnCase7() {
        final Exception exception = new Exception();
        final CompositeException compositeException = new CompositeException(
                new JSONException(""),
                new CompositeException(
                        exception
                )
        );

        assertThat(ExceptionChecker.getFirst(compositeException, Exception.class), is(equalTo(exception)));
    }

    @Test
    public void shouldGetFirstAndReturnCase8() {
        final Exception exception1 = new Exception();
        final Exception exception2 = new Exception();
        final CompositeException compositeException = new CompositeException(
                exception1,
                exception2
        );

        assertThat(ExceptionChecker.getFirst(compositeException, Exception.class), is(equalTo(exception1)));
    }

    @Test
    public void shouldLastFirstFromNull() {
        assertThat(ExceptionChecker.getLast(null, Exception.class), is(equalTo(null)));
    }

    @Test
    public void shouldLastFirstAndReturnCase1() {
        final Exception exception = new Exception();
        assertThat(ExceptionChecker.getLast(exception, Exception.class), is(equalTo(exception)));
    }

    @Test
    public void shouldLastFirstAndReturnCase2() {
        final Exception exception = new Exception();
        assertThat(ExceptionChecker.getLast(exception, JSONException.class), is(equalTo(null)));
    }

    @Test
    public void shouldLastFirstAndReturnCase3() {
        final JSONException jsonException = new JSONException("error");
        assertThat(ExceptionChecker.getLast(jsonException, Exception.class), is(equalTo(null)));
    }

    @Test
    public void shouldLastFirstAndReturnCase4() {
        final Exception exception = new Exception();
        final CompositeException compositeException = new CompositeException(
                exception
        );

        assertThat(ExceptionChecker.getLast(compositeException, Exception.class), is(equalTo(exception)));
    }

    @Test
    public void shouldLastFirstAndReturnCase5() {
        final Exception exception = new Exception();
        final CompositeException compositeException = new CompositeException(
                exception,
                new JSONException("")
        );

        assertThat(ExceptionChecker.getLast(compositeException, Exception.class), is(equalTo(exception)));
    }

    @Test
    public void shouldLastFirstAndReturnCase6() {
        final Exception exception = new Exception();
        final CompositeException compositeException = new CompositeException(
                exception,
                new JSONException("")
        );

        assertThat(ExceptionChecker.getLast(compositeException, IOException.class), is(equalTo(null)));
    }

    @Test
    public void shouldLastFirstAndReturnCase7() {
        final Exception exception = new Exception();
        final CompositeException compositeException = new CompositeException(
                new JSONException(""),
                new CompositeException(
                        exception
                )
        );

        assertThat(ExceptionChecker.getLast(compositeException, Exception.class), is(equalTo(exception)));
    }

    @Test
    public void shouldLastFirstAndReturnCase8() {
        final Exception exception1 = new Exception();
        final Exception exception2 = new Exception();
        final CompositeException compositeException = new CompositeException(
                exception1,
                exception2
        );

        assertThat(ExceptionChecker.getLast(compositeException, Exception.class), is(equalTo(exception2)));
    }

}