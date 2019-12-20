package org.ranapat.examples.githubbrowser.ui.notification;

import android.content.Context;
import android.content.res.Resources;
import android.view.View;

import io.reactivex.subjects.PublishSubject;

public abstract class OnScreenInformation {
    protected final Context context;
    protected final Resources resources;

    public final PublishSubject<Boolean> onApply;
    public final PublishSubject<Boolean> onError;

    public OnScreenInformation(final Context context, final Resources resources) {
        this.context = context;
        this.resources = resources;

        onApply = PublishSubject.create();
        onError = PublishSubject.create();
    }

    public abstract View info(final View view, final String message);

    public abstract View success(final View view, final String message);

    public abstract View apply(final View view, final String message, final String label);

    public abstract View warning(final View view, final String message);

    public abstract View warningIndefinite(final View view, final String message);

    public abstract View error(final View view, final String message);

    public abstract View errorIndefinite(final View view, final String message);

    public abstract View errorWithAction(final View view, final String message, final String label);

}
