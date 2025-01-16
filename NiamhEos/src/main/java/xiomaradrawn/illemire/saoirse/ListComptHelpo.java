package xiomaradrawn.illemire.saoirse;


import androidx.annotation.NonNull;

public class ListComptHelpo {
    @NonNull
    public static ListComprtn getComparator(ListChoseA.SortingType sortingType) {
        switch (sortingType) {
            case NAME_DESC:
                return new ListNameDescComprtn();
            case SIZE_ASC:
                return new ListSizeAscComprtn();
            case SIZE_DESC:
                return new ListSizeDescComprtn();
            case DATE_ASC:
                return new ListDateAscComprtn();
            case DATE_DESC:
                return new ListDateDescComprtn();
            default:
                return new ListNameAscComprtn();
        }
    }
}
