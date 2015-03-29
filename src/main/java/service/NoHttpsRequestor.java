package service;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.URL;

import com.dropbox.core.http.HttpRequestor;

/**
 * {@link HttpRequestor} implementation that uses Java's standard library
 * {@link HttpsURLConnection}.  If you just want a connection with the
 * default settings, use the predefined {@link #Instance}.
 *
 * <p>
 * If you want to customize the way the connection is configured, create a
 * subclass that overrides {@link #configureConnection}.
 * </p>
 */
public class NoHttpsRequestor extends HttpRequestor
{
    private final Proxy proxy;

    /**
     * Creates an instance that uses a direct HTTP connection (as opposed to
     * using a proxy).
     */
    public NoHttpsRequestor()
    {
        this(Proxy.NO_PROXY);
    }

    /**
     * Creates an instance that connects through the given proxy.
     */
    public NoHttpsRequestor(Proxy proxy)
    {
        this.proxy = proxy;
    }

    /**
     * We pass this value to {@link HttpsURLConnection#setConnectTimeout}.  You can
     * change this setting by creating a subclass and overriding
     * {@link #configureConnection}.
     */
    public static final int DefaultConnectTimeoutMillis = 35 * 1000;

    /**
     * We pass this value to {@link HttpsURLConnection#setReadTimeout}.  You can
     * change this setting by creating a subclass and overriding
     * {@link #configureConnection}.
     */
    public static final int DefaultReadTimeoutMillis = 35 * 1000;

    /**
     * A thread-safe instance of {@code StandardHttpRequestor} that connects directly
     * (as opposed to using a proxy).
     */
    public static final NoHttpsRequestor Instance = new NoHttpsRequestor();

    private static Response toResponse(HttpURLConnection conn)
        throws IOException
    {
        int responseCode = conn.getResponseCode();
        InputStream bodyStream;
        if (responseCode >= 400) {
            bodyStream = conn.getErrorStream();
        } else {
            bodyStream = conn.getInputStream();
        }
        return new Response(conn.getResponseCode(), bodyStream, conn.getHeaderFields());
    }

    @Override
    public Response doGet(String url, Iterable<Header> headers) throws IOException
    {
        HttpURLConnection conn = prepRequest(url, headers);
        conn.setRequestMethod("GET");
        conn.connect();
        return toResponse(conn);
    }

    @Override
    public Uploader startPost(String url, Iterable<Header> headers) throws IOException
    {
        HttpURLConnection conn = prepRequest(url, headers);
        conn.setRequestMethod("POST");
        return new Uploader(conn);
    }

    @Override
    public Uploader startPut(String url, Iterable<Header> headers) throws IOException
    {
        HttpURLConnection conn = prepRequest(url, headers);
        conn.setRequestMethod("PUT");
        return new Uploader(conn);
    }

    /**
     * Can be overridden to configure the underlying {@link HttpsURLConnection} used to make
     * network requests.  If you override this method, you should probably call
     * {@code super.configureConnection(conn)} in your overridden method.
     */
    protected void configureConnection(HttpURLConnection conn)
        throws IOException
    {
    }

    private static class Uploader extends HttpRequestor.Uploader
    {
        private HttpURLConnection conn;

        public Uploader(HttpURLConnection conn2)
            throws IOException
        {
            super(getOutputStream(conn2));
            conn2.connect();
            this.conn = conn2;
        }

        private static OutputStream getOutputStream(HttpURLConnection conn2)
            throws IOException
        {
            conn2.setDoOutput(true);
            return conn2.getOutputStream();
        }

        @Override
        public void abort()
        {
            if (conn == null) {
                throw new IllegalStateException("Can't abort().  Uploader already closed.");
            }
            this.conn.disconnect();
        }

        @Override
        public void close()
        {
            if (conn == null) return;
            this.conn.disconnect();
        }

        @Override
        public Response finish() throws IOException
        {
            HttpURLConnection conn = this.conn;
            if (conn == null) {
                throw new IllegalStateException("Can't finish().  Uploader already closed.");
            }
            this.conn = null;
            return toResponse(conn);
        }
    }

    private HttpURLConnection prepRequest(String url, Iterable<Header> headers) throws IOException
    {
        URL urlObject = new URL(url);
        HttpURLConnection conn = (HttpURLConnection) urlObject.openConnection(this.proxy);

        conn.setConnectTimeout(DefaultConnectTimeoutMillis);
        conn.setReadTimeout(DefaultReadTimeoutMillis);
        conn.setUseCaches(false);
        conn.setAllowUserInteraction(false);

        configureConnection(conn);

        for (Header header : headers) {
            conn.addRequestProperty(header.key, header.value);
        }

        return conn;
    }
}
