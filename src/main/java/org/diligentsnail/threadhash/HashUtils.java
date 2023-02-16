package org.diligentsnail.threadhash;

public abstract class HashUtils {
	public static final int MAX_TRAILING_ZEROES = Integer.toString(Integer.MAX_VALUE).length() - 1;

	private HashUtils() {
	}

	/**
	 * Проверяет, содержит ли число {@code number} {@code trailingZeroes} нулей справа в десятичной системе
	 *
	 * @param number         для проверки
	 * @param trailingZeroes число нулей справа
	 * @return {@code true}, если число {@code number} содержит {@code trailingZeroes} нулей справа, иначе - {@code false}
	 */
	public static boolean hasTrailingZeroes(int number, int trailingZeroes) {
		Assert.isTrue(trailingZeroes >= 0,
				() -> "trailingZeroes must be >= 0 but was = '" + trailingZeroes + "'");
		Assert.isTrue(trailingZeroes <= MAX_TRAILING_ZEROES,
				() -> "trailingZeroes must be <= " + MAX_TRAILING_ZEROES + " but was = '" + trailingZeroes + "'");

		int pow = (int) Math.pow(10, trailingZeroes);
		return number % pow == 0;
	}

}
