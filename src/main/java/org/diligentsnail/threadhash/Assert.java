package org.diligentsnail.threadhash;

import java.util.function.Supplier;

/**
 * Утилитный класс с методами для проверок
 */
public abstract class Assert {
	private Assert() {
	}

	/**
	 * Проверить состояние, кинуть {@link IllegalStateException}, если {@code state == false}
	 *
	 * @param state           состояние для проверки
	 * @param messageSupplier {@link Supplier} сообщения для исключения
	 * @throws IllegalStateException если {@code state == null}
	 */
	public static void state(boolean state, Supplier<String> messageSupplier) {
		if (!state) {
			throw new IllegalStateException(nullSafeGet(messageSupplier));
		}
	}

	/**
	 * Кинуть {@link IllegalArgumentException}, если {@code condition == false}
	 *
	 * @param condition       условия для проверки
	 * @param messageSupplier {@link Supplier} сообщения для исключения
	 * @throws IllegalArgumentException если {@code condition == null}
	 */
	public static void isTrue(boolean condition, Supplier<String> messageSupplier) {
		if (!condition) {
			throw new IllegalArgumentException(nullSafeGet(messageSupplier));
		}
	}

	private static String nullSafeGet(Supplier<String> supplier) {
		if (supplier == null) {
			return null;
		}
		return supplier.get();
	}
}
