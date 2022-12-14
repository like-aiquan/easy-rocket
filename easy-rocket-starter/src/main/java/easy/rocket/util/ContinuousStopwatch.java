package easy.rocket.util;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

import com.google.common.base.Stopwatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author chenaiquan
 * @date 2022/6/12 14:29
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
    long elapsedTimeMs = stopwatch.elapsed(MILLISECONDS);
    stopwatch.reset();
    stopwatch.start();
    return elapsedTimeMs;
  }

  /**
   * Resets and logs elapsed time in milliseconds.
   */
  public void resetAndLog(String label) {
    logger.info(label + ": " + reset() + "ms");
  }
}
