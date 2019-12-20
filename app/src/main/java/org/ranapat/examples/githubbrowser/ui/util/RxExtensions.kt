package org.ranapat.examples.githubbrowser.ui.util

import android.app.Activity
import io.reactivex.*
import io.reactivex.disposables.Disposable

inline fun Completable.subscribeUiThread(activity: Activity, crossinline action: () -> Unit): Disposable {
    return subscribe {
        activity.runOnUiThread {
            action()
        }
    }
}

inline fun <T> Maybe<T>.subscribeUiThread(activity: Activity, crossinline onSuccess: (T) -> Unit): Disposable {
    return subscribeUiThread(activity, onSuccess, null)
}

inline fun <T> Maybe<T>.subscribeUiThread(activity: Activity, crossinline onSuccess: (T) -> Unit, noinline onError: ((Throwable) -> Unit)?): Disposable {
    return subscribe({
        activity.runOnUiThread { onSuccess(it) }
    }, {
        activity.runOnUiThread { onError?.invoke(it) }
    })
}

inline fun <T> Flowable<T>.subscribeUiThread(activity: Activity, crossinline onSuccess: (T) -> Unit): Disposable {
    return subscribeUiThread(activity, onSuccess, null)
}

inline fun <T> Flowable<T>.subscribeUiThread(activity: Activity, crossinline onSuccess: (T) -> Unit, noinline onError: ((Throwable) -> Unit)?): Disposable {
    return subscribe({
        activity.runOnUiThread { onSuccess(it) }
    }, {
        activity.runOnUiThread { onError?.invoke(it) }
    })
}

inline fun <T> Observable<T>.subscribeUiThread(activity: Activity, crossinline onSuccess: (T) -> Unit): Disposable {
    return subscribeUiThread(activity, onSuccess, null)
}

inline fun <T> Observable<T>.subscribeUiThread(activity: Activity, crossinline onSuccess: (T) -> Unit, noinline onError: ((Throwable) -> Unit)? = null): Disposable {
    return subscribe({
        activity.runOnUiThread { onSuccess(it) }
    }, {
        activity.runOnUiThread { onError?.invoke(it) }
    })
}


inline fun <T> Single<T>.subscribeUiThread(activity: Activity, crossinline onSuccess: (T) -> Unit): Disposable {
    return subscribeUiThread(activity, onSuccess, null)
}

inline fun <T> Single<T>.subscribeUiThread(activity: Activity, crossinline onSuccess: (T) -> Unit, noinline onError: ((Throwable) -> Unit)? = null): Disposable {
    return subscribe({
        activity.runOnUiThread { onSuccess(it) }
    }, {
        activity.runOnUiThread { onError?.invoke(it) }
    })
}