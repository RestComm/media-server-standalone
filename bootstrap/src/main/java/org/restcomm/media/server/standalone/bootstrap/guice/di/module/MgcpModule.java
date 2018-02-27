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

package org.restcomm.media.server.standalone.bootstrap.guice.di.module;

import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.ListeningScheduledExecutorService;
import com.google.inject.AbstractModule;
import com.google.inject.name.Names;
import org.restcomm.media.control.mgcp.call.MgcpCallManager;
import org.restcomm.media.control.mgcp.command.MgcpCommandProvider;
import org.restcomm.media.control.mgcp.connection.MgcpConnectionProvider;
import org.restcomm.media.control.mgcp.endpoint.MgcpEndpointManager;
import org.restcomm.media.control.mgcp.endpoint.provider.MediaGroupProvider;
import org.restcomm.media.control.mgcp.network.netty.AsyncMgcpChannel;
import org.restcomm.media.control.mgcp.network.netty.MgcpChannelInboundHandler;
import org.restcomm.media.control.mgcp.network.netty.MgcpChannelInitializer;
import org.restcomm.media.control.mgcp.network.netty.MgcpNetworkManager;
import org.restcomm.media.control.mgcp.pkg.MgcpEventProvider;
import org.restcomm.media.control.mgcp.pkg.MgcpPackageManager;
import org.restcomm.media.control.mgcp.pkg.MgcpSignalProvider;
import org.restcomm.media.control.mgcp.pkg.r.RtpEventProvider;
import org.restcomm.media.control.mgcp.transaction.MgcpTransactionManager;
import org.restcomm.media.control.mgcp.transaction.MgcpTransactionManagerProvider;
import org.restcomm.media.control.mgcp.transaction.MgcpTransactionNumberspace;
import org.restcomm.media.network.netty.filter.NetworkGuard;
import org.restcomm.media.server.standalone.bootstrap.guice.di.provider.core.ListeningScheduledExecutorServiceProvider;
import org.restcomm.media.server.standalone.bootstrap.guice.di.provider.mgcp.MgcpCallManagerProvider;
import org.restcomm.media.server.standalone.bootstrap.guice.di.provider.mgcp.*;
import org.restcomm.media.spi.ServerManager;

/**
 * @author Henrique Rosa (henrique.rosa@telestax.com)
 */
public class MgcpModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(MgcpConnectionProvider.class).toProvider(MgcpConnectionProviderProvider.class).asEagerSingleton();
        bind(MgcpEndpointInstallerProvider.MgcpEndpointInstallerListType.INSTANCE).toProvider(MgcpEndpointInstallerProvider.class).asEagerSingleton();
        bind(MgcpEndpointManager.class).toProvider(MgcpEndpointManagerProvider.class).asEagerSingleton();
        bind(MgcpCommandProvider.class).toProvider(MgcpCommandProviderProvider.class).asEagerSingleton();
        bind(MgcpTransactionNumberspace.class).toProvider(MgcpTransactionNumberspaceProvider.class).asEagerSingleton();
        bind(MgcpTransactionManagerProvider.class).toProvider(SubMgcpTransactionManagerProviderProvider.class).asEagerSingleton();
        bind(MgcpTransactionManager.class).toProvider(GlobalMgcpTransactionManagerProvider.class).asEagerSingleton();
        bind(MgcpChannelInboundHandler.class).toProvider(MgcpChannelInboundHandlerProvider.class).asEagerSingleton();
        bind(MgcpChannelInitializer.class).toProvider(MgcpChannelInitializerProvider.class).asEagerSingleton();
        bind(MgcpNetworkManager.class).toProvider(MgcpNetworkManagerProvider.class).asEagerSingleton();
        bind(AsyncMgcpChannel.class).toProvider(AsyncMgcpChannelProvider.class).asEagerSingleton();
        bind(ServerManager.class).toProvider(Mgcp2ControllerProvider.class).asEagerSingleton();
        bind(MgcpPackageManager.class).toProvider(DynamicMgcpPackageManagerProvider.class).asEagerSingleton();
        bind(MgcpCallManager.class).toProvider(MgcpCallManagerProvider.class).asEagerSingleton();
        bind(RtpEventProvider.class).toProvider(RtpEventProviderProvider.class).asEagerSingleton();
        bind(MgcpEventProvider.class).toProvider(GlobalMgcpEventProviderProvider.class).asEagerSingleton();
        bind(MgcpSignalProvider.class).toProvider(MgcpSignalProviderProvider.class).asEagerSingleton();
        bind(MediaGroupProvider.class).toProvider(MediaGroupProviderProvider.class).asEagerSingleton();
        bind(ListeningScheduledExecutorService.class).toProvider(ListeningScheduledExecutorServiceProvider.class).asEagerSingleton();
        bind(ListeningExecutorService.class).to(ListeningScheduledExecutorService.class);
        bind(NetworkGuard.class).annotatedWith(Names.named("mgcpNetworkGuard")).toProvider(LocalNetworkGuardProvider.class).asEagerSingleton();
    }

}
