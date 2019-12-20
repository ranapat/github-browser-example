package org.ranapat.examples.githubbrowser;

final public class Settings {
    public static final long debounceNavigationInMilliseconds = BuildConfig.debounce_navigation_in_milliseconds;

    public static final int assetsNetworkTimeout = BuildConfig.assets_network_timeout;
    public static final String cacheAssetPrefix = BuildConfig.cache_asset_prefix;

    public static final Boolean resetDatabase = BuildConfig.reset_database;

    public static final String configApi = BuildConfig.config_api;

    public static final int keepConfigurationFor = BuildConfig.keep_configuration_for;
    public static final int keepEntitiesFor = BuildConfig.keep_entities_for;
}
