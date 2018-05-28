import java.util.ArrayList;
import java.util.List;

public class DATrie {
    private final int ARRAY_SIZE = 655350;
    private final int ROOT_BASE = 1;
    private final int ROOT_CHECK_BASE = -1;
    private final int BASE_NULL = ARRAY_SIZE+1;
    private final int CHECK_NULL = -2;
    private int base[];
    private int check[];

    public void build(List<String> words) {
        init();

        int pos = 0;
        for (int c = 0; c < words.size(); c++) {
            for (String word : words) {
                char chars[] = word.toCharArray();
                if (chars.length > pos) {
                    int startState = 0;
                    for (int i = 0; i <= pos-1; i++) {
                        startState = transfer(startState, getCode(chars[i]));
                    }
                    insert(startState, getCode(chars[pos]), (chars.length == pos+1));
                }
            }
            pos++;
        }
    }

    public void debug() {
        System.out.println();
        System.out.printf("%5s", "idx");
        for (int i = 0; i < ARRAY_SIZE; i++) {
            if (base[i] != BASE_NULL) {
                System.out.printf("%7d", i);
            }
        }
        System.out.println();
        System.out.printf("%5s", "base");
        for (int i = 0; i < ARRAY_SIZE; i++) {
            if (base[i] != BASE_NULL) {
                System.out.printf("%7d", base[i]);
            }
        }
        System.out.println();
        System.out.printf("%5s", "check");
        for (int i = 0; i < ARRAY_SIZE; i++) {
            if (base[i] != BASE_NULL) {
                System.out.printf("%7d", check[i]);
            }
        }
        System.out.println();
    }

    /**
     * 根据起始状态和转移基数插入新节点并返回结束状态
     *
     * @param startState 起始状态
     * @param offset 转移基数
     * @return
     */
    private int insert(int startState, int offset, boolean isLeaf) {
        int endState = transfer(startState, offset); //状态转移

        if (base[endState] != BASE_NULL && check[endState] != startState) { //已被占用
            do {
                endState += 1;
            } while (base[endState] != BASE_NULL);

            base[startState] = endState - offset; //改变父节点转移基数
        }

        if (isLeaf) {
            base[endState] = Math.abs(base[startState])*-1; //叶子节点转移基数标识为父节点转移基数的相反数
        } else {
            if (base[endState] == BASE_NULL) {
                base[endState] = Math.abs(base[startState]);
            }
        }
        check[endState] = startState;//check中记录当前状态的父状态

        return endState;
    }

    public List<String> match(String keyWord) {
        List<String> result = new ArrayList<String>();
        List<Character> characterList;
        int startState, endState;

        char chars[] = keyWord.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            startState = 0;
            characterList = new ArrayList<Character>();
            for (int j = i; j < chars.length; j++) {
                endState = transfer(startState, getCode(chars[j]));
                if (base[endState] != BASE_NULL && check[endState] == startState) {
                    characterList.add(chars[j]);
                    if (base[endState] < 0 && base[endState] == base[startState]*-1) {
                        //叶子节点
                        StringBuilder sb = new StringBuilder();
                        for (Character c : characterList) {
                            sb.append(c);
                        }
                        result.add(sb.toString());
                    }
                    startState = endState;
                } else {
                    break;
                }
            }
        }

        return result;
    }

    /**
     * 根据起始状态和转移基数返回结束状态
     *
     * @param startState 起始状态
     * @param offset 转移基数
     * @return
     */
    private int transfer(int startState, int offset) {
        return Math.abs(base[startState])+offset; //状态转移
    }

    private int getCode(char c) {
        return (int)c;//这里必须大于0
    }

    private void init() {
        base = new int[ARRAY_SIZE];
        check = new int[ARRAY_SIZE];

        for (int i = 0; i < ARRAY_SIZE; i++) {
            base[i] = BASE_NULL;
            check[i] = CHECK_NULL;
        }
        base[0] = ROOT_BASE;
        check[0] = ROOT_CHECK_BASE;
    }
}
