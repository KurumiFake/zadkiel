//
// Todos los derechos reservados a Daniel.Arvizu.Rosselli
//
package me.kurumifake.zadkiel;
import java.io.FileOutputStream;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import me.kurumifake.zadkiel.Proxies;

public class ProxiesGetter {
    public static void downloadFile(Proxies proxies) {
    try {
        URL website = new URL("https://api.proxyscrape.com/?request=getproxies&proxytype=socks4&timeout=500&country=all&anonymity=elite&ssl=yes");
        ReadableByteChannel rbc = Channels.newChannel(website.openStream());
        FileOutputStream fos = new FileOutputStream("proxies.txt");
        fos.getChannel().transferFrom(rbc, 0L, Long.MAX_VALUE);
        fos.close();
        rbc.close();
    } catch (Exception exception) {}
    }
}
