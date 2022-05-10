package com.example.lab3_pi;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Klassa implementująca interfejs Parcelable dzięki której będziemy w stanie
 * sledzic status pobierania, dzięki implementacji Parcelable jesteśmy w stanie spakować
 * obiekt tej klasy jako Extra prz wysyłaniu broadcastu
 */

public class ProgressInfo implements Parcelable {

    private int downloadedBytes;
    private String status;
    private int size;


    protected ProgressInfo(int downloadedBytes, String status, int size) {
        this.downloadedBytes = downloadedBytes;
        this.status = status;
        this.size = size;
    }

    protected ProgressInfo(Parcel in) {
        downloadedBytes = in.readInt();
        status = in.readString();
        size = in.readInt();
    }

    public static final Creator<ProgressInfo> CREATOR = new Creator<ProgressInfo>() {
        @Override
        public ProgressInfo createFromParcel(Parcel in) {
            return new ProgressInfo(in);
        }

        @Override
        public ProgressInfo[] newArray(int size) {
            return new ProgressInfo[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(downloadedBytes);
        parcel.writeString(status);
        parcel.writeInt(size);
    }

    public int getSize(){
        return this.size;
    }
    public int getDownloadedBytes(){
        return downloadedBytes;
    }
    public String getStatus(){
        return status;
    }
}
