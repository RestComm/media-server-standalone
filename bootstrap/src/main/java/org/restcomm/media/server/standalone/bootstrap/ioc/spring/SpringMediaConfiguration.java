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
import org.restcomm.media.asr.AsrEngineProvider;
import org.restcomm.media.component.dsp.DspFactoryImpl;
import org.restcomm.media.control.mgcp.endpoint.provider.MediaGroupProvider;
import org.restcomm.media.core.resource.vad.VoiceActivityDetectorProvider;
import org.restcomm.media.network.deprecated.UdpManager;
import org.restcomm.media.resource.dtmf.DetectorProvider;
import org.restcomm.media.resource.dtmf.DtmfDetectorFactory;
import org.restcomm.media.resource.player.audio.AudioPlayerProvider;
import org.restcomm.media.resource.player.audio.CachedRemoteStreamProvider;
import org.restcomm.media.resource.player.audio.DirectRemoteStreamProvider;
import org.restcomm.media.resource.player.audio.RemoteStreamProvider;
import org.restcomm.media.resource.recorder.audio.AudioRecorderProvider;
import org.restcomm.media.rtp.ChannelsManager;
import org.restcomm.media.rtp.channels.MediaChannelProvider;
import org.restcomm.media.rtp.crypto.AlgorithmCertificate;
import org.restcomm.media.rtp.crypto.CipherSuite;
import org.restcomm.media.rtp.crypto.DtlsSrtpServerProvider;
import org.restcomm.media.scheduler.PriorityQueueScheduler;
import org.restcomm.media.sdp.format.AVProfile;
import org.restcomm.media.sdp.format.RTPFormat;
import org.restcomm.media.sdp.format.RTPFormats;
import org.restcomm.media.server.standalone.configuration.CodecType;
import org.restcomm.media.spi.dsp.DspFactory;
import org.restcomm.media.spi.dtmf.DtmfDetectorProvider;
import org.restcomm.media.spi.player.PlayerProvider;
import org.restcomm.media.spi.recorder.RecorderProvider;
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

    public CodecType[] loadCodecs(@Value("${mediaserver.media.codecs}") String codecs) {
        final CodecType[] codecTypes;
        if (codecs == null || codecs.isEmpty()) {
            codecTypes = new CodecType[0];
        } else {
            final String[] codecSplit = codecs.split(",");
            codecTypes = new CodecType[codecSplit.length];

            for (int i = 0; i < codecSplit.length; i++) {
                CodecType codecType = CodecType.fromName(codecs);
                if (codecType != null) {
                    codecTypes[i] = codecType;
                }
            }
        }
        return codecTypes;
    }

    @Bean
    public DspFactory dspFactory(@Value("${mediaserver.media.codecs}") String codecs) {
        final DspFactoryImpl dsp = new DspFactoryImpl();
        final CodecType[] codecTypes = loadCodecs(codecs);

        for (CodecType codecType : codecTypes) {
            if (codecType != null && !codecType.getEncoder().isEmpty() && !codecType.getDecoder().isEmpty()) {
                dsp.addCodec(codecType.getDecoder());
                dsp.addCodec(codecType.getEncoder());
            }
        }
        return dsp;
    }

    @Bean
    public AudioPlayerProvider audioPlayerProvider(PriorityQueueScheduler scheduler, RemoteStreamProvider streamProvider, DspFactory dspFactory) {
        return new AudioPlayerProvider(scheduler, streamProvider, dspFactory);
    }

    @Bean
    public AudioRecorderProvider audioRecorderProvider(PriorityQueueScheduler scheduler, VoiceActivityDetectorProvider vadProvider) {
        return new AudioRecorderProvider(scheduler, vadProvider);
    }

    @Bean
    public DtmfDetectorProvider dtmfDetectorProvider(PriorityQueueScheduler scheduler, @Value("${mediaserver.resources.dtmfDetector.dbi}") int volume, @Value("${mediaserver.resources.dtmfDetector.toneDuration}") int duration, @Value("${mediaserver.resources.dtmfDetector.toneInterval}") int interval) {
        return new DetectorProvider(scheduler, volume, duration, interval);
    }

    @Bean
    public RTPFormats rtpFormats(@Value("${mediaserver.media.codecs}") String codecs) {
        final RTPFormats rtpFormats = new RTPFormats();
        final CodecType[] codecTypes = loadCodecs(codecs);

        for (CodecType codecType : codecTypes) {
            if (codecType != null) {
                RTPFormat format = AVProfile.audio.find(codecType.getPayloadType());
                if (format != null) {
                    rtpFormats.add(format);
                }
            }
        }
        return rtpFormats;
    }

    @Bean
    public ChannelsManager channelsManager(UdpManager udpManager, PriorityQueueScheduler scheduler, DtlsSrtpServerProvider dtlsProvider, RTPFormats rtpFormats, @Value("${mediaserver.media.jitterBuffer.size}") int jitterBufferSize) {
        final ChannelsManager channelsManager = new ChannelsManager(udpManager, rtpFormats, dtlsProvider);
        channelsManager.setScheduler(scheduler);
        channelsManager.setJitterBufferSize(jitterBufferSize);
        return channelsManager;
    }

    @Bean
    public MediaChannelProvider mediaChannelProvider(ChannelsManager channelsManager, DspFactory dspFactory) {
        return new MediaChannelProvider(channelsManager, dspFactory);
    }

    @Bean
    public MediaGroupProvider mediaGroupProvider(PlayerProvider players, DtmfDetectorProvider detectors, RecorderProvider recorders, AsrEngineProvider asrEngines) {
        return new MediaGroupProvider(players, detectors, recorders, asrEngines);
    }

}
