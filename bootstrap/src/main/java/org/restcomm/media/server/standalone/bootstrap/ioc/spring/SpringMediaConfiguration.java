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

import org.bouncycastle.crypto.tls.ProtocolVersion;
import org.restcomm.media.component.dsp.DspFactoryImpl;
import org.restcomm.media.resource.player.audio.CachedRemoteStreamProvider;
import org.restcomm.media.resource.player.audio.DirectRemoteStreamProvider;
import org.restcomm.media.rtp.crypto.AlgorithmCertificate;
import org.restcomm.media.rtp.crypto.CipherSuite;
import org.restcomm.media.rtp.crypto.DtlsSrtpServerProvider;
import org.restcomm.media.server.standalone.bootstrap.ioc.guice.provider.media.DspProvider;
import org.restcomm.media.server.standalone.configuration.CodecType;
import org.restcomm.media.spi.dsp.DspFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Henrique Rosa (henrique.rosa@telestax.com) created on 22/02/2018
 */
@Configuration
public class SpringMediaConfiguration {

    @Bean
    @ConditionalOnProperty(name = "mediaserver.resources.player.cache.enabled", havingValue = "true")
    public CachedRemoteStreamProvider cachedRemoteStreamProvider(@Value("${mediaserver.resources.player.connectionTimeout}") int connectionTimeout, @Value("${mediaserver.resources.player.cache.size}") int cacheSize) {
        return new CachedRemoteStreamProvider(cacheSize, connectionTimeout);
    }

    @Bean
    @ConditionalOnProperty(name = "mediaserver.resources.player.cache.enabled", havingValue = "false")
    public DirectRemoteStreamProvider directRemoteStreamProvider(@Value("${mediaserver.resources.player.connectionTimeout}") int connectionTimeout) {
        return new DirectRemoteStreamProvider(connectionTimeout);
    }

    @Bean
    public DtlsSrtpServerProvider dtlsSrtpServerProvider(@Value("${mediaserver.dtls.minVersion}") int minVersion, @Value("${mediaserver.dtls.maxVersion}") int maxVersion, @Value("${mediaserver.dtls.cipherSuites}") String cipherSuites, @Value("${mediaserver.dtls.certificate.path}") String certificatePath, @Value("${mediaserver.dtls.certificate.key}") String keyPath, @Value("${mediaserver.dtls.certificate.algorithm}") String algorithmCertificate) {
        ProtocolVersion minProtocolVersion = ProtocolVersion.DTLSv10;
        if (minVersion == ProtocolVersion.DTLSv12.getFullVersion()) {
            minProtocolVersion = ProtocolVersion.DTLSv12;
        }

        ProtocolVersion maxProtocolVersion = ProtocolVersion.DTLSv12;
        if (maxVersion == ProtocolVersion.DTLSv10.getFullVersion()) {
            maxProtocolVersion = ProtocolVersion.DTLSv10;
        }

        return new DtlsSrtpServerProvider(minProtocolVersion, maxProtocolVersion, buildCipherSuite(cipherSuites), certificatePath, keyPath, AlgorithmCertificate.valueOf(algorithmCertificate.toUpperCase()));
    }

    private CipherSuite[] buildCipherSuite(String cipherSuites) {
        String[] values = cipherSuites.split(",");
        CipherSuite[] cipherSuiteTemp = new CipherSuite[values.length];
        for (int i = 0; i < values.length; i++) {
            cipherSuiteTemp[i] = CipherSuite.valueOf(values[i].trim());
        }
        return cipherSuiteTemp;
    }

    @Bean
    public DspFactory dspFactory(@Value("${mediaserver.media.codecs}") String codecs) {
        final DspFactoryImpl dsp = new DspFactoryImpl();
        if (codecs != null && !codecs.isEmpty()) {
            for (String codec : codecs.split(",")) {
                final CodecType codecType = CodecType.fromName(codec);
                if (codecType != null && !codecType.getEncoder().isEmpty() && !codecType.getDecoder().isEmpty()) {
                    dsp.addCodec(codecType.getDecoder());
                    dsp.addCodec(codecType.getEncoder());
                }
            }
        }
        return dsp;
    }

}
