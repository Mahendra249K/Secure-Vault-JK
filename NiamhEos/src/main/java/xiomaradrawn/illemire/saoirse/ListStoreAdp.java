package xiomaradrawn.illemire.saoirse;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class ListStoreAdp extends RecyclerView.Adapter<ListStoreFile> {
    private static final int ITEM_TYPE_LIST_FILE = 0;
    private static final int ITEM_TYPE_GRID_FILE = 1;
    private static final int ITEM_TYPE_LIST_DIRECTORY = 2;
    private static final int ITEM_TYPE_GRID_DIRECTORY = 3;
    private static final int ITEM_TYPE_GRID_UP = 4;
    private static final int ITEM_TYPE_LIST_UP = 5;
    @NonNull
    private List<File> mItems = new ArrayList<>();
    @NonNull
    private ArrayList<File> mNotFilteredItems = new ArrayList<>();
    @NonNull
    private List<String> mSelectedItems = new ArrayList<>();
    @Nullable
    private ListItemClick mListener;
    private boolean mIsMultiChoiceModeEnabled;
    private boolean mIsGridModeEnabled;
    private boolean mCanChooseOnlyFiles;
    private boolean mUseFirstItemAsUpEnabled;

    @Override
    public ListStoreFile onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case ITEM_TYPE_LIST_FILE:
                return new ListStoreData(inflate(parent, R.layout.ex_lst));
            case ITEM_TYPE_GRID_FILE:
                return new ListStoreData(inflate(parent, R.layout.ex_grd));
            case ITEM_TYPE_LIST_DIRECTORY:
                return new ListStoreFileDir(inflate(parent, R.layout.ex_lst));
            case ITEM_TYPE_LIST_UP:
                return new ListFurUp(inflate(parent, R.layout.ex_lst));
            case ITEM_TYPE_GRID_UP:
                return new ListFurUp(inflate(parent, R.layout.ex_lst));
            default:
                return new ListStoreFileDir(inflate(parent, R.layout.ex_grd));
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (mUseFirstItemAsUpEnabled && position == 0) {
            return mIsGridModeEnabled ? ITEM_TYPE_GRID_UP : ITEM_TYPE_LIST_UP;
        } else {
            if (mIsGridModeEnabled) {
                return getItem(position).isDirectory() ? ITEM_TYPE_GRID_DIRECTORY : ITEM_TYPE_GRID_FILE;
            } else {
                return getItem(position).isDirectory() ? ITEM_TYPE_LIST_DIRECTORY : ITEM_TYPE_LIST_FILE;
            }
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ListStoreFile holder, int position) {
        if (getItemViewType(position) == ITEM_TYPE_LIST_UP || getItemViewType(position) == ITEM_TYPE_GRID_UP) {
            ((ListFurUp) holder).bind(mListener);
        } else {
            holder.bind(getItem(position), mIsMultiChoiceModeEnabled, isItemSelected(position), mListener);
        }
    }

    @Override
    public int getItemCount() {
        if (mUseFirstItemAsUpEnabled) {
            return mItems.size() + 1;
        } else {
            return mItems.size();
        }
    }

    public void setItems(@NonNull List<File> items, @NonNull ListChoseA.SortingType sortingType) {
        mSelectedItems.clear();
        mItems.clear();
        mItems.addAll(items);
        sort(sortingType);
    }

    @NonNull
    public File getItem(int position) {
        if (mUseFirstItemAsUpEnabled) {
            return mItems.get(position - 1);
        } else {
            return mItems.get(position);
        }
    }

    public void setOnListItemClickListener(@Nullable ListItemClick listener) {
        mListener = listener;
    }

    public void setMultiChoiceModeEnabled(boolean enabled) {
        mIsMultiChoiceModeEnabled = enabled;
        if (!mIsMultiChoiceModeEnabled) {
            mSelectedItems.clear();
        }
        if (mCanChooseOnlyFiles) {
            if (enabled) {
                mNotFilteredItems = new ArrayList<>(mItems);
                removeDirectories(mItems);
            } else {
                mItems = new ArrayList<>(mNotFilteredItems);
            }
        }
        notifyDataSetChanged();
    }

    public void setItemSelected(int position, boolean selected) {
        String name = getItem(position).getName();
        if (selected) {
            mSelectedItems.add(name);
        } else {
            mSelectedItems.remove(name);
        }
        notifyItemChanged(position);
    }

    public boolean isItemSelected(int position) {
        return mSelectedItems.contains(getItem(position).getName());
    }

    public void sort(@NonNull ListChoseA.SortingType sortingType) {
        Collections.sort(mItems, ListComptHelpo.getComparator(sortingType));
        notifyDataSetChanged();
    }

    public void selectAll() {
        mSelectedItems.clear();
        for (File item : mItems) {
            mSelectedItems.add(item.getName());
        }
        notifyDataSetChanged();
    }

    public void deselect() {
        mSelectedItems.clear();
        notifyDataSetChanged();
    }

    public void invertSelection() {
        List<String> selectedItems = new ArrayList<>();
        for (File item : mItems) {
            if (!mSelectedItems.contains(item.getName())) {
                selectedItems.add(item.getName());
            }
        }
        mSelectedItems = new ArrayList<>(selectedItems);
        notifyDataSetChanged();
    }

    public boolean isGridModeEnabled() {
        return mIsGridModeEnabled;
    }

    public void setGridModeEnabled(boolean enabled) {
        mIsGridModeEnabled = enabled;
        notifyDataSetChanged();
    }

    @NonNull
    public List<String> getSelectedItems() {
        return mSelectedItems;
    }

    public void setCanChooseOnlyFiles(boolean canChooseOnlyFiles) {
        mCanChooseOnlyFiles = canChooseOnlyFiles;
    }

    public void setUseFirstItemAsUpEnabled(boolean enabled) {
        mUseFirstItemAsUpEnabled = enabled;
    }

    @NonNull
    private View inflate(@NonNull ViewGroup parent, @LayoutRes int layout) {
        return LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
    }

    private void removeDirectories(List<File> items) {
        ListCommend.filterList(items, new ListCommend.ConditionChecker<File>() {
            @Override
            public boolean check(@NonNull File file) {
                return file.isDirectory();
            }
        });
    }
}
