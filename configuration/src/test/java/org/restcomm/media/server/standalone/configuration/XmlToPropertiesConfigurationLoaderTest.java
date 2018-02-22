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

package org.restcomm.media.server.standalone.configuration;

import static org.junit.Assert.*;

import org.bouncycastle.crypto.tls.ProtocolVersion;
import org.junit.Test;
import org.restcomm.media.server.standalone.configuration.loader.ConfigurationLoader;
import org.restcomm.media.server.standalone.configuration.loader.XmlToPropertiesConfigurationLoader;
import org.restcomm.media.spi.RelayType;

/**
 * @author Henrique Rosa (henrique.rosa@telestax.com) created on 22/02/2018
 */
public class XmlToPropertiesConfigurationLoaderTest {

    @Test
    public void testLoadConfiguration() throws Exception {
        // given
        String filepath = "mediaserver.xml";
        final ConfigurationLoader loader = new XmlToPropertiesConfigurationLoader();

        // when
        loader.load(filepath);

        // then
        assertEquals("192.168.1.175", System.getProperty("mediaserver.network.bindAddress"));
        assertEquals("50.54.74.123", System.getProperty("mediaserver.network.externalAddress"));
        assertEquals("192.168.1.0", System.getProperty("mediaserver.network.network"));
        assertEquals("255.255.255.255", System.getProperty("mediaserver.network.subnet"));
        assertEquals("true", System.getProperty("mediaserver.network.sbc"));

        assertEquals("198.162.1.175", System.getProperty("mediaserver.controller.mgcp.address"));
        assertEquals("3437", System.getProperty("mediaserver.controller.mgcp.port"));
        assertEquals("4000", System.getProperty("mediaserver.controller.mgcp.channelBuffer"));
        assertEquals(RelayType.SPLITTER.name(), System.getProperty("mediaserver.controller.mgcp.endpoint.mobicents/bridge/.relay"));
        assertEquals(RelayType.MIXER.name(), System.getProperty("mediaserver.controller.mgcp.endpoint.mobicents/ivr/.relay"));
        assertEquals(RelayType.MIXER.name(), System.getProperty("mediaserver.controller.mgcp.endpoint.mobicents/cnf/.relay"));

        assertEquals("5", System.getProperty("mediaserver.media.timeout"));
        assertEquals("54534", System.getProperty("mediaserver.media.lowPort"));
        assertEquals("64534", System.getProperty("mediaserver.media.highPort"));
        assertEquals("60", System.getProperty("mediaserver.media.jitterBuffer.size"));
        assertEquals("0", System.getProperty("mediaserver.media.codec.l16.priority"));
        assertEquals("1", System.getProperty("mediaserver.media.codec.pcmu.priority"));
        assertEquals("2", System.getProperty("mediaserver.media.codec.pcma.priority"));
        assertEquals("3", System.getProperty("mediaserver.media.codec.gsm.priority"));
        assertEquals("4", System.getProperty("mediaserver.media.codec.opus.priority"));
        assertEquals("5", System.getProperty("mediaserver.media.codec.g729.priority"));
        assertEquals("6", System.getProperty("mediaserver.media.codec.telephone-event.priority"));

        assertEquals("-25", System.getProperty("mediaserver.resources.dtmfDetector.dbi"));
        assertEquals("100", System.getProperty("mediaserver.resources.dtmfDetector.toneDuration"));
        assertEquals("400", System.getProperty("mediaserver.resources.dtmfDetector.toneInterval"));
        assertEquals("-25", System.getProperty("mediaserver.resources.dtmfGenerator.toneVolume"));
        assertEquals("100", System.getProperty("mediaserver.resources.dtmfGenerator.toneDuration"));
        assertEquals("2000", System.getProperty("mediaserver.resources.player.connectionTimeout"));
        assertEquals("100", System.getProperty("mediaserver.resources.player.cache.size"));
        assertEquals("true", System.getProperty("mediaserver.resources.player.cache.enabled"));

        assertEquals(String.valueOf(ProtocolVersion.DTLSv10.getFullVersion()), System.getProperty("mediaserver.dtls.minVersion"));
        assertEquals(String.valueOf(ProtocolVersion.DTLSv12.getFullVersion()), System.getProperty("mediaserver.dtls.maxVersion"));
        assertEquals("TLS_ECDHE_ECDSA_WITH_AES_128_GCM_SHA256,TLS_ECDHE_ECDSA_WITH_CHACHA20_POLY1305_SHA256,TLS_ECDHE_ECDSA_WITH_AES_128_CBC_SHA,TLS_ECDHE_ECDSA_WITH_AES_256_CBC_SHA", System.getProperty("mediaserver.dtls.cipherSuites"));
        assertEquals("../conf/dtls/x509-server-ecdsa.pem", System.getProperty("mediaserver.dtls.certificate.path"));
        assertEquals("../conf/dtls/x509-server-key-ecdsa.pem", System.getProperty("mediaserver.dtls.certificate.key"));
        assertEquals("ECDSA", System.getProperty("mediaserver.dtls.certificate.algorithm"));

        assertEquals("org.mobicents.media.resource.asr.StubAsrDriver", System.getProperty("mediaserver.driver.asr.stub.class"));
        assertEquals("Stub Driver", System.getProperty("mediaserver.driver.asr.stub.stubName"));
        assertEquals("org.mobicents.media.resource.asr.StubAsrDriver2", System.getProperty("mediaserver.driver.asr.stub2.class"));
        assertEquals("Stub Driver 2", System.getProperty("mediaserver.driver.asr.stub2.stubName2"));
    }
}
