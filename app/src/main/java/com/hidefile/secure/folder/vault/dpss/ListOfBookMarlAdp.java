package com.hidefile.secure.folder.vault.dpss;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.hidefile.secure.folder.vault.R;
import com.hidefile.secure.folder.vault.cluecanva.BookmarkEm;
import java.util.ArrayList;
import java.util.List;

public class ListOfBookMarlAdp extends RecyclerView.Adapter<ListOfBookMarlAdp.ViewHolder> {
    Activity act;
    List<BookmarkEm> arrAppList = new ArrayList<>();
    OnImageItemClickListner listener;
    OnItemLongClickListner onItemLongClickListner;

    public ListOfBookMarlAdp(Activity activity, List<BookmarkEm> arrayList, OnImageItemClickListner listener) {
        this.act = activity;
        this.arrAppList = arrayList;
        this.listener = listener;
    }
    public void setOnItemLongClickListner(OnItemLongClickListner onItemLongClickListner) {
        this.onItemLongClickListner = onItemLongClickListner;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View v = LayoutInflater.from(act).inflate(R.layout.list_bookmark, viewGroup, false);
        return new ViewHolder(v);
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        BookmarkEm data = arrAppList.get(position);
        holder.tv_tital.setText(data.getTitle());
        holder.tv_url.setText(data.getContent());

        holder.ll_root.setOnClickListener(v -> {
            if (listener != null) {
                listener.onImageItemClick(position);
            }
        });
    }
    public void removeAt(int position) {
        arrAppList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, arrAppList.size());
    }
    public List<BookmarkEm> getAllItems() {
        return arrAppList;
    }

    @Override
    public int getItemCount() {
        return arrAppList.size();
    }

    public interface OnItemLongClickListner {

        void onItemLongClick(int position);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView iv_icon;
        TextView tv_tital;
        TextView tv_url;
        LinearLayout ll_root;
        ViewHolder(@NonNull View itemView) {
            super(itemView);
            iv_icon = itemView.findViewById(R.id.iv_icon);
            tv_tital = itemView.findViewById(R.id.tv_tital);
            tv_url = itemView.findViewById(R.id.tv_url);
            ll_root = itemView.findViewById(R.id.lin_root);
            ll_root.setOnLongClickListener(v -> {
                if (onItemLongClickListner != null)
                    onItemLongClickListner.onItemLongClick(getAdapterPosition());
                return false;
            });
        }
    }
}
