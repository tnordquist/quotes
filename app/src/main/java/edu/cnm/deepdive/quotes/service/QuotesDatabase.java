package edu.cnm.deepdive.quotes.service;

import android.app.Application;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import edu.cnm.deepdive.quotes.model.dao.QuoteDao;
import edu.cnm.deepdive.quotes.model.dao.SourceDao;
import edu.cnm.deepdive.quotes.model.entity.Quote;
import edu.cnm.deepdive.quotes.model.entity.Source;

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
            // Set other options for builder to use when creating QuotesDatabase instance.
            .build();

  }

}
