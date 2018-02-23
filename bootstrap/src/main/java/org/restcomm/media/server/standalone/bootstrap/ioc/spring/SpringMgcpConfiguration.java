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

import com.google.common.util.concurrent.ListeningScheduledExecutorService;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;
import org.restcomm.media.control.mgcp.message.MgcpMessageParser;
import org.restcomm.media.control.mgcp.network.netty.*;
import org.restcomm.media.control.mgcp.transaction.*;
import org.restcomm.media.network.netty.channel.NettyNetworkChannelGlobalContext;
import org.restcomm.media.network.netty.handler.NetworkFilter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Henrique Rosa (henrique.rosa@telestax.com) created on 22/02/2018
 */
@Configuration
public class SpringMgcpConfiguration {

    @Bean
    public MgcpMessageParser mgcpMessageParser() {
        return new MgcpMessageParser();
    }

    @Bean
    public MgcpMessageDecoder mgcpMessageDecoder(MgcpMessageParser parser) {
        return new MgcpMessageDecoder(parser);
    }

    @Bean
    public MgcpMessageEncoder mgcpMessageEncoder() {
        return new MgcpMessageEncoder();
    }

    @Bean
    public MgcpChannelInboundHandler mgcpChannelInboundHandler() {
        return new MgcpChannelInboundHandler();
    }

    @Bean
    public MgcpChannelInitializer mgcpChannelInitializer(@Value("${mediaserver.controller.mgcp.channelBuffer}") int channelBuffer, @Qualifier("LocalNetworkFilter") NetworkFilter filter, MgcpMessageDecoder decoder, MgcpChannelInboundHandler inboundHandler, MgcpMessageEncoder encoder) {
        return new MgcpChannelInitializer(channelBuffer, filter, decoder, inboundHandler, encoder);
    }

    @Bean
    public MgcpTransactionNumberspace mgcpTransactionNumberspace() {
        return new MgcpTransactionNumberspace();
    }

    @Bean
    public MgcpTransactionManagerProvider subMgcpTransactionManagerProvider(MgcpTransactionNumberspace numberspace, ListeningScheduledExecutorService executor) {
        return new SubMgcpTransactionManagerProvider(numberspace, executor);
    }

    @Bean
    public MgcpTransactionManager mgcpTransactionManager(MgcpTransactionManagerProvider transactionManagerProvider) {
        return new GlobalMgcpTransactionManager(transactionManagerProvider);
    }

    @Bean("MgcpNioEventLoopGroup")
    public NioEventLoopGroup mgcpNioEventLoopGroup(ListeningScheduledExecutorService executor) {
        return new NioEventLoopGroup(Runtime.getRuntime().availableProcessors(), executor);
    }

    @Bean("MgcpNioBootstrap")
    public Bootstrap mgcpNioBootstrap(@Qualifier("MgcpNioEventLoopGroup") EventLoopGroup eventLoopGroup) {
        return new Bootstrap().channel(NioDatagramChannel.class).group(eventLoopGroup);
    }

    @Bean
    public MgcpNetworkManager mgcpNetworkManager(@Qualifier("MgcpNioBootstrap") Bootstrap bootstrap, MgcpChannelInitializer initializer) {
        return new MgcpNetworkManager(bootstrap, initializer);
    }

    @Bean
    public AsyncMgcpChannel asyncMgcpChannel(MgcpNetworkManager networkManager, MgcpChannelInboundHandler inboundHandler) {
        return new AsyncMgcpChannel(new NettyNetworkChannelGlobalContext(networkManager), inboundHandler);
    }

}
