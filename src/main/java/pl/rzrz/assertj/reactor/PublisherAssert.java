package pl.rzrz.assertj.reactor;

import org.assertj.core.api.AbstractAssert;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class PublisherAssert<T> extends AbstractAssert<PublisherAssert<T>, Publisher<T>> {

    private final PublisherResult<T> result;

    public PublisherAssert(Publisher<T> publisher) {
        super(publisher, PublisherAssert.class);
        this.result = consumeItems(publisher);
    }

    private static <T> PublisherResult<T> consumeItems(Publisher<T> publisher) {
        List<T> items = new ArrayList<>();
        try {
            Flux.from(publisher).toIterable().forEach(items::add);
            return PublisherResult.completed(items);
        } catch (Throwable error) {
            return PublisherResult.error(error, items);
        }
    }

    public PublisherAssert<T> sendsError() {
        return sendsError(__ -> {
        });
    }

    public PublisherAssert<T> sendsError(Consumer<Throwable> assertions) {
        if (result.completed()) {
            failWithMessage("Publisher completed without an error.");
        }
        assertions.accept(result.getError());
        return this;
    }

    public PublisherAssert<T> completes() {
        if (result.hasError()) {
            failWithMessage("Publisher sent an error: %s", result.getError());
        }
        return this;
    }

    public PublisherAssert<T> emitsCount(int expectedCount) {
        completes();
        int actualCount = result.getItems().size();
        if (actualCount != expectedCount) {
            failWithActualExpectedAndMessage(actualCount, expectedCount,
                    "Expected count %d but was %d", expectedCount, actualCount);
        }
        return this;
    }

    public PublisherAssert<T> emits(T expectedItem) {
        completes();
        if (!result.getItems().contains(expectedItem)) {
            failWithActualExpectedAndMessage(result.getItems(), expectedItem,
                    "Expected %s to contain %s", result.getItems(), expectedItem
            );
        }
        return this;
    }
}
