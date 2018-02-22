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

package org.restcomm.media.server.standalone.bootstrap.ioc.guice.provider.core;

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
        instance = new org.restcomm.media.resource.player.audio.DirectRemoteStreamProvider(config.getResourcesConfiguration().getPlayerConnectionTimeout());
    }

    @Override
    public org.restcomm.media.resource.player.audio.DirectRemoteStreamProvider get() {
        return instance;
    }
}
