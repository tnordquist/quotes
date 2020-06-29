package edu.cnm.deepdive.quotes.controller;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;
import edu.cnm.deepdive.quotes.R;
import edu.cnm.deepdive.quotes.viewmodel.MainViewModel;

public class QuoteEditFragment extends DialogFragment {

  private static final String ID_KEY = "quote_id";

  private long quoteId;
  private View root;
  private EditText quoteText;
  private AutoCompleteTextView sourceName;
  private AlertDialog dialog;
  private MainViewModel viewModel;

  public static QuoteEditFragment newInstance(long quoteId) {
    QuoteEditFragment fragment = new QuoteEditFragment();
    Bundle args = new Bundle();
    args.putLong(ID_KEY, quoteId);
    fragment.setArguments(args);
    return fragment;
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (getArguments() != null) {
      quoteId = getArguments().getLong(ID_KEY, 0);
    }
  }

  @NonNull
  @Override
  public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
    root = LayoutInflater.from(getContext()).inflate(R.layout.fragment_quote_edit, null, false);
    quoteText = root.findViewById(R.id.quote_text);
    sourceName = root.findViewById(R.id.source_name);
    // TODO Add listeners to fields.
    dialog = new AlertDialog.Builder(getContext())
//        .setIcon(android.R.drawable.ic_m)
        .setTitle("Quote Details")
        .setView(root)
        .setPositiveButton(android.R.string.ok, (dlg, which) -> {})
        .setNegativeButton(android.R.string.cancel, (dlg, which) -> {})
        .create();
    // TODO Add onShow listener.
    return dialog;
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    return root;
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    viewModel = new ViewModelProvider(getActivity()).get(MainViewModel.class);
    if (quoteId != 0) {
      viewModel.getQuote().observe(getViewLifecycleOwner(), (quote) -> {
        if (quote != null) {
          quoteText.setText(quote.getText());
          sourceName.setText((quote.getSource() != null) ? quote.getSource().getName() : "");
        }
      });
      viewModel.setQuoteId(quoteId);
    }
  }

}


