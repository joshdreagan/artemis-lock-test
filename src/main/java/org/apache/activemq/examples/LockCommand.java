package org.apache.activemq.examples;

import org.apache.activemq.artemis.core.server.ActivateCallback;
import org.apache.activemq.artemis.core.server.impl.FileLockNodeManager;
import picocli.CommandLine;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@CommandLine.Command
public class LockCommand implements Runnable {

  @CommandLine.Parameters
  public Path path;

  @Override
  public void run() {

    if (!Files.exists(path)) {
      try {
        Files.createDirectories(path);
      } catch (IOException e) {
        throw new RuntimeException("Unable to create directory: " + path, e);
      }
    }
    if (!Files.isDirectory(path)) {
      throw new RuntimeException("Not a directory: " + path);
    }

    FileLockNodeManager fileLockNodeManager = new FileLockNodeManager(path.toFile(), false);
    try {
      Runtime.getRuntime().addShutdownHook(new Thread(() -> System.out.println("Stop time: " + System.currentTimeMillis())));
      System.out.println("Start time: " + System.currentTimeMillis());
      System.out.println("Waiting to acquire lock...");
      fileLockNodeManager.start();
      ActivateCallback callback = fileLockNodeManager.startPrimaryNode();
      callback.activationComplete();
      System.out.println("Lock time: " + System.currentTimeMillis());
      System.out.println("Press ctrl+c to quit.");
      while (true) {
        Thread.sleep(1000L);
      }
    } catch (Exception e) {
      throw new RuntimeException("Unable to acquire lock.", e);
    }
  }
}
