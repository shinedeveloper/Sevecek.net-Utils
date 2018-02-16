package net.sevecek.util.event;

import java.lang.ref.*;
import java.lang.reflect.*;
import java.util.*;
import java.util.concurrent.*;
import net.sevecek.util.*;

/**
 * <p>
 * A class that holds a list of event listeners and simplifies distributing events to them.
 * Example listener interfaces are
 * {@link java.awt.event.ActionListener},
 * {@link java.awt.event.KeyListener}
 * or any other interface of yours.
 * It is supposed to be used as an internal object within your component to
 * simplify handling of listeners, firing events
 * and help you avoid fatal mistakes. For instance:
 * <ul>
 *     <li>
 *         Listeners can be registered using
 *         {@link java.lang.ref.WeakReference WeakReferences}
 *         which prevents cyclic reference problem in OSGi,
 *         NetBeans Platform
 *         and other modular environments.
 *         Off course, it can be disabled for simple use cases.
 *     </li>
 *     <li>
 *         Internal listener list is thread-safe with zero overhead
 *         when firing events (uses
 *         {@link java.util.concurrent.CopyOnWriteArrayList CopyOnWriteArrayList})
 *     </li>
 *     <li>
 *         Listeners can either be called synchronously
 *         (in the same thread) or
 *         asynchronously in one other single thread or
 *         asynchronously in as many threads as listeners or
 *         asynchronously in certain maximum number of threads or
 *         anyhow else using an {@link java.util.concurrent.Executor}
 *         implementation you provide,
 *         such as using JMS on a Java EE application server
 *         or using SwingWorker Executor
 *         or using JavaFX 2 event thread.
 *     </li>
 * </ul>
 * </p>
 *
 * <p>
 * A single instance of the <code>EventSupport</code> can be used to hold
 * one type of a listener
 * and it has to be passed as a parameter to the factory method.
 * </p>
 *
 * <p>
 * The only responsibility of your component using the <code>EventSupport</code>
 * is to provide public methods
 * <code>add<i>XYZ</i>Listener</code> and
 * <code>remove<i>XYZ</i>Listener</code>
 * which will delegate to
 * the <code>EventSupport</code>.
 * </p>
 *
 * <p>
 * When the component which uses the <code>EventSupport</code> needs to fire an event to all listeners
 * it can use {@link #fireOnAllListeners()} to get an object that looks like a listener
 * (but in fact is a "multicast delegate" as known from .NET) and call a single event method on it
 * (with appropriate parameters). The <code>EventSupport</code> will then distribute
 * the call to all listeners.
 * </p>
 *
 * <p>
 * Note: Usage of the <code>EventSupport</code> is relatively cheap in case of no listeners.
 * </p>
 * <p>
 * Note: EventSupport is thread-safe at all times.
 * </p>
 *
 * <p>
 * <h3>Usage example:</h3>
 * Say you are creating a Button component that sends out {@link java.awt.event.ActionEvent ActionEvent}s,
 * and you want to allow users of the class to register {@link java.awt.event.ActionListener ActionListener}s
 * and receive notification when an {@link java.awt.event.ActionEvent ActionEvent} occurs.
 * </p>
 * <p>
 * The following should be added to the class definition:
 * </p>

 * <pre>
 *
 *  EventSupport&lt;ActionListener&gt; actionListeners = EventSupport.newInstance(ActionListener.class);
 *
 *  public void addActionListener(ActionListener l) {
 *      actionListeners.addListener(l);
 *  }
 *
 *  public void removeActionListener(ActionListener l) {
 *      actionListeners.removeListener(l);
 *  }
 *
 *  // Notify all listeners that have registered interest for this event type.
 *  // Note that fireOnAllListeners() returns a multicast delegate and everything you invoke on that
 *  // is distributed (delegated) to all listeners in the list.
 *  protected void fireActionPerformed() {
 *      ActionEvent actionEvent = new ActionEvent(this, 1, "button-click");
 *      actionListeners.fireOnAllListeners().actionPerformed(actionEvent);
 *  }
 *
 * </pre>
 *
 * <p>
 * <h3>Warning:</h3>
 * Serialized objects of this class may not be compatible with
 * future releases. The current serialization support is
 * appropriate for short term storage or RMI between applications running
 * the same version of the <code>EventSupport</code> class.  As of 1.4, support for long term storage
 * of all JavaBeans<sup><font size="-2">TM</font></sup>
 * has been added to the <code>java.beans</code> package.
 * Please see {@link java.beans.XMLEncoder}.
 * </p>
 *
 * @param <ListenerType> type of the listener the EventSupport should work with
 *
 * @author Kamil Sevecek (http://www.sevecek.net)
 * @since 2014/04/29
 * @version 1.3
 */
public class EventSupport<ListenerType> {

    private CopyOnWriteArrayList<ListenerType> listeners;
    private CopyOnWriteArrayList<WeakReference<ListenerType>> weakListeners;
    private Class<ListenerType> listenerInterface;
    private Executor asynchronousExecutor;
    private boolean useWeakReferences;
    private transient ListenerType multicastDelegateToAllListeners;

    /**
     * A factory method. Creates a <b>synchronous</b> <code>EventSupport&lt;ListenerType&gt;</code>.
     * Listeners are invoked in the originating thread.
     * Therefore the call to <code>fireOnAllListeners().yourMethod()</code> is blocking until all listeners finish.
     * @param listenerInterface specify <code>YourListenerInterface.class</code> to make <code>EventSupport</code> type safe
     * @return an instance of <code>EventSupport&lt;ListenerType&gt;</code>
     */
    public static <ListenerType> EventSupport<ListenerType> newInstance(Class<ListenerType> listenerInterface) {
        return EventSupport.newInstanceWithExecutor(listenerInterface,
                (Executor) null,
                false);
    }


    public static <ListenerType> EventSupport<ListenerType> newInstance(Class<ListenerType> listenerInterface, boolean useWeakReferences) {
        return EventSupport.newInstanceWithExecutor(listenerInterface,
                (Executor) null,
                useWeakReferences);
    }


    public static <ListenerType> EventSupport<ListenerType> newInstanceWithEventsInSingleOtherThread(Class<ListenerType> listenerInterface) {
        return EventSupport.newInstanceWithExecutor(listenerInterface,
                Executors.newSingleThreadExecutor());
    }


    public static <ListenerType> EventSupport<ListenerType> newInstanceWithEventsInSingleOtherThread(Class<ListenerType> listenerInterface, boolean useWeakReferences) {
        return EventSupport.newInstanceWithExecutor(listenerInterface,
                Executors.newSingleThreadExecutor(),
                useWeakReferences);
    }


    public static <ListenerType> EventSupport<ListenerType> newInstanceWithExecutor(Class<ListenerType> listenerInterface, Executor eventMulticastExecutor) {
        return new EventSupport<ListenerType>(listenerInterface, eventMulticastExecutor, false);
    }


    public static <ListenerType> EventSupport<ListenerType> newInstanceWithExecutor(Class<ListenerType> listenerInterface, Executor eventMulticastExecutor, boolean useWeakReferences) {
        return new EventSupport<ListenerType>(listenerInterface, eventMulticastExecutor, useWeakReferences);
    }


    private EventSupport(Class<ListenerType> listenerInterface, Executor eventMulticastExecutor, boolean useWeakReferences) {
        this.listenerInterface = listenerInterface;
        this.asynchronousExecutor = eventMulticastExecutor;
        this.useWeakReferences = useWeakReferences;
        if (!useWeakReferences) {
            listeners = new CopyOnWriteArrayList<ListenerType>();
        } else {
            weakListeners = new CopyOnWriteArrayList<WeakReference<ListenerType>>();
        }
        constructMulticastDelegate();
    }


    @SuppressWarnings("unchecked")
    private void constructMulticastDelegate() {
        InvocationHandler eventInvocationHandler;
        if (asynchronousExecutor == null) {
            if (!useWeakReferences) {
                eventInvocationHandler = new FireEventSynchronouslyInvocationHandler();
            } else {
                eventInvocationHandler = new FireEventWeaklyAndSynchronouslyInvocationHandler();
            }
        } else {
            if (!useWeakReferences) {
                eventInvocationHandler = new FireEventUsingExecutorInvocationHandler();
            } else {
                eventInvocationHandler = new FireEventWeaklyUsingExecutorInvocationHandler();
            }
        }
        multicastDelegateToAllListeners = (ListenerType) Proxy.newProxyInstance(
                this.getClass().getClassLoader(),
                new Class<?>[] { listenerInterface },
                eventInvocationHandler);
    }


    /**
     * <p>
     * Adds a listener to the list of registered listeners if it is not already present
     * (presence tested using <code>==</code>). If this EventSupport already contains the listener,
     * the call leaves the EventSupport unchanged and returns <code>false</code>.
     * </p>
     * <p>
     * This method is <b>thread-safe</b> and can be called from multiple threads at the same time.
     * </p>
     * @param listener the listener to be added
     * @return <tt>true</tt> if this EventSupport did not already contain the specified
     *         listener, otherwise <code>false</code>
     */
    public boolean addListener(ListenerType listener) {
        if (listener == null) {
            throw new NullPointerException("The listener argument must not be null");
        }
        return listeners.addIfAbsent(listener);
    }


    /**
     * <p>
     * Removes the listener from the list of registered listeners and
     * returns <code>true</code> if it was present
     * or <code>false</code> if the listener was not registered and therefore not removed.
     * In such a case the state of the EventSupport is not changed.
     * </p>
     * <p>
     * This method is <b>thread-safe</b> and can be called from multiple threads at the same time.
     * </p>
     * @param listener the listener to be removed
     * @return <tt>true</tt> if this EventSupport contained the specified listener,
     *         otherwise <code>false</code>
     */
    public boolean removeListener(ListenerType listener) {
        if (listener == null) {
            throw new NullPointerException("The listener argument must not be null");
        }
        return listeners.remove(listener);
    }


    /**
     * @return the number of currently registered listeners
     */
    public int size() {
        return listeners.size();
    }


    /**
     * <p>
     * Allows you to call any method on all listeners (usually sequentially) by one call.
     * It is an analogy to the .NET multicast delegate.
     * </p>
     * <p>
     * The method returns an object
     * implementing the listener interface
     * but not a concrete listener.
     * Instead the object is
     * a multicast delegate which will delegate
     * any received calls to all listeners registered
     * in the parent EventSupport object.
     * </p>
     * <u>Warning</u>: Any exceptions possibly thrown by the listeners
     * are logged and not propagated higher.
     *
     * <h3>Usage example:</h3>
     * <pre>
     *  public class MyComponent {
     *
     *      private EventSupport&lt;ActionListener&gt; actionListenersSupport
     *              = EventSupport.newInstance(ActionListener.class);
     *
     *      protected void fireActionPerformed(ActionEvent eventToBeDistributed) {
     *          actionEventSupport.fireOnAllListeners().actionPerformed(eventToBeDistributed);
     *      }
     *  }
     * </pre>
     * @return a multicast delegate implementing the listener interface (<code>ListenerType</code>).
     *         Whatever you call on the delegate will be called on all listeners (sequentially).
     */
    public ListenerType fireOnAllListeners() {
        return multicastDelegateToAllListeners;
    }


    private class FireEventSynchronouslyInvocationHandler implements InvocationHandler {

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            for (ListenerType listener : listeners) {
                method.invoke(listener, args);
            }
            return null;
        }

    }


    private class FireEventUsingExecutorInvocationHandler extends FireEventSynchronouslyInvocationHandler {

        @Override
        public Object invoke(Object proxy, final Method method, final Object[] args) throws Throwable {
            for (final ListenerType listener : listeners) {
                asynchronousExecutor.execute(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            method.invoke(listener, args);
                        } catch (Exception ex) {
                            throw ExceptionUtils.rethrowAsUnchecked(ex);
                        }
                    }
                });
            }
            return null;
        }
    }

    private class FireEventWeaklyAndSynchronouslyInvocationHandler implements InvocationHandler {

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            Iterator<WeakReference<ListenerType>> iterator = weakListeners.iterator();
            while (iterator.hasNext()) {
                WeakReference<ListenerType> weakListener = iterator.next();
                ListenerType listener = weakListener.get();
                if (listener != null) {
                    method.invoke(weakListener, args);
                } else {
                    iterator.remove();
                }
            }
            return null;
        }

    }


    private class FireEventWeaklyUsingExecutorInvocationHandler extends FireEventSynchronouslyInvocationHandler {

        @Override
        public Object invoke(Object proxy, final Method method, final Object[] args) throws Throwable {
            Iterator<WeakReference<ListenerType>> iterator = weakListeners.iterator();
            while (iterator.hasNext()) {
                WeakReference<ListenerType> weakListener = iterator.next();
                final ListenerType listener = weakListener.get();
                if (listener == null) {
                    iterator.remove();
                } else {
                    asynchronousExecutor.execute(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                method.invoke(listener, args);
                            } catch (Exception ex) {
                                throw ExceptionUtils.rethrowAsUnchecked(ex);
                            }
                        }
                    });
                }
            }
            return null;
        }
    }
}
