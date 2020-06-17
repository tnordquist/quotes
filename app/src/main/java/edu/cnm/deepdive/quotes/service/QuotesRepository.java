package edu.cnm.deepdive.quotes.service;

import android.content.Context;
import edu.cnm.deepdive.quotes.model.dao.QuoteDao;
import edu.cnm.deepdive.quotes.model.dao.SourceDao;
import edu.cnm.deepdive.quotes.model.entity.Quote;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import java.util.List;

public class QuotesRepository {

  private final Context context;
  private final QuotesDatabase database;
  private final SourceDao sourceDao;
  private final QuoteDao quoteDao;

  public QuotesRepository(Context context) {
    this.context = context;
    database = QuotesDatabase.getInstance();
    sourceDao = database.getSourceDao();
    quoteDao = database.getQuoteDao();
  }

  public Single<List<Quote>> getQuotes() {
    return quoteDao.selectAll()
        .subscribeOn(Schedulers.io());
  }

  // TODO Add other methods as necessary.

}
