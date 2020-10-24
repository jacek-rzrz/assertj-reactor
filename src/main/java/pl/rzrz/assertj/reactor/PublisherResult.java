package pl.rzrz.assertj.reactor;

import java.util.List;

public class PublisherResult<T> {

    private final List<T> items;

    private final Throwable error;

    private PublisherResult(List<T> items, Throwable error) {
        this.items = items;
        this.error = error;
    }

    public static <T> PublisherResult<T> completed(List<T> items) {
        return new PublisherResult<>(items, null);
    }

    public static <T> PublisherResult<T> error(Throwable error, List<T> items) {
        return new PublisherResult<>(items, error);
    }

    public boolean completed() {
        return !hasError();
    }

    public boolean hasError() {
        return error != null;
    }

    public Throwable getError() {
        return error;
    }

    public List<T> getItems() {
        return items;
    }
}
