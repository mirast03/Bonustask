import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;

public class AhoCorasick{
    static final int ALPHABET=26;

    static class Node {
        Node[] next=new Node[ALPHABET];
        List<Integer>patternIds=new ArrayList<>();
        Node failure;
        Node output;
    }
    Node root=new Node();
    List<String>patterns;
    AhoCorasick(List<String> patterns){
        this.patterns =List.copyOf(patterns);

        for (int id=0;id<patterns.size();id++){
            String pattern=patterns.get(id);
            if(pattern.isEmpty()){
                throw new IllegalArgumentException("Patterns must be nonempty");
            }
            Node node = root;
            for (int i=0;i<pattern.length();i++){
                int letter =pattern.charAt(i)-'a';
                if (letter <0||letter >= ALPHABET){
                    throw new IllegalArgumentException("Use lowercase a-z");
                }
                if(node.next[letter]== null){
                    node.next[letter] =new Node();
                }
                node= node.next[letter];
            }
            node.patternIds.add(id);
        }
        buildLinks();
    }
    void buildLinks(){
        ArrayDeque<Node> queue=new ArrayDeque<>();
        root.failure=root;
        for (int letter=0;letter<ALPHABET;letter++){
            Node child=root.next[letter];
            if(child==null){
                root.next[letter]=root;
            }else{
                child.failure=root;
                queue.addLast(child);
            }
        }
        while(!queue.isEmpty()){
            Node node=queue.removeFirst();
            for (int letter=0;letter<ALPHABET;letter++){
                Node child=node.next[letter];
                Node fallback=node.failure.next[letter];
                if(child==null){
                    node.next[letter]=fallback;
                }else{
                    child.failure=fallback;
                    child.output=fallback;
                    if(fallback.patternIds.isEmpty()){
                        child.output = fallback.output;
                    }
                    queue.addLast(child);
                }
            }
        }
    }
    List<Match>search(String text){
        List<Match>matches=new ArrayList<>();
        Node node=root;
        for (int i=0;i<text.length();i++){
            int letter=text.charAt(i)-'a';
            if(letter<0||letter>=ALPHABET){
                node=root;
                continue;
            }
            node=node.next[letter];
            for (Node end=node;end!=null;end=end.output){
                for (int id : end.patternIds){
                    int start=i-patterns.get(id).length()+1;
                    matches.add(new Match(id,start));
                }
            }
        }
        return matches;
    }
}