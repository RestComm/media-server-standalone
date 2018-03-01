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

package org.restcomm.media.server.standalone.bootstrap.spring;

import org.restcomm.media.server.standalone.configuration.loader.XmlToPropertiesConfigurationLoader;
import org.springframework.boot.Banner;
import org.springframework.boot.ResourceBanner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.io.Resource;

/**
 * @author Henrique Rosa (henrique.rosa@telestax.com) created on 27/02/2018
 */
@SpringBootApplication(scanBasePackages = {"org.restcomm.media.server.standalone.bootstrap.spring.di","org.restcomm.media.plugin"})
public class SpringBootstrapper {

    public static void main(String[] args) {
        try {
            final String configPath = System.getProperty("mediaserver.config.file");
            new XmlToPropertiesConfigurationLoader().load(configPath);

            final SpringApplication application = new SpringApplication(SpringBootstrapper.class);
            application.setHeadless(true);
            application.setWebEnvironment(false);
            application.setRegisterShutdownHook(true);
            application.run(args);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
