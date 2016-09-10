package Goose;

import java.sql.ResultSet;
import java.util.HashMap;

public class ChatFilter {
    HashMap<String, String> WordFilter;

    public ChatFilter() throws Exception {
        this.WordFilter = new HashMap<String, String>();
    }

    public void loadFilter(GameWorld world) throws Exception {
        ResultSet resultSet =
                world.getSqlConnection().createStatement()
                        .executeQuery("SELECT word,filtered FROM wordfilter");
        while (resultSet.next()) {
            WordFilter.put(resultSet.getString("word"), resultSet.getString("filtered"));
        }
        resultSet.close();
    }

    public String filter(String input) throws Exception {
        String replaced;
        String output = "";
        for (String word : input.split(" ")) {
            boolean isReplaced = this.WordFilter.containsKey(word.toLowerCase());
            if (isReplaced) {
                replaced = this.WordFilter.get(word.toLowerCase());
                output += replaced + " ";
            } else {
                output += word + " ";
            }
        }
        return output;
    }

    public int getCount() throws Exception {
        return this.WordFilter.size();
    }
}
