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
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class Main {

    // confined to swing thread
    private boolean stepMode;
    private boolean stepTyped;
    private Set<Cell> cells = Collections.emptySet();
    private boolean working;

    void run() {
        SwingUtilities.invokeLater(new Runnable() {
            //@Override
            public void run() {
                final JFrame jFrame = new JFrame("Life");
                jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

                Canvas canvas = new Canvas();

                jFrame.getContentPane().add(canvas);
                jFrame.addKeyListener(new MyKeyListener(canvas));
                jFrame.pack();
                jFrame.setVisible(true);

                cells = Collections.newSetFromMap(new ConcurrentHashMap<Cell, Boolean>());
                trafficLight(cells);
                walker(cells);
                manyCells(cells);
                working = true;
                new LifeWorker(canvas, true, cells).execute();
            }

            private void manyCells(Set<Cell> cells) {
                for (int i = 0; i < 100000; i++) {
                    cells.add(new Cell((int) Math.round(Math.random() * 400.0 + 50.0), (int) Math.round((Math.random() * 400.0 + 50.0))));
                }
            }

            private void walker(Set<Cell> cells) {
                cells.add(new Cell(0, 10));
                cells.add(new Cell(1, 10));
                cells.add(new Cell(2, 10));
                cells.add(new Cell(2, 11));
                cells.add(new Cell(1, 12));
            }

            private void trafficLight(Set<Cell> cells) {
                cells.add(new Cell(0, 0));
                cells.add(new Cell(0, 1));
                cells.add(new Cell(0, 2));
            }
        });
    }

    public static void main(String[] args) throws InterruptedException, InvocationTargetException {
        new Main().run();
    }

    private class LifeWorker extends SwingWorker<Set<Cell>, Object> {

        private final Canvas canvas;
        private final boolean compute;
        private final Set<Cell> cells;

        public LifeWorker(Canvas canvas, boolean compute, Set<Cell> oldCells) {
            this.canvas = canvas;
            this.compute = compute;
            this.cells = oldCells;
        }

        @Override
        protected Set<Cell> doInBackground() throws Exception {
            if (compute) {
                Set<Cell> newCells = Collections.newSetFromMap(new ConcurrentHashMap<Cell, Boolean>(cells.size()));
                Set<Cell> potentialBirths = Collections.newSetFromMap(new ConcurrentHashMap<Cell, Boolean>());
                Life.gen(cells, newCells, potentialBirths);
                return newCells;
            } else {
                return cells;
            }
        }

        @Override
        protected void done() {
            try {
                Main.this.cells = get();
                canvas.setCells(Main.this.cells);
                canvas.repaint();
                if (!stepMode) {
                    new LifeWorker(canvas, true, Main.this.cells).execute();
                } else if (stepTyped) {
                    stepTyped = false;
                    new LifeWorker(canvas, true, Main.this.cells).execute();
                } else if (canvas.repaintNeeded()) {
                    new LifeWorker(canvas, false, Main.this.cells).execute();
                } else {
                    working = false;
                }
            } catch (Exception ignore) {
            }
        }
    }

    private class MyKeyListener implements KeyListener {
        private final Canvas canvas;

        public MyKeyListener(Canvas canvas) {
            this.canvas = canvas;
        }

        @Override
        public void keyTyped(KeyEvent e) {
            switch (e.getKeyChar()) {
                case ' ':
                    stepMode = !stepMode;
                    if (stepMode && !working) {
                        working = true;
                        new LifeWorker(canvas, true, cells).execute();
                    }
                    break;
                case '1':
                    if (stepMode) {
                        stepTyped = true;
                        if (!working) {
                            stepTyped = false;
                            working = true;
                            new LifeWorker(canvas, true, cells).execute();
                        }
                    }
                    break;
            }
        }

        @Override
        public void keyPressed(KeyEvent e) {
            processKey(e, true);
            if (!working) {
                working = true;
                new LifeWorker(canvas, false, cells).execute();
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {
            processKey(e, false);
        }

        private void processKey(KeyEvent e, boolean pressed) {
            switch (e.getKeyChar()) {
                case 'w':
                    canvas.setUpKeyPressed(pressed);
                    break;
                case 'a':
                    canvas.setLeftKeyPressed(pressed);
                    break;
                case 's':
                    canvas.setDownKeyPressed(pressed);
                    break;
                case 'd':
                    canvas.setRightKeyPressed(pressed);
                    break;
                case 'q':
                    canvas.setScaleDownKeyPressed(pressed);
                    break;
                case 'e':
                    canvas.setScaleUpKeyPressed(pressed);
                    break;
            }
        }
    }
}

