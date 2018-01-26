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

import org.restcomm.media.server.standalone.configuration.MediaServerConfiguration;
import org.restcomm.media.resource.dtmf.DetectorImpl;
import org.restcomm.media.resource.dtmf.DtmfDetectorFactory;
import org.restcomm.media.scheduler.PriorityQueueScheduler;
import org.restcomm.media.spi.pooling.PooledObjectFactory;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.TypeLiteral;

/**
 * @author Henrique Rosa (henrique.rosa@telestax.com)
 *
 */
public class DtmfDetectorFactoryProvider implements Provider<DtmfDetectorFactory> {

    private final PriorityQueueScheduler mediaScheduler;
    private final MediaServerConfiguration configuration;

    @Inject
    public DtmfDetectorFactoryProvider(MediaServerConfiguration configuration, PriorityQueueScheduler mediaScheduler) {
        this.mediaScheduler = mediaScheduler;
        this.configuration = configuration;
    }

    @Override
    public DtmfDetectorFactory get() {
        final int volume = this.configuration.getResourcesConfiguration().getDtmfDetectorDbi();
        final int duration = this.configuration.getResourcesConfiguration().getDtmfDetectorToneDuration();
        final int interval = this.configuration.getResourcesConfiguration().getDtmfDetectorToneInterval();
        return new DtmfDetectorFactory(this.mediaScheduler, volume, duration, interval);
    }

    public static final class DtmfDetectorFactoryType extends TypeLiteral<PooledObjectFactory<DetectorImpl>> {

        public static final DtmfDetectorFactoryType INSTANCE = new DtmfDetectorFactoryType();

        private DtmfDetectorFactoryType() {
            super();
        }

    }

}
