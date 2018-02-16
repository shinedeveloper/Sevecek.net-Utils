package net.sevecek.util.swing;

import java.awt.event.*;
import javax.swing.*;

public class JTimer extends Timer {

    public JTimer() {
        this(500, null);
    }

    public JTimer(int delay, ActionListener listener) {
        super(delay, listener);
    }
}
