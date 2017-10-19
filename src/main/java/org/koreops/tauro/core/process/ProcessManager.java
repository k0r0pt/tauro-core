package org.koreops.tauro.core.process;

import org.koreops.tauro.core.exceptions.ProcessResumptionException;
import org.koreops.tauro.core.loggers.Logger;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * Manager class for running process. This class assists in pausing and resuming scanning (and scraping).
 *
 * @author Sudipto Sarkar (k0r0pt) (sudiptosarkar@visioplanet.org).
 * @since 30 Sep, 2017 10:13 PM
 */
public class ProcessManager {

  private static final String processInfoFile;
  private static PrintWriter out;
  private static boolean initComplete;

  static {
    String processDumpFile = System.getProperty("tauro.core.processDumpFile");
    processInfoFile = processDumpFile != null ? processDumpFile : "k0r0ptRouterCracker.process";
  }

  /**
   * Get saved arguments.
   *
   * @return An array of arguments that were passed to the program the first time around.
   */
  public static String[] getArgs() throws ProcessResumptionException {
    String argsInfo = null;
    try {
      BufferedReader reader = new BufferedReader(new FileReader(processInfoFile));
      argsInfo = reader.readLine();
      reader.close();
    } catch (IOException ex) {
      Logger.error("Failed to resume. Process status file has problems: " + ex.getMessage(), true);
      throw new ProcessResumptionException("Failed to resume. Process status file has problems: " + ex.getMessage());
    }
    argsInfo += " --resume";
    return argsInfo.split(" ");
  }

  /**
   * Saves the arguments passed to the program to a file on disk for resuming in case of a halt before finish.
   *
   * @param args The arguments passed to the program.
   */
  public static void saveArgs(String[] args) {
    if (!initComplete) {
      initProcessMonitoring(false);
    }
    int i = 1;
    for (String arg : args) {
      out.print(arg);
      if (i < args.length) {
        out.print(" ");
      }
      i++;
    }
    out.println();
  }

  /**
   * Gets the hosts that have already been covered (scanned/scraped).
   *
   * @return A List of hosts that have already been covered.
   */
  public static List<String> getCoveredHosts() throws ProcessResumptionException {
    List<String> coveredHosts = new ArrayList<>();
    try {
      BufferedReader reader = new BufferedReader(new FileReader(processInfoFile));
      String line;
      boolean argsLine = true;
      while ((line = reader.readLine()) != null) {
        if (argsLine) {
          //Ignore first line.
          argsLine = false;
          continue;
        }
        coveredHosts.add(line);
      }
    } catch (IOException ex) {
      Logger.error("Failed to resume. Process status file has problems: " + ex.getMessage(), true);
      throw new ProcessResumptionException("Failed to resume. Process status file has problems: " + ex.getMessage());
    }
    // Now let's make sure we can append to it.
    initProcessMonitoring(true);
    return coveredHosts;
  }

  private static synchronized void initProcessMonitoring(boolean append) {
    File file = new File(processInfoFile);
    if (!file.exists()) {
      try {
        if (file.createNewFile()) {
          Logger.info("Created process monitoring file.", true);
        } else {
          Logger.info("Appending to already existing process monitoring file.", true);
        }
      } catch (IOException ex) {
        Logger.error("Failed to initialize file logging to: " + processInfoFile + ": " + ex.getMessage(), true);
      }
    }
    try {
      FileWriter fileWriter = new FileWriter(file, append);
      out = new PrintWriter(new BufferedWriter(fileWriter));
      initComplete = true;
    } catch (IOException ex) {
      Logger.error("Failed to initialize file logging to: " + processInfoFile + ": " + ex.getMessage(), true);
    }
  }

  /**
   * Writes off a host as processed.
   *
   * @param host The host that's to be written off.
   */
  public static synchronized void writeOffHost(String host) {
    out.println(host);
  }

  /**
   * Finalizes Process Logging (file closing, flushing etc.)
   *
   * @param scanEnded   A boolean flag specifying if all hosts have been covered and scan has ended.
   */
  public static synchronized void finalizeProcessLogging(boolean scanEnded) {
    if (initComplete) {
      out.flush();
      out.close();
      initComplete = false;
    }

    if (scanEnded) {
      new File(processInfoFile).delete();
    } else {
      Logger.info("Process saved to: " + processInfoFile, true);
    }
  }
}
