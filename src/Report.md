# How Aho–Corasick works

## 1. Idea
Aho–Corasick finds all the patterns in one pass through the text. If there are 5000 patterns, searching for them one by one would mean going through the text up to 5000 times.

## 2. Trie
We take `he`, `she`, `his`, `hers` and add them to a tree, letter by letter. If words start the same way, they share a branch. A star means a word ends here, and its number is stored in this node.

```
root
├── h
│   ├── he ★
│   │   └── her
│   │       └── hers ★
│   └── hi
│       └── his ★
└── s
    └── sh
        └── she ★
```

## 3. Failure links
If there is no way to go on the next letter, the failure link tells us where to move and try the same letter again. For example, `she` has two shorter endings: `he` and `e`. `he` exists in the trie, so from `she` we go to `he`. In the same way, `sh` leads to `h`, and `his` and `hers` lead to `s`. All other nodes link to the root.

We build the links level by level (BFS), so that the parent's link is already ready. First we follow the parent's link and look for a move by the needed letter. If there is no such move, we keep following links until we reach the root.

## 4. Search
We read `ushers` letter by letter. When we reach `e`, we find `she`. Then, using the dictionary link, we go straight to `he` and write down the second match. Both words end at the same letter.

## 5. Complexity
Building and memory take `O(M)`, where `M` is the total length of all patterns.
Search takes `O(n + z)`, where `n` is the length of the text and `z` is how many matches we have to output.