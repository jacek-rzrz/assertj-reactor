# assertj-reactor
AssertJ extensions for Mono and Flux.

## Getting started
Assertions extend from AssertJ core 
so a single import statement is enough
to get both standard AssertJ methods as well
as the reactive additions:

```
import static pl.rzrz.assertj.reactor.Assertions.assertThat;
```

### Check for an error signal
```
Mono<Void> mono = Mono.error(new Exception());

assertThat(mono).sendsError();
```

Inspect the exception:
```
Mono<Void> mono = Mono.error(new RuntimeException("oops"));

assertThat(mono).sendsError(error -> {
    assertThat(error).isInstanceOf(RuntimeException.class);
    assertThat(error).hasMessage("oops");
});
```

### Check successful completion
```
Mono<Void> mono = Mono.just("this");

assertThat(mono).completes();
```

### Count items
```
Flux<String> flux = Flux.just("one", "two", "three");

assertThat(flux).sendsItems(3);
```

