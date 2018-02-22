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

package org.restcomm.media.server.standalone.bootstrap.ioc.spring.core;

import com.google.common.util.concurrent.ListeningScheduledExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.restcomm.media.scheduler.Clock;
import org.restcomm.media.scheduler.PriorityQueueScheduler;
import org.restcomm.media.scheduler.ServiceScheduler;
import org.restcomm.media.scheduler.WallClock;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;

/**
 * @author Henrique Rosa (henrique.rosa@telestax.com) created on 22/02/2018
 */
@Configuration
public class SpringCoreConfiguration {

    private static final int N_PROC = Runtime.getRuntime().availableProcessors();

    @Bean("WallClock")
    public Clock wallClock() {
        return new WallClock();
    }

    @Bean("TaskScheduler")
    public ServiceScheduler serviceScheduler(Clock wallClock) {
        return new ServiceScheduler(wallClock);
    }

    @Bean("MediaScheduler")
    public PriorityQueueScheduler mediaScheduler(Clock clock) {
        return new PriorityQueueScheduler(clock);
    }

    @Bean("MgcpScheduler")
    public ListeningScheduledExecutorService listeningScheduledExecutorService() {
        ThreadFactory threadFactory = new ThreadFactoryBuilder().setNameFormat("mgcp-%d").build();
        // TODO set uncaught exception handler

        ScheduledThreadPoolExecutor executor = (ScheduledThreadPoolExecutor) Executors.newScheduledThreadPool(N_PROC, threadFactory);
        executor.prestartAllCoreThreads();
        executor.setRemoveOnCancelPolicy(true);
        return MoreExecutors.listeningDecorator(executor);
    }

}
