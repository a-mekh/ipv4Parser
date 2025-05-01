package com.example.ip4counter;

import com.example.ip4counter.parser.IpParser;
import com.example.ip4counter.parser.impl.IpV4Parser;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class IpParserTest {

    private static final IpParser parser = new IpV4Parser();

    /**
     * Unit test for parseIpFromBuffer.
     * <p>
     * We subtract 2 from the input string length to exclude the newline character '\n'
     * (and potentially '\r' on Windows). This ensures we only pass the actual IP characters
     * to the parser, avoiding invalid characters that could break parsing.
     */
    @Test
    void testParseValidIp() {
        byte[] ip1 = "1.2.3.4\n".getBytes();
        byte[] ip2 = "192.168.1.2\n".getBytes();
        byte[] ip3 = "255.255.255.255\n".getBytes();

        assertEquals(0x01020304, parser.parse(ip1, 0, ip1.length - 2));
        assertEquals(0xC0A80102, parser.parse(ip2, 0, ip2.length - 2));
        assertEquals(0xFFFFFFFF, parser.parse(ip3, 0, ip3.length - 2));
    }

}