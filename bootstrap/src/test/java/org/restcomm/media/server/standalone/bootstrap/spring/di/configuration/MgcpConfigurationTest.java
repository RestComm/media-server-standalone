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
import org.restcomm.media.server.standalone.bootstrap.spring.di.configuration.MgcpConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * @author Henrique Rosa (henrique.rosa@telestax.com) created on 26/02/2018
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {MgcpConfiguration.class})
public class MgcpConfigurationTest {

    @Autowired
    private MgcpConfiguration mgcpConfiguration;

    @Test
    public void testConfiguration() {
        assertEquals("198.162.1.175", mgcpConfiguration.getAddress());
        assertEquals(3437, mgcpConfiguration.getPort());
        assertEquals(3000, mgcpConfiguration.getChannelBuffer());
        final List<MgcpConfiguration.Endpoint> endpoints = mgcpConfiguration.getEndpoints();
        assertEquals(3, endpoints.size());
        assertEquals("restcomm/bridge/", endpoints.get(0).getName());
        assertEquals("splitter", endpoints.get(0).getRelay());
        assertEquals("restcomm/ivr/", endpoints.get(1).getName());
        assertEquals("mixer", endpoints.get(1).getRelay());
        assertEquals("restcomm/cnf/", endpoints.get(2).getName());
        assertEquals("mixer", endpoints.get(2).getRelay());
    }

}
