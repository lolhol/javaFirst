import java.net.*;
import java.security.Key;
import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.time.Instant;

public class forums {
    public static void main(String[] args) throws Exception {

        StringBuilder sb = new StringBuilder();

        Map<String, Map<String, Map<String, List<Map<String, Object>>>>> myMap;

        URL url = new URL("https://hypixel.net/skyblock-alpha/");
        BufferedReader in = new BufferedReader(
                new InputStreamReader(url.openStream()));

        String line = in.readLine();

        while (line != null) {
            sb.append(line);
            sb.append(System.lineSeparator());
            line = in.readLine();
        }
        in.close();

        String str = sb.toString();

        PrintWriter writer = new PrintWriter("offline.json", "UTF-8");
        writer.println(new Gson().toJson(str));
        writer.close();

    }
}
