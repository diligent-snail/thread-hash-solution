package org.diligentsnail.threadhash;

import org.junit.jupiter.api.Test;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertTimeoutPreemptively;

class FindHashThreadTest {

	@Test
	void interruptRespected() {
		FindHashThread findHashThread = new FindHashThread(HashUtils.MAX_TRAILING_ZEROES);
		findHashThread.start();
		findHashThread.interrupt();

		assertTimeoutPreemptively(Duration.ofSeconds(5), () -> findHashThread.join());
	}
}
