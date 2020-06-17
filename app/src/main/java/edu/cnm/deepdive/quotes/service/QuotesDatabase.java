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
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;
import java.util.Map;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;

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
            .build();

  }

  private static class QuotesCallback extends Callback {

    @Override
    public void onCreate(@NonNull SupportSQLiteDatabase db) {
      super.onCreate(db);
      // TODO Create a map from file contents
      // TODO Persist map contents to database.
    }

    private Map<Source, List<Quote>> parseFile(int resourceId) throws IOException {
      try (
          InputStream input = QuotesDatabase.context.getResources().openRawResource(resourceId);
          Reader reader = new InputStreamReader(input);
          CSVParser parser = CSVParser.parse(
              reader, CSVFormat.EXCEL.withIgnoreSurroundingSpaces().withIgnoreEmptyLines());
      ) {
        // TODO Add records from parser to map.
      }
    }

  }

}
