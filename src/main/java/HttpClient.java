
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import entity.Movie;
import entity.Movies;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.http.HttpEntity;
import org.apache.http.ParseException;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;



import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import util.ConnectionCheck;
import util.FileHandler;

public class HttpClient {

    final static CloseableHttpClient httpClient = HttpClients.createDefault();

    public static void main(String[] args) {

        boolean singleSearch = false;
        boolean sort = false;
        String sortby = null;

        StringBuilder searchParam = new StringBuilder();
        CommandLineParser parser = new DefaultParser();
        Options options = new Options();

        options.addOption("y", "year", true, "Search by Year");
        options.addOption("s", "search", true, "Search multiple movies with title, use quotes if title have spaces");
        options.addOption("t", "search", true, "Search movie with title, use quotes if title have spaces");
        options.addOption("sort", true, "Sort by Title | Year");
        options.addOption("h", "help", false, "Show help");

        try {
            CommandLine commandLine = parser.parse(options, args);

            if (commandLine.getOptionValue("y") != null) {
                searchParam.append("&y=" + commandLine.getOptionValue("y"));
            }

            if (commandLine.getOptionValue("s") != null) {
                searchParam.append("&s=" + commandLine.getOptionValue("s").replaceAll(" ", "+"));
            }

            if (commandLine.getOptionValue("t") != null) {
                searchParam.append("&t=" + commandLine.getOptionValue("t").replaceAll(" ", "+"));
            }

            if (commandLine.getOptionValue("t") == null && commandLine.getOptionValue("sort") != null) {
                sort = true;
                sortby = commandLine.getOptionValue("sort");
            }

            if (commandLine.hasOption("h")) {
                helpFormatterShow(options);
            }

        } catch (Exception e) {
        }

        if (searchParam.isEmpty()) {
            helpFormatterShow(options);
            return;
        }

        HttpEntity entity = ConnectionCheck.ins.check(httpClient, searchParam.toString());

        if (entity == null) {
            System.out.println("No result");
            return;
        }

        JsonNode node = null;
        try {
            node = new ObjectMapper().readTree(EntityUtils.toString(entity));
        } catch (ParseException | IOException e) {
            e.printStackTrace();
        }

        ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        String jsonString = null;

        if (searchParam.toString().contains("&t=")) {
            singleSearch = true;
        }

        if (singleSearch) {
            try {
                jsonString = node.toString();
            } catch (Exception e) {
                System.out.println("Invalid title");
                return;
            }
        } else {
            try {
                jsonString = node.get("Search").toString();
            } catch (Exception e) {
                System.out.println("Invalid title");
                return;
            }
        }

        List<Movies> moviesList = null;
        Movie singleMovie = null;
        if (singleSearch) {
            try {
                singleMovie = mapper.readValue(jsonString, new TypeReference<Movie>() {
                });
                // write single movie to csv
                FileHandler.ins.handle(singleMovie);

            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        } else {
            try {
                moviesList = mapper.readValue(jsonString, new TypeReference<List<Movies>>() {
                });

                if (sort) {
                    if (sortby.equalsIgnoreCase("title")) {
                        Collections.sort(moviesList, Comparator.comparing(Movies::getTitle));

                    } else if (sortby.equalsIgnoreCase("year")) {
                        Collections.sort(moviesList, Comparator.comparing(Movies::getYear));
                    } else {
                        System.out.println("Invalid sort");
                        return;
                    }

                }
                // write list of 10 movies to csv
                FileHandler.ins.handle(moviesList);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }


    }

    public static void helpFormatterShow(Options options) {
        HelpFormatter helpFormatter = new HelpFormatter();
        helpFormatter.printHelp("java -jar omdbcli.jar", options);
    }

}