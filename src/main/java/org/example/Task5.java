package org.example;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.List;

class Task5 implements Task {
    private Path master = null;
    private Path slave = null;
    private volatile boolean isRunning = false;
    private Thread workingThread;

    public Task5() {
    }

    public Task5(final String masterPath, final String slavePath) {
        this.master = Path.of(masterPath);
        this.slave = Path.of(slavePath);
    }

    public Path getMasterPath() {
        return this.master;
    }

    public void setMasterPath(final String masterPath) {
        if (isRunning)
            throw new IllegalStateException("Can't change path while running");
        this.master = Path.of(masterPath);
    }

    public Path getSlavePath() {
        return this.slave;
    }

    public void setSlavePath(final String slavePath) {
        if (isRunning)
            throw new IllegalStateException("Can't change path while running");
        this.slave = Path.of(slavePath);
    }

    @Override
    public void start() {
        if (!isRunning) {
            workingThread = new Thread(this::sync, "FileSyncThread");
            workingThread.start();
            System.out.println("[" + LocalDateTime.now() + "] synchronization started");
        } else {
            System.out.println("Already running");
        }
    }

    @Override
    public void stop() {
        isRunning = false;
        if (workingThread != null) {
            workingThread.interrupt();
            System.out.println("[" + LocalDateTime.now() + "] synchronization stopped");
        }
    }

    private void errorHandler(final String message) {
        System.err.println("[" + LocalDateTime.now() + "] Syncronization will be stopped because next error: " + message);
        stop();
    }

    private static boolean IsFilesEquals(final Path file1, final Path file2) throws NoSuchAlgorithmException, IOException {
        return getFileHash(file1).equals(getFileHash(file2));
    }

    private static String getFileHash(final Path file) throws NoSuchAlgorithmException, IOException {
        var md = MessageDigest.getInstance("SHA-256");
        InputStream is = Files.newInputStream(file);
        byte[] buf = new byte[1024];
        int readBytesCount;
        while ((readBytesCount = is.read(buf)) != -1)
            md.update(buf, 0, readBytesCount);
        is.close();
        var sb = new StringBuilder();
        for (byte b : md.digest())
            sb.append(String.format("%02x", b));
        return sb.toString();
    }

    private void synchronizeDeletedFiles(final List<Path> masterFilePaths, final List<Path> slaveFilePaths) {
        var masterFileRelativePaths = masterFilePaths.stream().map(mfp -> master.relativize(mfp)).toList();
        for (var slaveFilePath : slaveFilePaths.reversed()) {
            if (!isRunning) break;
            if (!masterFileRelativePaths.contains(slave.relativize(slaveFilePath))) {
                try {
                    Files.delete(slaveFilePath);
                    System.out.println("[" + LocalDateTime.now() + "] " + (Files.isDirectory(slaveFilePath)
                            ? "Folder"
                            : "File") + "\"" +
                            slave.relativize(slaveFilePath) +
                            "\" deleted by sync because it was deleted from master");
                } catch (IOException ex) {
                    errorHandler(ex.getMessage());
                    return;
                }
            }
        }
    }

    private void synchronizeAddedAndEditedFiles(final List<Path> masterFilePaths, final List<Path> slaveFilePaths) {
        for (var masterFilePath : masterFilePaths) {
            if (!isRunning) break;
            Path pathBuf = null;
            for (var slaveFilePath : slaveFilePaths) {
                if (slave.relativize(slaveFilePath).equals(master.relativize(masterFilePath))) {
                    pathBuf = slaveFilePath;
                    break;
                }
            }
            try {
                if (Files.isDirectory(masterFilePath))
                {
                    if (pathBuf == null)
                    {
                        Files.createDirectory(Paths.get(slave.toString(), master.relativize(masterFilePath).toString()));
                        System.out.println("[" + LocalDateTime.now() + "] Folder \"" +
                                master.relativize(masterFilePath) +
                                "\" synchronized because it was created");
                    }
                    continue;
                }
                if (pathBuf != null && !IsFilesEquals(masterFilePath, pathBuf))
                {
                    Files.copy(masterFilePath, pathBuf, StandardCopyOption.REPLACE_EXISTING);
                    System.out.println("[" + LocalDateTime.now() + "] File \"" +
                            master.relativize(masterFilePath) +
                            "\" synchronized because it was edited");
                }
                if (pathBuf == null)
                {
                    Files.copy(masterFilePath, Paths.get(slave.toString(), master.relativize(masterFilePath).toString()));
                    System.out.println("[" + LocalDateTime.now() + "] File \"" +
                            master.relativize(masterFilePath) +
                            "\" synchronized because it was created");
                }
            } catch (IOException | NoSuchAlgorithmException e) {
                errorHandler(e.getMessage());
                return;
            }
        }
    }

    public void sync() {
        while (isRunning)
        {
            try {
                var masterFilePaths = Files.walk(master).skip(1).toList();
                var slaveFilePaths = Files.walk(slave).skip(1).toList();
                synchronizeDeletedFiles(masterFilePaths, slaveFilePaths);
                synchronizeAddedAndEditedFiles(masterFilePaths, slaveFilePaths);
            } catch (Exception e) {
                errorHandler(e.getMessage());
                break;
            }
        }
    }
}
