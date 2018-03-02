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

package org.restcomm.media.server.standalone.bootstrap.spring.di.module;

import org.bouncycastle.crypto.tls.ProtocolVersion;
import org.restcomm.media.asr.AsrEngineProvider;
import org.restcomm.media.component.dsp.DspFactoryImpl;
import org.restcomm.media.control.mgcp.endpoint.provider.MediaGroupProvider;
import org.restcomm.media.core.resource.vad.VoiceActivityDetectorProvider;
import org.restcomm.media.network.deprecated.UdpManager;
import org.restcomm.media.resource.dtmf.DetectorProvider;
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
import org.restcomm.media.server.standalone.bootstrap.spring.di.configuration.DtlsConfiguration;
import org.restcomm.media.server.standalone.bootstrap.spring.di.configuration.DtmfDetectorConfiguration;
import org.restcomm.media.server.standalone.bootstrap.spring.di.configuration.MediaConfiguration;
import org.restcomm.media.server.standalone.bootstrap.spring.di.configuration.PlayerConfiguration;
import org.restcomm.media.server.standalone.configuration.CodecType;
import org.restcomm.media.spi.dsp.DspFactory;
import org.restcomm.media.spi.dtmf.DtmfDetectorProvider;
import org.restcomm.media.spi.player.PlayerProvider;
import org.restcomm.media.spi.recorder.RecorderProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Henrique Rosa (henrique.rosa@telestax.com) created on 22/02/2018
 */
@Configuration
public class SpringMediaModule {

    @Bean
    @ConditionalOnProperty(name = "mediaserver.resources.player.cache.enabled", havingValue = "true")
    public CachedRemoteStreamProvider cachedRemoteStreamProvider(PlayerConfiguration playerConfiguration) {
        return new CachedRemoteStreamProvider(playerConfiguration.getCache().getSize(), playerConfiguration.getConnectionTimeout());
    }

    @Bean
    @ConditionalOnProperty(name = "mediaserver.resources.player.cache.enabled", havingValue = "false")
    public DirectRemoteStreamProvider directRemoteStreamProvider(PlayerConfiguration playerConfiguration) {
        return new DirectRemoteStreamProvider(playerConfiguration.getConnectionTimeout());
    }

    @Bean
    public DtlsSrtpServerProvider dtlsSrtpServerProvider(DtlsConfiguration dtlsConfiguration) {
        ProtocolVersion minProtocolVersion = ProtocolVersion.DTLSv10;
        if (dtlsConfiguration.getMinVersion() == 1.2) {
            minProtocolVersion = ProtocolVersion.DTLSv12;
        }

        ProtocolVersion maxProtocolVersion = ProtocolVersion.DTLSv12;
        if (dtlsConfiguration.getMaxVersion() == 1.0) {
            maxProtocolVersion = ProtocolVersion.DTLSv10;
        }

        final CipherSuite[] cipherSuites = buildCipherSuite(dtlsConfiguration.getCipherSuites());
        final DtlsConfiguration.CertificateConfiguration certificate = dtlsConfiguration.getCertificate();

        return new DtlsSrtpServerProvider(minProtocolVersion, maxProtocolVersion, cipherSuites, certificate.getPath(), certificate.getKey(), AlgorithmCertificate.valueOf(certificate.getAlgorithm().toUpperCase()));
    }

    private CipherSuite[] buildCipherSuite(String[] cipherSuites) {
        CipherSuite[] cipherSuiteTemp = new CipherSuite[cipherSuites.length];
        for (int i = 0; i < cipherSuites.length; i++) {
            cipherSuiteTemp[i] = CipherSuite.valueOf(cipherSuites[i].trim());
        }
        return cipherSuiteTemp;
    }

    private CodecType[] loadCodecs(String[] codecs) {
        final CodecType[] codecTypes = new CodecType[codecs.length];
        for (int i = 0; i < codecs.length; i++) {
            CodecType codecType = CodecType.fromName(codecs[i]);
            if (codecType != null) {
                codecTypes[i] = codecType;
            }
        }
        return codecTypes;
    }

    @Bean
    public DspFactory dspFactory(MediaConfiguration mediaConfiguration) {
        final DspFactoryImpl dsp = new DspFactoryImpl();
        final CodecType[] codecTypes = loadCodecs(mediaConfiguration.getCodecs());

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
    public DtmfDetectorProvider dtmfDetectorProvider(PriorityQueueScheduler scheduler, DtmfDetectorConfiguration dtmfDetectorConfiguration) {
        return new DetectorProvider(scheduler, dtmfDetectorConfiguration.getDbi(), dtmfDetectorConfiguration.getToneDuration(), dtmfDetectorConfiguration.getToneInterval());
    }

    @Bean
    public RTPFormats rtpFormats(MediaConfiguration mediaConfiguration) {
        final RTPFormats rtpFormats = new RTPFormats();
        final CodecType[] codecTypes = loadCodecs(mediaConfiguration.getCodecs());

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
    public ChannelsManager channelsManager(UdpManager udpManager, PriorityQueueScheduler scheduler, DtlsSrtpServerProvider dtlsProvider, RTPFormats rtpFormats, MediaConfiguration mediaConfiguration) {
        final ChannelsManager channelsManager = new ChannelsManager(udpManager, rtpFormats, dtlsProvider);
        channelsManager.setScheduler(scheduler);
        channelsManager.setJitterBufferSize(mediaConfiguration.getJitterBuffer().getSize());
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
