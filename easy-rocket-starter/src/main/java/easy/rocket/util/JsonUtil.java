package easy.rocket.util;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;

/**
 * @author chenaiquan
 * @date 2022/6/14 17:05
 */
public final class JsonUtil {
  private static ObjectMapper DEFAULT_MAPPER;
  private static ObjectReader DEFAULT_READER;
  private static ObjectWriter DEFAULT_WRITER;

  public static boolean initializeEnable() {
    return DEFAULT_MAPPER != null && DEFAULT_WRITER != null && DEFAULT_READER != null;
  }

  /**
   * TODO 并发修改控制  单例模式 ?
   */
  public static void init() {
    ObjectMapper mapper = new ObjectMapper();
    mapper.enable(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL);
    mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    // 推荐使用下划线命名的方式序列化以及反序列化 Json 字符
    // mapper.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
    init(mapper);
  }

  public static void init(ObjectMapper mapper) {
    InitMapper(mapper);
  }

  private static void InitMapper(ObjectMapper mapper) {
    if (initializeEnable()) {
      return;
    }
    if (mapper == null) {
      init();
    }
    InitJsonUtil(mapper);
  }

  private static synchronized void InitJsonUtil(ObjectMapper mapper) {
    if (initializeEnable()) {
      return;
    }
    initJsonUtil(mapper);
  }

  private static void initJsonUtil(ObjectMapper mapper) {
    DEFAULT_MAPPER = mapper;
    DEFAULT_READER = DEFAULT_MAPPER.reader();
    DEFAULT_WRITER = DEFAULT_MAPPER.writer();
  }

  public static ObjectMapper copy() {
    return DEFAULT_MAPPER.copy();
  }

  public static ObjectReader reader() {
    InitMapper(null);
    return JsonUtil.DEFAULT_READER;
  }

  public static ObjectWriter writer() {
    InitMapper(null);
    return JsonUtil.DEFAULT_WRITER;
  }
}
