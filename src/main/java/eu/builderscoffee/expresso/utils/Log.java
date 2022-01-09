package eu.builderscoffee.expresso.utils;

import eu.builderscoffee.expresso.ExpressoBukkit;

public class Log {
    private static Log instance;

    static {
        Log.instance = new Log();
    }

    public static Log get() {
        return Log.instance;
    }

    public void info(final String msg) {
        ExpressoBukkit.getInstance().getLogger().info(msg);
    }

    public void severe(final String msg) {
        ExpressoBukkit.getInstance().getLogger().severe(msg);
    }

    public void warning(final String msg) {
        ExpressoBukkit.getInstance().getLogger().warning(msg);
    }
}
