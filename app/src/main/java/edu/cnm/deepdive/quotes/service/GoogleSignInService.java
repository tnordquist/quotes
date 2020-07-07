package edu.cnm.deepdive.quotes.service;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

public class GoogleSignInService {

  private static Application context;

  private final GoogleSignInClient client;
  private final MutableLiveData<GoogleSignInAccount> account;
  private final MutableLiveData<Throwable> throwable;

  private GoogleSignInService() {
    account = new MutableLiveData<>();
    throwable = new MutableLiveData<>();
    GoogleSignInOptions options = new GoogleSignInOptions.Builder()
        .requestEmail()
        .requestId()
        .requestProfile()
//        .requestIdToken(BuildConfig.CLIENT_ID)
        .build();
    client = GoogleSignIn.getClient(context, options);
  }

  public static void setContext(Application context) {
    GoogleSignInService.context = context;
  }

  public static GoogleSignInService getInstance() {
    return InstanceHolder.INSTANCE;
  }

  public LiveData<GoogleSignInAccount> getAccount() {
    return account;
  }

  public LiveData<Throwable> getThrowable() {
    return throwable;
  }

  public Task<GoogleSignInAccount> refresh() {
    return client.silentSignIn(); // TODO Add listeners
  }

  public void startSignIn(Activity activity, int requestCode) {
    // TODO Update account & throwable.
    Intent intent = client.getSignInIntent();
    activity.startActivityForResult(intent, requestCode);
  }

  public Task<GoogleSignInAccount> completeSignIn(Intent data) {
    Task<GoogleSignInAccount> task = null;
    try {
      task = GoogleSignIn.getSignedInAccountFromIntent(data);
      account.setValue(task.getResult(ApiException.class));
    } catch (ApiException e) {
      // TODO Update throwble
    }
    return task;
  }

  public Task<Void> signOut() {
    return client.signOut()
        .addOnCompleteListener((ignored) -> {/* TODO Update account */});
  }

  // TODO add update method for account.

  // TODO add update method for throwable.

  private static class InstanceHolder {

    private static final GoogleSignInService INSTANCE = new GoogleSignInService();

  }

}
