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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Henrique Rosa (henrique.rosa@telestax.com) created on 22/02/2018
 */
@Configuration
public class SpringNetworkConfiguration {

    @Bean("RtpPortManager")
    public PortManager rtpPortManager(@Value("${mediaserver.media.lowPort}") int lowPort, @Value("${mediaserver.media.highPort}") int highPort) {
        return new RtpPortManager(lowPort, highPort);
    }

    @Bean("LocalPortManager")
    public PortManager localPortManager() {
        return new RtpPortManager();
    }

    @Bean("UdpManager")
    public UdpManager udpManager(Scheduler scheduler, @Qualifier("RtpPortManager") PortManager rtpPortManager, @Qualifier("LocalPortManager") PortManager localPortManager, @Value("${mediaserver.network.bindAddress}") String bindAddress, @Value("${mediaserver.controller.mgcp.address}") String mgcpAddress, @Value("${mediaserver.network.externalAddress}") String externalAddress, @Value("${mediaserver.network.network}") String network, @Value("${mediaserver.network.subnet}") String subnet, @Value("${mediaserver.media.timeout}") int timeout, @Value("${mediaserver.network.sbc}") boolean sbc) {
        final UdpManager udpManager = new UdpManager(scheduler, rtpPortManager, localPortManager);
        udpManager.setBindAddress(bindAddress);
        udpManager.setLocalBindAddress(mgcpAddress);
        udpManager.setExternalAddress(externalAddress);
        udpManager.setLocalNetwork(network);
        udpManager.setLocalSubnet(subnet);
        udpManager.setUseSbc(sbc);
        udpManager.setRtpTimeout(timeout);
        return udpManager;
    }

    @Bean("LocalNetworkGuard")
    public NetworkGuard localNetworkGuard(@Value("${mediaserver.network.network}") String network, @Value("${mediaserver.network.subnet}") String subnet) {
        return new LocalNetworkGuard(network, subnet);
    }

    @Bean("LocalNetworkFilter")
    public NetworkFilter mgcpNetworkFilter(@Qualifier("LocalNetworkGuard") NetworkGuard networkGuard) {
        return new NetworkFilter(networkGuard);
    }

}
