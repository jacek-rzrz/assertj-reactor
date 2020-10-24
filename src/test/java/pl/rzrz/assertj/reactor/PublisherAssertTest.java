package pl.rzrz.assertj.reactor;

import org.junit.jupiter.api.Test;
import org.reactivestreams.Publisher;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static pl.rzrz.assertj.reactor.Assertions.assertThat;
import static pl.rzrz.assertj.reactor.TestPublishers.completePublisher;
import static pl.rzrz.assertj.reactor.TestPublishers.errorPublisher;

class PublisherAssertTest {

    @Test
    void sendsError_whenError_passesAssertion() {
        assertThat(errorPublisher()).sendsError();
    }

    @Test
    void sendsError_whenNoError_failsAssertion() {
        assertThrows(AssertionError.class, () ->
                assertThat(completePublisher()).sendsError()
        );
    }

    @Test
    void sendsError_whenConsumerDoesntThrow_passesAssertion() {
        Publisher<String> publisher = errorPublisher(new RuntimeException("whooops"));

        assertThat(publisher).sendsError(error -> {
            assertThat(error).isInstanceOf(RuntimeException.class);
            assertThat(error).hasMessage("whooops");
        });
    }

    @Test
    void sendsError_whenConsumerThrows_failsAssertion() {
        Publisher<String> publisher = errorPublisher(new RuntimeException("whooops"));

        assertThrows(AssertionError.class, () -> {
            assertThat(publisher).sendsError(error -> {
                assertThat(error).isInstanceOf(IllegalStateException.class);
            });
        });
    }

    @Test
    public void completes_whenError_failsAssertion() {
        assertThrows(AssertionError.class, () -> {
            assertThat(errorPublisher()).completes();
        });
    }

    @Test
    public void completes_whenElementsWithNoError_passesAssertion() {
        assertThat(completePublisher()).completes();
    }

    @Test
    public void sendsItems_whenError_failsAssertion() {
        assertThrows(AssertionError.class, () -> {
            assertThat(errorPublisher(2)).sendsItems(2);
        });
    }

    @Test
    public void sendsItems_whenDifferentNumberOfItems_failsAssertion() {
        assertThrows(AssertionError.class, () -> {
            assertThat(completePublisher(5)).sendsItems(2);
        });
    }

    @Test
    public void sendsItems_whenSameNumberOfItems_passesAssertion() {
        assertThat(completePublisher(2)).sendsItems(2);
    }
}
