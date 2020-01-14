package org.apache.gobblin.salesforce;

import lombok.Data;

@Data
public class FileIdVO {
  private final String jobId;
  private final String batchId;
  private final String resultId;
  public String toString() {
    return String.format("[jobId=%s, batchId=%s, resultId=%s]", jobId, batchId, resultId);
  }
}
