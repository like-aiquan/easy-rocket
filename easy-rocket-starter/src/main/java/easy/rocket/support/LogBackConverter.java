package easy.rocket.support;

import ch.qos.logback.classic.pattern.ClassicConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;

/**
 * @author chenaiquan
 */
public class LogBackConverter extends ClassicConverter {

  @Override
  public String convert(ILoggingEvent event) {
    return null;
  }

}
