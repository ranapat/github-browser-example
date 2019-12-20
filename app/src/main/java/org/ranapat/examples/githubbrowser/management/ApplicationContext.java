package org.ranapat.examples.githubbrowser.management;

import android.content.Context;

import org.ranapat.examples.githubbrowser.GithubBrowserApplication;
import org.ranapat.instancefactory.StaticallyInstantiable;

@StaticallyInstantiable
public abstract class ApplicationContext extends Context {
    private ApplicationContext() {}

    public static Context getInstance() {
        return GithubBrowserApplication.getAppContext();
    }
}
