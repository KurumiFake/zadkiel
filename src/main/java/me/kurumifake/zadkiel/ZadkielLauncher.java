///Created by Daniel.Arvizu.Rosselli ///
// Free Use | No Licence //
// Made For Educational Purposes | Enjoy :D //
package me.kurumifake.zadkiel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;
import me.kurumifake.zadkiel.option.Options;

public class ZadkielLauncher {
    public static void main(String... args) throws IOException {
        final Zadkiel breaker = new Zadkiel(Options.Builder.of(args));
            breaker.launch();
    }
}