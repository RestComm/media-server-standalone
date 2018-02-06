package org.restcomm.media.server.standalone.bootstrap.ioc.provider;

import org.restcomm.media.server.standalone.configuration.MediaServerConfiguration;

import com.google.inject.Inject;
import com.google.inject.Provider;

/**
 * Created by achikin on 6/7/16.
 */
public class DirectRemoteStreamProvider implements Provider<org.restcomm.media.resource.player.audio.DirectRemoteStreamProvider> {

    private org.restcomm.media.resource.player.audio.DirectRemoteStreamProvider instance;

    @Inject
    public DirectRemoteStreamProvider(MediaServerConfiguration config) {
        instance = new org.restcomm.media.resource.player.audio.DirectRemoteStreamProvider(config.getResourcesConfiguration().getConnectionTimeout());
    }

    @Override
    public org.restcomm.media.resource.player.audio.DirectRemoteStreamProvider get() {
        return instance;
    }
}
