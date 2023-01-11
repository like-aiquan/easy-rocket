package easy.rocket.util;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

import com.google.common.base.Stopwatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * from com.google.inject.internal.util
 * </br> A continuously timing stopwatch that is used for simple performance monitoring.
 * </br> Author: crazybob@google.com (Bob Lee)
 */
public class ContinuousStopwatch {
  private static final Logger logger = LoggerFactory.getLogger(ContinuousStopwatch.class);

  private final Stopwatch stopwatch;

  /**
   * Constructs a ContinuousStopwatch, which will start timing immediately after construction.
   *
   * @param stopwatch the internal stopwatch used by ContinuousStopwatch
   */
  public ContinuousStopwatch(Stopwatch stopwatch) {
    this.stopwatch = stopwatch;
    reset();
  }

  /**
   * Resets and returns elapsed time in milliseconds.
   */
  public long reset() {
    long elapsedTimeMs = this.stopwatch.elapsed(MILLISECONDS);
    this.stopwatch.reset();
    this.stopwatch.start();
    return elapsedTimeMs;
  }

  /**
   * Resets and logs elapsed time in milliseconds.
   */
  public void resetAndLog(String label, Logger logger) {
    logger.info(label + ": " + reset() + "ms");
  }

  public void resetAndLog(String label) {
    this.resetAndLog(label, logger);
  }

  public long stop() {
    long elapsed = this.stopwatch.elapsed(MILLISECONDS);
    this.stopwatch.stop();
    return elapsed;
  }

  public void stopAndLog(String label) {
    this.stopAndLog(label, logger);
  }

  public void stopAndLog(String label, Logger logger) {
    logger.info(label + ": " + this.stop() + "ms");
  }
}
