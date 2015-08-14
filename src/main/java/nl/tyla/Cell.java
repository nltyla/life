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

class Cell {
    private final int x;
    private final int y;
    private final int hashCode;

    Cell(int x, int y) {
        this.x = x;
        this.y = y;
        hashCode = internalHashCode(x, y);
    }

    int getX() {
        return x;
    }

    int getY() {
        return y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Cell cell = (Cell) o;

        if (x != cell.x) return false;
        return y == cell.y;

    }

    private static int internalHashCode(int x, int y) {
        return 512 * y + x;
    }

    @Override
    public int hashCode() {
        return hashCode;
    }

    @Override
    public String toString() {
        return "nl.tyla.Cell{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
