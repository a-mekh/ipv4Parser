package com.example.ip4counter.parser.impl;

import com.example.ip4counter.parser.IpParser;

/**
 * Utility class for parsing IPv4 addresses from raw byte arrays.
 *
 * <p>An IPv4 address like "192.168.1.1" is parsed from ASCII-encoded bytes
 * and converted into a 32-bit integer (e.g., 3232235777).</p>
 *
 * <p>This is useful for compact storage and fast comparison.</p>
 */
public class IpV4Parser implements IpParser {

    private static final byte ASCII_ZERO = '0';
    private static final byte DOT_DELIMITER = '.';
    private static final int DECIMAL_BASE = 10;
    private static final int BITS_PER_OCTET = 8;

    /**
     * Parses an IPv4 address from a byte array. The address is assumed to be in standard dotted decimal format.
     * The method converts the byte array to a 32-bit integer representation of the IPv4 address.
     *
     * @param bytes the byte array containing the IP address in dotted decimal format
     * @param start the starting index of the IP address in the byte array
     * @param end   the ending index of the IP address (exclusive) in the byte array
     * @return the 32-bit integer representation of the IPv4 address
     */
    public int parse(byte[] bytes, int start, int end) {
        int result = 0;
        int part = 0;

        for (int i = start; i <= end; i++) {
            byte b = bytes[i];

            if (b == DOT_DELIMITER) {
                result = (result << BITS_PER_OCTET) | part;
                part = 0;
            } else {
                part = part * DECIMAL_BASE + (b - ASCII_ZERO);
            }
        }

        return ((result << Byte.SIZE) | part);
    }
}
