package edu.cnm.deepdive.quotes.service;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;
import edu.cnm.deepdive.quotes.model.dao.QuoteDao;
import edu.cnm.deepdive.quotes.model.dao.SourceDao;
import edu.cnm.deepdive.quotes.model.entity.Quote;
import edu.cnm.deepdive.quotes.model.entity.Source;
import io.reactivex.schedulers.Schedulers;

@Database(
    entities = {Source.class, Quote.class},
    version = 1,
    exportSchema = true
)
public abstract class QuotesDatabase extends RoomDatabase {

  private static final String DB_NAME = "quotes_db";

  private static Application context;

  public static void setContext(Application context) {
    QuotesDatabase.context = context;
  }

  public abstract SourceDao getSourceDao();

  public abstract QuoteDao getQuoteDao();

  public static QuotesDatabase getInstance() {
    return InstanceHolder.INSTANCE;
  }

  private static class InstanceHolder {

    private static final QuotesDatabase INSTANCE =
        Room.databaseBuilder(context, QuotesDatabase.class, DB_NAME)
            .addCallback(new Callback() {
              @Override
              public void onCreate(@NonNull SupportSQLiteDatabase db) {
                super.onCreate(db);
                QuotesDatabase database = QuotesDatabase.getInstance();
                SourceDao sourceDao = database.getSourceDao();
                Source source = new Source();
                source.setName("Albert Einstein");
                sourceDao.insert(source)
                    .subscribeOn(Schedulers.io())
                    .map((sourceId) -> {
                      Quote quote = new Quote();
                      quote.setSourceId(sourceId);
                      quote.setText("I would teach peace rather than war. I would inculcate love rather than hate.");
                      QuoteDao quoteDao = database.getQuoteDao();
                      quoteDao.insert(quote)
                          .subscribeOn(Schedulers.io())
                          .subscribe();
                      return sourceId;
                    })
                    .subscribe();
              }
            })
            .build();

  }

}
