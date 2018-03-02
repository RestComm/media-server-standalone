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

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author Henrique Rosa (henrique.rosa@telestax.com) created on 26/02/2018
 */
@Component
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "mediaserver.media")
public class MediaConfiguration {

    private int timeout = 0;
    private int halfOpenDuration = 300;
    private int maxDuration = 14400;
    private int lowPort = 34534;
    private int highPort = 65534;
    private JitterBufferConfiguration jitterBuffer;
    private String[] codecs = new String[0];

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public int getHalfOpenDuration() {
        return halfOpenDuration;
    }

    public void setHalfOpenDuration(int halfOpenDuration) {
        this.halfOpenDuration = halfOpenDuration;
    }

    public int getMaxDuration() {
        return maxDuration;
    }

    public void setMaxDuration(int maxDuration) {
        this.maxDuration = maxDuration;
    }

    public int getLowPort() {
        return lowPort;
    }

    public void setLowPort(int lowPort) {
        this.lowPort = lowPort;
    }

    public int getHighPort() {
        return highPort;
    }

    public void setHighPort(int highPort) {
        this.highPort = highPort;
    }

    public String[] getCodecs() {
        return codecs;
    }

    public void setCodecs(String[] codecs) {
        this.codecs = codecs;
    }

    public JitterBufferConfiguration getJitterBuffer() {
        return jitterBuffer;
    }

    public void setJitterBuffer(JitterBufferConfiguration jitterBuffer) {
        this.jitterBuffer = jitterBuffer;
    }

    public static class JitterBufferConfiguration {

        private int size = 50;

        public int getSize() {
            return size;
        }

        public void setSize(int size) {
            this.size = size;
        }

    }

}
