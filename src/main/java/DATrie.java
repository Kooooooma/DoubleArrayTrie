import java.util.ArrayList;
import java.util.List;

public class DATrie {
    private final int ARRAY_SIZE = 655350;
    private final int BASE_ROOT = 1;
    //根据状态转移公式：|base[start]|+code=end,可知当code>0时,|base[start]| < end
    //即： -end < base[start] < end，其中由于 base 数组中 0 作为根节点，则 end 最小值可取为 1
    //当 end 取 1 时则 base[start] = 0，则 code+1=1 得 code = 0，这和已知条件矛盾,因此可知
    //base数组中，闲置位置默认值可设置为: 0
    private final int BASE_NULL = 0;
    private final int CHECK_ROOT = -1;
    private final int CHECK_NULL = -2;
    private TrieNode base[];
    private int check[];

    public class TrieNode {
        private int transferRatio; //转移基数
        private boolean isLeaf = false; //是否为叶子节点
        private Character label = null; //节点标识即插入的字符本身
        private int value = -1; //当该节点为叶子节点时关联的字典表中对应词条的索引号

        public int getTransferRatio() {
            return transferRatio;
        }

        public void setTransferRatio(int transferRatio) {
            this.transferRatio = transferRatio;
        }

        public boolean isLeaf() {
            return isLeaf;
        }

        public void setLeaf(boolean leaf) {
            isLeaf = leaf;
        }

        public Character getLabel() {
            return label;
        }

        public void setLabel(Character label) {
            this.label = label;
        }

        public int getValue() {
            return value;
        }

        public void setValue(int value) {
            this.value = value;
        }
    }

    public void build(List<String> words) {
        init();

        int pos = 0;
        for (int c = 0; c < words.size(); c++) {
            for (int idx = 0; idx < words.size(); idx++) {
                char chars[] = words.get(idx).toCharArray();
                if (chars.length > pos) {
                    int startState = 0;
                    for (int i = 0; i <= pos-1; i++) {
                        startState = transfer(startState, getCode(chars[i]));
                    }
                    TrieNode node = insert(startState, getCode(chars[pos]), (chars.length == pos+1), idx);
                    node.setLabel(chars[pos]);
                }
            }
            pos++;
        }
    }

    public List<Integer> match(String keyWord) {
        List<Integer> result = new ArrayList<Integer>();
        int startState, endState;

        char chars[] = keyWord.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            startState = 0;
            for (int j = i; j < chars.length; j++) {
                endState = transfer(startState, getCode(chars[j]));
                if (base[endState].getTransferRatio() != BASE_NULL && check[endState] == startState) { //节点存在于 Trie 树上
                    if (base[endState].isLeaf()) {
                        if (!result.contains(base[endState].getValue())) {
                            result.add(base[endState].getValue());
                        }
                    }
                    startState = endState;
                } else {
                    break;
                }
            }
        }

        return result;
    }

    public void printTrie() {
        System.out.println();
        System.out.printf("%5s", "idx");
        for (int i = 0; i < ARRAY_SIZE; i++) {
            if (base[i].getTransferRatio() != BASE_NULL) {
                System.out.printf("%7d\t", i);
            }
        }
        System.out.println();
        System.out.printf("%5s", "base");
        for (int i = 0; i < ARRAY_SIZE; i++) {
            if (base[i].getTransferRatio() != BASE_NULL) {
                System.out.printf("%7d\t", base[i].getTransferRatio());
            }
        }
        System.out.println();
        System.out.printf("%5s", "leaf");
        for (int i = 0; i < ARRAY_SIZE; i++) {
            if (base[i].getTransferRatio() != BASE_NULL) {
                System.out.printf("%7d\t", base[i].isLeaf() ? 1 : 0);
            }
        }
        System.out.println();
        System.out.printf("%5s", "idx");
        for (int i = 0; i < ARRAY_SIZE; i++) {
            if (base[i].getTransferRatio() != BASE_NULL) {
                System.out.printf("%7d\t", base[i].getValue());
            }
        }
        System.out.println();
        System.out.printf("%5s", "char");
        for (int i = 0; i < ARRAY_SIZE; i++) {
            if (base[i].getTransferRatio() != BASE_NULL) {
                System.out.printf("%7c\t", base[i].getLabel());
            }
        }
        System.out.println();
        System.out.printf("%5s", "check");
        for (int i = 0; i < ARRAY_SIZE; i++) {
            if (base[i].getTransferRatio() != BASE_NULL) {
                System.out.printf("%7d\t", check[i]);
            }
        }
        System.out.println();
    }

    /**
     * 根据起始状态和转移基数插入新节点并返回插入的节点
     *
     * @param startState 起始状态
     * @param offset  状态偏移量
     * @param isLeaf  是否为叶子节点
     * @param idx 当前节点在词典中的索引号
     * @return
     */
    private TrieNode insert(int startState, int offset, boolean isLeaf, int idx) {
        int endState = transfer(startState, offset); //状态转移

        if (base[endState].getTransferRatio() != BASE_NULL && check[endState] != startState) { //已被占用
            do {
                endState += 1;
            } while (base[endState].getTransferRatio() != BASE_NULL);

            base[startState].setTransferRatio(endState - offset); //改变父节点转移基数
        }

        if (isLeaf) {
            base[endState].setTransferRatio(Math.abs(base[startState].getTransferRatio())*-1); //叶子节点转移基数标识为父节点转移基数的相反数
            base[endState].setLeaf(true);
            base[endState].setValue(idx); //为叶子节点时需要记录下该词在字典中的索引号
        } else {
            if (base[endState].getTransferRatio() == BASE_NULL) { //未有节点经过
                base[endState].setTransferRatio(Math.abs(base[startState].getTransferRatio())); //非叶子节点的转移基数一定为正
            }
        }
        check[endState] = startState;//check中记录当前状态的父状态

        return base[endState];
    }

    /**
     * 根据起始状态和转移基数返回结束状态
     *
     * @param startState 起始状态
     * @param offset 转移基数
     * @return
     */
    private int transfer(int startState, int offset) {
        return Math.abs(base[startState].getTransferRatio())+offset; //状态转移
    }

    private int getCode(char c) {
        return (int)c;//这里必须大于0
    }

    private void init() {
        base = new TrieNode[ARRAY_SIZE];
        check = new int[ARRAY_SIZE];

        for (int i = 0; i < ARRAY_SIZE; i++) {
            TrieNode node = new TrieNode();
            node.setTransferRatio(BASE_NULL);
            base[i] = node;
            check[i] = CHECK_NULL;
        }

        TrieNode root = new TrieNode();
        root.setTransferRatio(BASE_ROOT);
        base[0] = root;
        check[0] = CHECK_ROOT;
    }
}
