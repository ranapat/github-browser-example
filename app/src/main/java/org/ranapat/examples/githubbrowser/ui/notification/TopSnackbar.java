package org.ranapat.examples.githubbrowser.ui.notification;

import android.content.Context;
import android.content.res.Resources;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.google.android.material.snackbar.Snackbar;

import org.ranapat.examples.githubbrowser.R;

import static org.ranapat.examples.githubbrowser.ui.notification.Modes.APPLY;
import static org.ranapat.examples.githubbrowser.ui.notification.Modes.ERROR;
import static org.ranapat.examples.githubbrowser.ui.notification.Modes.ERROR_INDEFINITE;
import static org.ranapat.examples.githubbrowser.ui.notification.Modes.INFO;
import static org.ranapat.examples.githubbrowser.ui.notification.Modes.SUCCESS;
import static org.ranapat.examples.githubbrowser.ui.notification.Modes.WARNING;
import static org.ranapat.examples.githubbrowser.ui.notification.Modes.WARNING_INDEFINITE;

public class TopSnackbar extends OnScreenInformation {
    public TopSnackbar(final Context context, final Resources resources) {
        super(context, resources);
    }

    @Override
    public View info(final View view, final String message) {
        return dismissSnackbar(view, message, INFO);
    }

    @Override
    public View success(View view, String message) {
        return dismissSnackbar(view, message, SUCCESS);
    }

    @Override
    public View apply(final View view, final String message, final String label) {
        final Snackbar snackbar = getSnackbar(view, message, APPLY);

        snackbar
                .setAction(label, new View.OnClickListener() {
                    @Override
                    public void onClick(final View v) {
                        onApply.onNext(true);
                    }
                })
                .show();

        return snackbar.getView();
    }

    @Override
    public View warning(final View view, final String message) {
        return dismissSnackbar(view, message, WARNING);
    }

    @Override
    public View warningIndefinite(final View view, final String message) {
        return dismissSnackbar(view, message, WARNING_INDEFINITE);
    }

    @Override
    public View error(final View view, final String message) {
        return dismissSnackbar(view, message, ERROR);
    }

    @Override
    public View errorIndefinite(final View view, final String message) {
        return dismissSnackbar(view, message, ERROR_INDEFINITE);
    }

    @Override
    public View errorWithAction(final View view, final String message, final String label) {
        final Snackbar snackbar = getSnackbar(view, message, ERROR_INDEFINITE);

        snackbar
                .setAction(label, new View.OnClickListener() {
                    @Override
                    public void onClick(final View v) {
                        onError.onNext(true);
                    }
                })
                .show();

        return snackbar.getView();
    }

    private int getSnackbarDuration(final String mode) {
        switch (mode) {
            case INFO:
                return Snackbar.LENGTH_LONG;
            case SUCCESS:
                return Snackbar.LENGTH_LONG;
            case APPLY:
                return Snackbar.LENGTH_INDEFINITE;
            case WARNING:
                return Snackbar.LENGTH_LONG;
            case WARNING_INDEFINITE:
                return Snackbar.LENGTH_INDEFINITE;
            case ERROR:
                return Snackbar.LENGTH_LONG;
            case ERROR_INDEFINITE:
                return Snackbar.LENGTH_INDEFINITE;
            default:
                return Snackbar.LENGTH_SHORT;
        }
    }

    private Snackbar getSnackbar(final View view, final String message, final String mode) {
        return getSnackbar(view, message, mode, getSnackbarDuration(mode));
    }

    private Snackbar getSnackbar(final View view, final String message, final String mode, final int duration) {
        final Snackbar snackbar = Snackbar.make(view, message, duration);

        final View snackbarView = snackbar.getView();
        snackbarView.setElevation(0);
        final FrameLayout.LayoutParams parameters = (FrameLayout.LayoutParams) snackbarView.getLayoutParams();
        parameters.gravity = Gravity.TOP | Gravity.CENTER_HORIZONTAL;
        parameters.width = FrameLayout.LayoutParams.MATCH_PARENT;
        snackbarView.setLayoutParams(parameters);

        final TextView textViewDefault = snackbar.getView().findViewById(R.id.snackbar_text);
        final TextView textViewAction = snackbar.getView().findViewById(R.id.snackbar_action);
        textViewDefault.setMaxLines(3);
        textViewDefault.setGravity(Gravity.CENTER_VERTICAL);
        textViewDefault.setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimension(R.dimen.smaller_font_size));
        textViewAction.setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimension(R.dimen.smaller_font_size));

        if (mode.equals(INFO)) {
            snackbarView.setBackgroundColor(ContextCompat.getColor(context, R.color.snackbar_info_background_color));
            textViewDefault.setTextColor(ContextCompat.getColor(context, R.color.snackbar_info_font_color));
            textViewAction.setTextColor(ContextCompat.getColor(context, R.color.snackbar_info_font_color));
        } else if (mode.equals(SUCCESS)) {
            snackbarView.setBackgroundColor(ContextCompat.getColor(context, R.color.snackbar_success_background_color));
            textViewDefault.setTextColor(ContextCompat.getColor(context, R.color.snackbar_success_font_color));
            textViewAction.setTextColor(ContextCompat.getColor(context, R.color.snackbar_success_font_color));
            textViewDefault.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_done_white, 0, 0, 0);
            textViewDefault.setCompoundDrawablePadding(resources.getDimensionPixelSize(R.dimen.global_spacing_xs));
        } else if (mode.equals(APPLY)) {
            snackbarView.setBackgroundColor(ContextCompat.getColor(context, R.color.snackbar_apply_background_color));
            textViewDefault.setTextColor(ContextCompat.getColor(context, R.color.snackbar_apply_font_color));
            textViewAction.setTextColor(ContextCompat.getColor(context, R.color.snackbar_apply_font_color));
        } else if (mode.equals(WARNING) || mode.equals(WARNING_INDEFINITE)) {
            snackbarView.setBackgroundColor(ContextCompat.getColor(context, R.color.snackbar_warning_background_color));
            textViewDefault.setTextColor(ContextCompat.getColor(context, R.color.snackbar_warning_font_color));
            textViewAction.setTextColor(ContextCompat.getColor(context, R.color.snackbar_warning_font_color));
            textViewDefault.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_warning_white, 0, 0, 0);
            textViewDefault.setCompoundDrawablePadding(resources.getDimensionPixelSize(R.dimen.global_spacing_xs));
        } else if (mode.equals(ERROR) || mode.equals(ERROR_INDEFINITE)) {
            snackbarView.setBackgroundColor(ContextCompat.getColor(context, R.color.snackbar_error_background_color));
            textViewDefault.setTextColor(ContextCompat.getColor(context, R.color.snackbar_error_font_color));
            textViewAction.setTextColor(ContextCompat.getColor(context, R.color.snackbar_error_font_color));
            textViewDefault.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_error_white, 0, 0, 0);
            textViewDefault.setCompoundDrawablePadding(resources.getDimensionPixelSize(R.dimen.global_spacing_xs));
        }

        snackbarView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                //
            }
        });

        return snackbar;
    }

    private View dismissSnackbar(final View view, final String message, final String mode) {
        return dismissSnackbar(view, message, mode, getSnackbarDuration(mode));
    }

    private View dismissSnackbar(final View view, final String message, final String mode, final int duration) {
        final Snackbar snackbar = getSnackbar(view, message, mode, duration);
        final View snackbackView = snackbar.getView();

        snackbackView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snackbar.dismiss();
            }
        });

        snackbar.show();

        return snackbackView;
    }
}
