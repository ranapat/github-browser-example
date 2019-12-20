package org.ranapat.examples.githubbrowser.ui.publishable;

import java.util.Map;

import io.reactivex.subjects.PublishSubject;

public class ParameterizedMessages {
    final public static String MESSAGE = "message";
    final public static String LABEL = "label";

    final public PublishSubject<ParameterizedMessage> info;
    final public PublishSubject<ParameterizedMessage> success;
    final public PublishSubject<Map<String, ParameterizedMessage>> apply;
    final public PublishSubject<ParameterizedMessage> warning;
    final public PublishSubject<ParameterizedMessage> warningIndefinite;
    final public PublishSubject<ParameterizedMessage> error;
    final public PublishSubject<ParameterizedMessage> errorIndefinite;
    final public PublishSubject<Map<String, ParameterizedMessage>> errorWithAction;

    public ParameterizedMessages() {
        info = PublishSubject.create();
        success = PublishSubject.create();
        apply = PublishSubject.create();
        warning = PublishSubject.create();
        warningIndefinite = PublishSubject.create();
        error = PublishSubject.create();
        errorIndefinite = PublishSubject.create();
        errorWithAction = PublishSubject.create();
    }

}
