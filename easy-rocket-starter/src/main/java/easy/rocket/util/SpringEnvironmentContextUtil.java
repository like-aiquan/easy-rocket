package easy.rocket.util;

import java.util.Objects;
import java.util.stream.Stream;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/**
 * @author chenaiquan
 */
@Component
public class SpringEnvironmentContextUtil implements EnvironmentAware {
  private static Environment environment;

  @Override
  public void setEnvironment(Environment env) {
    environment = env;
  }

  public static <T> T get(String name, Class<T> type) {
    return environment.getProperty(name, type);
  }

  public static String get(String name) {
    return environment.getProperty(name);
  }

  public static String[] getActiveProfile() {
    return environment.getActiveProfiles();
  }

  public static String firstActiveProfile() {
    return Stream.of(getActiveProfile()).filter(Objects::nonNull).findFirst().orElse(null);
  }
}
