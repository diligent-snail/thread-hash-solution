package org.diligentsnail.threadhash;

import java.util.Optional;
import java.util.Scanner;

public class Main {
	public static final String INPUT_PROMPT = "Введите число нулей справа (от 0 до 9) или 'exit', " +
			"чтобы выйти или 'x', чтобы прервать поток, если он запущен";

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		System.out.println(INPUT_PROMPT);
		FindHashThread thread = null;
		while (scanner.hasNext()) {
			String line = scanner.nextLine();
			if (line.equals("exit")) {
				System.out.println("До свидания");
				break;
			}
			if (line.equals("x")) {
				if (thread != null) {
					// Поток был запущен, прерываем
					thread.interrupt();
					// Ждём
					joinUninterruptibly(thread);
					// После этой строки гарантировано, что фоновый поток сказал, что его прервали или написал число
				} else {
					System.out.println("Поток ещё не запущен");
					System.out.println(INPUT_PROMPT);
					continue;
				}
			}

			// Смотрим, что ввели
			Optional<Integer> integer = tryParse(line);
			// Ввели не число
			if (integer.isEmpty()) {
				if ("x".equals(line)) {
					// Был поток, ввели 'x', но поток всё равно успел досчитать.
					// Здесь мы оказались после interrupt + join
					if (thread.hasFoundNumber()) {
						// Поток успел найти число и выписать в консоль.
						// Значит, мы игнорируем то, что ввёл пользователь
						System.out.println("Запрос на прерывание проигнорирован");
					}
					thread = null;
				} else {
					System.out.println(INPUT_PROMPT);
				}
			} else {
				if (thread != null) {
					// synchronized, чтобы сделать атомарной проверку hasFoundNumber + вывод в консоль

					// noinspection SynchronizationOnLocalVariableOrMethodParameter
					synchronized (thread) {
						// Мы взяли блокировку.
						// Фоновый поток в одном из состояний:
						// 1. Ищет число
						// 2. Нашёл число, но не записал, висит перед своим блоком synchronized (this)
						// 3. Нашёл число и записал его
						Assert.state(!thread.isInterrupted(), () -> "thread must be uninterrupted there");
						if (!thread.hasFoundNumber()) {
							// Случаи 1 и 2.
							// В первом, фоновый поток продолжит искать число, а мы попросим ввести 'x', чтобы прервать
							// Во втором - мы сначала скажем, что поток не завершил работу, а потом, когда отпустил блокировку,
							// фоновый поток скажет, что нашёл число
							System.out.println("Поток не завершил работу, введите 'x', чтобы прервать или 'exit', чтобы выйти");
							// Заново в цикл
							continue;
						}
					}
				}

				System.out.println("Ок запускаю поток");
				thread = new FindHashThread(integer.get());
				thread.start();
			}
		}

		if (thread != null) {
			// Был запущен поток, возможно, уже остановленный.
			// Всё равно прерываем, ждём и выходим
			thread.interrupt();
			joinUninterruptibly(thread);
		}
	}

	private static Optional<Integer> tryParse(String string) {
		try {
			int value = Integer.parseInt(string);
			if (value < 0 || value > HashUtils.MAX_TRAILING_ZEROES) {
				return Optional.empty();
			}
			return Optional.of(value);
		} catch (NumberFormatException e) {
			return Optional.empty();
		}
	}

	private static void joinUninterruptibly(FindHashThread thread) {
		while (true) {
			try {
				thread.join();
				return;
			} catch (InterruptedException ignore) {
			}
		}
	}
}
