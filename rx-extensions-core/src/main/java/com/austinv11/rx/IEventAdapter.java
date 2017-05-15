package com.austinv11.rx;

import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import sx.blah.discord.api.events.Event;
import sx.blah.discord.api.events.EventDispatcher;
import sx.blah.discord.api.events.IListener;

/**
 * This represents an abstract event adapter for converting listeners for {@link Event}s to Reactive Streams. 
 */
public interface IEventAdapter {
	
	/**
	 * This generates a stream of events which are filtered by checking instanceof each class using the passed argument.
	 * 
	 * @param c The class of the events to receive.
	 * @param <T> The event type to receive.
	 * @return The {@link Publisher} which represents the stream of events.
	 */
	<T extends Event> Publisher<T> stream(Class<T> c);
	
	/**
	 * This generates a stream of ALL events dispatched by the client.
	 * 
	 * @return The {@link Publisher} which represents the stream of events.
	 */
	Publisher<Event> streamAll();
	
	/**
	 * This generates an implementation of {@link IEventAdapter} using only default Reactive Streams interfaces.
	 * This is not recommended! Use a proper implementation like for RxJava or Reactor.
	 * 
	 * @param dispatcher The dispatcher to wrap.
	 * @return The new adapter.
	 */
	static IEventAdapter newDefaultAdapter(EventDispatcher dispatcher) {
		return new DefaultEventAdapter(dispatcher);
	}
}

class DefaultEventAdapter implements IEventAdapter {
	
	private final EventDispatcher dispatcher;
	
	DefaultEventAdapter(EventDispatcher dispatcher) {
		this.dispatcher = dispatcher;
	}
	
	@Override
	public <T extends Event> Publisher<T> stream(Class<T> c) {
		return new PublisherListener<>(dispatcher, c);
	}
	
	@Override
	public Publisher<Event> streamAll() {
		return new PublisherListener<>(dispatcher, Event.class);
	}
	
	class PublisherListener<E extends Event> implements IListener<E>, Publisher<E> {
		
		private volatile Subscriber<? super E> subscriber;
		private final EventDispatcher dispatcher;
		private final Class<E> clazz;
		
		PublisherListener(EventDispatcher dispatcher, Class<E> clazz) {this.dispatcher = dispatcher;
			this.clazz = clazz;
		}
		
		@Override
		public void subscribe(Subscriber<? super E> s) {
			subscriber = s;
			s.onSubscribe(new Subscription() {
				@Override
				public void request(long n) {
					try {
						E event = dispatcher.waitFor(clazz, n);
						subscriber.onNext(event);
					} catch (InterruptedException e) {
						if (subscriber != null)
							subscriber.onError(e);
					}
				}
				
				@Override
				public void cancel() {
					dispatcher.unregisterListener(this);
				}
			});
		}
		
		@Override
		public void handle(E event) {
			if (subscriber != null)
				subscriber.onNext(event);
		}
	}
}
