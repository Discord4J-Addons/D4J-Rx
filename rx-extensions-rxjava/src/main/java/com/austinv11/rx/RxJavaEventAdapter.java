package com.austinv11.rx;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import sx.blah.discord.api.events.Event;
import sx.blah.discord.api.events.EventDispatcher;
import sx.blah.discord.api.events.IListener;

import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * This represents an implementation of {@link IEventAdapter} which is backed by RxJava streams.
 */
public class RxJavaEventAdapter implements IEventAdapter {
	
	private final EventDispatcher dispatcher;
	private final Flowable<Event> eventFlowable;
	
	public RxJavaEventAdapter(EventDispatcher dispatcher) {
		this.dispatcher = dispatcher;
		eventFlowable = Flowable.create(new IListenerAdaptor(dispatcher), BackpressureStrategy.BUFFER);
	}
	
	/**
	 * @see IEventAdapter#stream(Class)
	 *
	 * Note that this returns a {@link Flowable} instance rather than {@link org.reactivestreams.Publisher}.
	 */
	@Override
	public <T extends Event> Flowable<T> stream(Class<T> c) {
		return eventFlowable.ofType(c);
	}
	
	/**
	 * @see IEventAdapter#streamAll()
	 *
	 * Note that this returns a {@link Flowable} instance rather than {@link org.reactivestreams.Publisher}.
	 */
	@Override
	public Flowable<Event> streamAll() {
		return eventFlowable;
	}
	
	private class IListenerAdaptor implements FlowableOnSubscribe<Event> {
		
		private final EventDispatcher dispatcher;
		private final ConcurrentLinkedQueue<FlowableEmitter<Event>> emitters = new ConcurrentLinkedQueue<>();
		
		IListenerAdaptor(EventDispatcher dispatcher) {
			this.dispatcher = dispatcher;
			dispatcher.registerListener(new IListener() {
				@Override
				public void handle(Event event) {
					emitters.forEach(emitter -> emitter.onNext(event));
				}
			});
		}
		
		@Override
		public void subscribe(FlowableEmitter<Event> e) throws Exception {
			emitters.add(e);
		}
	}
}
