package org.diligentsnail.threadhash;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class HashUtilsTest {

	@ParameterizedTest
	@MethodSource("getInputsForHavingZeroes")
	void haveZeroes(int number, int trailingZeroes) {
		Assertions.assertTrue(HashUtils.hasTrailingZeroes(number, trailingZeroes));
	}

	public static Stream<Arguments> getInputsForHavingZeroes() {
		return Stream.of(
				Arguments.of(10, 1),
				Arguments.of(10, 0),
				Arguments.of(100, 1),
				Arguments.of(100, 2),
				Arguments.of(1000, 3),
				Arguments.of(2000000000, 9),
				Arguments.of(-2000000000, 9),
				Arguments.of(9, 0),
				Arguments.of(-10, 1),
				Arguments.of(-10, 0),
				Arguments.of(-1000, 3)
		);
	}

	@ParameterizedTest
	@MethodSource("getInputsForNotHavingZeroes")
	void doNotHaveZero(int number, int trailingZeroes) {
		assertFalse(HashUtils.hasTrailingZeroes(number, trailingZeroes));
	}

	public static Stream<Arguments> getInputsForNotHavingZeroes() {
		return Stream.of(
				Arguments.of(1, 1),
				Arguments.of(90, 2),
				Arguments.of(900, 3),
				Arguments.of(2100000000, 9)
		);
	}

	@ParameterizedTest
	@ValueSource(ints = {-1, 10, 11, Integer.MAX_VALUE, Integer.MIN_VALUE})
	void throwsForInvalidTrailingZeroes(int trailingZeroes) {
		int ignored = 10;
		assertThrows(IllegalArgumentException.class, () -> HashUtils.hasTrailingZeroes(ignored, trailingZeroes));
	}

	@Test
	void zeroDoesCount() {
		assertTrue(HashUtils.hasTrailingZeroes(0, 9));
	}
}
