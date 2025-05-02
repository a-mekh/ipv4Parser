package com.example.ip4counter;

import com.example.ip4counter.container.IntContainer;
import com.example.ip4counter.container.impl.DualBitSetContainer;
import com.example.ip4counter.parser.IpV4FileParser;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ProcessFileTest {

    @Test
    void testProcessFileInChunks() throws IOException {
        String testData = "1.2.3.4\n192.168.0.1\r\n10.0.0.1\n255.255.255.255\n";
        File tempFile = File.createTempFile("ip_chunk_test", ".txt");
        Files.write(tempFile.toPath(), testData.getBytes());

        IntContainer container = new DualBitSetContainer(100);
        IpV4FileParser parser = new IpV4FileParser(container);
        parser.processFile(tempFile.getAbsolutePath()); // simulate chunking

        assertEquals(4, container.countUnique());

        tempFile.deleteOnExit();
    }

}
