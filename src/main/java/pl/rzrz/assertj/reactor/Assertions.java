package pl.rzrz.assertj.reactor;

import org.reactivestreams.Publisher;

public class Assertions extends org.assertj.core.api.Assertions {

    /**
     * Create an assert instance for a publisher.
     *
     * This method immediately subscribes to the received publisher
     * and blocks until there is an error or a complete signal.
     *
     * @param publisher the publisher to assert on
     * @param <T> type of published items
     */
    public static <T> PublisherAssert<T> assertThat(Publisher<T> publisher) {
        return new PublisherAssert<>(publisher);
    }
}
