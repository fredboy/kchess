package ru.fredboy.utils;

import ru.fredboy.kchess.Canvas;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.Consumer;

public class Matrix2<E> {

    private Object[][] values;

    private int width, height;

    public Matrix2(int width, int height) {
        this.width = width;
        this.height = height;
        values = new Object[width][height];
    }

    private boolean checkBounds(int x, int y) {
        return (x >= 0 && x < width && y >= 0 && y < height);
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    @SuppressWarnings("unchecked")
    public E get(int x, int y) {
        if (checkBounds(x, y)) return (E) values[x][y];
        else return null;
    }

    public void set(int x, int y, E value) {
        if (checkBounds(x, y)) values[x][y] = value;
    }

    public void clear() {
        for (int x = 0; x < width; x++) for (int y = 0; y < height; y++) values[x][y] = null;
    }

    public Iterator<E> iterator() {
        return new Itr();
    }

    private class Itr implements Iterator<E> {
        int x, y;

        Itr() {
            x = 0;
            y = 0;
        }

        @Override
        public boolean hasNext() {
            return (x <= width - 1 && y <= height - 1);
        }

        @Override
        @SuppressWarnings("unchecked")
        public E next() {
            if (x >= width && y >= height) throw new NoSuchElementException();
            E e = get(x++, y);
            if (x >= width) {
                x = 0;
                y++;
            }
            return e;
        }

        @Override
        public void remove() {
            set(x, y, null);
        }

        @Override
        public void forEachRemaining(Consumer<? super E> action) {
            while (hasNext()) action.accept(next());
        }
    }

}
