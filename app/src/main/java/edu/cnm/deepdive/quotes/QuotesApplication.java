package edu.cnm.deepdive.quotes;

import android.app.Application;
import com.facebook.stetho.Stetho;
import edu.cnm.deepdive.quotes.service.GoogleSignInService;
import edu.cnm.deepdive.quotes.service.QuotesDatabase;
import io.reactivex.schedulers.Schedulers;

public class QuotesApplication extends Application {

  @Override
  public void onCreate() {
    super.onCreate();
    GoogleSignInService.setContext(this);
    QuotesDatabase.setContext(this);
    QuotesDatabase database = QuotesDatabase.getInstance();
    database.getSourceDao().delete()
        .subscribeOn(Schedulers.io())
        .subscribe();
    Stetho.initializeWithDefaults(this);
  }

}
