package com.example.ip4counter.converter;

public interface IpConverter {

    /**
     * Parses a single IPv4 address from a byte array (in ASCII form) into a 32-bit integer.
     *
     * @param bytes  the byte array containing the IP address characters
     * @param start the start index in the array
     * @param end the length of the IP address
     * @return the IP address as a 32-bit integer
     */
    int parse(byte[] bytes, int start, int end);

}
