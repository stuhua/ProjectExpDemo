import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.logging.SimpleFormatter;

public class CopyFile {
	public static void main(String[] args) {
		URI u = URI.create("file:///f:/1");
		Path p = Paths.get(u);
		try {
			System.out.println("size=" + getDirSize(p));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	static long size = 0;

	public static long getDirSize(Path sourcePath) throws IOException {

		Files.walkFileTree(sourcePath, new SimpleFileVisitor<Path>() {

			@Override
			public FileVisitResult visitFile(Path file,
					BasicFileAttributes attrs) throws IOException {
				size += attrs.size();
				System.out.println("file:"+file.getFileName()+"...size:"+attrs.size());
				return FileVisitResult.CONTINUE;
			}

			@Override
			public FileVisitResult visitFileFailed(Path file, IOException exc)
					throws IOException {
				System.out.println("visitFileFailed---:" + file.getFileName());
				return FileVisitResult.TERMINATE;
			}

		});
		return size;
	}

	private void method() {

		try {
			URI u = URI.create("file:///f:/1");
			Path p = Paths.get(u);
			Files.walkFileTree(p, new SimpleFileVisitor<Path>() {

				private String pathName = "1";
				private String fileTarget = "f:/2";

				@Override
				public FileVisitResult preVisitDirectory(Path dir,
						BasicFileAttributes attrs) throws IOException {

					String dirAbsPath = dir.toAbsolutePath().toString();
					String subStringOfSource = dirAbsPath.substring(dirAbsPath
							.indexOf(pathName) + pathName.length());
					File tempFile = new File(fileTarget, subStringOfSource);
					if (!tempFile.exists()
							&& !pathName.equals(subStringOfSource)) {
						tempFile.mkdirs();
					}

					return FileVisitResult.CONTINUE;
				}

				@Override
				public FileVisitResult visitFile(Path file,
						BasicFileAttributes attrs) throws IOException {
					file.toAbsolutePath().toString();
					System.out.println("visitFile---:" + file.getFileName()
							+ " " + file.getNameCount() + " "
							+ file.getParent() + " "
							+ file.toAbsolutePath().toString());

					return FileVisitResult.CONTINUE;
				}

				@Override
				public FileVisitResult visitFileFailed(Path file,
						IOException exc) throws IOException {
					System.out.println("visitFileFailed---:"
							+ file.getFileName());
					// TODO Auto-generated method stub
					return FileVisitResult.TERMINATE;
				}

			});
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
