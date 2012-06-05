/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.mobicents.media.server.impl;


import org.mobicents.media.MediaSource;
import org.mobicents.media.server.scheduler.Scheduler;
import org.mobicents.media.server.scheduler.Task;
import org.mobicents.media.server.spi.io.Pipe;
import org.mobicents.media.server.spi.memory.Frame;
import org.apache.log4j.Logger;

/**
 * The base implementation of the Media source.
 * 
 * <code>AbstractSource</code> and <code>AbstractSink</code> are implement general wirring contruct. All media
 * components have to extend one of these classes.
 * 
 * @author Oifa Yulian
 */
public abstract class AbstractSource extends BaseComponent implements MediaSource {

    //transmission statisctics
    private volatile long txPackets;
    private volatile long txBytes;
    
    //shows if component is started or not.
    private volatile boolean started;

    //stream synchronization flag
    private volatile boolean isSynchronized;

    //local media time
    private volatile long timestamp = 0;
    
    //initial media time
    private long initialOffset;
    
    //frame sequence number
    private long sn = 1;

    //scheduler instance
    private Scheduler scheduler;

    //media generator
    private final Worker worker;

  //duration of media stream in nanoseconds
    protected long duration = -1;

    //intial delay for media processing
    private long initialDelay = 0;
    
    //media transmission pipe
    protected PipeImpl pipe;        

    /**
     * Creates new instance of source with specified name.
     * 
     * @param name
     *            the name of the source to be created.
     */
    public AbstractSource(String name, Scheduler scheduler,int queueNumber) {
        super(name);
        this.scheduler = scheduler;
        this.worker = new Worker(scheduler,queueNumber);        
    }    

    /**
     * (Non Java-doc.)
     * 
     * @see org.mobicents.media.server.impl.AbstractSource#setInitialDelay(long) 
     */
    public void setInitialDelay(long initialDelay) {
        this.initialDelay = initialDelay;
    }

    
    /**
     * (Non Java-doc).
     * 
     * @see org.mobicents.media.MediaSource#getMediaTime();
     */
    public long getMediaTime() {
        return timestamp;
    }
    
    /**
     * (Non Java-doc).
     * 
     * @see org.mobicents.media.MediaSource#setDuration(long duration);
     */
    public void setDuration(long duration) {
        this.duration = duration;
    }
    
    /**
     * (Non Java-doc).
     * 
     * @see org.mobicents.media.MediaSource#getDuration();
     */
    public long getDuration() {
        return this.duration;
    }
    
    /**
     * (Non Java-doc).
     * 
     * @see org.mobicents.media.MediaSource#setMediaTime(long timestamp);
     */
    public void setMediaTime(long timestamp) {
        this.initialOffset = timestamp;
    }       

    /**
     * (Non Java-doc).
     * 
     * @see org.mobicents.media.MediaSource#start().
     */
    public void start() {
    	synchronized(worker) {
    		//check scheduler
    		try {
    			//prevent duplicate starting
    			if (started) {
    				return;
    			}

    			if (scheduler == null) {
    				throw new IllegalArgumentException("Scheduler is not assigned");
    			}

    			this.txBytes = 0;
    			this.txPackets = 0;
            
    			//reset media time and sequence number
    			timestamp = this.initialOffset;
    			this.initialOffset = 0;
            
    			sn = 0;

    			//switch indicator that source has been started
    			started = true;

    			//just started component always synchronized as well
    			this.isSynchronized = true;

    			//scheduler worker    
    			worker.reinit();
    			scheduler.submit(worker,worker.getQueueNumber());

    			//started!
    			started();
    		} catch (Exception e) {
    			started = false;
    			failed(e);
    		}
    	}
    }

    /**
     * Restores synchronization
     */
    public void wakeup() {    	
        synchronized(worker) {
            if (!started) {            	
                return;
            }
            
            if (!this.isSynchronized) {
                this.isSynchronized = true;
                scheduler.submit(worker,worker.getQueueNumber());
            }
        }
    }
    
    /**
     * (Non Java-doc).
     * 
     * @see org.mobicents.media.MediaSource#stop().
     */
    public void stop() {
        if (started) {
            stopped();
        }
        started = false;
        if (worker != null) {
            worker.cancel();
        }
        timestamp = 0;
    }

    /**
     * (Non Java-doc).
     *
     * @see org.mobicents.media.MediaSource#connect(org.mobicents.media.server.spi.io.Pipe)
     */
    public void connect(Pipe pipe) {
        this.pipe = (PipeImpl) pipe;
        this.pipe.source.set(this);
    }

    /**
     * (Non Java-doc).
     *
     * @see org.mobicents.media.MediaSource#disconnect(org.mobicents.media.server.spi.io.Pipe)
     */
    public void disconnect(Pipe pipe) {
    	((PipeImpl)pipe).source.set(null);
        this.pipe = null;
    }

    /**
     * (Non Java-doc).
     * 
     * @see org.mobicents.media.MediaSink#isConnected().
     */
    public boolean isConnected() {
        return pipe != null;
    }

    /**
     * (Non Java-doc).
     * 
     * @see org.mobicents.media.MediaSource#isStarted().
     */
    public boolean isStarted() {
        return this.started;
    }

    /**
     * This method must be overriden by concrete media source. T
     * he media have to fill buffer with media data and
     * attributes.
     * 
     * @param buffer the buffer object for media.
     * @param sequenceNumber
     *            the number of timer ticks from the begining.
     */
    public abstract Frame evolve(long timestamp);

    /**
     * Sends notification that media processing has been started.
     */
    protected void started() {
    }

    /**
     * Sends failure notification.
     * 
     * @param e the exception caused failure.
     */
    protected void failed(Exception e) {
    }

    /**
     * Sends notification that signal is completed.
     * 
     */
    protected void completed() {
        this.started = false;
    }

    /**
     * Called when source is stopped by request
     * 
     */
    protected void stopped() {
    }

    /**
     * (Non Java-doc).
     * 
     * @see org.mobicents.media.MediaSource#getPacketsReceived()
     */
    public long getPacketsTransmitted() {
    	return txPackets;
    }

    /**
     * (Non Java-doc).
     * 
     * @see org.mobicents.media.MediaSource#getBytesTransmitted()
     */
    public long getBytesTransmitted() {
        return txBytes;
    }

    @Override
    public void reset() {
        this.txPackets = 0;
        this.txBytes = 0;        
    }
    
    public String report() {
        return "";
    }
    
    
    /* (non-Javadoc)
     * @see org.mobicents.media.MediaSink#getInterface(java.lang.Class)
     */
    @Override
    public <T> T getInterface(Class<T> interfaceType) {
        //should we check default?
        return null;
    }

    /**
     * Media generator task
     */
    private class Worker extends Task {
    	/**
         * Creates new instance of task.
         *
         * @param scheduler the scheduler instance.
         */
    	private int queueNumber;    	
    	private long initialTime;
    	int readCount=0;
    	long overallDelay=0;
    	Frame frame;
    	long frameDuration;
    	
    	public Worker(Scheduler scheduler,int queueNumber) {
            super(scheduler);
            this.queueNumber=queueNumber;
            initialTime=scheduler.getClock().getTime();            
        }
        
        public void reinit()
        {
        	initialTime=scheduler.getClock().getTime();        	
        }
        
        public int getQueueNumber()
        {
        	return queueNumber;
        }

        /**
         * (Non Java-doc.)
         *
         * @see org.mobicents.media.server.scheduler.Task#perform()
         */
        public long perform() {
        	if(initialDelay+initialTime>scheduler.getClock().getTime())
        	{
        		//not a time yet
        		scheduler.submit(this,queueNumber);
                return 0;
        	}
        	
        	readCount=0;
        	overallDelay=0;
        	while(overallDelay<20000000L)
        	{
        		readCount++;
        		frame = evolve(timestamp);
        		if (frame == null) {
        			if(readCount==1)
        			{     
        				//stop if frame was not generated
        				isSynchronized = false;
        				return 0;
        			}
        			else
        			{
        				//frame was generated so continue
        				scheduler.submit(this,queueNumber);
        	            return 0;
        			}
            	}

            	//mark frame with media time and sequence number
            	frame.setTimestamp(timestamp);
            	frame.setSequenceNumber(sn);

            	//update media time and sequence number for the next frame
            	timestamp += frame.getDuration();
            	overallDelay += frame.getDuration();
            	sn = inc(sn);

            	//set end_of_media flag if stream has reached the end
            	if (duration > 0 && timestamp >= duration) {
            		frame.setEOM(true);
            	}

            	frameDuration = frame.getDuration();            	            	            	          
            	
            	//delivering data to the other party.
            	if (pipe != null) {
            		pipe.write(frame);
            	}
            	
            	//update transmission statistics
            	txPackets++;
            	txBytes += frame.getLength();
            
            	//send notifications about media termination
            	//and do not resubmit this task again if stream has bee ended
            	if (frame.isEOM()) { 
            		started = false;
        			completed();
        			return -1;
            	}

            	//check synchronization
            	if (frameDuration <= 0) {
            		//los of synchronization
                	isSynchronized = false;
                	return 0;
            	}            
        	}
        	
        	scheduler.submit(this,queueNumber);
            return 0;
        }

        @Override
        public String toString() {
            return getName();
        }

    }
}