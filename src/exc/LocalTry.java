package exc;

import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;

public abstract class LocalTry<T> {

    private final boolean success;

    public LocalTry(boolean success) {
        this.success = success;
    }

    public boolean isSuccess() {
        return this.success;
    }

    public boolean isFailure() {
        return !this.success;
    }

    public abstract Throwable getThrownMessage();

    public abstract T get();

    public abstract <U> LocalTry<U> map(Function<? super T, ? extends U> fn);

    public abstract <U> LocalTry<U> flatMap(Function<? super T, LocalTry<U>> fn);

    static <T> LocalTry<T> failure(Throwable t) {
        Objects.requireNonNull(t);
        return new Failure<>(t);
    }

    static <V> LocalTry.Success<V> success(V value) {
        Objects.requireNonNull(value);
        return new Success<>(value);
    }

    public static <T> LocalTry<T> of(Supplier<T> fn) {
        Objects.requireNonNull(fn);
        try {
            return LocalTry.success(fn.get());
        }
        catch (Throwable t) {
            return LocalTry.failure(t);
        }
    }

    static class Failure<T> extends LocalTry<T> {

        private final RuntimeException exception;

        public Failure(Throwable t) {
            super(false);
            this.exception = new RuntimeException(t);
        }

        @Override
        public T get() {
            throw this.exception;
        }

        @Override
        public <U> LocalTry<U> map(Function<? super T, ? extends U> fn) {
            Objects.requireNonNull(fn);
            return LocalTry.failure(this.exception);
        }

        @Override
        public <U> LocalTry<U> flatMap(Function<? super T, LocalTry<U>> fn) {
            Objects.requireNonNull(fn);
            return LocalTry.failure(this.exception);
        }

        @Override
        public Throwable getThrownMessage() {
            return this.exception;
        }
    }

    static class Success<T> extends LocalTry<T> {

        private final T value;

        public Success(T value) {
            super(true);
            this.value = value;
        }

        @Override
        public T get() {
            return this.value;
        }

        @Override
        public <U> LocalTry<U> map(Function<? super T, ? extends U> fn) {
            Objects.requireNonNull(fn);
            try {
                return LocalTry.success(fn.apply(this.value));
            }
            catch (Throwable t) {
                return LocalTry.failure(t);
            }
        }

        @Override
        public <U> LocalTry<U> flatMap(Function<? super T, LocalTry<U>> fn) {
            Objects.requireNonNull(fn);
            try {
                return fn.apply(this.value);
            }
            catch (Throwable t) {
                return LocalTry.failure(t);
            }
        }

        @Override
        public Throwable getThrownMessage() {
            throw new IllegalStateException("Success never has an exception");
        }
    }
}
