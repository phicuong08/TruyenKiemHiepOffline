package vn.gq.udv.db.items;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import org.json.JSONException;
import org.json.JSONObject;

public class ChapterItem
  implements Parcelable
{
  public static final Parcelable.Creator<ChapterItem> CREATOR = new Parcelable.Creator()
  {
    public ChapterItem createFromParcel(Parcel paramParcel)
    {
      return new ChapterItem(paramParcel);
    }

    public ChapterItem[] newArray(int paramInt)
    {
      return new ChapterItem[paramInt];
    }
  };
  private int catId;
  private int id;
  private int position = 0;
  private int scrollY = 0;
  private String title;

  public ChapterItem()
  {
  }

  public ChapterItem(Parcel paramParcel)
  {
    this.id = paramParcel.readInt();
    this.catId = paramParcel.readInt();
    this.title = paramParcel.readString();
    this.position = paramParcel.readInt();
    this.scrollY = paramParcel.readInt();
  }

  public ChapterItem(String paramString)
    throws JSONException
  {
    JSONObject localJSONObject = new JSONObject(paramString);
    this.id = localJSONObject.getInt("id");
    this.catId = localJSONObject.getInt("catId");
    this.title = localJSONObject.getString("title");
    this.position = localJSONObject.getInt("position");
    this.scrollY = localJSONObject.getInt("scrollY");
  }

  public int describeContents()
  {
    return 0;
  }

  public int getCatId()
  {
    return this.catId;
  }

  public int getId()
  {
    return this.id;
  }

  public int getPosition()
  {
    return this.position;
  }

  public int getScrollY()
  {
    return this.scrollY;
  }

  public String getTitle()
  {
    return this.title;
  }

  public void setCatId(int paramInt)
  {
    this.catId = paramInt;
  }

  public void setId(int paramInt)
  {
    this.id = paramInt;
  }

  public void setPosition(int paramInt)
  {
    this.position = paramInt;
  }

  public void setScrollYPos(int paramInt)
  {
    this.scrollY = paramInt;
  }

  public void setTitle(String paramString)
  {
    this.title = paramString;
  }

  public JSONObject toJSonObject()
  {
    JSONObject localJSONObject = new JSONObject();
    try
    {
      localJSONObject.put("id", this.id);
      localJSONObject.put("catId", this.catId);
      localJSONObject.put("title", this.title);
      localJSONObject.put("position", this.position);
      localJSONObject.put("scrollY", this.scrollY);
      return localJSONObject;
    }
    catch (JSONException localJSONException)
    {
      localJSONException.printStackTrace();
    }
    return localJSONObject;
  }

  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeInt(this.id);
    paramParcel.writeInt(this.catId);
    paramParcel.writeString(this.title);
    paramParcel.writeInt(this.position);
    paramParcel.writeInt(this.scrollY);
  }
}