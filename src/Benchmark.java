import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class Benchmark{
    static final int RUNS=5;
    static final int ALPHABET=26;
    static String randomWord(Random random,int length) {
        StringBuilder word = new StringBuilder();
        for (int i = 0; i < length; i++) {
            word.append((char) ('a' + random.nextInt(ALPHABET)));
        }
        return word.toString();
    }
    static void benchmark(int patternCount, int textLength){
        Random random=new Random(42);
        int wordLength=6;
        LinkedHashSet<String> uniquePatterns=new LinkedHashSet<>();
        while (uniquePatterns.size()<patternCount) {
            uniquePatterns.add(randomWord(random, wordLength));
        }
        List<String> patterns=new ArrayList<>(uniquePatterns);
        StringBuilder input=new StringBuilder(randomWord(random,textLength));
        for(int i=0;i+wordLength<=textLength;i+=50){
            String pattern = patterns.get(random.nextInt(patternCount));
            input.replace(i,i+wordLength,pattern);
        }
        String text=input.toString();
        long acTotal=0;
        long kmpTotal=0;
        int matchCount=0;
        for (int round=0;round<2*RUNS;round++) {
            long start =System.nanoTime();
            List<Match> ac= new AhoCorasick(patterns).search(text);
            long acTime=System.nanoTime()-start;
            start =System.nanoTime();
            List<Match>kmp= Kmp.search(patterns, text);
            long kmpTime= System.nanoTime()-start;
            Main.checkMatches(ac,kmp);
            matchCount = ac.size();
            if (round >= RUNS) {
                acTotal +=acTime;
                kmpTotal +=kmpTime;
            }
        }
        double acMs=acTotal/RUNS/1_000_000.0;
        double kmpMs=kmpTotal/RUNS/1_000_000.0;
        System.out.printf(Locale.ROOT,"| %d | %d | %d | %.3f | %.3f |%n",
                patternCount, textLength,matchCount,acMs,kmpMs);
    }
}