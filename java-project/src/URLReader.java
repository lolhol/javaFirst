import java.net.*;
import java.security.Key;
import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.time.Instant;

public class URLReader {
    public static void main(String[] args) throws Exception {

        StringBuilder sb = new StringBuilder();

        Map<String, Map<String, Map<String, List<Map<String, Object>>>>> myMap;
        boolean connection;

        try {
            final URL url = new URL("http://www.google.com");
            final URLConnection conn = url.openConnection();
            conn.connect();
            conn.getInputStream().close();
            connection = true;
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            connection = false;
        }

        if (connection) {
            URL url = new URL("https://api.hypixel.net/skyblock/bazaar");
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

            Type mapType = new TypeToken<Object>() {
            }.getType();
            myMap = new Gson().fromJson(str, mapType);

            PrintWriter writer = new PrintWriter("offline.json", "UTF-8");
            writer.println(new Gson().toJson(myMap));
            writer.close();
        } else {
            BufferedReader bm = new BufferedReader(new FileReader("Offline.json"));
            StringBuilder bk = new StringBuilder();
            String fileTextOffline = bm.readLine();

            bm.close();

            String strat = fileTextOffline.toString();

            Type mapType12 = new TypeToken<Object>() {
            }.getType();
            myMap = new Gson().fromJson(strat, mapType12);
        }

        Map<String, Object> itempriceMap = new HashMap<String, Object>();

        for (String name : myMap.get("products").keySet()) {
            if (myMap.get("products").get(name).get("sell_summary").size() > 0) {
                Object item_price = myMap.get("products").get(name).get("sell_summary").get(0).get("pricePerUnit");
                itempriceMap.put(name, item_price);
            } else {
            }
        }

        double currentTime = Instant.now().getEpochSecond();

        Map<String, Object> timeMap = new HashMap<String, Object>();

        List<Map<String, Object>> myList = new ArrayList<>();

        BufferedReader bm = new BufferedReader(new FileReader("the-file-name.json"));
        String fileTextFile = bm.readLine();
        bm.close();

        if (fileTextFile != null) {
            Type listType = new TypeToken<Object>() {
            }.getType();
            myList = new Gson().fromJson(fileTextFile, listType);
        }

        timeMap.put("time", currentTime);
        timeMap.put("items", itempriceMap);

        myList.add(timeMap);

        PrintWriter writer = new PrintWriter("the-file-name.json", "UTF-8");
        writer.println(new Gson().toJson(myList));
        writer.close();

        for (int i = 0; i < myList.size(); i++) {
            if (currentTime - (Double) myList.get(i).get("time") >= 14400) {
                myList.remove(i);
                i = i - 1;
            }

            if (myList.get(1).get("items") != null) {
                @SuppressWarnings("unchecked")
                Map<String, Object> oldItems = (Map<String, Object>) myList.get(0).get("items");
                @SuppressWarnings("unchecked")
                Map<String, Object> newItems = (Map<String, Object>) myList.get(myList.size() - 1).get("items");

                for (Map.Entry<String, Object> entry : oldItems.entrySet()) {
                    Object unitName = entry.getKey();
                    double oldUnitPrice = (Double) entry.getValue();
                    double newUnitPrice = (Double) newItems.get(unitName);

                    double margin = newUnitPrice - oldUnitPrice;
                    double percent = (margin / oldUnitPrice) * 100;

                    if (percent >= 2) {
                        System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
                        System.out.println(" ");
                        System.out.println(
                                unitName + "'s margin is " + margin + "coins" + " (from " + oldUnitPrice + " to "
                                        + newUnitPrice + ")" + " or increase by " + percent + "%");
                        System.out.println(" ");
                        System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
                        System.out.println(" ");
                    }

                }
            } else {
                System.out.println("No information in file found, please run again.");
            }

        }
    }
}