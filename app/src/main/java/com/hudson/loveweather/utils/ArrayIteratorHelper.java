package com.hudson.loveweather.utils;

import java.util.Iterator;

/**
 * Created by Hudson on 2017/12/3.
 */

public class ArrayIteratorHelper<T> implements Iterator {
    private T[] array;
    private int position;

    public ArrayIteratorHelper(T[] array){
        this.array = array;
    }

    @Override
    public boolean hasNext() {
        return !(position>array.length-1||array[position] == null);
    }

    @Override
    public T next() {
        return array[position++];
    }
}
