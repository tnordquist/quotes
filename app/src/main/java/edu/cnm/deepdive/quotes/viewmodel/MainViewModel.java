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
import edu.cnm.deepdive.quotes.model.entity.Source;
import edu.cnm.deepdive.quotes.model.pojo.QuoteWithSource;
import edu.cnm.deepdive.quotes.service.QuoteRepository;
import edu.cnm.deepdive.quotes.service.SourceRepository;
import io.reactivex.disposables.CompositeDisposable;
import java.util.List;

public class MainViewModel extends AndroidViewModel implements LifecycleObserver {

  private final QuoteRepository quoteRepository;
  private final SourceRepository sourceRepository;
  private final MutableLiveData<Throwable> throwable;
  private final CompositeDisposable pending;
  private final MutableLiveData<QuoteWithSource> quote;

  public MainViewModel(@NonNull Application application) {
    super(application);
    quoteRepository = new QuoteRepository(application);
    sourceRepository = new SourceRepository(application);
    quote = new MutableLiveData<>();
    throwable = new MutableLiveData<>();
    pending = new CompositeDisposable();
  }

  public LiveData<List<QuoteWithSource>> getQuotes() {
    return quoteRepository.getAll();
  }

  public LiveData<List<Source>> getSources() {
    return sourceRepository.getAll();
  }

  public LiveData<QuoteWithSource> getQuote() {
    return quote;
  }

  public LiveData<Throwable> getThrowable() {
    return throwable;
  }

  public void setQuoteId(long id) {
    throwable.setValue(null);
    pending.add(
        quoteRepository.get(id)
            .subscribe(
                (quote) -> this.quote.postValue(quote),
                (throwable) -> this.throwable.postValue(throwable)
            )
    );
  }

  public void saveQuote(Quote quote) {
    throwable.setValue(null);
    pending.add(
        quoteRepository.save(quote)
            .subscribe(
                () -> {
                },
                (throwable) -> this.throwable.postValue(throwable)
            )
    );
  }

  public void deleteQuote(Quote quote) {
    throwable.setValue(null);
    pending.add(
        quoteRepository.delete(quote)
            .subscribe(
                () -> {
                },
                (throwable) -> this.throwable.postValue(throwable)
            )
    );
  }

  @OnLifecycleEvent(Event.ON_STOP)
  private void clearPending() {
    pending.clear();
  }

}