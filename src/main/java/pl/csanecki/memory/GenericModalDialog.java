package pl.csanecki.memory;

import javax.swing.*;

public abstract class GenericModalDialog extends JDialog {

    public GenericModalDialog(JFrame owner, String title) {
        super(owner, title, true);

        setSize(owner.getWidth() / 2, owner.getHeight() / 3);
        setLocation(
                owner.getLocation().x + (owner.getWidth() - getWidth()) / 2,
                owner.getLocation().y + (owner.getHeight() - getHeight()) / 2);
        setResizable(false);
    }
}
