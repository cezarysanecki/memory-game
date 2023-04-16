package pl.csanecki.memory.ui.dialogs;

import javax.swing.*;

abstract class GenericModalDialog extends JDialog {

    GenericModalDialog(JFrame owner, String title) {
        super(owner, title, true);
        setVisible(false);

        setSize(owner.getWidth() / 2, owner.getHeight() / 3);
        setLocation(
                owner.getLocation().x + (owner.getWidth() - getWidth()) / 2,
                owner.getLocation().y + (owner.getHeight() - getHeight()) / 2);
        setResizable(false);
    }
}
