package com.ypwl.xiaotouzi.im.socketio.client;


import com.ypwl.xiaotouzi.im.socketio.parser.Parser;
import com.ypwl.xiaotouzi.utils.LogUtil;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.concurrent.ConcurrentHashMap;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;


@SuppressWarnings("unused")
public class IO {

    private static final String TAG = "ymseeage:" + "IO";

    private static final ConcurrentHashMap<String, Manager> managers = new ConcurrentHashMap<>();

    /**
     * Protocol version.
     */
    public static int protocol = Parser.protocol;

    public static void setDefaultSSLContext(SSLContext sslContext) {
        Manager.defaultSSLContext = sslContext;
    }

    public static void setDefaultHostnameVerifier(HostnameVerifier hostnameVerifier) {
        Manager.defaultHostnameVerifier = hostnameVerifier;
    }

    private IO() {
    }

    public static Socket socket(String uri) throws URISyntaxException {
        return socket(uri, null);
    }

    public static Socket socket(String uri, Options opts) throws URISyntaxException {
        return socket(new URI(uri), opts);
    }

    public static Socket socket(URI uri) {
        return socket(uri, null);
    }

    /**
     * Initializes a {@link Socket} from an existing {@link Manager} for multiplexing.
     *
     * @param uri  uri to connect.
     * @param opts options for socket.
     * @return {@link Socket} instance.
     */
    public static Socket socket(URI uri, Options opts) {
        if (opts == null) {
            opts = new Options();
        }

        URL parsed = Url.parse(uri);
        URI source;
        try {
            source = parsed.toURI();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        Manager io;

        if (opts.forceNew || !opts.multiplex) {
            LogUtil.e(TAG, String.format("ignoring socket cache for %s", source));
            io = new Manager(source, opts);
        } else {
            String id = Url.extractId(parsed);
            if (!managers.containsKey(id)) {
                LogUtil.e(TAG, String.format("new io instance for %s", source));
                managers.putIfAbsent(id, new Manager(source, opts));
            }
            io = managers.get(id);
        }

        return io.socket(parsed.getPath());
    }


    public static class Options extends Manager.Options {

        public boolean forceNew;

        /**
         * Whether to enable multiplexing. Default is true.
         */
        public boolean multiplex = true;
    }
}
