package vn.gq.udv.db.items;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import org.json.JSONException;
import org.json.JSONObject;

public class Category
  implements Parcelable
{
  public static final Parcelable.Creator<Category> CREATOR = new Parcelable.Creator()
  {
    public Category createFromParcel(Parcel paramParcel)
    {
      return new Category(paramParcel);
    }

    public Category[] newArray(int paramInt)
    {
      return new Category[paramInt];
    }
  };
  private int catId;
  private String catName;
  private int root;

  public Category()
  {
  }

  public Category(Parcel paramParcel)
  {
    this.catId = paramParcel.readInt();
    this.catName = paramParcel.readString();
    this.root = paramParcel.readInt();
  }

  public Category(String paramString)
    throws JSONException
  {
    JSONObject localJSONObject = new JSONObject(paramString);
    this.catId = localJSONObject.getInt("catId");
    this.catName = localJSONObject.getString("catName");
    this.root = localJSONObject.getInt("root");
  }

  public int describeContents()
  {
    return 0;
  }

  public int getCatId()
  {
    return this.catId;
  }

  public String getCatName()
  {
    return this.catName;
  }

  public int getRoot()
  {
    return this.root;
  }

  public void setCatId(int paramInt)
  {
    this.catId = paramInt;
  }

  public void setCatName(String paramString)
  {
    this.catName = paramString;
  }

  public void setRoot(int paramInt)
  {
    this.root = paramInt;
  }

  public JSONObject toJSONObject()
  {
    JSONObject localJSONObject = new JSONObject();
    try
    {
      localJSONObject.put("catId", this.catId);
      localJSONObject.put("catName", this.catName);
      localJSONObject.put("root", this.root);
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
    paramParcel.writeInt(this.catId);
    paramParcel.writeString(this.catName);
    paramParcel.writeInt(this.root);
  }
}