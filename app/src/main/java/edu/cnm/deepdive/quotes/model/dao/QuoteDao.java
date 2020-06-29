package edu.cnm.deepdive.quotes.model.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;
import edu.cnm.deepdive.quotes.model.entity.Quote;
import edu.cnm.deepdive.quotes.model.pojo.QuoteWithSource;
import io.reactivex.Single;
import java.util.Collection;
import java.util.List;

@Dao
public interface QuoteDao {

  @Insert(onConflict = OnConflictStrategy.IGNORE)
  Single<Long> insert(Quote quote);

  @Insert(onConflict = OnConflictStrategy.IGNORE)
  Single<List<Long>> insert(Collection<Quote> quotes);

  @Update
  Single<Integer> update(Quote... quotes);

  @Delete
  Single<Integer> delete(Quote... quotes);

  @Transaction
  @Query("SELECT * FROM Quote ORDER BY text")
  Single<List<QuoteWithSource>> selectAll();

  @Query("SELECT * FROM Quote WHERE source_id = :sourceId")
  Single<List<Quote>> selectBySourceId(Long sourceId);

  @Transaction
  @Query("SELECT * FROM Quote WHERE quote_id = :quoteId")
  Single<QuoteWithSource> selectById(long quoteId);

}
