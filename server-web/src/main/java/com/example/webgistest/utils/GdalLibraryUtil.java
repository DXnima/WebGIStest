package com.example.webgistest.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

/*
 * 动态库加载类：从资源加载动态库
 * @date:  20220915
 * @author: wnm
 **/
public class GdalLibraryUtil {
	private static final Logger log = LoggerFactory.getLogger(GdalLibraryUtil.class);
    /**
	 * 从资源文件加载类
	 * * @param clazz jar中类
	 * @param file resources目录下文件全路径：/gdal/win32/gdalalljni.dll
	 */
	public static void loadFromResource(String file) throws IOException{
		try {
			//获取系统路径
			String[] libraryPaths=initializePath("java.library.path");
			log.info(String.format("---LibraryUtil-----java.library.path={};", libraryPaths.toString()));
			if(libraryPaths.length == 0) {
				log.info("---LibraryUtil--请设置环境变量java.library.path");
				return;
			}
			String nativeTempDir=libraryPaths[0];
			
			int sepIndex=file.lastIndexOf(File.separator);
			if(sepIndex==-1) {
				sepIndex=file.lastIndexOf("/");
			}
		    String fileName=file.substring(sepIndex+1);
		    
		    log.info("---LibraryUtil--从环境变量{}加载{}",nativeTempDir,fileName);
		    //系统库不存在，就从资源文件复制
		    File extractedLibFile = new File(nativeTempDir+File.separator +fileName);   
		    if(!extractedLibFile.exists()){
		    	//file resources目录下文件全路径：/lib/gdalalljni.dll
		    	InputStream in = GdalLibraryUtil.class.getResourceAsStream(file);
	            if(in==null) {
	            	log.info("---LibraryUtil--资源文件不存在{}",file);
	            	throw new FileNotFoundException(file);
	            } 
	            saveFile(in,extractedLibFile);
		        //保存文件到java.library.path
	            log.info("---LibraryUtil--成功保存文件{}到{}",fileName,extractedLibFile.getPath());
		    }
		    //注意采用loadLibrary加载时mapLibraryName方法会根据系统补全名称
		    int startIndex=fileName.startsWith("lib")?3:0;
		    String libName=fileName.substring(startIndex,fileName.indexOf("."));
		    String mapLibraryName=System.mapLibraryName(libName);
		    log.info("---LibraryUtil--mapLibraryName={}",mapLibraryName);
		    //输出调试信息
			log.info("---LibraryUtil--系统加载动态库{}开始",libName);
			System.loadLibrary(libName);
			log.info("---LibraryUtil--系统加载动态库{}完成",libName);
		}
		catch(Exception ex) {
			log.error(ex.getMessage());
		}
	}
	private static String[] initializePath(String propname) {
        String ldpath = System.getProperty(propname, "");
        String ps = File.pathSeparator;
        int ldlen = ldpath.length();
        int i, j, n;
        // Count the separators in the path
        i = ldpath.indexOf(ps);
        n = 0;
        while (i >= 0) {
            n++;
            i = ldpath.indexOf(ps, i + 1);
        }

        // allocate the array of paths - n :'s = n + 1 path elements
        String[] paths = new String[n + 1];

        // Fill the array with paths from the ldpath
        n = i = 0;
        j = ldpath.indexOf(ps);
        while (j >= 0) {
            if (j - i > 0) {
                paths[n++] = ldpath.substring(i, j);
            } else if (j - i == 0) {
                paths[n++] = ".";
            }
            i = j + 1;
            j = ldpath.indexOf(ps, i);
        }
        paths[n] = ldpath.substring(i, ldlen);
        return paths;
    }
	public static void saveFile(InputStream in, File extractedLibFile) throws IOException{
	    BufferedInputStream reader = null;   
	    FileOutputStream writer = null;   
        try {
        	//文件不存在创建文件，否则获取流报异常
        	createFile(extractedLibFile);
        	
            reader = new BufferedInputStream(in);   
            writer = new FileOutputStream(extractedLibFile);   
               
            byte[] buffer = new byte[1024];   
               
            while (reader.read(buffer) > 0){   
                writer.write(buffer);   
                buffer = new byte[1024];   
            }   
        } catch (IOException e){   
        	throw e;  
        } finally {   
            if(in!=null)   
                in.close();   
            if(writer!=null)   
                writer.close();   
        }
	}
	/**
	 * 文件不存在创建文件，包括上级目录
	 */
	public static void createFile(File destFile) throws IOException{
		File pfile = destFile.getParentFile();
		if (!pfile.exists()) {
			pfile.mkdirs();
		}
		if(!destFile.exists()){
			destFile.createNewFile();
		}
	}
}
