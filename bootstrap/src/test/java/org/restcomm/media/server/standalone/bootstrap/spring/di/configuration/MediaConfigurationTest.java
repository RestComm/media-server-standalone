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
import org.restcomm.media.server.standalone.bootstrap.spring.di.configuration.MediaConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;

/**
 * @author Henrique Rosa (henrique.rosa@telestax.com) created on 26/02/2018
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {MediaConfiguration.class})
public class MediaConfigurationTest {

    @Autowired
    private MediaConfiguration mediaConfiguration;

    @Test
    public void testConfiguration() {
        assertEquals(5, mediaConfiguration.getTimeout());
        assertEquals(120, mediaConfiguration.getHalfOpenDuration());
        assertEquals(6000, mediaConfiguration.getMaxDuration());
        assertEquals(50000, mediaConfiguration.getLowPort());
        assertEquals(60000, mediaConfiguration.getHighPort());
        assertEquals(60, mediaConfiguration.getJitterBuffer().getSize());
        final String[] codecs = mediaConfiguration.getCodecs();
        assertEquals(3, codecs.length);
        assertEquals("pcmu", codecs[0]);
        assertEquals("pcma", codecs[1]);
        assertEquals("telephone-event", codecs[2]);
    }

}
