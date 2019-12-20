package org.ranapat.examples.githubbrowser.ui;

import androidx.lifecycle.ViewModel;

import org.ranapat.examples.githubbrowser.management.NetworkManager;
import org.ranapat.examples.githubbrowser.ui.publishable.ParameterizedMessages;
import org.ranapat.instancefactory.Fi;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public abstract class BaseViewModel extends ViewModel {
    final public ParameterizedMessages messages;

    private final NetworkManager networkManager;
    private final CompositeDisposable compositeDisposable;
    private boolean networkOnline;

    public BaseViewModel(
            final NetworkManager networkManager
    ) {
        this.messages = new ParameterizedMessages();

        this.networkManager = networkManager;

        compositeDisposable = new CompositeDisposable();

        networkOnline = networkManager.isOnline();
    }

    public BaseViewModel() {
        this(
                Fi.get(NetworkManager.class)
        );
    }

    @Override
    protected void onCleared() {
        compositeDisposable.clear();
    }

    public void onNetworkStatus() {
        boolean isOnline = networkManager.isOnline();

        if (isOnline != networkOnline) {
            triggerNetworkStatus(isOnline);
        }

        networkOnline = isOnline;
    }

    protected abstract void triggerNetworkStatus(final Boolean isOnline);

    protected void subscription(final Disposable disposable) {
        compositeDisposable.add(disposable);
    }

    protected boolean isOnline() {
        return networkManager.isOnline();
    }

    protected boolean isWifi() {
        return networkManager.isWifi();
    }

    protected boolean isMobile() {
        return networkManager.isMobile();
    }

    protected boolean isEthernet() {
        return networkManager.isEthernet();
    }

}
