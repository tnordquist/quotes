package edu.cnm.deepdive.quotes.service;

import android.content.Context;
import androidx.lifecycle.LiveData;
import edu.cnm.deepdive.quotes.model.dao.QuoteDao;
import edu.cnm.deepdive.quotes.model.dao.SourceDao;
import edu.cnm.deepdive.quotes.model.entity.Quote;
import edu.cnm.deepdive.quotes.model.pojo.QuoteWithSource;
import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import java.util.List;

public class QuoteRepository {

  private final Context context;
  private final QuotesDatabase database;
  private final SourceDao sourceDao;
  private final QuoteDao quoteDao;

  public QuoteRepository(Context context) {
    this.context = context;
    database = QuotesDatabase.getInstance();
    sourceDao = database.getSourceDao();
    quoteDao = database.getQuoteDao();
  }

  public LiveData<List<QuoteWithSource>> getAll() {
    return quoteDao.selectAll();
  }

  public Single<QuoteWithSource> get(long id) {
    return quoteDao.selectById(id)
        .subscribeOn(Schedulers.io());
  }

  public Completable save(Quote quote) {
    if (quote.getId() == 0) {
      return Completable.fromSingle(quoteDao.insert(quote))
          .subscribeOn(Schedulers.io());
    } else {
      return Completable.fromSingle(quoteDao.update(quote))
          .subscribeOn(Schedulers.io());
    }
  }

  public Completable delete(Quote quote) {
    if (quote.getId() == 0) {
      return Completable.fromAction(() -> {})
          .subscribeOn(Schedulers.io());
    } else {
      return Completable.fromSingle(quoteDao.delete(quote))
          .subscribeOn(Schedulers.io());
    }
  }

}
