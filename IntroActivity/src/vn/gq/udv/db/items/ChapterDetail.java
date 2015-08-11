package vn.gq.udv.db.items;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class ChapterDetail
  implements Parcelable
{
  public static final Parcelable.Creator<ChapterDetail> CREATOR = new Parcelable.Creator()
  {
    public ChapterDetail createFromParcel(Parcel paramParcel)
    {
      return new ChapterDetail(paramParcel);
    }

    public ChapterDetail[] newArray(int paramInt)
    {
      return new ChapterDetail[paramInt];
    }
  };
  private int catId;
  private String detail;
  private int id;

  public ChapterDetail()
  {
  }

  public ChapterDetail(Parcel paramParcel)
  {
    this.id = paramParcel.readInt();
    this.detail = paramParcel.readString();
    this.catId = paramParcel.readInt();
  }

  public int describeContents()
  {
    return 0;
  }

  public int getCatId()
  {
    return this.catId;
  }

  public String getDetail()
  {
    return this.detail;
  }

  public int getId()
  {
    return this.id;
  }

  public void setCatId(int paramInt)
  {
    this.catId = paramInt;
  }

  public void setDetail(String paramString)
  {
    this.detail = paramString;
  }

  public void setId(int paramInt)
  {
    this.id = paramInt;
  }

  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeInt(this.id);
    paramParcel.writeString(this.detail);
    paramParcel.writeInt(this.catId);
  }
}