package pl.rzrz.assertj.reactor;

import org.assertj.core.api.AbstractAssert;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
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

    @SafeVarargs
    public final PublisherAssert<T> emitsExactly(T... expectedItems) {
        return emitsExactly(Arrays.asList(expectedItems));
    }

    public PublisherAssert<T> emitsItems(Consumer<List<T>> assertions) {
        if (result.hasError()) {
            failWithMessage("Publisher sent an error: %s", result.getError());
        }
        assertions.accept(result.getItems());
        return this;
    }

    private PublisherAssert<T> emitsExactly(List<T> expectedItems) {

        if (result.getItems().size() != expectedItems.size()) {
            failWithActualExpectedAndMessage(result.getItems(), expectedItems,
                "Expected %s to contain %d items", result.getItems(), expectedItems.size()
            );
        }

        Set<T> actualUniqueItems = new HashSet<>(result.getItems());
        Set<T> expectedUniqueItems = new HashSet<>(expectedItems);

        Set<T> unexpectedItems = difference(actualUniqueItems, expectedUniqueItems);
        Set<T> missingItems = difference(expectedUniqueItems, actualUniqueItems);

        if (unexpectedItems.size() + missingItems.size() > 0) {
            failWithActualExpectedAndMessage(result.getItems(),
                expectedItems,
                "Expected %s to contain different items. Unexpected: %s, missing: %s",
                result.getItems(),
                unexpectedItems,
                missingItems
            );
        }

        for (int i = 0; i < expectedItems.size(); i++) {
            T actual = result.getItems().get(i);
            T expected = expectedItems.get(i);
            if (!Objects.equals(actual, expected)) {
                failWithActualExpectedAndMessage(result.getItems(),
                    expectedItems,
                    "Items in %s were expected in different order. Expected %s at index %d, found %s",
                    result.getItems(),
                    expected,
                    i,
                    actual);
            }
        }

        return this;
    }

    private Set<T> difference(Set<T> minuend, Set<T> subtrahend) {
        Set<T> set = new HashSet<>(minuend);
        set.removeAll(subtrahend);
        return set;
    }
}
