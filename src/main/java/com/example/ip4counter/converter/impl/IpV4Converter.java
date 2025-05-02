package com.example.ip4counter.converter.impl;

import com.example.ip4counter.converter.IpConverter;

/**
 * Utility class for parsing IPv4 addresses from raw byte arrays.
 *
 * <p>An IPv4 address like "192.168.1.1" is parsed from ASCII-encoded bytes
 * and converted into a 32-bit integer (e.g., 3232235777).</p>
 *
 * <p>This is useful for compact storage and fast comparison.</p>
 */
public class IpV4Converter implements IpConverter {

    private static final int ASCII_ZERO = '0';
    private static final int ASCII_NINE = '9';
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
    public long parse(byte[] bytes, int start, int end) {
        long result = 0;
        long part = 0;
        int dots = 0;

        for (int i = start; i < end; i++) {
            byte b = bytes[i];

            // Ignore the symbols of the new line and the return of the carriage
            if (b == '\n' || b == '\r') {
                continue;
            }

            if (b == DOT_DELIMITER) {
                if (part < 0 || part > 255) {
                    throw new IllegalArgumentException("Octet out of range: " + part);
                }
                result = (result << BITS_PER_OCTET) | part;
                part = 0;
                dots++;
            } else if (b >= ASCII_ZERO && b <= ASCII_NINE) {
                part = part * DECIMAL_BASE + (b - ASCII_ZERO);
                if (part > 255) {
                    throw new IllegalArgumentException("Octet value exceeds 255");
                }
            } else {
                throw new IllegalArgumentException("Invalid character in IP: " + (char)b);
            }
        }

        // Last octet validation
        if (dots != 3 || part < 0 || part > 255) {
            throw new IllegalArgumentException("Invalid IP format");
        }

        return (result << BITS_PER_OCTET) | part;
    }
}
