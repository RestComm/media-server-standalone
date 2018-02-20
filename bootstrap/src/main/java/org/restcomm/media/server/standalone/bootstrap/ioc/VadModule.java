package org.restcomm.media.server.standalone.bootstrap.ioc;

import com.google.inject.AbstractModule;
import org.restcomm.media.core.resource.vad.VoiceActivityDetectorProvider;
import org.restcomm.media.server.standalone.bootstrap.ioc.provider.vad.GuiceNoiseThresholdDetectorProvider;

/**
 * @author Henrique Rosa (henrique.rosa@telestax.com) created on 20/02/2018
 */
public class VadModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(VoiceActivityDetectorProvider.class).toProvider(GuiceNoiseThresholdDetectorProvider.class).asEagerSingleton();

    }

}
