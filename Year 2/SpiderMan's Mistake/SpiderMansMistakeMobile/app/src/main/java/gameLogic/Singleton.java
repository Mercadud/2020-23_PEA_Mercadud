package gameLogic;

import android.content.res.AssetManager;

import java.io.File;

public class Singleton {
    static private Scorage sc;
    static private Player player;
    static private File file;
    static private AssetManager assets;

    static public Scorage getScorage() {
        if (sc == null) {
            sc = new Scorage();
        }
        return sc;
    }

    static public void setFile(File f) {
        file = f;
    }

    static public File getFile() {
        return file;
    }


    public static AssetManager getAssets() {
        return assets;
    }

    public static void setAssets(AssetManager assets) {
        Singleton.assets = assets;
    }

    public static Player getPlayer() {
        return player;
    }

    public static void setPlayer(Player player) {
        Singleton.player = player;
    }
}
