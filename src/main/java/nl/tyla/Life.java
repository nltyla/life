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

import java.util.*;
import java.util.function.Consumer;

class Life {
    static void gen(final Set<Cell> oldCells, final Set<Cell> newCells, final Set<Cell> potentialBirths) {
        oldCells.parallelStream().forEach(new Consumer<Cell>() {
            @Override
            public void accept(Cell cell) {
                int neighbors = 0;
                for (Cell c : getSurrounding(cell)) {
                    if (oldCells.contains(c)) {
                        neighbors++;
                    } else {
                        if (potentialBirths.add(c)) {
                            if (neighbors(oldCells, c) == 3) {
                                newCells.add(c);
                            }
                        }
                    }
                }
                if (neighbors == 2 || neighbors == 3) {
                    newCells.add(cell);
                }
            }
        });
    }

    private static int neighbors(Set<Cell> cells, Cell cell) {
        int neighbors = 0;
        for (Cell c : getSurrounding(cell)) {
            if (cells.contains(c)) {
                neighbors++;
            }
        }
        return neighbors;
    }

    private static Cell[] getSurrounding(Cell cell) {
        Cell[] result = new Cell[8];
        int x = cell.getX();
        int y = cell.getY();
        result[0] = new Cell(x - 1, y + 1);
        result[1] = new Cell(x, y + 1);
        result[2] = new Cell(x + 1, y + 1);
        result[3] = new Cell(x - 1, y);
        result[4] = new Cell(x + 1, y);
        result[5] = new Cell(x - 1, y - 1);
        result[6] = new Cell(x, y - 1);
        result[7] = new Cell(x + 1, y - 1);
        return result;
    }
}
