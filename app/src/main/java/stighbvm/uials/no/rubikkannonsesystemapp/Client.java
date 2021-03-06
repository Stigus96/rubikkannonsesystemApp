package stighbvm.uials.no.rubikkannonsesystemapp;

import android.util.JsonReader;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class Client {
    private static Client SINGLETON;

    public static final String DATE_FORMAT_PATTERN = "yyyy-MM-dd'T'HH:mm:ss.S'Z'[z]";

    public static final String IP = "192.168.1.68";
    public static final String APIURL = "http://" + IP + ":8080/rubikkannonsesystem_v2/resources/";
    public static final String LOGIN_URL = APIURL + "auth/login";
    public static final String CURRENT_USER_URL = APIURL + "auth/currentuser";
    public static final String ADD_ITEM_URL = APIURL + "REST/addItem";
    public static final String DELETE_ITEM_URL = APIURL + "REST/delete";
    public static final String PURCHASE_ITEM_URL = APIURL + "REST/purchase";
    public static final String GET_ITEM_URL = APIURL + "REST/item/";
    public static final String GET_ITEMS_URL = APIURL + "REST/items";
    public static final String CREATE_USER_URL = APIURL + "auth/create";

    public static SimpleDateFormat DATE_FORMAT = new SimpleDateFormat(DATE_FORMAT_PATTERN);

    /** JWT token returned from login */
    String token;



    public static synchronized Client getSingleton() {
        if(SINGLETON == null) {
            SINGLETON = new Client();
        }

        return SINGLETON;
    }


    public interface OnItemsLoaded{
        void onLoaded(List<Item> items);
    }

    protected User user;
    protected OnItemsLoaded onItemsLoaded = items ->{};
    protected List<Item> items = new ArrayList<>();

    public void setItemsLoadedListener(OnItemsLoaded listener){
        this.onItemsLoaded = listener;
    }


    public HttpURLConnection getSecureConnection(String url) throws IOException {
        HttpURLConnection result = (HttpURLConnection) new URL(url).openConnection();
        result.setRequestProperty("Authorization", "Bearer " + token);
        result.setConnectTimeout(3000);
        return result;
    }

    public User login(String userid, String password) throws IOException {
        HttpURLConnection con = null;
        try {
            URL url = new URL(LOGIN_URL + "?userid=" + userid + "&password=" + password);
            con = (HttpURLConnection) url.openConnection();
            con.setConnectTimeout(3000);
            token = new BufferedReader(new InputStreamReader(con.getInputStream())).readLine();
            user = getCurrentUser();
            con.getInputStream().close(); // Why?
        } finally {
            if(con != null) con.disconnect();
        }

        return user;
    }


    public User getCurrentUser() throws IOException {
        User result;
        HttpURLConnection con = null;
        try {
            con = getSecureConnection(CURRENT_USER_URL);
            JsonReader jr = new JsonReader(new InputStreamReader(new BufferedInputStream(con.getInputStream())));
            user = loadUser(jr);
            con.getInputStream().close(); // Why?
        } finally {
            if(con != null) con.disconnect();
        }

        return user;
    }

    public User createUser(String userid, String password, String email) throws IOException {
        User result;

        String urlParameters = "userid=" + userid + "&password=" + password + "&email=" + email;

        byte[] postData = urlParameters.getBytes(StandardCharsets.UTF_8);
        int postDataLength = postData.length;
        HttpURLConnection con = null;

            URL url = new URL(CREATE_USER_URL);
            con = (HttpURLConnection) url.openConnection();

            con.setDoOutput(true);
            con.setInstanceFollowRedirects(false);
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            con.setRequestProperty("charset", "utf-8");
            con.setRequestProperty("Content-Length", Integer.toString(postDataLength));
            con.setUseCaches(false);

            try (DataOutputStream wr = new DataOutputStream(con.getOutputStream())) {
                wr.write(postData);
            }
            // Read JSON result
            try (JsonReader jr = new JsonReader(new InputStreamReader(new BufferedInputStream(con.getInputStream())))) {
                result = loadUser(jr);
                con.getInputStream().close(); // Why?
            } finally {
                if (con != null) con.disconnect();
            }
            return result;
    }



    public Item createItem(String title, BigDecimal price, String description) throws IOException {
        Item result;
        String urlParameters = "&title=" + title + "&description=" + description + "&price=" + price;


        byte[] postData = urlParameters.getBytes(StandardCharsets.UTF_8);
        int postDataLength = postData.length;
        HttpURLConnection con = null;

        con = getSecureConnection(ADD_ITEM_URL);

        con.setDoOutput(true);
        con.setInstanceFollowRedirects(false);
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        con.setRequestProperty("charset", "utf-8");
        con.setRequestProperty("Content-Length", Integer.toString(postDataLength));
        con.setUseCaches(false);

        try (DataOutputStream wr = new DataOutputStream(con.getOutputStream())) {
            wr.write(postData);
        }
        // Read JSON result
        try (JsonReader jr = new JsonReader(new InputStreamReader(new BufferedInputStream(con.getInputStream())))) {
            result = loadItem(jr);
            con.getInputStream().close(); // Why?
        } finally {
            if (con != null) con.disconnect();
        }
        return result;

    }




    private User loadUser(JsonReader jr) throws IOException {
        User result = new User();

        jr.beginObject();
        while (jr.hasNext()) {
            switch (jr.nextName()) {
                case "userid":
                    result.setUserid(jr.nextString());
                    break;
                case "email":
                    result.setEmail(jr.nextString());
                    break;
                default:
                    jr.skipValue();
            }
        }
        jr.endObject();

        return result;
    }

    private Item loadItem(JsonReader jr) throws IOException {
        Item result = new Item();


        jr.beginObject();
        while (jr.hasNext()) {
            switch (jr.nextName()) {
                case "itemid":
                    result.setId(jr.nextLong());
                    break;
                case "title":
                    result.setTitle(jr.nextString());
                    break;
                case "description":
                    result.setDescription(jr.nextString());
                    break;
                case "price":
                    String stringprice = jr.nextString();
                    BigDecimal price = new BigDecimal(stringprice);
                    result.setPrice(price);
                    break;
                default:
                    jr.skipValue();
            }
        }
        jr.endObject();

        return result;

    }

    public List<Item> loadItems() throws IOException {
        List<Item> result = new ArrayList<>();

        HttpURLConnection c = null;
        try{
            URL url = new URL(GET_ITEMS_URL);
            c = (HttpURLConnection) url.openConnection();
            c.setUseCaches(true);
            c.setRequestMethod("GET");

            if(c.getResponseCode() == HttpURLConnection.HTTP_OK){
                BufferedReader br = new BufferedReader(new InputStreamReader(c.getInputStream(), StandardCharsets.UTF_8));
                JsonReader jr = new JsonReader(br);
                result = loadItems(jr);
                jr.close();
                c.getInputStream().close();
            }

        } finally {
            if(c != null) c.disconnect();
        }

        items = new ArrayList<>(result);
        onItemsLoaded.onLoaded(items);

        return result;
    }

    private List<Item> loadItems(JsonReader jr) throws IOException {
        List<Item> result = new ArrayList<>();

        jr.beginArray();
        while(jr.hasNext()){
            result.add(loadItem(jr));
        }
        jr.endArray();
        return result;
    }

}
