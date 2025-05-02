package com.example.ip4counter;

import com.example.ip4counter.converter.IpConverter;
import com.example.ip4counter.converter.impl.IpV4Converter;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class IpConverterTest {

    private static final IpConverter parser = new IpV4Converter();

    /**
     * Unit test for parseIpFromBuffer.
     * <p>
     * We subtract 1 from the input string length to exclude the newline character '\n'.
     * This ensures we only pass the actual IP characters
     * to the parser, avoiding invalid characters that could break parsing.
     */
    @Test
    void testParseValidIp() {
        byte[] ip1 = "1.2.3.4\n".getBytes();
        byte[] ip2 = "192.168.1.2\n".getBytes();
        byte[] ip3 = "255.255.255.255\n".getBytes();
        byte[] ip4 = "128.0.0.1\n".getBytes();
        byte[] ip5 = "200.100.50.25\n".getBytes();
        byte[] ip6 = "250.250.250.250\n".getBytes();
        byte[] ip7 = "255.0.0.0\n".getBytes();

        assertEquals(0x01020304L, parser.parse(ip1, 0, ip1.length - 1));
        assertEquals(0xC0A80102L, parser.parse(ip2, 0, ip2.length - 1));
        assertEquals(0xFFFFFFFFL, parser.parse(ip3, 0, ip3.length - 1));
        assertEquals(0x80000001L, parser.parse(ip4, 0, ip4.length - 1));
        assertEquals(0xC8643219L, parser.parse(ip5, 0, ip5.length - 1));
        assertEquals(0xFAFAFAFAL, parser.parse(ip6, 0, ip6.length - 1));
        assertEquals(0xFF000000L, parser.parse(ip7, 0, ip7.length - 1));
    }

    @Test
    void testParseMinIp() {
        byte[] ip = "0.0.0.0\n".getBytes();

        assertEquals(0x00000000, parser.parse(ip, 0, ip.length - 1));
    }

    @Test
    void testInvalidIpParsing() {
        assertThrows(IllegalArgumentException.class, () -> parser.parse("256.0.0.1".getBytes(), 0, 9));
        assertThrows(IllegalArgumentException.class, () -> parser.parse("1.2.3".getBytes(), 0, 5));
        assertThrows(IllegalArgumentException.class, () -> parser.parse("1.2.3.4.5".getBytes(), 0, 9));
        assertThrows(IllegalArgumentException.class, () -> parser.parse("1.2.-1.4".getBytes(), 0, 8));
        assertThrows(IllegalArgumentException.class, () -> parser.parse("abc.def.ghi.jkl".getBytes(), 0, 15));
    }

    @Test
    void testIpWithWhitespace() {
        assertThrows(IllegalArgumentException.class, () -> parser.parse(" 1.2.3.4".getBytes(), 0, 8));
        assertThrows(IllegalArgumentException.class, () -> parser.parse("1.2.3.4 ".getBytes(), 0, 8));
        assertThrows(IllegalArgumentException.class, () -> parser.parse("\t1.2.3.4\n".getBytes(), 0, 9));
    }

    @Test
    void testOctetBoundaries() {
        assertEquals(0x000000FFL, parser.parse("0.0.0.255".getBytes(), 0, "0.0.0.255".length()));
        assertEquals(0xFF000000L, parser.parse("255.0.0.0".getBytes(), 0, "255.0.0.0".length()));
    }

    @Test
    void testNoNewlineAtEnd() {
        assertEquals(0x01010101, parser.parse("1.1.1.1".getBytes(), 0, 7));
    }

    @Test
    void testMalformedInput() {
        assertThrows(IllegalArgumentException.class, () -> parser.parse("1.2.3.a".getBytes(), 0, 7));
    }

}