package org.root.streamEx;

@FunctionalInterface
interface Supplier<T> {
    T supply() throws Exception;
}

//@FunctionalInterface
//interface FunctionEx<T, R> {
//    R apply(T t) throws Exception;
//}
//static <T, R> Function<T, R> execute(FunctionEx<T, R> function) {
//    return t -> {
//        try {
//            return function.apply(t);
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//    };
//}