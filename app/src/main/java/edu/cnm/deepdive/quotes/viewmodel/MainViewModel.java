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
import edu.cnm.deepdive.quotes.service.QuotesRepository;
import edu.cnm.deepdive.quotes.service.SourcesRepository;
import io.reactivex.disposables.CompositeDisposable;
import java.util.List;

public class MainViewModel extends AndroidViewModel implements LifecycleObserver {

  private final QuotesRepository quotesRepository;
  private final SourcesRepository sourcesRepository;
  private final MutableLiveData<Throwable> throwable;
  private final CompositeDisposable pending;
  private final MutableLiveData<QuoteWithSource> quote;

  public MainViewModel(@NonNull Application application) {
    super(application);
    quotesRepository = new QuotesRepository(application);
    sourcesRepository = new SourcesRepository(application);
    quote = new MutableLiveData<>();
    throwable = new MutableLiveData<>();
    pending = new CompositeDisposable();
  }

  public LiveData<List<QuoteWithSource>> getQuotes() {
    return quotesRepository.getAll();
  }

  public LiveData<List<Source>> getSources() {
    return sourcesRepository.getAll();
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
        quotesRepository.get(id)
            .subscribe(
                (quote) -> this.quote.postValue(quote),
                (throwable) -> this.throwable.postValue(throwable)
            )
    );
  }

  public void saveQuote(Quote quote) {
    throwable.setValue(null);
    pending.add(
        quotesRepository.save(quote)
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
        quotesRepository.delete(quote)
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