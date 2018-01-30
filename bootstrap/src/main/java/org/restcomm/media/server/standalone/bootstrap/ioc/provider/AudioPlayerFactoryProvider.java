/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2011-2016, Telestax Inc and individual contributors
 * by the @authors tag. 
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.restcomm.media.server.standalone.bootstrap.ioc.provider;

import org.restcomm.media.resource.player.audio.AudioPlayerFactory;
import org.restcomm.media.resource.player.audio.AudioPlayerImpl;
import org.restcomm.media.resource.player.audio.RemoteStreamProvider;
import org.restcomm.media.scheduler.PriorityQueueScheduler;
import org.restcomm.media.spi.dsp.DspFactory;
import org.restcomm.media.spi.pooling.PooledObjectFactory;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.TypeLiteral;

/**
 * @author Henrique Rosa (henrique.rosa@telestax.com)
 *
 */
public class AudioPlayerFactoryProvider implements Provider<AudioPlayerFactory> {

    private final PriorityQueueScheduler mediaScheduler;
    private final DspFactory dspFactory;
    private final RemoteStreamProvider remoteStreamProvider;

    @Inject
    public AudioPlayerFactoryProvider(PriorityQueueScheduler mediaScheduler, DspFactory dspFactory, RemoteStreamProvider remoteStreamProvider) {
        this.mediaScheduler = mediaScheduler;
        this.dspFactory = dspFactory;
        this.remoteStreamProvider = remoteStreamProvider;
    }

    @Override
    public AudioPlayerFactory get() {
        return new AudioPlayerFactory(mediaScheduler, dspFactory, remoteStreamProvider);
    }

    public static final class AudioPlayerFactoryType extends TypeLiteral<PooledObjectFactory<AudioPlayerImpl>> {

        public static final AudioPlayerFactoryType INSTANCE = new AudioPlayerFactoryType();

        private AudioPlayerFactoryType() {
            super();
        }

    }

}
