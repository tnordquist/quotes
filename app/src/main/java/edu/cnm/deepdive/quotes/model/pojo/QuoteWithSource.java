package edu.cnm.deepdive.quotes.model.pojo;

import androidx.annotation.NonNull;
import androidx.room.Relation;
import edu.cnm.deepdive.quotes.model.entity.Quote;
import edu.cnm.deepdive.quotes.model.entity.Source;

public class QuoteWithSource extends Quote {

  @Relation(entity = Source.class, entityColumn = "source_id", parentColumn = "source_id")
  private Source source;

  public Source getSource() {
    return source;
  }

  public void setSource(Source source) {
    this.source = source;
  }

  @NonNull
  @Override
  public String toString() {
    String sourceName = (source != null) ? source.getName() : "(unknown)";
    return String.format("%s%n\u2014%s", getText(), sourceName);
  }

}
