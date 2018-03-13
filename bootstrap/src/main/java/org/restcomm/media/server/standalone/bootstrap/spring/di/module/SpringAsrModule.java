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
import org.apache.logging.log4j.Logger;
import org.restcomm.media.core.asr.AsrEngineProvider;
import org.restcomm.media.core.asr.AsrEngineProviderImpl;
import org.restcomm.media.core.asr.driver.AsrDriverManagerImpl;
import org.restcomm.media.core.drivers.asr.AsrDriverManager;
import org.restcomm.media.core.resource.vad.VoiceActivityDetectorProvider;
import org.restcomm.media.core.scheduler.PriorityQueueScheduler;
import org.restcomm.media.server.standalone.bootstrap.spring.di.configuration.DriversConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

/**
 * @author Henrique Rosa (henrique.rosa@telestax.com) created on 26/02/2018
 */
@Configuration
public class SpringAsrModule {

    private static final Logger log = LogManager.getLogger(SpringAsrModule.class);

    @Bean
    public AsrEngineProvider asrEngineProvider(DriversConfiguration drivers, PriorityQueueScheduler scheduler, VoiceActivityDetectorProvider vadProvider) {
        final AsrDriverManager mng = new AsrDriverManagerImpl();
        final Map<String, DriversConfiguration.DriverProperties> asrDrivers = drivers.getDrivers().get("asr");

        if (asrDrivers != null && !asrDrivers.isEmpty()) {
            for (Map.Entry<String, DriversConfiguration.DriverProperties> driver : asrDrivers.entrySet()) {
                final DriversConfiguration.DriverProperties driverProperties = driver.getValue();
                mng.registerDriver(driver.getKey(), driverProperties.getType(), driverProperties.getParameters());

                if (log.isInfoEnabled()) {
                    log.info("Driver asr." + driver.getKey() + " (" + driver.getValue().getType() + ") was successfully registered");
                }
            }
        } else {
            log.warn("No ASR drivers were registered. Feature is deactivated.");
        }
        return new AsrEngineProviderImpl(scheduler, mng, vadProvider);
    }
}
