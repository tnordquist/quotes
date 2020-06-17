package edu.cnm.deepdive.quotes.model.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(
    foreignKeys = @ForeignKey(
        entity = Source.class,
        parentColumns = "source_id",
        childColumns = "source_id",
        onDelete = ForeignKey.SET_NULL
    )
)
public class Quote {

  @PrimaryKey(autoGenerate = true)
  @ColumnInfo(name = "quote_id")
  private long id;

  @ColumnInfo(name = "source_id", index = true)
  private Long sourceId;

  @NonNull
  @ColumnInfo(collate = ColumnInfo.NOCASE)
  private String text = "";

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public Long getSourceId() {
    return sourceId;
  }

  public void setSourceId(Long sourceId) {
    this.sourceId = sourceId;
  }

  @NonNull
  public String getText() {
    return text;
  }

  public void setText(@NonNull String text) {
    this.text = text;
  }

  @NonNull
  @Override
  public String toString() {
    return text;
  }

}
