package edu.cnm.deepdive.quotes.view;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import edu.cnm.deepdive.quotes.model.pojo.QuoteWithSource;
import edu.cnm.deepdive.quotes.view.QuoteAdapter.Holder;
import java.util.List;

public class QuoteAdapter extends RecyclerView.Adapter<Holder> {

  private final Context context;
  private final List<QuoteWithSource> quotes;

  public QuoteAdapter(Context context, List<QuoteWithSource> quotes) {
    super();
    this.context = context;
    this.quotes = quotes;
  }

  @NonNull
  @Override
  public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    return null;
  }

  @Override
  public void onBindViewHolder(@NonNull Holder holder, int position) {
    holder.bind(position);
  }

  @Override
  public int getItemCount() {
    return quotes.size();
  }

  class Holder extends RecyclerView.ViewHolder {

    public Holder(@NonNull View itemView) {
      super(itemView);
    }

    private void bind(int position) {
      // TODO Populate view object contents with quote content at position.
    }
  }

}
