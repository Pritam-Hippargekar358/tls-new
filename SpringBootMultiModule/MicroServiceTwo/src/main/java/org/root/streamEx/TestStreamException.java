package org.root.streamEx;

import java.util.List;

public class TestStreamException {


//    There are two possible outcomes:
//- if there is am exception and it is important that the whole stream must be processed atomically, then we stop executing and re-throwing exception is an appropriate approach. We don’t care about the previously processed elements and rollback.
//- if there is an exception and it is important to process the whole stream till the end, then re-throwing the exception is not an appropriate approach. We need to continue processing the stream till the end. Next we will talk about how to deal with this problem.
    public static void main(String[] args) {
        TestStreamException test =new TestStreamException();
        var ids = List.of(1, 7, 4, 2);
        var resultStream = ids.stream()
                .map(id -> Try.apply(() -> test.findUser(id)))
                .toList();

        // Filtering successful tries
        var userStream = resultStream
                .stream().filter(result -> result instanceof Success<User>);

        // Filtering exceptional tries
        var errorStream = resultStream
                .stream().filter(result -> result instanceof Failure<User>);

        // Printing users retrieved
        userStream
                .forEach(userSuccess -> System.out.println(((Success<User>) userSuccess).value()));

        // Printing errors
//        errorStream
//                .forEach(userFailure -> {
//                            String message = ((Failure<User>) userFailure).value().getMessage();
//                            System.out.println("Exception: " + message);
//                        }
//                );

        resultStream.forEach(
                result -> {
                    switch (result) {
                        case Success<User> success -> System.out.println("Success: " + success.value());
                        case Failure<User> failure -> System.out.println("Exception: " + failure.value().getMessage());
                    }
                }
        );
    }

    private final List<User> users = List.of(
            new User(1, "baggio"),
            new User(2, "dave"),
            new User(3, "wayne"),
            new User(4, "bruce")
    );

    public User findUser(Integer id) throws InterruptedException {
        // Imitate  a network call
        Thread.sleep(100);
        return users.stream()
                .filter(user -> user.getId() == id)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("service.User not found with id: " + id));
    }
}
