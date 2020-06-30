package edu.cnm.deepdive.quotes.controller;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import edu.cnm.deepdive.quotes.R;
import edu.cnm.deepdive.quotes.model.pojo.QuoteWithSource;
import edu.cnm.deepdive.quotes.view.QuoteAdapter;
import edu.cnm.deepdive.quotes.viewmodel.MainViewModel;

public class QuotesFragment extends Fragment implements QuoteAdapter.OnClickListener {

  private MainViewModel mainViewModel;
  private RecyclerView quoteList;
  private FloatingActionButton addQuote;

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    //noinspection ConstantConditions
    mainViewModel = new ViewModelProvider(getActivity()).get(MainViewModel.class);
    mainViewModel.getQuotes().observe(getViewLifecycleOwner(),
        (quotes) -> quoteList.setAdapter(new QuoteAdapter(getContext(), quotes, this)));
  }

  public View onCreateView(@NonNull LayoutInflater inflater,
      ViewGroup container, Bundle savedInstanceState) {
    View root = inflater.inflate(R.layout.fragment_quotes, container, false);
    quoteList = root.findViewById(R.id.quote_list);
    addQuote = root.findViewById(R.id.add_quote);
    addQuote.setOnClickListener((v) -> editQuote(0));
    return root;
  }

  @Override
  public void onClick(View v, int position, QuoteWithSource quote) {
    editQuote(quote.getId());
//    Toast.makeText(getContext(),
//        String.format("Item %d [%s] was clicked", position, quote), Toast.LENGTH_LONG).show();
  }

  private void editQuote(long quoteId) {
    QuoteEditFragment fragment = QuoteEditFragment.newInstance(quoteId);
    fragment.show(getChildFragmentManager(), fragment.getClass().getName());
  }

}
