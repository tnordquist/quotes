package edu.cnm.deepdive.quotes.controller;

import android.app.Dialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;
import edu.cnm.deepdive.quotes.R;
import edu.cnm.deepdive.quotes.model.entity.Quote;
import edu.cnm.deepdive.quotes.model.entity.Source;
import edu.cnm.deepdive.quotes.viewmodel.MainViewModel;
import java.util.List;

public class QuoteEditFragment extends DialogFragment implements TextWatcher {

  private static final String ID_KEY = "quote_id";

  private long quoteId;
  private View root;
  private EditText quoteText;
  private AutoCompleteTextView sourceName;
  private AlertDialog dialog;
  private MainViewModel viewModel;
  private Quote quote;
  private List<Source> sources;

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
    quoteText.addTextChangedListener(this);
    dialog = new AlertDialog.Builder(getContext())
        .setIcon(R.drawable.ic_message)
        .setTitle("Quote Details")
        .setView(root)
        .setPositiveButton(android.R.string.ok, (dlg, which) -> save())
        .setNegativeButton(android.R.string.cancel, (dlg, which) -> {})
        .create();
    dialog.setOnShowListener((dlg) -> checkSubmitCondition());
    return dialog;
  }

  private void save() {
    quote.setText(quoteText.getText().toString().trim());
    Source source = null;
    String name = sourceName.getText().toString().trim();
    quote.setSourceId(null);
    if (!name.isEmpty()) {
      for (Source s : sources) {
        if (name.equalsIgnoreCase(s.getName())) {
          quote.setSourceId(s.getId());
          break;
        }
      }
    }
    viewModel.saveQuote(quote);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    return root;
  }

  @SuppressWarnings("ConstantConditions")
  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    viewModel = new ViewModelProvider(getActivity()).get(MainViewModel.class);
    viewModel.getSources().observe(getViewLifecycleOwner(), (sources) -> {
      this.sources = sources;
      ArrayAdapter<Source> adapter =
          new ArrayAdapter<>(getContext(), android.R.layout.simple_dropdown_item_1line, sources);
      sourceName.setAdapter(adapter);
    });
    if (quoteId != 0) {
      viewModel.getQuote().observe(getViewLifecycleOwner(), (quote) -> {
        this.quote = quote;
        if (quote != null) {
          quoteText.setText(quote.getText());
          sourceName.setText((quote.getSource() != null) ? quote.getSource().getName() : "");
        }
      });
      viewModel.setQuoteId(quoteId);
    } else {
      quote = new Quote();
    }
  }

  @Override
  public void beforeTextChanged(CharSequence s, int start, int count, int after) {
  }

  @Override
  public void onTextChanged(CharSequence s, int start, int before, int count) {
  }

  @Override
  public void afterTextChanged(Editable s) {
    checkSubmitCondition();
  }

  private void checkSubmitCondition() {
    Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
    positive.setEnabled(!quoteText.getText().toString().trim().isEmpty());
  }

}


