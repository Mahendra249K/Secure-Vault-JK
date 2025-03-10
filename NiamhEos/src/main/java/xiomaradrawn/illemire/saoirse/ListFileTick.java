package xiomaradrawn.illemire.saoirse;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;

public class ListFileTick extends AppCompatActivity implements ListItemClick,
        Toolbar.OnMenuItemClickListener, View.OnClickListener, ListDig.OnNewFolderNameEnteredListener,
        ListSortDig.OnSortingSelectedListener, ListStoreDig.OnStorageSelectedListener {

    public static final String EXTRA_CAN_CHOOSE_ONLY_ONE_ITEM = "CAN_CHOOSE_ONLY_ONE_ITEM";
    public static final String EXTRA_SHOW_ONLY_EXTENSIONS = "SHOW_ONLY_EXTENSIONS";
    public static final String EXTRA_EXCEPT_EXTENSIONS = "EXCEPT_EXTENSIONS";
    public static final String EXTRA_IS_NEW_FOLDER_BUTTON_DISABLED = "IS_NEW_FOLDER_BUTTON_DISABLED";
    public static final String EXTRA_IS_SORT_BUTTON_DISABLED = "IS_SORT_BUTTON_DISABLED";
    public static final String EXTRA_IS_QUIT_BUTTON_ENABLED = "IS_QUIT_BUTTON_ENABLED";
    public static final String EXTRA_CHOICE_TYPE = "CHOICE_TYPE";
    public static final String EXTRA_SORTING_TYPE = "SORTING_TYPE";
    public static final String EXTRA_START_DIRECTORY = "START_DIRECTORY";
    public static final String EXTRA_USE_FIRST_ITEM_AS_UP_ENABLED = "USE_FIRST_ITEM_AS_UP_ENABLED";
    public static final String EXTRA_HIDE_HIDDEN_FILES = "HIDE_HIDDEN_FILES";
    public static final String PERMISSION_READ_EXTERNAL_STORAGE = "android.permission.READ_EXTERNAL_STORAGE";
    private static final String DIRECTORY_STATE = "DIRECTORY_STATE";
    private static final int REQUEST_CODE_READ_EXTERNAL_STORAGE = 1;
    private static final int REQUEST_CODE_WRITE_EXTERNAL_STORAGE = 2;
    private static final String TOP_DIRECTORY = "/";

    private boolean mCanChooseOnlyOneItem;
    @Nullable
    private String[] mShowOnlyExtensions;
    @Nullable
    private String[] mExceptExtensions;
    private boolean mIsNewFolderButtonDisabled;
    private boolean mIsSortButtonDisabled;
    private boolean mIsQuitButtonEnabled;
    @NonNull
    private ListChoseA.ChoiceType mChoiceType = ListChoseA.ChoiceType.ALL;
    @NonNull
    private ListChoseA.SortingType mSortingType = ListChoseA.SortingType.NAME_ASC;
    private File mCurrentDirectory;
    private ListTools mToolbar;
    private RecyclerView mRecyclerView;
    private View mEmptyView;
    private ListStoreAdp mAdapter;
    private boolean mIsMultiChoiceModeEnabled;
    private boolean mUseFirstItemAsUpEnabled;
    private boolean mHideHiddenFiles;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.ex_pick);

        handleIntent();
        setupViews();

        if (savedInstanceState != null && savedInstanceState.containsKey(DIRECTORY_STATE)) {
            mCurrentDirectory = new File(savedInstanceState.getString(DIRECTORY_STATE));
        }

        if (ContextCompat.checkSelfPermission(this, PERMISSION_READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            readDirectory(mCurrentDirectory);
        } else {
            ActivityCompat.requestPermissions(this, new String[]{PERMISSION_READ_EXTERNAL_STORAGE}, REQUEST_CODE_READ_EXTERNAL_STORAGE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_READ_EXTERNAL_STORAGE:
                readDirectory(mCurrentDirectory);
                break;
            case REQUEST_CODE_WRITE_EXTERNAL_STORAGE:
                showNewFolderDialog();
                break;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onListItemClick(int position) {
        if (mIsMultiChoiceModeEnabled) {
            if (mCanChooseOnlyOneItem) {
                mAdapter.deselect();
            }
            mAdapter.setItemSelected(position, !mAdapter.isItemSelected(position));
        } else {
            if (position == ListItemClick.POSITION_UP) {
                readUpDirectory();
            } else {
                File item = mAdapter.getItem(position);
                if (item.isDirectory()) {
                    mCurrentDirectory = new File(mCurrentDirectory, item.getName());
                    readDirectory(mCurrentDirectory);
                } else {
                    finishWithResult(mCurrentDirectory, item.getName());
                }
            }
        }
    }

    @Override
    public void onListItemLongClick(int position) {
        if (!mIsMultiChoiceModeEnabled && position != ListItemClick.POSITION_UP) {
            mIsMultiChoiceModeEnabled = true;
            if (mChoiceType != ListChoseA.ChoiceType.FILES || !mAdapter.getItem(position).isDirectory()) {
                mAdapter.setItemSelected(position, true);
            }
            setMultiChoiceModeEnabled(true);
        }
    }

    @Override
    public boolean onMenuItemClick(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.ok) {
            if (mIsMultiChoiceModeEnabled) {
                finishWithResult(mCurrentDirectory, mAdapter.getSelectedItems());
            } else if (mChoiceType == ListChoseA.ChoiceType.DIRECTORIES) {
                if (isTopDirectory(mCurrentDirectory)) {
                    finishWithResult(mCurrentDirectory, "/");
                } else {
                    finishWithResult(mCurrentDirectory.getParentFile(), mCurrentDirectory.getName());
                }
            }
        } else if (itemId == R.id.sort) {
            ListSortDig dialog = new ListSortDig(this);
            dialog.setOnSortingSelectedListener(this);
            dialog.show();
        } else if (itemId == R.id.storage) {
            ListStoreDig dialog = new ListStoreDig(this);
            dialog.setOnStorageSelectedListener(this);
            dialog.show();
        } else if (itemId == R.id.new_folder) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                showNewFolderDialog();
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_WRITE_EXTERNAL_STORAGE);
            }
        } else if (itemId == R.id.select_all) {
            mAdapter.selectAll();
        } else if (itemId == R.id.deselect) {
            mAdapter.deselect();
        } else if (itemId == R.id.invert_selection) {
            mAdapter.invertSelection();
        } else if (itemId == R.id.change_view) {
            toggleViewMode();
        } else {
            return false;
        }
        return true;
    }

    @Override
    public void onClick(@NonNull View view) {
        if (mIsMultiChoiceModeEnabled) {
            setMultiChoiceModeEnabled(false);
            setupOkButtonVisibility();
        } else {
            finish();
        }
    }

    @Override
    public boolean dispatchKeyEvent(@NonNull KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            if (event.getAction() == KeyEvent.ACTION_UP) {
                if (mIsMultiChoiceModeEnabled) {
                    setMultiChoiceModeEnabled(false);
                    setupOkButtonVisibility();
                } else {
                    if (isTopDirectory(mCurrentDirectory)) {
                        finish();
                    } else {
                        readUpDirectory();
                    }
                }
            } else if (event.getAction() == KeyEvent.ACTION_DOWN && (event.getFlags() & KeyEvent.FLAG_LONG_PRESS) == KeyEvent.FLAG_LONG_PRESS) {
                finish();
            }
            return true;
        }
        return super.dispatchKeyEvent(event);
    }

    @Override
    public void onNewFolderNameEntered(@NonNull String name) {
        if (name.length() > 0) {
            File file = new File(mCurrentDirectory, name);
            if (file.exists()) {
                Toast.makeText(ListFileTick.this, R.string.efp__folder_already_exists, Toast.LENGTH_SHORT).show();
            } else {
                if (file.mkdir()) {
                    readDirectory(mCurrentDirectory);
                    Toast.makeText(ListFileTick.this, R.string.efp__folder_created, Toast.LENGTH_SHORT).show();
                } else
                    Toast.makeText(ListFileTick.this, R.string.efp__folder_not_created, Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onSortingSelected(@NonNull ListChoseA.SortingType sortingType) {
        mSortingType = sortingType;
        mAdapter.sort(mSortingType);
    }

    @Override
    public void onStorageSelected(String path) {
        mCurrentDirectory = new File(path);
        readDirectory(mCurrentDirectory);
    }

    private void readUpDirectory() {
        mCurrentDirectory = mCurrentDirectory.getParentFile();
        readDirectory(mCurrentDirectory);
    }

    private void readDirectory(@NonNull File directory) {
        setTitle(directory);
        mAdapter.setUseFirstItemAsUpEnabled(!isTopDirectory(directory) && mUseFirstItemAsUpEnabled);
        File[] files = directory.listFiles();
        if (files == null || files.length == 0) {
            if (mUseFirstItemAsUpEnabled) {
                mRecyclerView.setVisibility(View.VISIBLE);
                mEmptyView.setVisibility(View.GONE);
                mAdapter.setItems(new ArrayList<File>(), mSortingType);
            } else {
                mEmptyView.setVisibility(View.VISIBLE);
                mRecyclerView.setVisibility(View.GONE);
            }
        } else {
            mRecyclerView.setVisibility(View.VISIBLE);
            mEmptyView.setVisibility(View.GONE);
            List<File> list = new ArrayList<>();
            ListCommend.ConditionChecker<File> checker;
            if (mShowOnlyExtensions != null && mShowOnlyExtensions.length > 0 && mChoiceType != ListChoseA.ChoiceType.DIRECTORIES) {
                final List<String> showOnlyExtensions = Arrays.asList(mShowOnlyExtensions);
                checker = new ListCommend.ConditionChecker<File>() {
                    @Override
                    public boolean check(@NonNull File file) {
                        return file.isDirectory() || showOnlyExtensions.contains(ListCommand.getFileExtension(file.getName()));
                    }
                };
            } else {
                if (mChoiceType == ListChoseA.ChoiceType.DIRECTORIES) {
                    checker = new ListCommend.ConditionChecker<File>() {
                        @Override
                        public boolean check(@NonNull File file) {
                            return file.isDirectory();
                        }
                    };
                } else {
                    checker = null;
                }
            }
            ListCommend.copyListWithCondition(files, list, checker);
            if (mExceptExtensions != null && mExceptExtensions.length > 0 && mChoiceType != ListChoseA.ChoiceType.DIRECTORIES) {
                final List<String> exceptExtensions = Arrays.asList(mExceptExtensions);
                ListCommend.filterList(list, new ListCommend.ConditionChecker<File>() {
                    @Override
                    public boolean check(@NonNull File file) {
                        return !file.isDirectory() && exceptExtensions.contains(ListCommand.getFileExtension(file.getName()));
                    }
                });
            }
            if (mHideHiddenFiles) {
                ListCommend.filterList(list, new ListCommend.ConditionChecker<File>() {
                    @Override
                    public boolean check(@NonNull File file) {
                        return file.isHidden();
                    }
                });
            }
            mAdapter.setItems(list, mSortingType);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressLint("ResourceAsColor")
    private void setTitle(@NonNull File directory) {
        if (isTopDirectory(directory)) {
            mToolbar.setTitle(TOP_DIRECTORY);
        } else {
            mToolbar.setTitle(directory.getName());
            mToolbar.setTitleTextColor(getColor(R.color.black));
        }
    }

    private void handleIntent() {
        Intent intent = getIntent();
        mCanChooseOnlyOneItem = intent.getBooleanExtra(EXTRA_CAN_CHOOSE_ONLY_ONE_ITEM, false);
        mShowOnlyExtensions = intent.getStringArrayExtra(EXTRA_SHOW_ONLY_EXTENSIONS);
        mExceptExtensions = intent.getStringArrayExtra(EXTRA_EXCEPT_EXTENSIONS);
        mIsNewFolderButtonDisabled = intent.getBooleanExtra(EXTRA_IS_NEW_FOLDER_BUTTON_DISABLED, false);
        mIsSortButtonDisabled = intent.getBooleanExtra(EXTRA_IS_SORT_BUTTON_DISABLED, false);
        mIsQuitButtonEnabled = intent.getBooleanExtra(EXTRA_IS_QUIT_BUTTON_ENABLED, false);
        mChoiceType = (ListChoseA.ChoiceType) intent.getSerializableExtra(EXTRA_CHOICE_TYPE);
        mSortingType = (ListChoseA.SortingType) intent.getSerializableExtra(EXTRA_SORTING_TYPE);
        mCurrentDirectory = getStartDirectory(intent);
        mUseFirstItemAsUpEnabled = intent.getBooleanExtra(EXTRA_USE_FIRST_ITEM_AS_UP_ENABLED, false);
        mHideHiddenFiles = intent.getBooleanExtra(EXTRA_HIDE_HIDDEN_FILES, false);
    }

    private int calculateGridColumnsCount() {
        return (int) (getResources().getDisplayMetrics().widthPixels / getResources().getDimension(R.dimen.files_grid_item_size));
    }

    private void showNewFolderDialog() {
        ListDig dialog = new ListDig(this);
        dialog.setOnNewFolderNameEnteredListener(this);
        dialog.show();
    }

    private void toggleViewMode() {
        MenuItem menuItem = mToolbar.getMenu().findItem(R.id.change_view);
        if (mAdapter.isGridModeEnabled()) {
            mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            menuItem.setIcon(ListCommand.attrToResId(this, R.attr.xtp_grd));
            menuItem.setTitle(R.string.efp__action_grid);
            mAdapter.setGridModeEnabled(false);
        } else {
            mRecyclerView.setLayoutManager(new GridLayoutManager(this, calculateGridColumnsCount()));
            menuItem.setIcon(ListCommand.attrToResId(this, R.attr.xtp_lst));
            menuItem.setTitle(R.string.efp__action_list);
            mAdapter.setGridModeEnabled(true);
        }
        setChangeViewIcon(mToolbar.getMenu());
    }

    private void setMultiChoiceModeEnabled(boolean enabled) {
        mIsMultiChoiceModeEnabled = enabled;
        mToolbar.setMultiChoiceModeEnabled(enabled);
        mAdapter.setUseFirstItemAsUpEnabled(!enabled && mUseFirstItemAsUpEnabled && !isTopDirectory(mCurrentDirectory));
        mAdapter.setMultiChoiceModeEnabled(enabled);
        setChangeViewIcon(mToolbar.getMenu());
    }

    private boolean isTopDirectory(@Nullable File directory) {
        return directory != null && TOP_DIRECTORY.equals(directory.getAbsolutePath());
    }

    private void setChangeViewIcon(@NonNull Menu menu) {
        MenuItem item = menu.findItem(R.id.change_view);
        if (item != null) {
            item.setIcon(ListCommand.attrToResId(this, mAdapter.isGridModeEnabled() ? R.attr.xtp_lst : R.attr.xtp_grd));
            item.setTitle(mAdapter.isGridModeEnabled() ? R.string.efp__action_list : R.string.efp__action_grid);
        }
    }

    private void finishWithResult(@NonNull File path, @NonNull String file) {
        finishWithResult(path, new ArrayList<>(Collections.singletonList(file)));
    }

    private void finishWithResult(@NonNull File path, @NonNull List<String> files) {
        String resultPath = path.getAbsolutePath();
        if (!resultPath.endsWith("/")) {
            resultPath += "/";
        }
        ListResPickStore result = new ListResPickStore(resultPath, files);
        Intent intent = new Intent();
        intent.putExtra(ListChoseA.EXTRA_RESULT, result);
        setResult(RESULT_OK, intent);
        finish();
    }

    private void setupViews() {
        mRecyclerView = findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new ListStoreAdp();
        mAdapter.setOnListItemClickListener(this);
        mAdapter.setCanChooseOnlyFiles(mChoiceType == ListChoseA.ChoiceType.FILES);
        mAdapter.setUseFirstItemAsUpEnabled(mUseFirstItemAsUpEnabled);
        mRecyclerView.setAdapter(mAdapter);
        mToolbar = findViewById(R.id.main_toolbar);
        mToolbar.setOnMenuItemClickListener(this);
        mToolbar.setNavigationOnClickListener(this);
        mToolbar.setQuitButtonEnabled(mIsQuitButtonEnabled);
        mToolbar.setMultiChoiceModeEnabled(false);
        Menu menu = mToolbar.getMenu();
        setupOkButtonVisibility();
        menu.findItem(R.id.new_folder).setVisible(!mIsNewFolderButtonDisabled);
        menu.findItem(R.id.sort).setVisible(!mIsSortButtonDisabled);
        mEmptyView = findViewById(R.id.empty_view);
    }

    private void setupOkButtonVisibility() {
        mToolbar.getMenu().findItem(R.id.ok).setVisible(mChoiceType == ListChoseA.ChoiceType.DIRECTORIES);
    }

    @NonNull
    private File getStartDirectory(@NonNull Intent intent) {
        File path = null;
        String startPath = intent.getStringExtra(EXTRA_START_DIRECTORY);
        if (startPath != null && startPath.length() > 0) {
            File tmp = new File(startPath);
            if (tmp.exists() && tmp.isDirectory()) {
                path = tmp;
            }
        }
        if (path == null) {
            LinkedHashMap<String, String> storages = ListCommand.getExternalStoragePaths(this);
            if (storages.size() > 0) {
                path = new File((String) storages.keySet().toArray()[0]);
            }
        }
        if (path == null) {
            path = new File("/");
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                path = Environment.getExternalStorageDirectory();
            }
        }
        return path;
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putString(DIRECTORY_STATE, mCurrentDirectory.getAbsolutePath());
    }
}
