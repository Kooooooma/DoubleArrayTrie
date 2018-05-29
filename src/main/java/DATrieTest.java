import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DATrieTest extends TestCase {
    public void testDATrie() {
        //定义词典
        List<String> words = new ArrayList<String>();
//        words.add("清华");
//        words.add("清华大学");
//        words.add("清新");
//        words.add("中华");
//        words.add("中华人民");
//        words.add("华人");
//        words.add("学生");
//        words.add("大学生");

        words.add("qin");
        words.add("shi");
        words.add("ming");
        words.add("yue");
        words.add("zhi");
        words.add("jun");
        words.add("lin");
        words.add("tian");
        words.add("xia");

        //制作码表，以备验证
        Set<Character> codes = new HashSet<Character>();
        for (String word : words) {
            char chars[] = word.toCharArray();
            for (int i = 0; i < chars.length; i++) {
                codes.add(chars[i]);
            }
        }
        for (Character character : codes) {
            System.out.printf("%6s\t", character.charValue());
        }
        System.out.println();
        for (Character character : codes) {
            System.out.printf("%6d\t",  (int)character.charValue());
        }
        System.out.println();

        //构建 Trie 树
        DATrie daTrie = new DATrie();
        daTrie.build(words);
        daTrie.showTrie();

        //执行匹配
//        List<String> result = daTrie.match("清华大学生都是华人");
        List<String> result = daTrie.match("qinshimingyuezhijunlintianxia");

        //打印匹配结果
        System.out.println();
        System.out.printf("Match: {");
        for (int i = 0; i < result.size(); i++) {
            if (i == 0) {
                System.out.printf("%s", result.get(i));
            } else {
                System.out.printf(", %s", result.get(i));
            }
        }
        System.out.printf("}");
        System.out.println();
    }
}
