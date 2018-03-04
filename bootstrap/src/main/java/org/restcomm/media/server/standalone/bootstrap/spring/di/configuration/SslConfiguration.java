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

package org.restcomm.media.server.standalone.bootstrap.spring.di.configuration;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @author Henrique Rosa (henrique.rosa@telestax.com) created on 04/03/2018
 */
@Component
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "mediaserver.ssl")
public class SslConfiguration {

    private static final Logger log = LogManager.getLogger(SslConfiguration.class);

    private String trustStore = "";
    private String trustStorePassword = "";
    private String trustStoreType = "";
    private String keyStore = "";
    private String keyStorePassword = "";
    private Boolean debug;

    public void setTrustStore(String trustStore) {
        this.trustStore = trustStore;
    }

    public void setTrustStorePassword(String trustStorePassword) {
        this.trustStorePassword = trustStorePassword;
    }

    public void setKeyStore(String keyStore) {
        this.keyStore = keyStore;
    }

    public void setKeyStorePassword(String keyStorePassword) {
        this.keyStorePassword = keyStorePassword;
    }

    public void setTrustStoreType(String trustStoreType) {
        this.trustStoreType = trustStoreType;
    }

    public void setDebug(Boolean debug) {
        this.debug = debug;
    }

    @PostConstruct
    public void apply() {
        if (this.trustStore != null && !this.trustStore.isEmpty()) {
            System.setProperty("javax.net.ssl.trustStore", this.trustStore);
        }

        if (this.trustStorePassword != null && !this.trustStorePassword.isEmpty()) {
            System.setProperty("javax.net.ssl.trustStorePassword", this.trustStorePassword);
        }
        if (this.trustStoreType != null && !this.trustStoreType.isEmpty()) {
            System.setProperty("javax.net.ssl.trustStoreType", this.trustStoreType);
        }

        if (this.keyStore != null && !this.keyStore.isEmpty()) {
            System.setProperty("javax.net.ssl.keyStore", this.keyStore);
        }

        if (this.keyStorePassword != null && !this.keyStorePassword.isEmpty()) {
            System.setProperty("javax.net.ssl.keyStorePassword", this.keyStorePassword);
        }

        if (this.debug != null) {
            System.setProperty("javax.net.debug", String.valueOf(this.debug));
        }

        log.info("SYSTEM PROPERTIES:");
        System.getProperties().forEach((key, value) -> log.info(key + ": " + value));
    }
}
