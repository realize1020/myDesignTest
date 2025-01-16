package com.example.design.leetcode.字符串.生成字符串所有子序列;

import java.lang.reflect.Array;
import java.util.*;

/**
 * 子序列：从原字符串中选择任意数量的字符（包括零个字符）形成的序列，字符之间的相对顺序保持不变。
 * 例如，对于字符串 "abc"，它的子序列包括 "" (空串), "a", "ab", "abc", "ac", "b", "bc", 和 "c"。
 */
public class Solution {
    public static List<String> subs(String s) {
        char[] str = s.toCharArray();
        String path = "";
        List<String> ans = new ArrayList<>();
        process1(str, 0, ans, path);
        return ans;
    }

    // str固定，不变
    // index此时来到的位置, 要  or 不要
    // 如果index来到了str中的终止位置，把沿途路径所形成的答案，放入ans中
    // 之前做出的选择，就是path
    public static void process1(char[] str, int index, List<String> ans, String path) {
        if (index == str.length) {
            ans.add(path);
            return;
        }
        String no = path;
        process1(str, index + 1, ans, no);
        String yes = path + String.valueOf(str[index]);
        process1(str, index + 1, ans, yes);
    }

    public static List<String> subsNoRepeat(String s) {
        char[] str = s.toCharArray();
        String path = "";
        HashSet<String> set = new HashSet<>();
        processNorepeat(str, 0, set, path);
        List<String> ans = new ArrayList<>();
        for (String cur : set) {
            ans.add(cur);
        }
        return ans;
    }

    // str  index  set
    public static void processNorepeat(char[] str, int index,
                                HashSet<String> set, String path) {
        if (index == str.length) {
            set.add(path);
            return;
        }
        String no = path;
        processNorepeat(str, index + 1, set, no);
        String yes = path + String.valueOf(str[index]);
        processNorepeat(str, index + 1, set, yes);
    }



    private static List<String> mySubs(String test) {
        char[] chars = test.toCharArray();
        String path="";
        ArrayList<String> ans =new ArrayList<>();
        myProcess(chars,path,ans,0);
        return ans;
    }

    private static void myProcess(char[] chars, String path, ArrayList<String> ans, int index) {
        if(index==chars.length){
            ans.add(path);
            return;
        }
        myProcess(chars,path,ans,index+1);
        path=chars[index]+path;
        myProcess(chars,path,ans,index+1);
    }

    /**
     * 全排列的实现方式
     * 全排列：将原字符串中的所有字符重新排序形成的所有可能的组合。
     * 对于字符串 "abc"，它的全排列包括 "abc", "acb", "bac", "bca", "cab", 和 "cba"。
     *      * @param str
     * @return
     */
    public static List<String> subs2(String str){
        char[] chars = str.toCharArray();
        ArrayList<String> ans =new ArrayList<>();
        HashSet<String> used =new HashSet<>();
//        for(int i=0;i<chars.length;i++){
//            process2(0,ans,used,chars[i]+"",chars);
//            used.remove(i);
//        }

        process3(ans,used,"",chars);
        return ans;

    }

    /**
     * 最小字典序的代码方式不对，因为字符串中可能有重复元素，导致user.containsKey失效。
     * 只有在字符串中完全没有重复字符的情况下才可以使用。
     * @param ans
     * @param used
     * @param character
     * @param chars
     */
    private static void process3(ArrayList<String> ans, HashSet<String> used, String character,char[] chars){
        if(used.size()==chars.length){
            ans.add(character);
        }else{
            for(int i=0;i<chars.length;i++){
                if(!used.contains(chars[i]+"")){
                    used.add(chars[i]+"");
                    process3(ans,used,character+chars[i],chars);
                    used.remove(chars[i]+"");
                }
            }
        }

    }

    /**
     * 全排列方式2
     * @param s
     * @return
     */
    public static List<String> permute(String s) {
        List<String> result = new ArrayList<>();
        if (s == null || s.isEmpty()) {
            return result;
        }
        char[] str = s.toCharArray();
        processPermute(str, 0, result);
        return result;
    }

    private static void processPermute(char[] str, int index, List<String> result) {
        if (index == str.length) {
            result.add(new String(str));
            return;
        }
        for (int i = index; i < str.length; i++) {
            swap(str, index, i); // 交换位置
            processPermute(str, index + 1, result); // 递归处理下一个位置
            swap(str, index, i); // 回溯，恢复原状
        }
    }

    private static void swap(char[] arr, int i, int j) {
        if (i != j) {
            char temp = arr[i];
            arr[i] = arr[j];
            arr[j] = temp;
        }
    }



    public static List<String> generateAllCombinations(String s) {
        List<String> result = new ArrayList<>();
        if (s == null || s.isEmpty()) {
            return result;
        }
        char[] str = s.toCharArray();
        Set<String> resultSet = new LinkedHashSet<>(); // 使用Set去重并保持插入顺序
        processCombination(str, new boolean[str.length], 0, "", resultSet);
        return new ArrayList<>(resultSet);
    }

    private static void processCombination(char[] str, boolean[] used, int index, String current, Set<String> result) {
        if (index == str.length) {
            if (!current.isEmpty()) {
                List<String> strings = permute2(current.toCharArray());
                System.out.println("全排列后："+Arrays.toString(strings.toArray()));
                result.addAll(strings);//使用set而非list的原因是例如、有的以c开头后会形成[bc, cb]、以b开头会也形成[bc, cb]，所以还是要再去重
            }
            return;
        }
        // 不选当前字符
        processCombination(str, used, index + 1, current, result);
        // 选当前字符
        processCombination(str, used, index + 1, current + str[index], result);
    }

    private static List<String> permute2(char[] str) {
        List<String> result = new ArrayList<>();
        processPermute2(str, 0, result);
        return result;
    }

    private static void processPermute2(char[] str, int index, List<String> result) {
        if (index == str.length) {
            result.add(new String(str));
            return;
        }
        Set<Character> placed = new HashSet<>();//主要是防止对于"aabbcc"这样的字符串中，把aa、bb、cc这种加到结果result中形成[aa,aa]、[bb,bb]、[cc,cc]、[aab,aab]、[bbc,bbc]这种。
        for (int i = index; i < str.length; i++) {
            if (!placed.contains(str[i])) {
                placed.add(str[i]);
                swap2(str, index, i);
                processPermute(str, index + 1, result);
                swap2(str, index, i); // backtrack
            }
        }
    }

    private static void swap2(char[] arr, int i, int j) {
        if (i != j) {
            char temp = arr[i];
            arr[i] = arr[j];
            arr[j] = temp;
        }
    }

    /**
     * 先求子序列，再for循环求每一个的全排列
     * @param args
     */
//    public static void main(String[] args) {
//        String test = "aabbcc";
//        List<String> ans1 = subs(test);
//        HashSet<String> allRight =new HashSet();
//        for (String str : ans1) {
//            List<String> permute = permute(str);
//            allRight.addAll(permute);
//        }
//        System.out.println("多少个？"+allRight.size());
//        allRight.stream().forEach(s-> System.out.println(s));
//    }

    /**
     * 先求子序列，再for循环求每一个的全排列
     * @param args
     * @return
     */
    public static void main(String[] args) {
        String input = "aabbcc";
        List<String> combinations = generateAllCombinations(input);
        System.out.println(combinations);
        System.out.println("有多少个？"+combinations.size());
    }


//    public static void main(String[] args) {
//        List<String> list = permute3("abc");
//        list.forEach(l-> System.out.println(l));
//    }


    // 生成一个字符串的所有排列
    public static List<String> permute3(String str) {
        List<String> result = new ArrayList<>();
        permuteHelper("", str, result);
        return result;
    }

    /**
     * 递归改写成for循环
     * @param prefix
     * @param str
     * @param result
     */
    private static void permuteHelper(String prefix, String str, List<String> result) {
        int n = str.length();
        if (n == 0) {
            result.add(prefix);
        } else {
            for (int i = 0; i < n; i++)
                permuteHelper(prefix + str.charAt(i), str.substring(0, i) + str.substring(i+1, n), result);
        }
    }

}
