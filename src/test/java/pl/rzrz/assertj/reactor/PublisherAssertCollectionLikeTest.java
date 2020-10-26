package pl.rzrz.assertj.reactor;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static pl.rzrz.assertj.reactor.Assertions.assertThat;
import static pl.rzrz.assertj.reactor.TestPublishers.completePublisher;
import static pl.rzrz.assertj.reactor.TestPublishers.errorPublisher;

public class PublisherAssertCollectionLikeTest {

    @Test
    public void emitsCount_whenError_failsAssertion() {
        assertThrows(AssertionError.class, () -> {
            assertThat(errorPublisher(2)).emitsCount(2);
        });
    }

    @Test
    public void emitsCount_whenDifferentNumberOfItems_failsAssertion() {
        assertThrows(AssertionError.class, () -> {
            assertThat(completePublisher(5)).emitsCount(2);
        });
    }

    @Test
    public void emitsCount_whenSameNumberOfItems_passesAssertion() {
        assertThat(completePublisher(2)).emitsCount(2);
    }

    @Test
    public void emits_whenError_failsAssertion() {
        assertThrows(AssertionError.class, () -> {
            assertThat(errorPublisher(2)).emits("two");
        });
    }

    @Test
    public void emits_whenItemEmitted_passesAssertion() {
        assertThat(completePublisher("one", "two", "three")).emits("two");
    }

    @Test
    public void emits_whenItemNotEmitted_failsAssertion() {
        assertThatThrownBy(() ->
                assertThat(completePublisher("one", "two")).emits("three")
        )
                .isInstanceOf(AssertionError.class)
                .hasMessage("Expected [one, two] to contain three");
    }
}
