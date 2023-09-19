import java.util.function.BiFunction;
import java.util.function.Function;

public class CurryingSample2 {

	public static void main(String[] args) {

		/**
		 * Implementamos la suma de 2 Longs con una BIFunction (recibe 2 parametros)
		 */
		BiFunction<Long, Long, Long> biFunctionSuma = new BiFunction<Long, Long, Long>() {

			@Override
			public Long apply(Long t, Long u) {
				return t + u;
			}
		};
		
		System.out.println("BI-FUNCTION [10 + 20] = " + biFunctionSuma.apply(10L, 20L));
		
		biFunctionSuma = (x, y) -> x + y;

		System.out.println("BI-FUNCTION-LAMBDA [10 + 20] = " + biFunctionSuma.apply(10L, 20L));
		
		/**
		 * Pero tambien podemos implementarlo con Function de la siguiente manera
		 */
		
		Function<Long, Function<Long, Long>> functionSuma = new Function<Long, Function<Long, Long>>() {

			@Override
			public Function<Long, Long> apply(Long t) {
				return new Function<Long, Long>() {

					@Override
					public Long apply(Long u) {
						// TODO Auto-generated method stub
						return t + u;
					}
					
				};
			}
		};

		System.out.println("FUNCTION [10 + 20] = " + functionSuma.apply(10L).apply(20L));
		
		functionSuma = x -> y -> x + y;

		System.out.println("FUNCTION-LAMBDA [10 + 20] = " + functionSuma.apply(10L).apply(20L));
		
		/**
		 * f(x, y, z) = x + 2y + 3z
		 */
		
		Function<Long, Function<Long, Function<Long, Long>>> func = new Function<Long, Function<Long, Function<Long, Long>>>() {

			@Override
			public Function<Long, Function<Long, Long>> apply(Long x) {
				return new Function<Long, Function<Long, Long>>() {

					@Override
					public Function<Long, Long> apply(Long y) {
						return new Function<Long, Long>() {

							@Override
							public Long apply(Long z) {
								return x + 2 * y + 3 * z;
							}
							
						};
					}
					
				};
			}
		
		};

		System.out.println("FUNCTION [10 + 2 * 20 + 3 * 30] = " + func.apply(10L).apply(20L).apply(30L));
		
		func = x -> y -> z -> x + 2 * y + 3 * z;

		System.out.println("FUNCTION-LAMBDA [10 + 2 * 20 + 3 * 30] = " + func.apply(10L).apply(20L).apply(30L));
	
	}

}
