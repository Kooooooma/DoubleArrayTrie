import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DATrieTest extends TestCase {
    public void testDATrie() {
        List<String> words = new ArrayList<String>();
        words.add("清华");
        words.add("清华大学");
        words.add("清新");
        words.add("中华");
        words.add("中华人民");
        words.add("华人");
        words.add("学生");
        words.add("大学生");

        //先制作一份码表，以备验证
        Set<Character> codes = new HashSet<Character>();
        for (String word : words) {
            char chars[] = word.toCharArray();
            for (int i = 0; i < chars.length; i++) {
                codes.add(chars[i]);
            }
        }
        for (Character character : codes) {
            System.out.printf("%6s", character.charValue());
        }
        System.out.println();
        for (Character character : codes) {
            System.out.printf("%7d",  (int)character.charValue());
        }
        System.out.println();

        DATrie daTrie = new DATrie();
        daTrie.build(words);
        daTrie.debug();

        System.out.println();
        List<String> result = daTrie.match("清华大学生都是华人");
        System.out.printf("%5s", "Match");
        for (String s : result) {
            System.out.printf("%6s", s);
        }
        System.out.println();
    }
}
