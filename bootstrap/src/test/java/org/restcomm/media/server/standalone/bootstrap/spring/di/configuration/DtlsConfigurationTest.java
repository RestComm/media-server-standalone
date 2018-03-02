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

import org.junit.Test;
import org.junit.runner.RunWith;
import org.restcomm.media.server.standalone.bootstrap.spring.di.configuration.DtlsConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;

/**
 * @author Henrique Rosa (henrique.rosa@telestax.com) created on 26/02/2018
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {DtlsConfiguration.class})
public class DtlsConfigurationTest {

    @Autowired
    private DtlsConfiguration dtlsConfiguration;

    @Test
    public void testConfiguration() {
        assertEquals(1.1, dtlsConfiguration.getMinVersion(), 0);
        assertEquals(1.1, dtlsConfiguration.getMaxVersion(), 0);
        assertEquals(2, dtlsConfiguration.getCipherSuites().length);
        assertEquals("TLS_ECDHE_ECDSA_WITH_AES_128_GCM_SHA256", dtlsConfiguration.getCipherSuites()[0]);
        assertEquals("TLS_ECDHE_ECDSA_WITH_CHACHA20_POLY1305_SHA256", dtlsConfiguration.getCipherSuites()[1]);
        final DtlsConfiguration.CertificateConfiguration certificate = dtlsConfiguration.getCertificate();
        assertEquals("../conf/dtls/certificate.pem", certificate.getPath());
        assertEquals("../conf/dtls/mykey.pem", certificate.getKey());
        assertEquals("rsa", certificate.getAlgorithm());
    }

}
