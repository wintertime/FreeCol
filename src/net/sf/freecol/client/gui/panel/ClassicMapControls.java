/**
 *  Copyright (C) 2002-2020   The FreeCol Team
 *
 *  This file is part of FreeCol.
 *
 *  FreeCol is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 2 of the License, or
 *  (at your option) any later version.
 *
 *  FreeCol is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with FreeCol.  If not, see <http://www.gnu.org/licenses/>.
 */

package net.sf.freecol.client.gui.panel;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;

import net.sf.freecol.client.FreeColClient;
import net.sf.freecol.client.gui.FontLibrary;
import net.sf.freecol.client.gui.action.ActionManager;
import net.sf.freecol.common.resources.ResourceManager;


/**
 * A collection of panels and buttons that are used to provide the
 * user with a more detailed view of certain elements on the map and
 * also to provide a means of input in case the user can't use the
 * keyboard.
 */
public final class ClassicMapControls extends MapControls {

    /** A font for the buttons. */
    private final Font arrowFont;

    /** The main panel. */
    private final JPanel panel;

    /** Helper container for the abstract API functions. */
    private final List<Component> componentList = new ArrayList<>();


    /**
     * The basic constructor.
     *
     * @param freeColClient The {@code FreeColClient} for the game.
     */
    public ClassicMapControls(final FreeColClient freeColClient) {
        super(freeColClient, false);

        this.arrowFont = FontLibrary.createFont(FontLibrary.FontType.SIMPLE,
            FontLibrary.FontSize.SMALL, Font.BOLD);

        this.panel = new MigPanel(new MigLayout("wrap 3"));
        this.panel.add(this.miniMap, "span, width " + MINI_MAP_WIDTH
                                     + ", height " + MINI_MAP_HEIGHT);
        this.panel.add(this.miniMapZoomInButton, "newline 10");
        this.panel.add(this.miniMapZoomOutButton, "skip");

        this.panel.add(makeButton("NW", ResourceManager.getString("arrow.NW")),
                       "newline 20");
        this.panel.add(makeButton("N",  ResourceManager.getString("arrow.N")));
        this.panel.add(makeButton("NE", ResourceManager.getString("arrow.NE")));
        this.panel.add(makeButton("W",  ResourceManager.getString("arrow.W")));
        this.panel.add(makeButton("E",  ResourceManager.getString("arrow.E")),
                       "skip");
        this.panel.add(makeButton("SW", ResourceManager.getString("arrow.SW")));
        this.panel.add(makeButton("S",  ResourceManager.getString("arrow.S")));
        this.panel.add(makeButton("SE", ResourceManager.getString("arrow.SE")),
                       "wrap 20");
        // initialization completes in initializeUnitButtons
        
        this.componentList.add(this.panel);
    }

    /**
     * Makes a {@code JButton} for a given arrow direction.
     * By default, the direction and arrow parameters should
     * be the same literal direction, just formatted for a
     * unique context.
     *
     * @param direction The Direction on the {@code Map} to move
     * @param arrow The Direction of the arrow itself
     * @return A JButton with the correct display and action
     *
     * @since 0.10.6
     */
    private JButton makeButton(String direction, String arrow) {
        final ActionManager am = getFreeColClient().getActionManager();
        JButton button
            = new JButton(am.getFreeColAction("moveAction." + direction));
        button.setFont(arrowFont);
        button.setText(arrow);
        return button;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean initializeUnitButtons() {
        if (!super.initializeUnitButtons()) return false;

        for (UnitButton ub : this.unitButtons) this.panel.add(ub);
        this.panel.add(this.infoPanel, "newline push, span, width "
            + this.infoPanel.getWidth() + ", height "
            + this.infoPanel.getHeight());
        return true;
    }

    // Implement MapControls
    
    /**
     * {@inheritDoc}
     */
    @Override
    public List<Component> getComponentsToAdd(Dimension newSize) {
        if (getGame() == null || this.panel.isShowing()) {
            return Collections.<Component>emptyList();
        }
        int width = (int)this.panel.getPreferredSize().getWidth();
        this.panel.setSize(width, newSize.height);
        this.panel.setLocation(newSize.width - width, 0);
        return this.componentList;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Component> getComponentsPresent() {
        return (this.panel.isShowing()) ? this.componentList
            : Collections.<Component>emptyList();
    }
}
