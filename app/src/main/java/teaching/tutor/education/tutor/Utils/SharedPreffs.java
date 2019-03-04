package teaching.tutor.education.tutor.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static com.firebase.ui.auth.AuthUI.TAG;

/**
 * Created by Shehnila Kareem on 14-Nov-16.
 */

public class SharedPreffs {
    ArrayList<Favourites> favourites;
    public SharedPreffs() {
        Log.e(TAG, "SharedPreffs: constructor");
        favourites = new ArrayList<>();

    }

    private static SharedPreferences prefs;
    private Context context;
    static public String filename="SharedData";
    public static final String PREFS_NAME = "GIFT_APP";
    public static final String FAVORITES  = "GIFTS";

    public static final String FIREBASE_CLOUD_MESSAGING = "fcm";
    public static final String SET_NOTIFY = "set_notify";
    boolean exists=false;

    public SharedPreffs(Context context){
        this.context = context;
        prefs = context.getSharedPreferences(FIREBASE_CLOUD_MESSAGING, Context.MODE_PRIVATE);
    }

    public static void setSharedData(Context context, String key, String value){
        context.getSharedPreferences(filename,0).edit().putString(key,value).apply();
    }
    public static void setIntSharedData(Context context, String key, int value){
        context.getSharedPreferences(filename,0).edit().putInt(key,value).apply();
    }
    static public int getIntSharedPrefferences(Context context, String key){
        return context.getSharedPreferences(filename,0).getInt(key, 0);
    }static public String getSharedPrefferences(Context context, String key){
        return context.getSharedPreferences(filename,0).getString(key,"");
    }
    public void saveFavorites(Context context, String key, Favourites fav){
        ArrayList favourites2 = new ArrayList<>();
        Gson gson = new Gson();
        String getfav = getFavorites(context,key);
        Log.e(TAG, "saveFavorites: getFav:"+getfav );
        Type type = new TypeToken<List<Favourites>>(){}.getType();
        favourites2 = gson.fromJson(getfav,type);
        Log.e(TAG, "saveFavorites: fav2:"+favourites2 );
        if (favourites2!= null) {
            favourites.addAll(favourites2);
        }
        if (fav!=null){
        favourites.add(fav);
        }
        Log.e(TAG, "saveFavorites: "+favourites.size());

        String jsonItems =gson.toJson(favourites);
        Log.e(TAG, "saveFavorites: getFavFinal:"+jsonItems );
        context.getSharedPreferences(filename,0).edit().putString(key,jsonItems).apply();
    }
    public String getFavorites(Context context, String key){
        return context.getSharedPreferences(filename,0).getString(key, "");
    }
    public void removeFavorite(Context context, String key, Favourites fav){
        Gson gson = new Gson();
        String getfav = getFavorites(context,key);
        Log.e(TAG, "remvoveFavorite: getFav:"+getfav );
        Type type = new TypeToken<List<Favourites>>(){}.getType();
        favourites = gson.fromJson(getfav,type);
        for (int i =0;i<favourites.size();i++){
            if (favourites.get(i).getEmail().contains(fav.getEmail())){
                favourites.remove(i);
            }
        }
        Log.e(TAG, "removeFavorite: "+favourites.size() );
        String jsonItems =gson.toJson(favourites);
        Log.e(TAG, "REmove: getFavFinal:"+jsonItems );
        context.getSharedPreferences(filename,0).edit().putString(key,jsonItems).apply();
    }
//     public void saveFavorite(Context context, List<Favourites> giftItems) {
//
//        SharedPreferences settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = settings.edit();
//
//        Gson gson = new Gson();
//        String jsonFavorites = gson.toJson(giftItems);
//
//        editor.putString(FAVORITES, jsonFavorites);
//        editor.apply();
////        addFavourites(context,giftItems);
//
//    }
//    public void addFavorite(Context context, Favourites giftItem) {
//        List<Favourites> favorites = getFavorites(context);
//        if (favorites == null) {
//            favorites = new ArrayList<>();
//            favorites.add(giftItem);
//            saveFavorite(context, favorites);
//        }
//    }
//
//    public void removeFavorite(Context context, Favourites giftItem) {
//        ArrayList<Favourites> favorites = getFavorites(context);
//        if (favorites != null) {
//            favorites = new ArrayList<>();
//            favorites.remove(giftItem);
//            saveFavorite(context, favorites);
//        }
//    }
//     public ArrayList<Favourites> getFavorites(Context context){
//        SharedPreferences settings;
//        List<Favourites> favorites;
//
//        settings = context.getSharedPreferences(PREFS_NAME,
//                Context.MODE_PRIVATE);
//        if (settings.contains(FAVORITES)) {
//            String jsonFavourites = settings.getString(FAVORITES, null);
//            Log.e(TAG, "addFavourites: " + jsonFavourites);
//            Gson gson = new Gson();
//            Favourites[] favouritesItems = gson.fromJson(jsonFavourites, Favourites[].class);
//            favorites = Arrays.asList(favouritesItems);
//            favorites = new ArrayList<>(favorites);
//        }
//        else {
//            return null;
//        }
//        return (ArrayList<Favourites>) favorites;
//    }
}
