package org.restcomm.media.server.standalone.bootstrap.ioc.provider.vad;

import com.google.inject.Provider;
import org.restcomm.media.plugin.vad.spring.NoiseThresholdDetectorSpringProvider;

/**
 * @author Henrique Rosa (henrique.rosa@telestax.com) created on 20/02/2018
 */
public class GuiceNoiseThresholdDetectorProvider implements Provider<NoiseThresholdDetectorSpringProvider> {

    private static final int SILENCE_LEVEL = 10;

    @Override
    public NoiseThresholdDetectorSpringProvider get() {
        return new NoiseThresholdDetectorSpringProvider(SILENCE_LEVEL);
    }

}
