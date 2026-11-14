# Aho-Corasick and KMP in Java

This program compares two ways to find all occurrences of several patterns in a text. The patterns are nonempty strings made up of lowercase letters from `a` to `z`. Each match gives the pattern's index in the list and its starting position in the text. Both indices start at zero.

## Run the program

Use Java 17 or later. Run these commands from the project folder.

```bash
javac -d out src/*.java
java -cp out Main
```

The program checks that both algorithms find `she` and `he` in `she`, find both overlapping occurrences of `aa` in `aaa`, and return no matches for `cat` in `dog`. It also checks that both algorithms return the same matches in every benchmark case.

## How the search works

Aho-Corasick stores the patterns in a trie, a tree that lets patterns share the letters they start with. A failure link points to the longest shorter ending of the current path that is also a path in the trie. The program builds the links in order of depth using a queue, so the shorter paths already have their transitions when a longer path needs them.

The search reads the text once. When it reaches the end of a pattern, it records the match. It also follows output links to find shorter patterns that end at the same position. For example, after reading `she`, it can report `he` too.

KMP builds a prefix table for one pattern at a time. If a letter does not match, the table tells KMP how much of the current match it can keep. The program runs KMP separately for each pattern, so it scans the text once per pattern.

## Complexity

Let N be the text length, P the number of patterns, S their total length, L the length of the longest pattern, and Z the number of matches.

| Algorithm | Setup time | Search time | Extra memory without results |
|---|---|---|---|
| Aho-Corasick | O(S) | O(N + Z) | O(S) |
| KMP for each pattern | O(S) in total | O(P × N + Z) | O(L) |

The trie has at most S + 1 nodes, each with a fixed table of 26 transitions. KMP only needs one prefix table at a time. Both algorithms use O(Z) space to store the matches they return.

## Benchmark

I tested 10, 100, and 1,000 patterns with texts of 1,000, 10,000, and 100,000 characters. The program generates unique six-letter patterns and inserts some of them into the text every 50 characters. Both algorithms get the same input.

For each case, the program skips five warm-up runs and averages the next five runs using `System.nanoTime()`. The times include setup and search. The program generates the input and compares the matches outside the timed sections.

Measured on Apple M5 with OpenJDK 25.

| Patterns | Text length | Matches | Aho-Corasick (ms) | KMP (ms) |
|---:|---:|---:|---:|---:|
| 10 | 1000 | 20 | 0.107 | 0.232 |
| 10 | 10000 | 200 | 0.181 | 0.180 |
| 10 | 100000 | 2000 | 0.509 | 1.884 |
| 100 | 1000 | 20 | 0.096 | 0.178 |
| 100 | 10000 | 200 | 0.105 | 1.860 |
| 100 | 100000 | 2000 | 0.537 | 18.817 |
| 1000 | 1000 | 20 | 0.476 | 1.815 |
| 1000 | 10000 | 200 | 0.407 | 18.584 |
| 1000 | 100000 | 2000 | 1.099 | 189.196 |

## What the results show

The times were nearly identical for 10 patterns and 10,000 characters. The gap was widest for 1,000 patterns and 100,000 characters, where Aho-Corasick took about 1.1 ms and KMP took about 189 ms.

KMP uses less extra memory and works well for one pattern. Its time grows with the number of patterns because it goes through the text again for each one. Aho-Corasick needs more memory for the trie and its transitions. Once built, the same trie can search other texts for the same patterns.
