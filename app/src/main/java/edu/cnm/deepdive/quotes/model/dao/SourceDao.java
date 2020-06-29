package edu.cnm.deepdive.quotes.model.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;
import edu.cnm.deepdive.quotes.model.entity.Source;
import edu.cnm.deepdive.quotes.model.pojo.SourceWithQuotes;
import io.reactivex.Single;
import java.util.Collection;
import java.util.List;

@Dao
public interface SourceDao {

  @Insert(onConflict = OnConflictStrategy.IGNORE)
  Single<Long> insert(Source source);

  @Insert(onConflict = OnConflictStrategy.IGNORE)
  Single<List<Long>> insert(Source... sources);

  @Insert(onConflict = OnConflictStrategy.IGNORE)
  Single<List<Long>> insert(Collection<Source> sources);

  @Update
  Single<Integer> update(Source... sources);

  @Delete
  Single<Integer> delete(Source... sources);

  @Query("SELECT * FROM Source ORDER BY name")
  Single<List<Source>> selectAll();

  @Transaction
  @Query("SELECT * FROM Source ORDER BY name")
  Single<List<SourceWithQuotes>> selectAllWithQuotes();

  @Transaction
  @Query("SELECT * FROM Source WHERE source_id = :sourceId")
  Single<SourceWithQuotes> selectById(long sourceId);

}
