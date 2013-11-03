package de.htwg.upfaz.backgammon.gui;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;

public class StatusPanel
        extends JPanel {

    private final JLabel statusLabel = new JLabel("");
    private static final long serialVersionUID = 1L;

    public StatusPanel() {

        setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));

        setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
        add(statusLabel);
    }

    public final void setText(final String text) {
        statusLabel.setText(" " + text);
    }
}
