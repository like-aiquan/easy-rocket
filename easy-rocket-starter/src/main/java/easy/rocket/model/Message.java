package easy.rocket.model;

import java.net.SocketAddress;

/**
 * @author chenaiquan
 * @date 2022/6/13 22:37
 */
public class Message {
  private long bornTimestamp;
  private SocketAddress bornHost;
  private String msgId;
  private String topic;
  private byte[] body;
  private int reconsumeTimes;
  private String tags;

  public long getBornTimestamp() {
    return bornTimestamp;
  }

  public Message setBornTimestamp(long bornTimestamp) {
    this.bornTimestamp = bornTimestamp;
    return this;
  }

  public SocketAddress getBornHost() {
    return bornHost;
  }

  public Message setBornHost(SocketAddress bornHost) {
    this.bornHost = bornHost;
    return this;
  }

  public String getMsgId() {
    return msgId;
  }

  public Message setMsgId(String msgId) {
    this.msgId = msgId;
    return this;
  }

  public String getTopic() {
    return topic;
  }

  public Message setTopic(String topic) {
    this.topic = topic;
    return this;
  }

  public byte[] getBody() {
    return body;
  }

  public Message setBody(byte[] body) {
    this.body = body;
    return this;
  }

  public int getReconsumeTimes() {
    return reconsumeTimes;
  }

  public Message setReconsumeTimes(int reconsumeTimes) {
    this.reconsumeTimes = reconsumeTimes;
    return this;
  }

  public String getTags() {
    return tags;
  }

  public Message setTags(String tags) {
    this.tags = tags;
    return this;
  }
}
