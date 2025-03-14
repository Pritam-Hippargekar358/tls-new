package org.root.streamEx;


sealed public abstract class Try<T> permits Success, Failure {

    static <T> Try<T> apply(Supplier<T> function) {
        try {
            var result = function.supply();
            return new Success<>(result);
        } catch (Exception ex) {
            return new Failure<>(ex);
        }
    }

}

