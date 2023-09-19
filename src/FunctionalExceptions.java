import exc.LocalTry;
import io.vavr.control.Either;
import io.vavr.control.Try;

import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

public class FunctionalExceptions {

    private final Format dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

    private void pruebaTryCatch() {

        Optional.of("10/01/2023 23:20:20")
                .map(source -> {
                    try {
                        return dateFormat.parseObject(source);
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }
                })
                .map(dateFormat::format)
                .ifPresent(System.out::println);

    }

    @FunctionalInterface
    interface ExFunction<T, R, E extends Exception> {
        R apply(T t) throws Exception;
    }

    <T, R, E extends Exception> Function<T, R> uncheck(ExFunction<T, R, E> fn) {
        return t -> {
            try {
                return fn.apply(t);
            }
            catch (Exception e) {
                throw new RuntimeException(e);
            }
        };
    }


    private void pruebaUncheck() {
        Optional.of("10/01/2023 23:20:20")
                .map(uncheck(dateFormat::parseObject))
                .map(dateFormat::format)
                .ifPresent(System.out::println);
    }

    private void pruebaTry() {
        System.out.println(Optional.ofNullable("10")
                .map(v -> LocalTry.of(() -> Integer.parseInt(v)))
                .filter(LocalTry::isSuccess)
                .map(LocalTry::get)
                .orElseThrow(() -> new RuntimeException("El formato numerico no es valido")));

    }

    private void pruebaVavr() {
        Stream.of("10", "a", "13")
                .forEach(value -> Try.of(() -> Integer.parseInt(value))
                                        .onSuccess(v -> System.out.println("El valor " + v + " es entero"))
                                        .onFailure(e -> System.out.println(e.getMessage())));
    }

    private void pruebaEither() {

        Function<String, Either<Exception, Integer>> eitherParse = v -> {
                try {
                    return Either.right(Integer.parseInt(v));
                } catch (Exception e) {
                    return Either.left(e);
                }

            };

        Stream.of("10", "a", "13")
                .map(eitherParse)
                .map(e -> e.fold(exc -> exc.getMessage(), r -> r.toString()))
                .forEach(v -> System.out.println(v));



    }

    public static void main(String[] args) {
        FunctionalExceptions fe = new FunctionalExceptions();

        fe.pruebaTryCatch();

        fe.pruebaUncheck();

        fe.pruebaTry();

        fe.pruebaVavr();

        fe.pruebaEither();
    }
}
