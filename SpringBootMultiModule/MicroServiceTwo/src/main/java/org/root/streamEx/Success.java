package org.root.streamEx;

public final class Success<T> extends Try<T> {
    private final T t;

    public Success(T t) {
        this.t = t;
    }

    public T value() {
        return t;
    }

}
