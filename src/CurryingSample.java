import java.util.function.Function;
import java.util.stream.Stream;

public class CurryingSample {

    public interface SumaFunction<A> {
         A suma(A a, A b, A c) ;
    }

    public int suma(int a, int b, int c) {
        return a + b + c;
    }

    public void prueba() {
        System.out.println(suma(1, 2, 3));

        SumaFunction<Integer> s = (a, b, c) -> a + b + c;

        System.out.println(s.suma(1, 2, 3));

        Function<Integer, Function<Integer, Function<Integer, Integer>>> curry = new Function<Integer, Function<Integer, Function<Integer, Integer>>>() {
            @Override
            public Function<Integer, Function<Integer, Integer>> apply(Integer a) {
                return new Function<Integer, Function<Integer, Integer>>() {
                    @Override
                    public Function<Integer, Integer> apply(Integer b) {
                        return new Function<Integer, Integer>() {
                            @Override
                            public Integer apply(Integer c) {
                                return a + b + c;
                            }
                        };
                    }
                };
            }
        };

        System.out.println(curry.apply(1).apply(2).apply(3));

        Function<Integer, Function<Integer, Function<Integer, Integer>>> curry2 = a -> b -> c -> a + b + c;

        System.out.println(curry2.apply(1).apply(2).apply(3));

    }

    public static void main(String[] args) {

        new CurryingSample().prueba();

    }
}
