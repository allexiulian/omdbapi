package util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import entity.Movie;
import entity.Movies;
import org.apache.commons.cli.*;
import org.apache.http.HttpEntity;
import org.apache.http.ParseException;
import org.apache.http.util.EntityUtils;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;

public class App {
    StringBuilder searchParam = new StringBuilder();
    CommandLineParser parser = new DefaultParser();
    ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    Options options = new Options();
    boolean sort = false;
    String sortBy = null;

    public App(String[] args) throws IOException {
        createCLIOptions(args);
        if (!searchIsEmpty()) {
            if (ConnectionCheck.ins.checkStatus(searchParam) == 200) {
                if (singleSearch()) {
                    writeToCsv(ConnectionCheck.ins.getEntity());
                } else {
                    writeListOfMoviesToCsv(ConnectionCheck.ins.getEntity());
                }
            }
        }
    }

    private void createCLIOptions(String[] args) {
        options.addOption("y", "year", true, "Search by Year");
        options.addOption("s", "search", true, "Search multiple movies with title, use quotes if title have spaces");
        options.addOption("t", "title", true, "Search movie with title, use quotes if title have spaces");
        options.addOption("sort", true, "Sort by Title | Year");
        options.addOption("h", "help", false, "Show help");
        parseInput(args);
    }

    private void parseInput(String[] args) {
        try {
            CommandLine commandLine = parser.parse(options, args);
            if (commandLine.getOptionValue("y") != null) {
                searchParam.append("&y=").append(commandLine.getOptionValue("y"));
            }
            if (commandLine.getOptionValue("s") != null) {
                searchParam.append("&s=").append(commandLine.getOptionValue("s").replaceAll(" ", "+"));
            }
            if (commandLine.getOptionValue("t") != null) {
                searchParam.append("&t=").append(commandLine.getOptionValue("t").replaceAll(" ", "+"));
            }
            if (commandLine.getOptionValue("t") == null && commandLine.getOptionValue("sort") != null) {
                sort = true;
                sortBy = commandLine.getOptionValue("sort");
            }
            if (commandLine.hasOption("h")) {
                helpFormatterShow(options);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void helpFormatterShow(Options options) {
        HelpFormatter helpFormatter = new HelpFormatter();
        helpFormatter.printHelp("java -jar omdbcli.jar", options);
    }

    private boolean searchIsEmpty() {
        return searchParam.isEmpty();
    }

    private boolean singleSearch() {
        return searchParam.toString().contains("&t=");
    }

    private void writeToCsv(HttpEntity entity) throws ParseException, IOException {
        Movie singleMovie = mapper.readValue(getSingleMovieData(entity), new TypeReference<>() {});
        FileHandler.ins.handle(singleMovie);
    }
    private String getSingleMovieData(HttpEntity entity) throws ParseException, IOException {
        return new ObjectMapper().readTree(EntityUtils.toString(entity)).toString();
    }

    private void writeListOfMoviesToCsv(HttpEntity entity) throws ParseException, IOException {
        List<Movies> moviesList = getSorted(entity);
        FileHandler.ins.handle(moviesList);
    }

    private List<Movies> getSorted(HttpEntity entity) throws ParseException, IOException {
        List<Movies> moviesList = mapper.readValue(getListMoviesData(entity), new TypeReference<>() {});
        if(sort) {
            if (sortBy.equalsIgnoreCase("title")) {
                moviesList.sort(Comparator.comparing(Movies::getTitle));
            } else if (sortBy.equalsIgnoreCase("year")) {
                moviesList.sort(Comparator.comparing(Movies::getYear));
            }
        }
        return moviesList;
    }

    private String getListMoviesData(HttpEntity entity) throws ParseException, IOException {
        return new ObjectMapper().readTree(EntityUtils.toString(entity)).get("Search").toString();
    }

}
