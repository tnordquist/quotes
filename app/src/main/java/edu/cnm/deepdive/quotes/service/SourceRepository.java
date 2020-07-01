package edu.cnm.deepdive.quotes.service;

import android.content.Context;
import androidx.lifecycle.LiveData;
import edu.cnm.deepdive.quotes.model.dao.SourceDao;
import edu.cnm.deepdive.quotes.model.entity.Source;
import java.util.List;

public class SourceRepository {

  private final Context context;
  private final QuotesDatabase database;
  private final SourceDao sourceDao;

  public SourceRepository(Context context) {
    this.context = context;
    database = QuotesDatabase.getInstance();
    sourceDao = database.getSourceDao();
  }

  public LiveData<List<Source>> getAll() {
    return sourceDao.selectAll();
  }

  // TODO Add methods for save, delete, get.

}
