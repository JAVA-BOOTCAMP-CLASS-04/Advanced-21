import java.util.Optional;
import java.util.function.Function;
import java.util.function.UnaryOperator;

public class RecursionLambdas {

	UnaryOperator<Long> fact = x -> x == 0 ? 1 : x * this.fact.apply(x - 1);

	static UnaryOperator<Long> fact2 = x -> x == 0 ? 1 : x * RecursionLambdas.fact2.apply(x - 1);

	UnaryOperator<Long> fact3 = value -> Optional.ofNullable(value)
			.map(v -> Optional.of(v)
							.filter(v1 -> v1 >= 0)
							.map(v1 -> Optional.of(v1)
									.filter(v2 -> v2 > 1)
									.map(v2 -> v2 * this.fact3.apply(v2 - 1))
									.orElse(1L))
							.orElseThrow(() -> new RuntimeException("Invalid value!")))
			.orElseThrow(() -> new RuntimeException("Null value exception!"));


	public void prueba() {
		System.out.println(fact.apply(10L));
		System.out.println(fact2.apply(10L));
		System.out.println(fact3.apply(10L));
		try {
			System.out.println(fact3.apply(null));
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

		try {
			System.out.println(fact3.apply(-1L));

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

	}
	
	public static void main(String[] args) {

		new RecursionLambdas().prueba();

	}

}
