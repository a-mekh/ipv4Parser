package com.example.ip4counter;

import com.example.ip4counter.container.IntContainer;
import com.example.ip4counter.container.impl.DualBitSetContainer;
import com.example.ip4counter.parser.IpV4FileParser;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ProcessFileTest {

    @Test
    void testProcessFile() throws IOException {
        List<String> ips = List.of(
                "145.67.23.4",
                "8.34.5.23",
                "89.54.3.124",
                "89.54.3.124", // duplicate
                "3.45.71.5"
        );

        File tempFile = File.createTempFile("ip_test_", ".txt");
        Files.write(tempFile.toPath(), String.join("\r\n", ips).getBytes());

        IntContainer container = new DualBitSetContainer();
        new IpV4FileParser(container).processFile(tempFile.getAbsolutePath());

        assertEquals(4, container.countUnique());

        tempFile.deleteOnExit();
    }
}
