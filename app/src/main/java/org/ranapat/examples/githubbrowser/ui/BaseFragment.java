package org.ranapat.examples.githubbrowser.ui;

import androidx.fragment.app.Fragment;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public abstract class BaseFragment extends Fragment {
    private final CompositeDisposable compositeDisposable;

    public BaseFragment() {
        compositeDisposable = new CompositeDisposable();
    }

    protected abstract void initializeUi();
    protected abstract void initializeListeners();
    protected abstract void initializeNetwork();

    @Override
    public void onStart() {
        super.onStart();

        initializeUi();
        initializeListeners();
        initializeNetwork();
    }

    @Override
    public void onStop() {
        super.onStop();

        compositeDisposable.clear();
    }

    protected void subscription(final Disposable disposable) {
        compositeDisposable.add(disposable);
    }
}
