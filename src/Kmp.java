import java.util.ArrayList;
import java.util.List;
public class Kmp{
    static int[]prefixTable(String pattern){
        int[] prefix=new int[pattern.length()];
        int matched=0;
        for (int i=1;i<pattern.length();i++){
            while(matched>0&&pattern.charAt(i)!=pattern.charAt(matched)) {
                matched=prefix[matched-1];
            }
            if(pattern.charAt(i)==pattern.charAt(matched)){
                matched++;
            }
            prefix[i]=matched;
        }
        return prefix;
    }
    static List<Match>search(List<String>patterns,String text){
        List<Match> matches=new ArrayList<>();
        for (int id=0;id<patterns.size();id++){
            String pattern=patterns.get(id);
            if(pattern.isEmpty()){
                throw new IllegalArgumentException("Patterns must be nonempty");
            }
            int[]prefix=prefixTable(pattern);
            int matched=0;
            for(int i=0;i<text.length();i++){
                while(matched>0 &&text.charAt(i)!=pattern.charAt(matched)) {
                    matched=prefix[matched-1];
                }
                if(text.charAt(i)==pattern.charAt(matched)) {
                    matched++;
                }
                if(matched==pattern.length()){
                    int start=i-pattern.length()+1;
                    matches.add(new Match(id,start));
                    matched=prefix[matched-1];
                }
            }
        }
        return matches;
    }
}