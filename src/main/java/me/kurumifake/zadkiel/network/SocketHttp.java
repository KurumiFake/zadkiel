///Created by Daniel.Arvizu.Rosselli ///
// Free Use | No Licence //
// Made For Educational Purposes | Enjoy :D //
package me.kurumifake.zadkiel.network;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.net.SocketAddress;

public class SocketHttp extends Socket {
    public SocketHttp(String targetHost, int targetPort, SocketAddress socketAddress, int timeout) throws IOException {
        connect(socketAddress, timeout);
        setSoTimeout(timeout);
        connectToTarget(targetHost, targetPort);
    }

    private void connectToTarget(String targetHost, int targetPort) throws IOException {
        PrintStream printStream = new PrintStream(getOutputStream());
        printStream.println("connect " + targetHost + ":" + targetPort + " HTTP/1.1");
        printStream.flush();
    }
}
