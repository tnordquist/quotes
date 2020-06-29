package edu.cnm.deepdive.quotes.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import edu.cnm.deepdive.quotes.R;
import edu.cnm.deepdive.quotes.model.entity.Quote;
import edu.cnm.deepdive.quotes.model.pojo.QuoteWithSource;
import edu.cnm.deepdive.quotes.view.QuoteAdapter.Holder;
import java.util.List;

public class QuoteAdapter extends RecyclerView.Adapter<Holder> {

  private final String unattributedSource;
  private final String sourceFormat;
  private final Context context;
  private final List<QuoteWithSource> quotes;
  private final OnClickListener clickListener;

  public QuoteAdapter(Context context, List<QuoteWithSource> quotes,
      OnClickListener clickListener) {
    super();
    this.context = context;
    this.quotes = quotes;
    unattributedSource = context.getString(R.string.unattributed_source);
    sourceFormat = context.getString(R.string.source_format);
    this.clickListener = clickListener;
  }

  @NonNull
  @Override
  public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(context).inflate(R.layout.item_quote, parent, false);
    return new Holder(view);
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

    private final View itemView;
    private final TextView quote;
    private final TextView source;

    public Holder(@NonNull View itemView) {
      super(itemView);
      this.itemView = itemView;
      quote = itemView.findViewById(R.id.quote);
      source = itemView.findViewById(R.id.source);
    }

    private void bind(int position) {
      QuoteWithSource item = quotes.get(position);
      String sourceName =
          (item.getSource() != null) ? item.getSource().getName() : unattributedSource;
      quote.setText(item.getText());
      source.setText(String.format(sourceFormat, sourceName));
      itemView.setOnClickListener((v) -> clickListener.onClick(v, getAdapterPosition(), item));
    }

  }

  public interface OnClickListener {

    void onClick(View v, int position, QuoteWithSource quote);

  }

}
