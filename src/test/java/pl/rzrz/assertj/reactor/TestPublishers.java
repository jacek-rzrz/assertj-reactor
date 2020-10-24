package pl.rzrz.assertj.reactor;

import org.reactivestreams.Publisher;
import reactor.test.publisher.TestPublisher;

public class TestPublishers {

    public static Publisher<String> errorPublisher() {
        return errorPublisher(new Exception());
    }

    public static Publisher<String> errorPublisher(int itemCount) {
        return errorPublisher(itemCount, new Exception());
    }

    public static Publisher<String> errorPublisher(Throwable error) {
        return errorPublisher(2, error);
    }

    public static Publisher<String> errorPublisher(int itemCount, Throwable error) {
        return publishElements(itemCount).error(error);
    }

    public static Publisher<String> completePublisher() {
        return completePublisher(2);
    }

    public static Publisher<String> completePublisher(int itemCount) {
        return publishElements(itemCount).complete();
    }

    private static TestPublisher<String> publishElements(int count) {
        TestPublisher<String> publisher = TestPublisher.createCold();
        for (int i = 0; i < count; i++) {
            publisher.next(i + "");
        }
        return publisher;
    }
}
