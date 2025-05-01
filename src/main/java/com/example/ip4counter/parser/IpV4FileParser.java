package com.example.ip4counter.parser;

import com.example.ip4counter.container.IntContainer;
import com.example.ip4counter.converter.impl.IpV4Converter;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class IpV4FileParser {

    private final IpV4Converter parser;
    private final IntContainer finalSet;

    public IpV4FileParser(IntContainer finalSet) {
        this.parser = new IpV4Converter();
        this.finalSet = finalSet;
    }

    public void processFile(String filename) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                processLine(line.getBytes(), line.length());
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
            System.exit(1);
        }
    }

    private void processLine(byte[] lineBuffer, int lineLength) {
        try {
            int ip = parser.parse(lineBuffer, 0, lineLength - 1);
            finalSet.add(ip);
        } catch (IllegalArgumentException e) {
            System.err.println("Invalid IP: " + new String(lineBuffer, 0, lineLength));
        }
    }

}