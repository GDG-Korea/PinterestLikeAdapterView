package com.huewu.pla.lib;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.SparseArray;
import android.util.SparseIntArray;

/**
 * Created by juyeong on 7/15/14.
 */
public class ParcelableSparseIntArray extends SparseIntArray implements Parcelable {

    public ParcelableSparseIntArray() {
    }

    public ParcelableSparseIntArray(int initialCapacity) {
        super(initialCapacity);
    }

    @SuppressWarnings("unchecked")
    private ParcelableSparseIntArray(Parcel in) {
        append(in.readSparseArray (ClassLoader.getSystemClassLoader()));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeSparseArray(toSparseArray());
    }

    private SparseArray<Object> toSparseArray() {
        SparseArray<Object> sparseArray = new SparseArray<Object>();
        for (int i = 0, size = size(); i < size; i++)
            sparseArray.append(keyAt(i), valueAt(i));
        return sparseArray;
    }

    private void append(SparseArray<Integer> sparseArray) {
        for (int i = 0, size = sparseArray.size(); i < size; i++)
            put(sparseArray.keyAt(i), sparseArray.valueAt(i));
    }

    public static final Parcelable.Creator<ParcelableSparseIntArray> CREATOR = new Parcelable.Creator<ParcelableSparseIntArray>() {
        public ParcelableSparseIntArray createFromParcel(Parcel source) {
            return new ParcelableSparseIntArray(source);
        }

        public ParcelableSparseIntArray[] newArray(int size) {
            return new ParcelableSparseIntArray[size];
        }
    };
}
