package Controllers;

import Views.frmPrincipal;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Arrays;
import java.util.List;

public class ConfigurationController extends MouseAdapter implements MouseListener {

    private frmPrincipal view;
    List<Component> jpanels;

    public ConfigurationController(frmPrincipal view) {
        this.view = view;
        jpanels = getListComponents(this.view.jpSidenav);
        this.loadEvents();
    }

    private void loadEvents() {
        for (Component jpanel : jpanels) {
            jpanel.addMouseListener(this);
        }
    }

    private List<Component> getListComponents(final Container container) {
        Component[] components = container.getComponents();
        return Arrays.asList(components);
    }

    private void markAllAsUnselect() {
        for (Component jpanel : jpanels) {
            jpanel.setBackground(new Color(240, 240, 240));
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        super.mouseEntered(e);
        markAllAsUnselect();
        for (Component jpanel : jpanels) {
            if (e.getSource().equals(jpanel)) {
                jpanel.setBackground(new Color(255, 255, 255));
            }
        }
    }

}
