package edu.cnm.deepdive.quotes.view;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import edu.cnm.deepdive.quotes.model.pojo.SourceWithQuotes;
import edu.cnm.deepdive.quotes.view.SourceAdapter.Holder;
import java.util.List;

public class SourceAdapter extends RecyclerView.Adapter<Holder> {

  private final Context context;
  private final List<SourceWithQuotes> sources;
  private final OnClickListener clickListener;

  public SourceAdapter(
      Context context, List<SourceWithQuotes> sources, OnClickListener clickListener) {
    super();
    this.context = context;
    this.sources = sources;
    this.clickListener = clickListener;
  }

  @NonNull
  @Override
  public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View view = null;
    // TODO Inflate correct layout.
    return new Holder(view);
  }

  @Override
  public void onBindViewHolder(@NonNull Holder holder, int position) {
    holder.bind(position);
  }

  @Override
  public int getItemCount() {
    return sources.size();
  }

  class Holder extends RecyclerView.ViewHolder {

    private final View itemView;
    // TODO Define new fields for source data.

    public Holder(@NonNull View itemView) {
      super(itemView);
      this.itemView = itemView;
      // TODO Use itemView.findViewById to get references to view objects.
    }

    private void bind(int position) {
      SourceWithQuotes item = sources.get(position);
      // TODO Set contents of view objects to hold contents of model objects.
      itemView.setOnClickListener((v) -> clickListener.onClick(v, getAdapterPosition(), item));
    }

  }

  public interface OnClickListener {

    void onClick(View v, int position, SourceWithQuotes source);

  }

}
