package stighbvm.uials.no.rubikkannonsesystemapp;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;


public class Item {
    long id;
    String title;
    BigDecimal price;
    String description;


public Item(JSONObject jo) throws JSONException{
    setId(jo.getLong("id"));
    setTitle(jo.getString("title"));

   String stringPrice = jo.getString("price");
   BigDecimal price = new BigDecimal(stringPrice);
    setPrice(price);

    if(jo.has("description")){
        setDescription(jo.getString("description"));
    }
}

public long getId() {return id;}

public void setId(long id) {this.id = id;}

public String getTitle() {return title;}

public void setTitle(String title){this.title = title;}

public BigDecimal getPrice() {return price;}

public void setPrice(BigDecimal price) {this.price = price;}

public String getDescription() {return description;}

public void setDescription(String description) {this.description = description;}

}