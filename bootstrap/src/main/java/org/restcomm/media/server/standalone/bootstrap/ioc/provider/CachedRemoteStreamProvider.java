package org.restcomm.media.server.standalone.bootstrap.ioc.provider;

import org.restcomm.media.server.standalone.configuration.MediaServerConfiguration;

import com.google.inject.Inject;
import com.google.inject.Provider;

/**
 * Created by achikin on 6/3/16.
 */
public class CachedRemoteStreamProvider implements Provider<org.restcomm.media.resource.player.audio.CachedRemoteStreamProvider> {

    private static org.restcomm.media.resource.player.audio.CachedRemoteStreamProvider instance;

    @Inject
    public CachedRemoteStreamProvider(MediaServerConfiguration config) {
        instance = new org.restcomm.media.resource.player.audio.CachedRemoteStreamProvider(
                config.getResourcesConfiguration().getPlayerCacheSize(), config.getResourcesConfiguration().getConnectionTimeout());
    }

    @Override
    public org.restcomm.media.resource.player.audio.CachedRemoteStreamProvider get() {
        return instance;
    }
}
