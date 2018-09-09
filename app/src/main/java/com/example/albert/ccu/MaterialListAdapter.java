package com.example.albert.ccu;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.webkit.URLUtil;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static android.content.ContentValues.TAG;
import static android.content.Context.DOWNLOAD_SERVICE;

public class MaterialListAdapter extends RecyclerView.Adapter<MaterialListAdapter.MaterialViewHolder>{
  public class MaterialViewHolder extends RecyclerView.ViewHolder {
    private final TextView title, size, date;
    public MaterialViewHolder(View itemView) {
      super(itemView);
      title = itemView.findViewById(R.id.material_desc);
      date = itemView.findViewById(R.id.material_date);
      size = itemView.findViewById(R.id.material_size);
    }
  }

  private final LayoutInflater layoutInflater;
  List<Material> allMaterial;

  private Context context;
  MaterialListAdapter(Context context) {
    layoutInflater = LayoutInflater.from(context);
    this.context = context;
  }

  @NonNull
  @Override
  public MaterialViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View itemView = layoutInflater.inflate(R.layout.material_items, parent, false);
    return new MaterialViewHolder(itemView);
  }

  @Override
  public void onBindViewHolder(@NonNull MaterialViewHolder holder, int position) {
    if(allMaterial != null) {
      final Material current = allMaterial.get(position);
      holder.title.setText(current.getMaterial_desc());
      holder.date.setText(current.getMaterial_date());
      holder.size.setText(converter(current.getMaterial_size()));
      holder.itemView.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          String url = "https://ecourse.ccu.edu.tw" + current.getMaterial_link();
          new DownloadTask(context).execute(url);
        }
      });
    }
  }

  @Override
  public int getItemCount() {
    return allMaterial == null ? 0 : allMaterial.size();
  }

  public void setAllMaterial(List<Material> allMaterial) {
    this.allMaterial = allMaterial;
    notifyDataSetChanged();
  }

  private String converter(String size) {
    float size_f = Float.parseFloat(size);
    DecimalFormat dec = new DecimalFormat("0.00");
    double k = size_f/1024.0;
    double m = size_f/1048576.0;
    double g = size_f/1073741824.0;
    String result;
    if (g > 1) {
      result = dec.format(g).concat("GB");
    } else if (m > 1) {
      result = dec.format(m).concat("MB");
    } else if (k > 1) {
      result = dec.format(k).concat("KB");
    } else {
      result = dec.format(size).concat("B");
    }
    return result;
  }
}
