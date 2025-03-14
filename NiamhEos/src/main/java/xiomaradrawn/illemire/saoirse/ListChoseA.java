package xiomaradrawn.illemire.saoirse;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class ListChoseA {

    public static final String EXTRA_RESULT = "RESULT";
    private boolean mCanChooseOnlyOneItem;
    @Nullable
    private String[] mShowOnlyExtensions;
    @Nullable
    private String[] mExceptExtensions;
    private boolean mIsNewFolderButtonDisabled;
    private boolean mIsSortButtonDisabled;
    private boolean mIsQuitButtonEnabled;
    @NonNull
    private ChoiceType mChoiceType = ChoiceType.ALL;
    @NonNull
    private SortingType mSortingType = SortingType.NAME_ASC;
    @Nullable
    private String mStartDirectory;
    private boolean mUseFirstItemAsUpEnabled;
    private boolean mHideHiddenFilesEnabled;

    public void setCanChooseOnlyOneItem(boolean canChooseOnlyOneItem) {
        mCanChooseOnlyOneItem = canChooseOnlyOneItem;}
    public void setShowOnlyExtensions(@Nullable String... extension) {
        mShowOnlyExtensions = extension;}
    public void setExceptExtensions(@Nullable String... extension) {
        mExceptExtensions = extension;}
    public void setNewFolderButtonDisabled(boolean disabled) {
        mIsNewFolderButtonDisabled = disabled;}
    public void setSortButtonDisabled(boolean disabled) {
        mIsSortButtonDisabled = disabled;
    }
    public void setQuitButtonEnabled(boolean enabled) {
        mIsQuitButtonEnabled = enabled;
    }
    public void setChoiceType(@NonNull ChoiceType choiceType) {
        mChoiceType = choiceType;
    }
    public void setSortingType(@NonNull SortingType sortingType) {
        mSortingType = sortingType;
    }
    public void setStartDirectory(@Nullable String startDirectory) {
        mStartDirectory = startDirectory;}
    public void setUseFirstItemAsUpEnabled(boolean enabled) {
        mUseFirstItemAsUpEnabled = enabled;
    }
    public void setHideHiddenFilesEnabled(boolean enabled) {
        mHideHiddenFilesEnabled = enabled;
    }
    public void start(@NonNull Activity activity, int requestCode) {activity.startActivityForResult(createIntent(activity), requestCode);}
    public void start(@NonNull Fragment fragment, int requestCode) {fragment.startActivityForResult(createIntent(fragment.getContext()), requestCode);}
    public void start(@NonNull android.app.Fragment fragment, int requestCode) {Context context;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            context = fragment.getContext();} else {context = fragment.getActivity();}fragment.startActivityForResult(createIntent(context), requestCode);}
    @NonNull
    private Intent createIntent(@NonNull Context context) {
        Intent intent = new Intent(context, ListFileTick.class);
        intent.putExtra(ListFileTick.EXTRA_CAN_CHOOSE_ONLY_ONE_ITEM, mCanChooseOnlyOneItem);
        intent.putExtra(ListFileTick.EXTRA_SHOW_ONLY_EXTENSIONS, mShowOnlyExtensions);
        intent.putExtra(ListFileTick.EXTRA_EXCEPT_EXTENSIONS, mExceptExtensions);
        intent.putExtra(ListFileTick.EXTRA_IS_NEW_FOLDER_BUTTON_DISABLED, mIsNewFolderButtonDisabled);
        intent.putExtra(ListFileTick.EXTRA_IS_SORT_BUTTON_DISABLED, mIsSortButtonDisabled);
        intent.putExtra(ListFileTick.EXTRA_IS_QUIT_BUTTON_ENABLED, mIsQuitButtonEnabled);
        intent.putExtra(ListFileTick.EXTRA_CHOICE_TYPE, mChoiceType);
        intent.putExtra(ListFileTick.EXTRA_SORTING_TYPE, mSortingType);
        intent.putExtra(ListFileTick.EXTRA_START_DIRECTORY, mStartDirectory);
        intent.putExtra(ListFileTick.EXTRA_USE_FIRST_ITEM_AS_UP_ENABLED, mUseFirstItemAsUpEnabled);
        intent.putExtra(ListFileTick.EXTRA_HIDE_HIDDEN_FILES, mHideHiddenFilesEnabled);
        return intent;}
    public enum ChoiceType {
        ALL, FILES, DIRECTORIES}
    public enum SortingType {
        NAME_ASC, NAME_DESC, SIZE_ASC, SIZE_DESC, DATE_ASC, DATE_DESC}
}
