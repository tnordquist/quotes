package edu.cnm.deepdive.quotes.controller;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import edu.cnm.deepdive.quotes.R;
import edu.cnm.deepdive.quotes.model.entity.Quote;
import edu.cnm.deepdive.quotes.model.pojo.QuoteWithSource;
import edu.cnm.deepdive.quotes.viewmodel.MainViewModel;

public class QuotesFragment extends Fragment {

  private MainViewModel mainViewModel;
  private ListView quoteList;

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    //noinspection ConstantConditions
    mainViewModel = new ViewModelProvider(getActivity()).get(MainViewModel.class);
    mainViewModel.getQuotes().observe(getViewLifecycleOwner(), (quotes) -> {
      ArrayAdapter<QuoteWithSource> adapter =
          new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, quotes);
      quoteList.setAdapter(adapter);
    });
  }

  public View onCreateView(@NonNull LayoutInflater inflater,
      ViewGroup container, Bundle savedInstanceState) {
    View root = inflater.inflate(R.layout.fragment_quotes, container, false);
    quoteList = root.findViewById(R.id.quote_list);
    return root;
  }

}
