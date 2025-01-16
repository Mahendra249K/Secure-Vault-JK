package com.hidefile.secure.folder.vault.dpss;

import static com.hidefile.secure.folder.vault.edptrs.InSafe.isGridlayout;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.Joaquin.thiago.ListItVid;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.hidefile.secure.folder.vault.R;
import com.hidefile.secure.folder.vault.cluecanva.TillStr;
import com.hidefile.secure.folder.vault.cluecanva.VTv;

import java.io.File;
import java.util.ArrayList;

public class VideoListAdp extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public OnImageItemClickListner onImageItemClickListner;
    public int spanCount = 1;
    Context mContext;
    int mImageResize;
    RequestOptions options;
    boolean isSelected = false;
    private ArrayList<ListItVid> mediaFiles;

    public VideoListAdp(Context mcContext, ArrayList<ListItVid> mediaFiles) {
        this.mediaFiles = mediaFiles;
        this.mContext = mcContext;
    }

    public void setData(ArrayList<ListItVid> videoList) {
        this.mediaFiles = videoList;
    }

    public void setItemClickEvent(OnImageItemClickListner onImageItemClickListner) {
        this.onImageItemClickListner = onImageItemClickListner;
    }

    public void setImageResize(int imageSize) {
        this.mImageResize = imageSize;

        options = new RequestOptions()
                .centerCrop()
                .override(mImageResize, mImageResize)
                .placeholder(R.drawable.ic_placeholder);
    }

    public void setSpanCount(int spanCount) {
        this.spanCount = spanCount;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (!isGridlayout) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_file_adapter, parent, false);
            return new ViewHolderList(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_image_adapter, parent, false);
            return new ViewHolderGrid(view);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return isGridlayout ? 0 : 1;
    }

    public void setSelectionMode(boolean selectionMode) {
        isSelected = selectionMode;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == 0) {
            ViewHolderGrid viewHolderGrid = (ViewHolderGrid) holder;

            File mediaFile = new File(mediaFiles.get(position).getNewPath());

            String parth = mediaFile.getAbsolutePath();
            File targetLocation = new File(parth);
            viewHolderGrid.media_thumbnail.setVisibility(View.VISIBLE);

            Glide.with(mContext)
                    .load(mediaFiles.get(position).getNewPath())
                    .apply(options)
                    .into(viewHolderGrid.media_thumbnail);

            viewHolderGrid.tv_filename.setText(mediaFiles.get(position).getDisplayName());
            viewHolderGrid.tv_size.setText(TillStr.convertStorage(targetLocation.length()));
            viewHolderGrid.tv_filename.setVisibility(View.GONE);
            viewHolderGrid.tv_size.setVisibility(View.GONE);


            viewHolderGrid.iv_chek.requestLayout();
            if (mediaFiles.get(position).checked) {
                viewHolderGrid.iv_chek.setVisibility(View.VISIBLE);
            } else {
                viewHolderGrid.iv_chek.setVisibility(View.INVISIBLE);
            }
            viewHolderGrid.iv_chek.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onImageItemClickListner != null) {
                        onImageItemClickListner.onImageItemClick(position);
                    }
                }
            });
            viewHolderGrid.frm_root.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onImageItemClickListner != null) {
                        onImageItemClickListner.onImageItemClick(position);
                    }
                }
            });
            viewHolderGrid.iv_chek.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (onImageItemClickListner != null) {
                        onImageItemClickListner.onImageItemLongClick(position);
                    }
                    return false;
                }
            });
            viewHolderGrid.frm_root.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (onImageItemClickListner != null) {
                        onImageItemClickListner.onImageItemLongClick(position);
                    }
                    return false;}});
        } else if (getItemViewType(position) == 1) {
            ViewHolderList viewHolderList = (ViewHolderList) holder;
            viewHolderList.media_thumbnail.setScaleType(ImageView.ScaleType.CENTER_CROP);
            viewHolderList.media_thumbnail.setVisibility(View.VISIBLE);
            Glide.with(mContext)
                    .load(mediaFiles.get(position).getNewPath())
                    .apply(options)
                    .into(viewHolderList.media_thumbnail);
            viewHolderList.tv_filename.setText(mediaFiles.get(position).getDisplayName());
            viewHolderList.tv_size.setText(TillStr.convertStorage(mediaFiles.get(position).getSize()));
            viewHolderList.tv_filename.setVisibility(View.VISIBLE);
            viewHolderList.tv_size.setVisibility(View.VISIBLE);

            if (mediaFiles.get(position).checked) {
                viewHolderList.iv_chek.setVisibility(View.VISIBLE);
                viewHolderList.iv_chek.setImageResource(R.drawable.check);
            } else {
                viewHolderList.iv_chek.setImageResource(R.drawable.uncheck);
                if (isSelected)
                    viewHolderList.iv_chek.setVisibility(View.VISIBLE);
                else
                    viewHolderList.iv_chek.setVisibility(View.INVISIBLE);
            }

            viewHolderList.iv_chek.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onImageItemClickListner != null) {
                        onImageItemClickListner.onImageItemClick(position);
                    }
                }
            });
            viewHolderList.frm_root.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onImageItemClickListner != null) {
                        onImageItemClickListner.onImageItemClick(position);
                    }
                }
            });
            viewHolderList.iv_chek.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (onImageItemClickListner != null) {
                        onImageItemClickListner.onImageItemLongClick(position);
                        isSelected = true;
                    }
                    return false;
                }
            });
            viewHolderList.frm_root.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (onImageItemClickListner != null) {
                        onImageItemClickListner.onImageItemLongClick(position);
                        isSelected = true;
                    }
                    return false;
                }
            });

        }
    }

    @Override
    public int getItemCount() {
        return mediaFiles.size();
    }


    public class ViewHolderGrid extends RecyclerView.ViewHolder {
        LinearLayout frm_root;
        ImageView media_thumbnail, iv_chek;
        VTv tv_filename, tv_size;

        ViewHolderGrid(View view) {
            super(view);
            media_thumbnail = view.findViewById(R.id.media_thumbnail);
            tv_filename = view.findViewById(R.id.tv_filename);
            tv_size = view.findViewById(R.id.tv_size);
            frm_root = view.findViewById(R.id.frm_root);
            iv_chek = view.findViewById(R.id.iv_chek);
        }
    }

    public class ViewHolderList extends RecyclerView.ViewHolder {
        LinearLayout frm_root;
        ImageView media_thumbnail, iv_chek;
        VTv tv_filename, tv_size;

        ViewHolderList(View view) {
            super(view);
            media_thumbnail = view.findViewById(R.id.media_thumbnail);
            tv_filename = view.findViewById(R.id.tv_filename);
            tv_size = view.findViewById(R.id.tv_size);
            frm_root = view.findViewById(R.id.frm_root);
            iv_chek = view.findViewById(R.id.iv_chek);
        }
    }

}
