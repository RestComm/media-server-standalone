/*
 *  TeleStax, Open Source Cloud Communications
 *  Copyright 2011-2018, Telestax Inc and individual contributors
 *  by the @authors tag.
 *
 *  This is free software; you can redistribute it and/or modify it
 *  under the terms of the GNU Lesser General Public License as
 *  published by the Free Software Foundation; either version 2.1 of
 *  the License, or (at your option) any later version.
 *
 *  This software is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 *  Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public
 *  License along with this software; if not, write to the Free
 *  Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 *  02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.restcomm.media.server.standalone.bootstrap.guice.di.module;

import com.google.inject.AbstractModule;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import org.restcomm.media.resource.player.audio.RemoteStreamProvider;
import org.restcomm.media.rtp.ChannelsManager;
import org.restcomm.media.rtp.channels.MediaChannelProvider;
import org.restcomm.media.rtp.crypto.DtlsSrtpServerProvider;
import org.restcomm.media.server.standalone.bootstrap.guice.di.provider.media.DtlsSrtpServerProviderProvider;
import org.restcomm.media.server.standalone.bootstrap.guice.di.provider.media.*;
import org.restcomm.media.spi.dsp.DspFactory;
import org.restcomm.media.spi.dtmf.DtmfDetectorProvider;
import org.restcomm.media.spi.player.PlayerProvider;
import org.restcomm.media.spi.recorder.RecorderProvider;

/**
 * @author Henrique Rosa (henrique.rosa@telestax.com)
 */
public class MediaModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(DspFactory.class).toProvider(DspProvider.class).asEagerSingleton();
        bind(DtlsSrtpServerProvider.class).toProvider(DtlsSrtpServerProviderProvider.class).asEagerSingleton();
        bind(ChannelsManager.class).toProvider(ChannelsManagerProvider.class).asEagerSingleton();
        bind(MediaChannelProvider.class).toProvider(MediaChannelProviderProvider.class).asEagerSingleton();
        bind(PlayerProvider.class).toProvider(AudioPlayerProviderProvider.class).asEagerSingleton();
        bind(RecorderProvider.class).toProvider(AudioRecorderProviderProvider.class).asEagerSingleton();
        bind(DtmfDetectorProvider.class).toProvider(DtmfDetectorProviderProvider.class).asEagerSingleton();
    }

}
