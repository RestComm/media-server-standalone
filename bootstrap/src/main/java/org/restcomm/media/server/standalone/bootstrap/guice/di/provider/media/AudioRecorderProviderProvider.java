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

package org.restcomm.media.server.standalone.bootstrap.guice.di.provider.media;

import com.google.inject.Inject;
import com.google.inject.Provider;
import org.restcomm.media.core.resource.vad.VoiceActivityDetectorProvider;
import org.restcomm.media.resource.recorder.audio.AudioRecorderProvider;
import org.restcomm.media.scheduler.PriorityQueueScheduler;
import org.restcomm.media.spi.recorder.RecorderProvider;

/**
 * @author Henrique Rosa (henrique.rosa@telestax.com)
 */
public class AudioRecorderProviderProvider implements Provider<RecorderProvider> {

    private final PriorityQueueScheduler scheduler;
    private final VoiceActivityDetectorProvider vadProvider;

    @Inject
    public AudioRecorderProviderProvider(PriorityQueueScheduler scheduler, VoiceActivityDetectorProvider vadProvider) {
        super();
        this.scheduler = scheduler;
        this.vadProvider = vadProvider;
    }

    @Override
    public RecorderProvider get() {
        return new AudioRecorderProvider(this.scheduler, this.vadProvider);
    }

}
