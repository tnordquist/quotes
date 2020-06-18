package edu.cnm.deepdive.quotes.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.Lifecycle.Event;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.OnLifecycleEvent;
import edu.cnm.deepdive.quotes.model.entity.Quote;
import edu.cnm.deepdive.quotes.model.pojo.QuoteWithSource;
import edu.cnm.deepdive.quotes.service.QuotesRepository;
import io.reactivex.disposables.CompositeDisposable;
import java.util.List;

public class MainViewModel extends AndroidViewModel implements LifecycleObserver {

  private final QuotesRepository repository;
  private final MutableLiveData<List<QuoteWithSource>> quotes;
  private final MutableLiveData<Throwable> throwable;
  private final CompositeDisposable pending;

  public MainViewModel(@NonNull Application application) {
    super(application);
    repository = new QuotesRepository(application);
    quotes = new MutableLiveData<>();
    throwable = new MutableLiveData<>();
    pending = new CompositeDisposable();
    loadQuotes();
  }

  public LiveData<List<QuoteWithSource>> getQuotes() {
    return quotes;
  }

  public LiveData<Throwable> getThrowable() {
    return throwable;
  }

  private void loadQuotes() {
    pending.add(
        repository.getQuotes()
            .subscribe(
                (quotes) -> this.quotes.postValue(quotes),
                (throwable) -> this.throwable.postValue(throwable)
            )
    );
  }

  @OnLifecycleEvent(Event.ON_STOP)
  private void clearPending() {
    pending.clear();
  }

}