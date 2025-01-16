package com.hidefile.secure.folder.vault.AdActivity;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;import com.socialbrowser.socialmedianetwork.allinoneplace.ads.appOpenLifeCycleChange;

public class AppLifecycleObserver implements LifecycleObserver {

    appOpenLifeCycleChange mAppOpenLifeCycleChange;

    public AppLifecycleObserver(appOpenLifeCycleChange mAppOpenLifeCycleChange) {
        this.mAppOpenLifeCycleChange = mAppOpenLifeCycleChange;
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    public void onEnterForeground() {
        mAppOpenLifeCycleChange.onForeground();

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    public void onEnterBackground() {
        mAppOpenLifeCycleChange.onBackground();
    }

}