package com.github.dr.rwserver.util.file;

import com.github.dr.rwserver.struct.Seq;
import com.github.dr.rwserver.util.log.Log;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;

import static com.github.dr.rwserver.util.IsUtil.isBlank;
//Java

/**
 * @author Dr
 */
public class FileUtil {
	/**
	 * 默认的地址前缀
	 * 如果不为null将会直接使用path而不是使用jar运行时位置
	 */
	public static String path = null;

	/** 内部的File */
	private File file;

	/** 当前操作的文件 */
	private String filepath;

	public FileUtil(File file){
		this.file = file;
		this.filepath = file.getPath();
		this.mkdir();
	}

	public FileUtil(String filepath){
		this.filepath = filepath;
		file = new File(filepath);
		this.mkdir();

	}

	private FileUtil(File file, String filepath){
		this.file = file;
		this.filepath = filepath;
		this.mkdir();
	}

	public static FileUtil file() {
		return file(null);
	}

	public static FileUtil file(String toFile) {
		File file;
		String filepath = null;
		String to = toFile;
		if (null!=toFile) {
			final String pth = "/";
            if(!pth.equals(String.valueOf(toFile.charAt(0)))) {
                to = "/" + toFile;
            }
        }
		try {
			File directory = new File("");
			if (null==toFile) {
                file = (isBlank(path)) ? new File(directory.getCanonicalPath()) : new File(path);
            } else {
				filepath=(isBlank(path)) ? directory.getCanonicalPath()+to : path + to;
				file = new File(filepath);
			}
		} catch (Exception e) {

			if (null==toFile) {
                file = (isBlank(path)) ? new File(System.getProperty("user.dir")) : new File(path);
            } else {
				filepath= (isBlank(path)) ? System.getProperty("user.dir")+to : path;
				file = new File(filepath);
			}
		}
		return new FileUtil(file,filepath);
	}

	public File getFile() {
		return file;
	}

	public boolean exists() {
		return file.exists();
	}

	public String getPath() {
		return filepath;
	}

	public FileUtil toPath(String filename) {
		String path = filepath+"/"+filename;
		filepath = path;
		file = new File(path);
		if(!file.getParentFile().exists()) {
			file.getParentFile().mkdirs();
		}
		return this;
	}

	public Seq<File> getFileList() {
		File[] array = file.listFiles();
		Seq<File> fileList = new Seq<>();
		for (File value : array) {
			if (!value.isDirectory()) {
				if (value.isFile()) {
					fileList.add(value);
				}
			}
		}
		return fileList;
	}

	public Seq<File> getFileListNotNullSize() {
		Seq<File> list = new Seq<>();
		getFileList().eachBooleanIfs(e -> (e.length() > 0),list::add);
		return list;
	}
/*
	public List<File> getFileList() {
		String[] list = file.list();
		List<File> fileList = new ArrayList<File>();
		if (list == null) {
			return fileList;
		}
		File file;
		for (String path : list) {
			file = new File(new String(path.getBytes(),Data.UTF_8));
			if(!file.isDirectory() && file.isFile()) {
				fileList.add(file);
			} else {
				try {
					file = new File(new String(path.getBytes(),"gbk"));
					if(!file.isDirectory() && file.isFile()) {
						fileList.add(file);
					}
				} catch (UnsupportedEncodingException e) {
					Log.error("[FILE GBK]",e);
				}
			}
		}
		return fileList;
	}
	*/
	/**
	 *
	 * @param log
	 * @param cover 是否尾部写入
	 */
	public void writeFile(Object log, boolean cover) {
		File parent = file.getParentFile();
		if(!parent.exists()) {
			parent.mkdirs();
		}
		if(!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				Log.error("Mk file",e);
			}
		}
		try (OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(file,cover), StandardCharsets.UTF_8)) {
			osw.write(log.toString());
			osw.flush();
		} catch (Exception e) {
			Log.error("writeFile",e);
		}
	}

	public void writeFileByte(byte[] bytes, boolean cover) {
		File parent = file.getParentFile();
		if(!parent.exists()) {
			parent.mkdirs();
		}
		if(!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				Log.error("Mk file",e);
			}
		}

		try (BufferedOutputStream osw = new BufferedOutputStream(new FileOutputStream(file,cover))) {
			osw.write(bytes);
			osw.flush();
		} catch (Exception e) {
			Log.error("writeByteFile",e);
		}
	}

	public OutputStream writeByteOutputStream(boolean cover) throws Exception {
			File parent = file.getParentFile();
			if(!parent.exists()) {
				parent.mkdirs();
			}
			if(!file.exists()) {
				file.createNewFile();
			}
			return new FileOutputStream(file,cover);
	}

	public FileInputStream getInputsStream() {
		File parent = file.getParentFile();
		if(!parent.exists()) {
			parent.mkdirs();
		}
		try {
			if(!file.exists()) {
				file.createNewFile();
			}
			return new FileInputStream(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public InputStreamReader readInputsStream() {
		try {
			return new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8);
		} catch (IOException e) { 
			e.printStackTrace(); 
		}
		return null;
	}

	public byte[] readFileByte() throws Exception {
		ByteBuffer byteBuffer;
		try (FileChannel channel = new FileInputStream(file).getChannel()) {
			byteBuffer = ByteBuffer.allocate((int) channel.size());
			while ((channel.read(byteBuffer)) > 0) {
			}
		}
		return byteBuffer.array();
	}

	public Object readFileData(boolean list) {
		try(InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8)) {
			return readFileData(list,inputStreamReader);
		} catch (FileNotFoundException fileNotFoundException) {
			Log.error("FileNotFoundException");
		} catch (IOException ioException) {
			Log.error("Read IO Error",ioException);
		}
		return null;
	}

	public static Object readFileData(boolean list, InputStreamReader isr) {
		try (BufferedReader br = new BufferedReader(isr)) {
			String line;
			if(list){
				Seq<String> fileContent = new Seq<>();
				while ((line = br.readLine()) != null) { 
					fileContent.add(line);
				} 
				return fileContent;
			} else {
				StringBuilder fileContent = new StringBuilder();
				while ((line = br.readLine()) != null) { 
					fileContent.append(line);
					fileContent.append("\r\n");
				}
				return fileContent.toString();
			}
		} catch (IOException e) { 
			e.printStackTrace(); 
		}
		return null;
	}

	private boolean mkdir() {
		return file.mkdirs();
	}

}