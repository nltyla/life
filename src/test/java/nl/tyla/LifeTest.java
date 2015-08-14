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

import org.junit.Test;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class LifeTest {

    public static final int COUNT = 1_000_000;

    @Test
    public void testGen() throws Exception {
        Set<Cell> cells = Collections.newSetFromMap(new ConcurrentHashMap<Cell, Boolean>(COUNT * 2));
        for (int i = 0; i < COUNT; i++) {
            cells.add(new Cell((int) Math.round(Math.random() * 5000.0), (int) Math.round((Math.random() * 5000.0))));
        }

        for (int i = 0; i < 1000; i++) {
            long start = System.currentTimeMillis();
            Set<Cell> newCells = Collections.newSetFromMap(new ConcurrentHashMap<Cell, Boolean>(COUNT * 2));
            Set<Cell> potentialBirths = Collections.newSetFromMap(new ConcurrentHashMap<Cell, Boolean>(COUNT / 4));
            Life.gen(cells, newCells, potentialBirths);
            long end = System.currentTimeMillis();
            System.out.println("duration = " + (end - start));
        }

    }
}
