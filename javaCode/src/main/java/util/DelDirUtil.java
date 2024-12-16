package util;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 删除目录工具类
 * @author jiaolong
 * @date 2024-6-14 11:12
 */
public class DelDirUtil {

	public static void main(String[] args) {
		// 20220914
		String path = "D:\\test\\aa";
		delFoldersRule(path, 0);
	}

	/**
	 * 目录名称是yyyymmdd格式，大于保留天数的目录将被删除
	 * 
	 * @param: @param path
	 * @param: @param saveDay
	 * @throws
	 */
	public static void delFoldersRule(String path, int saveDay) {
		Calendar sCal = Calendar.getInstance();
		sCal.setTime(new Date());
		sCal.add(Calendar.DATE, -saveDay);

		int compareDay = Integer.valueOf(new SimpleDateFormat("yyyyMMdd").format(sCal.getTime()));

		File dir = new File(path);
		if (dir.exists() && dir.isDirectory()) {
			File[] fs = dir.listFiles();
			for (File f : fs) {
				int fileName = Integer.valueOf(f.getName());
				if (fileName < compareDay) {
					System.out.println("--del" + f.getName());
					deleteFolders(f.getAbsolutePath());
				}
			}
		}
	}

	/**
	 * 删除指定的文件夹
	 * 
	 * @param: filePath
	 * @throws
	 */
	public static boolean deleteFolders(String filePath) {
		Path path = Paths.get(filePath);

		if (!path.toFile().exists()) {
			return true;
		}

		try {
			Files.walkFileTree(path, new SimpleFileVisitor<Path>() {
				@Override
				public FileVisitResult visitFile(Path file, BasicFileAttributes attributes) throws IOException {
					Files.delete(file);
					return FileVisitResult.CONTINUE;
				}

				@Override
				public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
					Files.delete(dir);
					return FileVisitResult.CONTINUE;
				}
			});
			return true;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

}
