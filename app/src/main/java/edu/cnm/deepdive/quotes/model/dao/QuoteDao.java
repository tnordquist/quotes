package edu.cnm.deepdive.quotes.model.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;
import edu.cnm.deepdive.quotes.model.entity.Quote;
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

  @Query("SELECT * FROM Quote ORDER BY text")
  Single<List<Quote>> selectAll();

}
