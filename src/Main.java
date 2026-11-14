import java.util.HashSet;
import java.util.List;
public class Main{
    static void checkMatches(List<Match> first, List<Match> second) {
        HashSet<Match> firstSet=new HashSet<>(first);
        HashSet<Match> secondSet=new HashSet<>(second);
        if (firstSet.size()!=first.size()
                || secondSet.size()!=second.size()
                || !firstSet.equals(secondSet)) {
            throw new AssertionError("Different matches: " + first + " and " + second);
        }
    }

    static void test(List<String> patterns, String text, List<Match> expected) {
        checkMatches(new AhoCorasick(patterns).search(text), expected);
        checkMatches(Kmp.search(patterns, text), expected);
    }
    public static void main(String[] args){
        test(List.of("she","he"),"she",
                List.of(new Match(0,0),new Match(1, 1)));
        test(List.of("aa"),"aaa",
                List.of(new Match(0, 0), new Match(0, 1)));
        test(List.of("cat"),"dog", List.of());
        System.err.println("All tests passed.");
        System.out.println("| Patterns | Text length | Matches | Aho-Corasick (ms) | KMP (ms) |");
        System.out.println("|---:|---:|---:|---:|---:|");
        for(int patterns:new int[]{10, 100, 1000}) {
            for(int length :new int[]{1000, 10000, 100000}) {
                Benchmark.benchmark(patterns, length);
            }
        }
    }
}