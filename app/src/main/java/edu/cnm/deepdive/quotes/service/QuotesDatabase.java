package edu.cnm.deepdive.quotes.service;

import android.app.Application;
import android.util.Log;
import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;
import edu.cnm.deepdive.quotes.R;
import edu.cnm.deepdive.quotes.model.dao.QuoteDao;
import edu.cnm.deepdive.quotes.model.dao.SourceDao;
import edu.cnm.deepdive.quotes.model.entity.Quote;
import edu.cnm.deepdive.quotes.model.entity.Source;
import io.reactivex.schedulers.Schedulers;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

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
            .addCallback(new QuotesCallback())
            .build();

  }

  private static class QuotesCallback extends Callback {

    @Override
    public void onCreate(@NonNull SupportSQLiteDatabase db) {
      super.onCreate(db);
      try {
        persist(parseFile(R.raw.quotes));
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }

    private Map<Source, List<Quote>> parseFile(@IdRes int rawResourceId) throws IOException {
      try (
          InputStream input = QuotesDatabase.context.getResources().openRawResource(R.raw.quotes);
          Reader reader = new InputStreamReader(input);
          CSVParser parser = CSVParser.parse(reader,
              CSVFormat.EXCEL.withIgnoreSurroundingSpaces().withIgnoreEmptyLines());
      ) {
        Map<Source, List<Quote>> map = new HashMap<>();
        for (CSVRecord record : parser) {
          Source source = null;
          String sourceName = record.get(0).trim();
          if (!sourceName.isEmpty()) {
            source = new Source();
            source.setName(record.get(0));
          }
          List<Quote> quotes = map.computeIfAbsent(source, (s) -> new LinkedList<>());
          Quote quote = new Quote();
          quote.setText(record.get(1));
          quotes.add(quote);
        }
        Log.d(getClass().getName(), map.toString());
        return map;
      }
    }

    private void persist(Map<Source, List<Quote>> map) {
      QuotesDatabase database = QuotesDatabase.getInstance();
      SourceDao sourceDao = database.getSourceDao();
      QuoteDao quoteDao = database.getQuoteDao();
      List<Source> sources = new LinkedList<>(map.keySet());
      List<Quote> unattributed = map.getOrDefault(null, Collections.emptyList());
      sources.remove(null);
      //noinspection ResultOfMethodCallIgnored
      sourceDao.insert(sources)
          .subscribeOn(Schedulers.io())
          .flatMap((sourceIds) -> {
            List<Quote> quotes = new LinkedList<>();
            Iterator<Long> idIterator = sourceIds.iterator();
            Iterator<Source> sourceIterator = sources.iterator();
            while (idIterator.hasNext()) {
              long sourceId = idIterator.next();
              for (Quote quote : map.getOrDefault(
                  sourceIterator.next(), Collections.emptyList())) {
                quote.setSourceId(sourceId);
                quotes.add(quote);
              }
            }
            quotes.addAll(unattributed);
            return quoteDao.insert(quotes);
          })
          .subscribe(
              (quoteIds) -> {},
              (throwable) -> {throw new RuntimeException(throwable);}
          );
    }

  }

}
