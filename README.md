
# IPv4 Address Counter

## Overview

This project solves the problem of counting unique IPv4 addresses from a large text file, where each line contains one IP address. The solution is designed to handle files of potentially massive size (tens or hundreds of gigabytes) efficiently using minimal memory and time.

The task was to count the unique IPs in a file and implement a solution that is more efficient than the naive approach using a `HashSet`.

---

## Problem Statement

You are provided with a text file containing IPv4 addresses, one per line:

```
145.67.23.4
8.34.5.23
89.54.3.124
89.54.3.124
3.45.71.5
...
```

The file can be enormous, with sizes up to several gigabytes. Your goal is to count the unique IPs in this file while optimizing both memory usage and processing time.

### Constraints:
- You can only use standard Java or Kotlin libraries.
- The file can be as large as 120 GB when unzipped.
- The solution should handle this efficiently without loading the entire file into memory.

---

## Approach

### Naive Solution:
A simple solution would be to read the file line by line and insert each IP address into a `HashSet`. However, this is inefficient for large files because a `HashSet` requires too much memory.

### Optimized Solution:
The optimized solution involves using a custom data structure that efficiently tracks unique IPs without consuming excessive memory. We used two key components:
1. **BitSets**: We split the entire IPv4 address space into positive and negative ranges using `BitSet` objects.
2. **Efficient Parsing**: IPs are parsed from their string representation to a 32-bit integer for efficient comparison and storage.

By using two `BitSet` objects, one for non-negative IP addresses and another for negative ones, we can track a large number of unique addresses efficiently. The `DualBitSetContainer` stores these addresses and the `IpV4FileParser` reads and processes the file.

---

## How It Works

### Code Overview

- **`IpV4Converter`**: A utility class that converts an IPv4 address from its dotted-decimal string representation (e.g., `"145.67.23.4"`) into a 32-bit integer.

- **`DualBitSetContainer`**: This class uses two `BitSet` instances to efficiently store IPv4 addresses. One `BitSet` tracks addresses that can be represented as positive integers (values <= `Integer.MAX_VALUE`), and another handles negative integers (addresses > `Integer.MAX_VALUE`).

- **`IpV4FileParser`**: This class processes the file line by line, parsing each IP address and adding it to the `DualBitSetContainer`.

- **`Main`**: This class contains the entry point (`main()`) for the program. It takes a filename as a command-line argument, processes the file, and prints the count of unique IP addresses along with the time taken for the operation.

### Execution Flow

1. The program reads the file line by line.
2. For each line, it parses the IP address into a 32-bit integer.
3. It adds the parsed address into the `DualBitSetContainer`.
4. Finally, it prints the total number of unique IP addresses.

---

## Running the Application

### Requirements

- Java 21 or above
- Gradle

### Running the Program

To run the program, use the following command:

```bash
java -jar ipV4Counter.jar <filename>
```

Where `<filename>` is the path to the file containing the list of IPv4 addresses. For example:

```bash
java -jar ipV4Counter.jar /path/to/ip_addresses.txt
```

The program will output the number of unique IPs and the time taken for the computation.

Example output:

```
Reading from: /path/to/ip_addresses.txt
Unique IPs: 1000000000
Completed in 277.371 seconds.
```

### Running Tests

To run the unit tests using Gradle, execute the following command:

```bash
./gradlew test
```

The tests ensure the functionality of the `IpV4FileParser` and `DualBitSetContainer` classes, including validating that the program correctly counts unique IPs.

---


## Memory Usage Analysis

The memory usage for the solution has been optimized by utilizing two `BitSet` instances. This approach allows the program to handle large files efficiently without consuming excessive memory.

For example, testing with a 20GB file showed that the program could handle the task in approximately 277 seconds while using minimal memory.

---

## Conclusion

This project demonstrates an efficient approach to counting unique IPv4 addresses from a large text file, leveraging Java’s `BitSet` and optimized file processing techniques. The solution is designed to be scalable and works well even with large input files.

---


HERE IS EXAMPLE OF MEMORY ANALYZE
![img.png](img.png)