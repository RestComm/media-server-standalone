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

package org.restcomm.media.server.standalone.bootstrap.ioc.spring;

import org.restcomm.media.network.deprecated.PortManager;
import org.restcomm.media.network.deprecated.RtpPortManager;
import org.restcomm.media.network.deprecated.UdpManager;
import org.restcomm.media.network.netty.filter.LocalNetworkGuard;
import org.restcomm.media.network.netty.filter.NetworkGuard;
import org.restcomm.media.network.netty.handler.NetworkFilter;
import org.restcomm.media.scheduler.Scheduler;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Henrique Rosa (henrique.rosa@telestax.com) created on 22/02/2018
 */
@Configuration
public class SpringNetworkConfiguration {

    @Bean("RtpPortManager")
    public PortManager rtpPortManager(MediaConfiguration mediaConfiguration) {
        return new RtpPortManager(mediaConfiguration.getLowPort(), mediaConfiguration.getHighPort());
    }

    @Bean("LocalPortManager")
    public PortManager localPortManager() {
        return new RtpPortManager();
    }

    @Bean("UdpManager")
    public UdpManager udpManager(Scheduler scheduler, @Qualifier("RtpPortManager") PortManager rtpPortManager, @Qualifier("LocalPortManager") PortManager localPortManager, NetworkConfiguration networkConfiguration, MgcpConfiguration mgcpConfiguration, MediaConfiguration mediaConfiguration) {
        final UdpManager udpManager = new UdpManager(scheduler, rtpPortManager, localPortManager);
        udpManager.setBindAddress(networkConfiguration.getAddress());
        udpManager.setLocalBindAddress(mgcpConfiguration.getAddress());
        udpManager.setExternalAddress(networkConfiguration.getExternalAddress());
        udpManager.setLocalNetwork(networkConfiguration.getNetwork());
        udpManager.setLocalSubnet(networkConfiguration.getSubnet());
        udpManager.setUseSbc(networkConfiguration.isSbc());
        udpManager.setRtpTimeout(mediaConfiguration.getTimeout());
        return udpManager;
    }

    @Bean("LocalNetworkGuard")
    public NetworkGuard localNetworkGuard(NetworkConfiguration networkConfiguration) {
        return new LocalNetworkGuard(networkConfiguration.getNetwork(), networkConfiguration.getSubnet());
    }

    @Bean("LocalNetworkFilter")
    public NetworkFilter mgcpNetworkFilter(@Qualifier("LocalNetworkGuard") NetworkGuard networkGuard) {
        return new NetworkFilter(networkGuard);
    }

}
