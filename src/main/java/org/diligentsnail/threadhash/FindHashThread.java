package org.diligentsnail.threadhash;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Поток ищет число, у которого справа будут {@link #numberOfZeroes} нулей справа
 */
public class FindHashThread extends Thread {
	/**
	 * Количество нулей справа, с которым нужно найти число
	 */
	private final int numberOfZeroes;
	/**
	 * Нашли мы число или нет
	 */
	private boolean foundNumber = false;

	public FindHashThread(int numberOfZeroes) {
		// Кинуть IllegalArgumentException, если параметр вне допустимого диапазона
		Assert.isTrue(numberOfZeroes >= 0 && numberOfZeroes <= HashUtils.MAX_TRAILING_ZEROES,
				() -> "numberOfZeroes must be >= 0 and <= " + HashUtils.MAX_TRAILING_ZEROES + " but was = " + numberOfZeroes);
		this.numberOfZeroes = numberOfZeroes;
	}

	@Override
	public void run() {
		// Пока нас не прервали
		while (!Thread.currentThread().isInterrupted()) {
			int l = ThreadLocalRandom.current().nextInt();
			if (HashUtils.hasTrailingZeroes(l, numberOfZeroes)) {
				// Захватываем блокировку, чтобы написать в консоль и выставить foundNumber атомарно.
				// Посмотрите комментарии, около synchronized (thread) в Main
				synchronized (this) {
					System.out.println("Найдено число: " + l);
					System.out.println(Main.INPUT_PROMPT);
					foundNumber = true;
				}
				break;
			}
		}

		if (Thread.interrupted() && !foundNumber) {
			System.out.println("Поиск числа прерван");
			System.out.println(Main.INPUT_PROMPT);
		}
	}

	public synchronized boolean hasFoundNumber() {
		return foundNumber;
	}
}
