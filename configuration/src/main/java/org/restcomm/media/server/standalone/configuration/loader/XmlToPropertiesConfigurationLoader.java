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

package org.restcomm.media.server.standalone.configuration.loader;

import org.bouncycastle.crypto.tls.ProtocolVersion;
import org.restcomm.media.rtp.crypto.CipherSuite;
import org.restcomm.media.server.standalone.configuration.*;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

/**
 * @author Henrique Rosa (henrique.rosa@telestax.com) created on 21/02/2018
 */
public class XmlToPropertiesConfigurationLoader extends XmlConfigurationLoader {

    @Override
    public MediaServerConfiguration load(String filepath) throws Exception {
        final MediaServerConfiguration configuration = super.load(filepath);
        convertToProperties(configuration);
        return configuration;
    }

    private void convertToProperties(MediaServerConfiguration configuration) {
        convertNetworkToProperties(configuration.getNetworkConfiguration());
        convertMediaToProperties(configuration.getMediaConfiguration());
        convertMgcpControllerToProperties(configuration.getControllerConfiguration());
        convertResourcesToProperties(configuration.getResourcesConfiguration());
        convertDtlsToProperties(configuration.getDtlsConfiguration());
        convertSubsystemsToProperties(configuration.getSubsystemsConfiguration());
    }

    private void setNetworkProperty(String key, String value) {
        System.setProperty("mediaserver.network." + key, value);
    }

    private void convertNetworkToProperties(NetworkConfiguration configuration) {
        setNetworkProperty("bindAddress", configuration.getBindAddress());
        setNetworkProperty("externalAddress", configuration.getExternalAddress());
        setNetworkProperty("network", configuration.getNetwork());
        setNetworkProperty("subnet", configuration.getSubnet());
        setNetworkProperty("sbc", String.valueOf(configuration.isSbc()));
    }

    private void setMgcpControllerProperty(String key, String value) {
        System.setProperty("mediaserver.controller.mgcp." + key, value);
    }

    private void convertMgcpControllerToProperties(MgcpControllerConfiguration configuration) {
        setMgcpControllerProperty("address", configuration.getAddress());
        setMgcpControllerProperty("port", String.valueOf(configuration.getPort()));
        setMgcpControllerProperty("channelBuffer", String.valueOf(configuration.getChannelBuffer()));

        final Iterator<MgcpEndpointConfiguration> endpoints = configuration.getEndpoints();
        int index = 0;
        while (endpoints.hasNext()) {
            final MgcpEndpointConfiguration endpoint = endpoints.next();
            setMgcpControllerProperty("endpoints[" + index +"].name", endpoint.getName());
            setMgcpControllerProperty("endpoints[" + index +"].relay", endpoint.getRelayType().name());
            index++;
        }
    }

    private void setMediaProperty(String key, String value) {
        System.setProperty("mediaserver.media." + key, value);
    }

    private void convertMediaToProperties(MediaConfiguration configuration) {
        setMediaProperty("timeout", String.valueOf(configuration.getTimeout()));
        setMediaProperty("halfOpenDuration", String.valueOf(configuration.getHalfOpenDuration()));
        setMediaProperty("maxDuration", String.valueOf(configuration.getMaxDuration()));
        setMediaProperty("lowPort", String.valueOf(configuration.getLowPort()));
        setMediaProperty("highPort", String.valueOf(configuration.getHighPort()));
        setMediaProperty("jitterBuffer.size", String.valueOf(configuration.getJitterBufferSize()));

        final Iterator<String> codecs = configuration.getCodecs();
        final StringBuilder codecsBuilder = new StringBuilder();
        while (codecs.hasNext()) {
            codecsBuilder.append(codecs.next()).append(",");
        }
        codecsBuilder.deleteCharAt(codecsBuilder.lastIndexOf(","));
        setMediaProperty("codecs", codecsBuilder.toString());
    }

    private void setResourcesProperty(String key, String value) {
        System.setProperty("mediaserver.resources." + key, value);
    }

    private void convertResourcesToProperties(ResourcesConfiguration configuration) {
        setResourcesProperty("player.connectionTimeout", String.valueOf(configuration.getPlayerConnectionTimeout()));
        setResourcesProperty("player.cache.size", String.valueOf(configuration.getPlayerCacheSize()));
        setResourcesProperty("player.cache.enabled", String.valueOf(configuration.getPlayerCacheEnabled()));
        setResourcesProperty("dtmfDetector.dbi", String.valueOf(configuration.getDtmfDetectorDbi()));
        setResourcesProperty("dtmfDetector.toneDuration", String.valueOf(configuration.getDtmfDetectorToneDuration()));
        setResourcesProperty("dtmfDetector.toneInterval", String.valueOf(configuration.getDtmfDetectorToneInterval()));
        setResourcesProperty("dtmfGenerator.toneVolume", String.valueOf(configuration.getDtmfGeneratorToneVolume()));
        setResourcesProperty("dtmfGenerator.toneDuration", String.valueOf(configuration.getDtmfGeneratorToneDuration()));
    }

    private void setDtlsProperty(String key, String value) {
        System.setProperty("mediaserver.dtls." + key, value);
    }

    private void convertDtlsToProperties(DtlsConfiguration configuration) {
        setDtlsProperty("minVersion", String.valueOf(configuration.getMinVersion().getFullVersion()));
        setDtlsProperty("maxVersion", String.valueOf(configuration.getMaxVersion().getFullVersion()));

        final CipherSuite[] cipherSuites = configuration.getCipherSuites();
        final StringBuilder cipherBuilder = new StringBuilder();
        for (CipherSuite cipher : cipherSuites) {
            cipherBuilder.append(cipher.name()).append(",");
        }
        cipherBuilder.deleteCharAt(cipherBuilder.lastIndexOf(","));

        setDtlsProperty("cipherSuites", cipherBuilder.toString());
        setDtlsProperty("certificate.path", configuration.getCertificatePath());
        setDtlsProperty("certificate.key", configuration.getKeyPath());
        setDtlsProperty("certificate.algorithm", configuration.getAlgorithmCertificate().name());
    }

    private void setSubsystemProperty(String domain, String driver, String key, String value) {
        System.setProperty("mediaserver.drivers." + domain + "." + driver + "." + key, value);
    }

    private void convertSubsystemsToProperties(SubsystemsConfiguration configuration) {
        final Collection<SubsystemConfiguration> subsystems = configuration.getSubsystems();
        for (SubsystemConfiguration subsystem : subsystems) {
            convertSubsystemToProperties(subsystem);
        }
    }

    private void convertSubsystemToProperties(SubsystemConfiguration configuration) {
        final String subsystem = configuration.getName();
        final Collection<DriverConfiguration> drivers = configuration.getDrivers();

        for (DriverConfiguration driver : drivers) {
            final String driverName = driver.getDriverName();

            setSubsystemProperty(subsystem, driverName, "type", driver.getClassName());

            for (Map.Entry<String, String> parameter : driver.getParameters().entrySet()) {
                setSubsystemProperty(subsystem, driverName, parameter.getKey(), parameter.getValue());
            }
        }
    }

}
