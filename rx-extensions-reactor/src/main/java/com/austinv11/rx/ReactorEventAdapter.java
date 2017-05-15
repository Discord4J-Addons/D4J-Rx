package com.austinv11.rx;

import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;
import sx.blah.discord.api.events.Event;
import sx.blah.discord.api.events.EventDispatcher;
import sx.blah.discord.api.events.IListener;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.Consumer;

/**
 * This represents an implementation of {@link IEventAdapter} which is backed by Reactor streams.
 */
public class ReactorEventAdapter implements IEventAdapter {
	
	private final EventDispatcher dispatcher;
	private final Flux<Event> flux;
	
	public ReactorEventAdapter(EventDispatcher dispatcher) {
		this.dispatcher = dispatcher;
		flux = Flux.create(new IListenerAdaptor(dispatcher), FluxSink.OverflowStrategy.BUFFER);
	}
	
	/**
	 * @see IEventAdapter#stream(Class) 
	 * 
	 * Note that this returns a {@link Flux} instance rather than {@link org.reactivestreams.Publisher}.
	 */
	@Override
	public <T extends Event> Flux<T> stream(Class<T> c) {
		return flux.ofType(c);
	}
	
	/**
	 * @see IEventAdapter#streamAll()
	 *
	 * Note that this returns a {@link Flux} instance rather than {@link org.reactivestreams.Publisher}.
	 */
	@Override
	public Flux<Event> streamAll() {
		return flux;
	}
	
	private class IListenerAdaptor implements Consumer<FluxSink<Event>> {
		
		private final EventDispatcher dispatcher;
		private final ConcurrentLinkedQueue<FluxSink<Event>> sinks = new ConcurrentLinkedQueue<>();
		
		IListenerAdaptor(EventDispatcher dispatcher) {
			this.dispatcher = dispatcher;
			dispatcher.registerListener(new IListener() {
				@Override
				public void handle(Event event) {
					sinks.forEach(sink -> sink.next(event));
				}
			});
		}
		
		@Override
		public void accept(FluxSink<Event> eventFluxSink) {
			sinks.add(eventFluxSink);
		}
	}
}
