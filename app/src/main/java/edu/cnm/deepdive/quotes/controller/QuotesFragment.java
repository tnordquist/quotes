package edu.cnm.deepdive.quotes.controller;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import edu.cnm.deepdive.quotes.R;
import edu.cnm.deepdive.quotes.view.QuoteAdapter;
import edu.cnm.deepdive.quotes.viewmodel.MainViewModel;

public class QuotesFragment extends Fragment {

  private MainViewModel mainViewModel;
  private RecyclerView quoteList;

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    //noinspection ConstantConditions
    mainViewModel = new ViewModelProvider(getActivity()).get(MainViewModel.class);
    mainViewModel.getQuotes().observe(getViewLifecycleOwner(),
        (quotes) -> quoteList.setAdapter(new QuoteAdapter(getContext(), quotes)));
  }

  public View onCreateView(@NonNull LayoutInflater inflater,
      ViewGroup container, Bundle savedInstanceState) {
    View root = inflater.inflate(R.layout.fragment_quotes, container, false);
    quoteList = root.findViewById(R.id.quote_list);
    return root;
  }

}
