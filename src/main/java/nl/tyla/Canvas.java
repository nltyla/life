/*
 * Copyright 2015 Tycho Lamerigts
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */

package nl.tyla;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.Collections;
import java.util.Set;

//@NotThreadSafe
// Should be confined to Swing thread
class Canvas extends JPanel {

    private double scale;
    private Set<Cell> cells;
    private int x;
    private int y;
    private boolean leftKeyPressed;
    private boolean rightKeyPressed;
    private boolean upKeyPressed;
    private boolean downKeyPressed;
    private boolean scaleUpKeyPressed;
    private boolean scaleDownKeyPressed;

    Canvas() {
        setPreferredSize(new Dimension(500, 500));
        scale = 1;
        cells = Collections.emptySet();
        x = y = 0;
    }

    @Override
    protected void paintComponent(Graphics g) {
        if (scaleDownKeyPressed) scaleDown();
        if (scaleUpKeyPressed) scaleUp();
        if (leftKeyPressed) left();
        if (rightKeyPressed) right();
        if (upKeyPressed) up();
        if (downKeyPressed) down();

        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;
        g2d.translate(x, getHeight() + y);
        g2d.scale(scale, -scale);
        g2d.setStroke(new BasicStroke(0));
        g2d.setPaint(SystemColor.BLUE);

        for (Cell cell : cells) {
            Rectangle2D.Double rectangle = new Rectangle2D.Double(cell.getX(), cell.getY(), 1, 1);
            g2d.draw(rectangle);
        }
    }

    void setCells(Set<Cell> cells) {
        this.cells = cells;
    }

    void setLeftKeyPressed(boolean leftKeyPressed) {
        this.leftKeyPressed = leftKeyPressed;
    }

    void setRightKeyPressed(boolean rightKeyPressed) {
        this.rightKeyPressed = rightKeyPressed;
    }

    void setUpKeyPressed(boolean upKeyPressed) {
        this.upKeyPressed = upKeyPressed;
    }

    boolean repaintNeeded() {
        return upKeyPressed || downKeyPressed || rightKeyPressed || leftKeyPressed || scaleDownKeyPressed || scaleUpKeyPressed;
    }

    void setDownKeyPressed(boolean downKeyPressed) {
        this.downKeyPressed = downKeyPressed;
    }

    void setScaleUpKeyPressed(boolean scaleUpKeyPressed) {
        this.scaleUpKeyPressed = scaleUpKeyPressed;
    }

    void setScaleDownKeyPressed(boolean scaleDownKeyPressed) {
        this.scaleDownKeyPressed = scaleDownKeyPressed;
    }

    private void scaleUp() {
        scale += .1;
    }

    private void scaleDown() {
        if (scale > 1.0) {
            scale -= 0.1;
        }
    }

    private void left() {
        x += scale;
    }

    private void right() {
        x -= scale;
    }

    private void up() {
        y += scale;
    }

    private void down() {
        y -= scale;
    }
}
