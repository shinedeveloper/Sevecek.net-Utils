package net.sevecek.util.swing;

import java.awt.*;
import java.awt.event.*;
import java.lang.ref.*;
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;

public class JKeyboard extends JComponent {

    private Map<Integer, Boolean> keys = new HashMap<Integer, Boolean>();
    private WeakReference<Window> parentWindowHolder;
    private WindowListener windowListener;
    private boolean active = true;

    public JKeyboard() {
        registerKeyboardDispatcher();
        addAncestorListener(new AncestorListener() {
            @Override
            public void ancestorAdded(AncestorEvent event) {
                onParentWindowChanged(event);
            }

            @Override
            public void ancestorRemoved(AncestorEvent event) {
                onParentWindowChanged(event);
            }

            @Override
            public void ancestorMoved(AncestorEvent event) {
                onParentWindowChanged(event);
            }
        });
    }

    private void onParentWindowChanged(AncestorEvent event) {
        Window newParentWindow = SwingUtilities.getWindowAncestor(this);
        unregisterWindowListener(newParentWindow);
        registerWindowListener(newParentWindow);
    }

    private void unregisterWindowListener(Window newParentWindow) {
        if (parentWindowHolder != null)  {
            Window oldParentWindow = parentWindowHolder.get();
            if (oldParentWindow != null && !oldParentWindow.equals(newParentWindow)) {
                oldParentWindow.removeWindowListener(windowListener);
            }
            parentWindowHolder = null;
        }
    }

    private void registerWindowListener(Window newParentWindow) {
        parentWindowHolder = new WeakReference<Window>(newParentWindow);
        windowListener = new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {
            }

            @Override
            public void windowClosing(WindowEvent e) {
                setActive(false);
            }

            @Override
            public void windowClosed(WindowEvent e) {
                setActive(false);
                e.getWindow().removeWindowListener(windowListener);
            }

            @Override
            public void windowIconified(WindowEvent e) {
                setActive(false);
            }

            @Override
            public void windowDeiconified(WindowEvent e) {
            }

            @Override
            public void windowActivated(WindowEvent e) {
                setActive(true);
            }

            @Override
            public void windowDeactivated(WindowEvent e) {
                setActive(false);
            }
        };
        newParentWindow.addWindowListener(windowListener);
    }

    private void registerKeyboardDispatcher() {
        KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
        manager.addKeyEventDispatcher(new KeyEventDispatcher() {
            @Override
            public boolean dispatchKeyEvent(KeyEvent e) {
                return JKeyboard.this.dispatchKeyEvent(e);
            }
        });
    }

    private boolean dispatchKeyEvent(KeyEvent e) {
        if (!active) {
            return false;
        }

        if (e.getID() == KeyEvent.KEY_PRESSED) {
            keyPressed(e);
        } else if (e.getID() == KeyEvent.KEY_RELEASED) {
            keyReleased(e);
        }
        return false;
    }

    private void keyPressed(KeyEvent e) {
        keys.put(e.getKeyCode(), Boolean.TRUE);
    }

    private void keyReleased(KeyEvent e) {
        keys.put(e.getKeyCode(), Boolean.FALSE);
    }

    public boolean isKeyDown(int key) {
        if (!active) {
            return false;
        }
        Boolean state = keys.get(key);
        return state != null && state.booleanValue();
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        if (!active) {
            keys.clear();
        }
        this.active = active;
    }
}
