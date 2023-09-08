package com.example.webgistest.utils;

import java.io.*;

public class ComTools {
    /**
     * 读取文件内容
     *
     * @param file File对象
     * @return 返回文件内容
     * @throws IOException IO 错误
     */
    public static String readFile(File file) throws IOException {
        BufferedReader in = new BufferedReader(new FileReader(file));
        String str;
        StringBuilder stringBuffer = new StringBuilder();

        while ((str = in.readLine()) != null) {
            stringBuffer.append(str);
        }

        return stringBuffer.toString();
    }

    /**
     * 提供curl post 请求方法
     *
     * @param cmds curl 命令行 String 数组
     * @return 执行curl命令返回值 JSON
     * @throws IOException 获取输入输出流不存在
     */
    public static String curl(String[] cmds) throws IOException {
        ProcessBuilder processBuilder = new ProcessBuilder(cmds);

        Process start = processBuilder.start();
        InputStream inputStream = start.getInputStream();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "GBK"));

        StringBuilder stringBuilder = new StringBuilder();

        bufferedReader.lines().forEach(stringBuilder::append);

        return stringBuilder.toString();
    }

    /**
     * 中文字符转为 Unicode 编码
     *
     * @param str
     * @return abc中国 -> abc&#x4e2d;&#x56fd;
     * @author wnm
     * @date 2021/7/7
     */
    public static String chinaToUnicode(String str) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < str.length(); i++) {
            int chr1 = str.charAt(i);
            if (chr1 >= 19968) {// 汉字范围 \u4e00-\u9fa5 (中文)
                result.append("&#x").append(Integer.toHexString(chr1)).append(";");
            } else {
                result.append(str.charAt(i));
            }
        }
        return result.toString();
    }

    /**
     * Unicode转中文
     *
     * @param unicode
     * @return abc&#x4e2d;&#x56fd; -> abc中国
     * @author wnm
     * @date 2021/7/7
     */
    public static String decodeUnicode(String unicode) {
        StringBuffer string = new StringBuffer();
        String[] hex = unicode.split("&#x");
        for (String s : hex) {
            try {
                // 汉字范围 \u4e00-\u9fa5 (中文)
                if (s.length() >= 4) {//取前四个，判断是否是汉字
                    String chinese = s.substring(0, 4);
                    try {
                        int chr = Integer.parseInt(chinese, 16);
                        boolean isChinese = isChinese((char) chr);
                        //转化成功，判断是否在  汉字范围内
                        if (isChinese) {//在汉字范围内
                            // 追加成string
                            string.append((char) chr);
                            //并且追加  后面的字符
                            String behindString = s.substring(5);
                            string.append(behindString);
                        } else {
                            string.append(s);
                        }
                    } catch (NumberFormatException e1) {
                        string.append(s);
                    }

                } else {
                    string.append(s);
                }
            } catch (NumberFormatException e) {
                string.append(s);
            }
        }
        return string.toString();
    }

    /**
     * 判断是否为中文字符
     *
     * @param c
     * @return
     */
    public static boolean isChinese(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        return ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
                || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS;
    }

}
