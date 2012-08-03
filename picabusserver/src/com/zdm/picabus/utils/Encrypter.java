package com.zdm.picabus.utils;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class Encrypter {

	/**
	 * Lower case Hex Digits.
	 */
	private static final String HEX_DIGITS = "0123456789abcdef";

	/**
	 * Byte mask.
	 */
	private static final int BYTE_MSK = 0xFF;

	/**
	 * Hex digit mask.
	 */
	private static final int HEX_DIGIT_MASK = 0xF;

	/**
	 * Number of bits per Hex digit (4).
	 */
	private static final int HEX_DIGIT_BITS = 4;

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		final String msgToEncrypt = "idanbachar";

		// encrypt msg with SHA1
		String encryptedMsg = computeSha1OfString(msgToEncrypt);
		System.out.println("****");
		System.out
				.println("Msg encrypted encrypted with SHA1: " + encryptedMsg);
		System.out.println("****");
		
		// encrypt msg with a secret key (can also be generated automatically
		String secretKey = "qnscAdgRlkIhAUPY44oiexBKtQbGY0orf7OV1I50";
		SecretKeySpec keySpec = new SecretKeySpec(secretKey.getBytes(),
				"HmacSHA1");

		Mac mac = null;
		try {
			mac = Mac.getInstance("HmacSHA1");
			mac.init(keySpec);
		} catch (NoSuchAlgorithmException e) {
			// TODO Handle errors
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			// TODO Handle errors
			e.printStackTrace();
		}

		byte[] result = mac.doFinal(msgToEncrypt.getBytes());

		System.out
				.println("Msg encrypted with HMACSHA1, with the following secret key ("
						+ secretKey + ") is:" + toHexString(result));
		System.out.println("****");
	}

	/**
	 * Compute the SHA-1 digest of a String and return the bytes in hexadecimal
	 * format.
	 * 
	 * @param message
	 *            the UTF-8 String to be encoded
	 * @return a SHA-1 hash
	 * @throws UnsupportedOperationException
	 *             in the case the VM doesn't support UTF-8 which could be
	 *             caused by a VM bug, you shouldn't bother catching this
	 *             exception
	 * @throws NullPointerException
	 *             if the String to be encoded is null
	 */
	public static String computeSha1OfString(final String message)
			throws UnsupportedOperationException, NullPointerException {
		try {
			return computeSha1OfByteArray(message.getBytes(("UTF-8")));
		} catch (UnsupportedEncodingException ex) {
			throw new UnsupportedOperationException(ex);
		}
	}

	/**
	 * Compute the SHA-1 digest of raw bytes and return the bytes in hexadecimal
	 * format.
	 * 
	 * @param message
	 *            the raw byte array to be encoded
	 * @return a SHA-1 hash
	 * @throws UnsupportedOperationException
	 *             in the case SHA-1 MessageDigest is not supported, you
	 *             shouldn't bother catching this exception
	 */
	private static String computeSha1OfByteArray(final byte[] message)
			throws UnsupportedOperationException {
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-1");
			md.update(message);
			byte[] res = md.digest();
			return toHexString(res);
		} catch (NoSuchAlgorithmException ex) {
			throw new UnsupportedOperationException(ex);
		}
	}

	/**
	 * Compute a String in HexDigit from the input.
	 * 
	 * @param byteArray
	 *            a row byte array
	 * @return a hex String
	 */
	private static String toHexString(final byte[] byteArray) {
		StringBuilder sb = new StringBuilder(byteArray.length * 2);
		for (int i = 0; i < byteArray.length; i++) {
			int b = byteArray[i] & BYTE_MSK;
			sb.append(HEX_DIGITS.charAt(b >>> HEX_DIGIT_BITS)).append(
					HEX_DIGITS.charAt(b & HEX_DIGIT_MASK));
		}
		return sb.toString();
	}
}
