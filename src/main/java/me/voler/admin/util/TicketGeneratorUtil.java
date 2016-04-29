package me.voler.admin.util;

import java.security.SecureRandom;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @see org.jasig.cas.util.DefaultUniqueTicketIdGenerator
 * @see org.jasig.cas.util.DefaultUniqueTicketIdGenerator
 * @see org.jasig.cas.util.DefaultRandomStringGenerator
 */
public class TicketGeneratorUtil {

	private AtomicLong count;
	private int maxLength;
	/** The array of printable characters to be used in our random string. */
	private static final char[] PRINTABLE_CHARACTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ012345679"
			.toCharArray();

	public TicketGeneratorUtil(int maxLength) {
		this.count = new AtomicLong(1);
		this.maxLength = maxLength;
	}

	public String getNewTicket(String prefix) {
		StringBuilder buffer = new StringBuilder();

		buffer.append(prefix);
		buffer.append("-");
		buffer.append(this.getNextValue());
		buffer.append("-");
		buffer.append(this.getNewString());

		return buffer.toString();
	}

	private String getNextValue() {
		if (this.count.compareAndSet(Long.MAX_VALUE, 0)) {
			return Long.toString(Long.MAX_VALUE);
		}
		return Long.toString(this.count.getAndIncrement());
	}

	private String getNewString() {
		byte[] random = new byte[this.maxLength];
		new SecureRandom().nextBytes(random);

		char[] output = new char[random.length];
		for (int i = 0; i < random.length; i++) {
			int index = Math.abs(random[i] % PRINTABLE_CHARACTERS.length);
			output[i] = PRINTABLE_CHARACTERS[index];
		}

		return new String(output);
	}

}
