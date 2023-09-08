package com.example.webgistest.utils;

import java.io.*;
import java.util.Arrays;

public class FileUtil {

    /**
     * 将byte数组写入文件
     * @author wnm
     * @date 2021/10/23
     * @param path, content
     * @return void
     */
    public static void createFile(String path, byte[] content) throws IOException {
        File file = new File(path);
        if(!file.exists()){
            File fileParent = file.getParentFile();
            if(!fileParent.exists()){
                fileParent.mkdirs();
            }
            file.createNewFile();
            FileOutputStream fos = new FileOutputStream(path);
            fos.write(content);
            fos.close();
        } else {
            //判断二进制文件与二进制内容是否一样，不一样则创建
            if (!compareFile(path,content)){
                FileOutputStream fos = new FileOutputStream(path);
                fos.write(content);
                fos.close();
            }
        }
    }

    /**
     * 比较二进制文件和二进制是否一样
     * @author wnm
     * @date 2021/10/23
     * @param path
     * @return boolean
     */
    public static boolean compareFile(String path, byte[] content) {
        byte[] b = readBuffer(path);
        if (b == null) {
            return false;
        } else {
            return Arrays.equals(content, b);
        }
    }

    public static byte[] readBuffer(String path) {
        File file = new File(path);
        if (file.exists()) {
            try {
                BufferedInputStream bis = new BufferedInputStream(new FileInputStream(path));
                int len = bis.available();
                byte[] b = new byte[len];
                bis.read(b, 0, len);
                return b;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        } else {
            return null;
        }
    }

    /**
     * 获取文件内容
     * @param filePath
     * @return
     */
    public String getFileContent(String filePath){
        StringBuffer sb = new StringBuffer();
        try {
            String encoding="GBK";
            File file=new File(filePath);
            if(file.isFile() && file.exists()){ //判断文件是否存在
                InputStreamReader read = new InputStreamReader(
                        new FileInputStream(file),encoding);//考虑到编码格式
                BufferedReader bufferedReader = new BufferedReader(read);
                String lineTxt = null;
                while((lineTxt = bufferedReader.readLine()) != null){
                    sb.append(lineTxt);
                }
                read.close();
            }
            else{
                System.out.println("找不到指定的文件");
            }
        }
        catch (Exception e) {
            System.out.println("读取文件内容出错");
            e.printStackTrace();
        }
        return sb.toString();
    }
    public void append2File(String file, String content) {
        FileWriter fw = null;

        try {
            //如果文件存在，则追加内容；如果文件不存在，则创建文件
            File f = new File(file);
            fw = new FileWriter(f, true);
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        PrintWriter pw = new PrintWriter(fw);
        pw.println(content);
        pw.flush();

        try {
            fw.flush();
            pw.close();
            fw.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
