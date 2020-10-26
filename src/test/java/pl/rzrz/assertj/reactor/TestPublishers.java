package pl.rzrz.assertj.reactor;

import org.reactivestreams.Publisher;
import reactor.test.publisher.TestPublisher;

import java.util.stream.IntStream;

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

    public static Publisher<String> completePublisher(String... items) {
        return publishElements(items).complete();
    }

    private static TestPublisher<String> publishElements(int count) {
        return publishElements(IntStream.range(0, count).mapToObj(i -> i+"").toArray(String[]::new));
    }

    private static TestPublisher<String> publishElements(String[] items) {
        TestPublisher<String> publisher = TestPublisher.createCold();
        for(String item : items) {
            publisher.next(item);
        }
        return publisher;
    }
}
