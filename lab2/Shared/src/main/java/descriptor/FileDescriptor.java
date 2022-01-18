package descriptor;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileDescriptor implements Serializable {
    private final String absolutePath;
    private final String name;
    private final String extension;
    private final long size;

    public FileDescriptor(Path filePath) throws IOException {
        if (!Files.exists(filePath)) {
            throw new FileNotFoundException("File with path " + filePath + " doesn't exist");
        }
        String fileName = filePath.getFileName().toString();
        int extensionDotIndex = fileName.lastIndexOf('.');
        extension = fileName.substring(extensionDotIndex + 1);
        name = fileName.substring(0, extensionDotIndex);
        absolutePath = filePath.toRealPath().toString();
        size = Files.size(filePath);
    }

    public String getAbsolutePath() {
        return absolutePath;
    }

    public String getName() {
        return name;
    }

    public String getExtension() {
        return extension;
    }

    public long getSize() {
        return size;
    }

    public String getFullName() {
        return name + '.' + extension;
    }
}
