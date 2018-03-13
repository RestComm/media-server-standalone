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

package org.restcomm.media.server.standalone.bootstrap.spring.di.module;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.Configurator;
import org.restcomm.media.core.network.deprecated.UdpManager;
import org.restcomm.media.core.scheduler.PriorityQueueScheduler;
import org.restcomm.media.core.scheduler.Scheduler;
import org.restcomm.media.server.standalone.bootstrap.StandaloneMediaServer;
import org.restcomm.media.server.standalone.bootstrap.spring.di.configuration.*;
import org.restcomm.media.core.spi.ServerManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * @author Henrique Rosa (henrique.rosa@telestax.com) created on 27/02/2018
 */
@Component
public class SpringMediaServer extends StandaloneMediaServer {

    @Autowired
    NetworkConfiguration networkConfiguration;
    @Autowired
    MediaConfiguration mediaConfiguration;
    @Autowired
    MgcpConfiguration mgcpConfiguration;
    @Autowired
    PlayerConfiguration playerConfiguration;
    @Autowired
    DtmfDetectorConfiguration dtmfDetectorConfiguration;
    @Autowired
    DtmfGeneratorConfiguration dtmfGeneratorConfiguration;
    @Autowired
    DtlsConfiguration dtlsConfiguration;
    @Autowired
    DriversConfiguration driversConfiguration;

    @Autowired
    public SpringMediaServer(PriorityQueueScheduler mediaScheduler, Scheduler taskScheduler, UdpManager udpManager, ServerManager controller) {
        super(mediaScheduler, taskScheduler, udpManager, controller);
    }

    @Override
    @PostConstruct
    public void start() throws IllegalStateException {
        // Start the Media Server
        super.start();

        // Call the Garbage Collector
        if (log.isInfoEnabled()) {
            log.info("Called the Garbage collector to clear bootstrap data");
        }
        System.gc();
    }

    @Override
    @PreDestroy
    public void stop() throws IllegalStateException {
        // Stop the Media Server
        super.stop();

        // Shutdown log4j2
        if (log.isInfoEnabled()) {
            log.info("Shutting down logger engine");
        }
        Configurator.shutdown((LoggerContext) LogManager.getContext());
    }
}
