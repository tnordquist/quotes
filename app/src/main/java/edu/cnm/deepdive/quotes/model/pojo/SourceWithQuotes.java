package edu.cnm.deepdive.quotes.model.pojo;

import androidx.room.Relation;
import edu.cnm.deepdive.quotes.model.entity.Quote;
import edu.cnm.deepdive.quotes.model.entity.Source;
import java.util.List;

public class SourceWithQuotes extends Source {

  @Relation(entity = Quote.class, entityColumn = "source_id", parentColumn = "source_id")
  private List<Quote> quotes;

  public List<Quote> getQuotes() {
    return quotes;
  }

  public void setQuotes(List<Quote> quotes) {
    this.quotes = quotes;
  }

}
