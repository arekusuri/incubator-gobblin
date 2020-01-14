package org.apache.gobblin.salesforce;

import com.google.gson.JsonElement;
import java.util.Iterator;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.gobblin.source.extractor.DataRecordException;
import org.apache.gobblin.source.extractor.watermark.Predicate;
import org.apache.gobblin.source.workunit.WorkUnit;

@Slf4j
public class QueryResultIterator implements Iterator<JsonElement> {

  private int recordCount = 0;
  private SalesforceExtractor extractor;
  private String schema;
  private String entity;
  private WorkUnit workUnit;
  private List<Predicate> predicateList;

  private Iterator<JsonElement> queryResultIter;

  public QueryResultIterator(
      SalesforceExtractor extractor,
      String schema,
      String entity,
      WorkUnit workUnit,
      List<Predicate> predicateList
  ) {
    log.info("create query result iterator.");
    this.extractor = extractor;
    this.schema = schema;
    this.entity = entity;
    this.workUnit = workUnit;
    this.predicateList = predicateList;
  }

  @Override
  public boolean hasNext() {
    if (queryResultIter == null) {
      initQueryResultIter();
    }
    if (!queryResultIter.hasNext()) {
      // no more data, print out total
      log.info("Soft delete records total:{}", recordCount);
    }
    return queryResultIter.hasNext();
  }

  private void initQueryResultIter() {
    try {
      log.info("Pull soft delete records");
      queryResultIter = extractor.getRecordSet(schema, entity, workUnit, predicateList);
    } catch (DataRecordException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public JsonElement next() {
    if (queryResultIter == null) {
      initQueryResultIter();
    }
    recordCount ++;
    if (!queryResultIter.hasNext()) {
      // no more data, print out total
      log.info("Soft delete records total:{}", recordCount);
    }
    return queryResultIter.next();
  }
}
