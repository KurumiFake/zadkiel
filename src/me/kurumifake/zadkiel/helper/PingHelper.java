///Created by Daniel.Arvizu.Rosselli ///
// Free Use | No Licence //
// Made For Educational Purposes | Enjoy :D //
package me.kurumifake.zadkiel.helper;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

public class PingHelper {
    public static void testPing(InetSocketAddress address, String host, int port) {
        try {
            Socket socket = new Socket();
            System.out.println("connecting...");
            socket.connect(address, 3000);
            System.out.println("done");
            DataOutputStream output = new DataOutputStream(socket.getOutputStream());
            DataInputStream input = new DataInputStream(socket.getInputStream());
            System.out.println("trying & sending hanshake... " + address.toString());
        } catch (Exception exception) {}
    }
}
