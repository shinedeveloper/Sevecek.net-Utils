package net.sevecek.util;

import java.lang.reflect.*;
import java.util.*;

public class SameObjectList<T> implements List<T> {

    private T singleObject;
    private int size;


    public SameObjectList(T singleObject, int size) {
        if (size < 0) {
            throw new IllegalArgumentException("Size must be 0 or more");
        }
        this.singleObject = singleObject;
        this.size = size;
    }


    @Override
    public int size() {
        return size;
    }


    @Override
    public T get(int index) {
        return singleObject;
    }


    @Override
    public boolean isEmpty() {
        return size == 0;
    }


    @Override
    public boolean contains(Object o) {
        return o.equals(singleObject);
    }


    @Override
    public Iterator<T> iterator() {
        return listIterator();
    }


    @Override
    public Object[] toArray() {
        Object[] array = new Object[size];
        Arrays.fill(array, singleObject);
        return array;
    }


    @Override
    @SuppressWarnings("unchecked")
    public <T1> T1[] toArray(T1[] array) {
        if (array.length == size) {
            Arrays.fill(array, singleObject);
            return array;
        } else if (array.length > size) {
            Arrays.fill(array, 0, size, singleObject);
            array[size] = null;
            return array;
        } else {
            array = (T1[]) Array.newInstance(array.getClass().getComponentType(), size);
            Arrays.fill(array, singleObject);
            return array;
        }
    }


    @Override
    public boolean add(T t) {
        throw new UnsupportedOperationException();
    }


    @Override
    public boolean remove(Object o) {
        throw new UnsupportedOperationException();
    }


    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object o : c) {
            if (!o.equals(singleObject)) return false;
        }
        return true;
    }


    @Override
    public boolean addAll(Collection<? extends T> c) {
        throw new UnsupportedOperationException();
    }


    @Override
    public boolean addAll(int index, Collection<? extends T> c) {
        throw new UnsupportedOperationException();
    }


    @Override
    public boolean removeAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }


    @Override
    public boolean retainAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }


    @Override
    public void clear() {
        throw new UnsupportedOperationException();
    }


    @Override
    public T set(int index, T element) {
        throw new UnsupportedOperationException();
    }


    @Override
    public void add(int index, T element) {
        throw new UnsupportedOperationException();
    }


    @Override
    public T remove(int index) {
        throw new UnsupportedOperationException();
    }


    @Override
    public int indexOf(Object o) {
        if (size > 0 && o.equals(singleObject)) {
            return 0;
        } else {
            return -1;
        }
    }


    @Override
    public int lastIndexOf(Object o) {
        if (size > 0 && o.equals(singleObject)) {
            return size-1;
        } else {
            return -1;
        }
    }


    @Override
    public ListIterator<T> listIterator() {
        return listIterator(0);
    }


    @Override
    public ListIterator<T> listIterator(final int index) {
        return new ListIterator<T>() {
            private int position = index;

            @Override
            public boolean hasNext() {
                return position < size;
            }


            @Override
            public T next() {
                if (!hasNext()) {
                    throw new NoSuchElementException("End of the List was reached");
                }
                T result = singleObject;
                position++;
                return result;
            }


            @Override
            public boolean hasPrevious() {
                return size > 0 && position > 0;
            }


            @Override
            public T previous() {
                if (!hasPrevious()) {
                    throw new NoSuchElementException("Start of the List was reached");
                }
                position--;
                T result = singleObject;
                return result;
            }


            @Override
            public int nextIndex() {
                return position + 1;
            }


            @Override
            public int previousIndex() {
                return position - 1;
            }


            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }


            @Override
            public void set(T t) {
                throw new UnsupportedOperationException();
            }


            @Override
            public void add(T t) {
                throw new UnsupportedOperationException();
            }
        };
    }


    @Override
    public List<T> subList(int fromIndex, int toIndex) {
        if (fromIndex < 0) {
            throw new IndexOutOfBoundsException("Invalid fromIndex " + fromIndex);
        }
        if (toIndex > size) {
            throw new IndexOutOfBoundsException("Invalid toIndex " + toIndex + ". Maximum is " + size);
        }
        if (fromIndex > toIndex) {
            throw new IndexOutOfBoundsException("The fromIndex (currently "+fromIndex+") must be less or equals to toIndex (currently " + toIndex + ")");
        }
        return new SameObjectList<T>(singleObject, toIndex - fromIndex);
    }
}
