import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

public class FileSystemTask {
    public static void main(String[] args) {
        String surname = "Litvinov";
        String name = "Artem";

        Path baseDir = Paths.get(surname);
        Path file = baseDir.resolve(name);

        try {
            //a. Создаём директорию <surname>
            Files.createDirectories(baseDir);
            System.out.println("a. Создана директория: " + baseDir);

            //b. Внутри создаём файл <name>
            Files.writeString(file, "Hello, world!");
            System.out.println("b. Создан файл: " + file);

            //c. Создаём вложенные директории dir1/dir2/dir3 и копируем туда файл
            Path dir3 = baseDir.resolve("dir1/dir2/dir3");
            Path dir2 = baseDir.resolve("dir1/dir2");
            Path dir1 = baseDir.resolve("dir1");

            Files.createDirectories(dir3);
            Files.copy(file, dir3.resolve(name), StandardCopyOption.REPLACE_EXISTING);
            System.out.println("c. Созданы dir1/dir2/dir3 и файл скопирован.");

            //d. В dir1 создаём file1
            Path file1 = dir1.resolve("file1.txt");
            Files.writeString(file1, "file1 content");
            System.out.println("d. Создан файл file1.txt");

            //e. В dir2 создаём file2
            Path file2 = dir2.resolve("file2.txt");
            Files.writeString(file2, "file2 content");
            System.out.println("e. Создан файл file2.txt");

            //f. Рекурсивный обход и вывод структуры
            System.out.println("\nf. Содержимое директории " + surname + ":");
            Files.walkFileTree(baseDir, new SimpleFileVisitor<>() {
                @Override
                public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) {
                    System.out.println("D: " + baseDir.relativize(dir));
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
                    System.out.println("F: " + baseDir.relativize(file));
                    return FileVisitResult.CONTINUE;
                }
            });

            //g. Удаляем dir1 со всем содержимым
            Files.walkFileTree(dir1, new SimpleFileVisitor<>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    Files.delete(file);
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                    Files.delete(dir);
                    return FileVisitResult.CONTINUE;
                }
            });
            System.out.println("\ng. Директория dir1 и её содержимое удалены.");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
