package pl.cezarysanecki.memory.ui.dialogs;

import javax.swing.*;

abstract class GenericModalDialog extends JDialog {

    GenericModalDialog(JFrame owner, String title) {
        super(owner, title, true);
        setVisible(false);
        setResizable(false);
    }
}
