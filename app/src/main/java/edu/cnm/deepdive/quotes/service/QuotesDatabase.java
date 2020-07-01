package edu.cnm.deepdive.quotes.service;

import android.annotation.SuppressLint;
import android.app.Application;
import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverter;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;
import edu.cnm.deepdive.quotes.R;
import edu.cnm.deepdive.quotes.model.dao.QuoteDao;
import edu.cnm.deepdive.quotes.model.dao.SourceDao;
import edu.cnm.deepdive.quotes.model.entity.Quote;
import edu.cnm.deepdive.quotes.model.entity.Source;
import edu.cnm.deepdive.quotes.service.QuotesDatabase.Converters;
import io.reactivex.schedulers.Schedulers;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
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
@TypeConverters({Converters.class})
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
        Map<Source, List<Quote>> map = parseFile(R.raw.quotes);
        persist(map);
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }

    private Map<Source, List<Quote>> parseFile(int resourceId) throws IOException {
      try (
          InputStream input = QuotesDatabase.context.getResources().openRawResource(resourceId);
          Reader reader = new InputStreamReader(input);
          CSVParser parser = CSVParser.parse(
              reader, CSVFormat.EXCEL.withIgnoreSurroundingSpaces().withIgnoreEmptyLines());
      ) {
        Map<Source, List<Quote>> map = new HashMap<>();
        for (CSVRecord record : parser) {
          Source source = null;
          String sourceName = record.get(0).trim();
          if (!sourceName.isEmpty()) {
            source = new Source();
            source.setName(sourceName);
          }
          List<Quote> quotes = map.computeIfAbsent(source, (s) -> new LinkedList<>());
          Quote quote = new Quote();
          quote.setText(record.get(1).trim());
          quotes.add(quote);
        }
        return map;
      }
    }

    @SuppressLint("CheckResult")
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
              for (Quote quote : map.getOrDefault(sourceIterator.next(), Collections.emptyList())) {
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

  public static class Converters {

    @TypeConverter
    public static Long dateToLong(Date value) {
      return (value != null) ? value.getTime() : null;
    }

    @TypeConverter
    public static Date longToDate(Long value) {
      return (value != null) ? new Date(value) : null;
    }

  }

}
