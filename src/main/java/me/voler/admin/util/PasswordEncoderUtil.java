package me.voler.admin.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @see org.jasig.cas.authentication.handler.DefaultPasswordEncoder
 *
 */
public class PasswordEncoderUtil {
	private static final char[] HEX_DIGITS = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd',
			'e', 'f' };

	private final String encodingAlgorithm;

	public PasswordEncoderUtil(final String encodingAlgorithm) {
		this.encodingAlgorithm = encodingAlgorithm;
	}

	public String encode(final String password) {
		if (password == null) {
			return null;
		}

		try {
			MessageDigest messageDigest = MessageDigest.getInstance(this.encodingAlgorithm);

			messageDigest.update(password.getBytes());
			final byte[] digest = messageDigest.digest();

			return getFormattedText(digest);
		} catch (final NoSuchAlgorithmException e) {
			throw new SecurityException(e);
		}
	}

	private String getFormattedText(final byte[] bytes) {
		final StringBuilder buf = new StringBuilder(bytes.length * 2);

		for (int j = 0; j < bytes.length; j++) {
			buf.append(HEX_DIGITS[(bytes[j] >> 4) & 0x0f]);
			buf.append(HEX_DIGITS[bytes[j] & 0x0f]);
		}
		return buf.toString();
	}

}
