package util;

import entity.Movie;
import entity.Movies;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.List;
import java.util.Scanner;

public final class FileHandler {

    public static final FileHandler ins = new FileHandler();

    public void handle(Movie singleMovie) {
        String filename = "movie.csv";
        File tempFile = new File(filename);
        if (fileExists(tempFile)) {
            try (PrintWriter writer = new PrintWriter(new FileOutputStream(filename, true))) {

                writer.write(String.valueOf(singleMovieInfo(singleMovie)));

            } catch (FileNotFoundException e) {
                System.out.println(e.getMessage());
            }
        } else {

            try (PrintWriter writer = new PrintWriter(new FileOutputStream(filename, true))) {

                String sb = "Title,Year,Rated,Released,Runtime,Genre,Director,Writer,Actors,Plot,Language,imdbRating" +
                        '\n' +
                        singleMovieInfo(singleMovie);
                writer.write(sb);

            } catch (FileNotFoundException e) {
                System.out.println(e.getMessage());
            }
        }
        displayCSV(filename);
    }

    public void handle(List<Movies> moviesList) {
        String filename = "movies.csv";
        File tempFile = new File(filename);
        if (fileExists(tempFile)) {
            try (PrintWriter writer = new PrintWriter(new FileOutputStream(filename, true))) {

                writer.write(String.valueOf(listOfMoviesInfo(moviesList)));

            } catch (FileNotFoundException e) {
                System.out.println(e.getMessage());
            }
        } else {

            try (PrintWriter writer = new PrintWriter(new FileOutputStream(filename, true))) {

                String sb = "Title,Year,imdbID,Type,Poster" +
                        '\n' +
                        listOfMoviesInfo(moviesList);
                writer.write(sb);

            } catch (FileNotFoundException e) {
                System.out.println(e.getMessage());
            }
        }
        displayCSV(filename);

    }

    private StringBuilder listOfMoviesInfo(List<Movies> moviesList) {
        StringBuilder sb = new StringBuilder();
        for (Movies movie : moviesList) {
            sb.append(movie.getTitle().replaceAll(",", " | ")).append(",");
            sb.append(movie.getYear().replaceAll(",", " | ")).append(",");
            sb.append(movie.getImdbID().replaceAll(",", " | ")).append(",");
            sb.append(movie.getType().replaceAll(",", " | ")).append(",");
            sb.append(movie.getPoster().replaceAll(",", " | ")).append("\n");
        }
        return sb;
    }

    private StringBuilder singleMovieInfo(Movie singleMovie) {
        StringBuilder sb = new StringBuilder();
        sb.append(singleMovie.getTitle().replaceAll(",", " | ")).append(",");
        sb.append(singleMovie.getYear().replaceAll(",", " | ")).append(",");
        sb.append(singleMovie.getRated().replaceAll(",", " | ")).append(",");
        sb.append(singleMovie.getReleased().replaceAll(",", " | ")).append(",");
        sb.append(singleMovie.getRuntime().replaceAll(",", " | ")).append(",");
        sb.append(singleMovie.getGenre().replaceAll(",", " | ")).append(",");
        sb.append(singleMovie.getDirector().replaceAll(",", " | ")).append(",");
        sb.append(singleMovie.getWriter().replaceAll(",", " | ")).append(",");
        sb.append(singleMovie.getActors().replaceAll(",", " | ")).append(",");
        sb.append(singleMovie.getPlot().replaceAll(",", " | ")).append(",");
        sb.append(singleMovie.getLanguage().replaceAll(",", " | ")).append(",");
        sb.append(singleMovie.getImdbRating());
        sb.append('\n');
        return sb;
    }

    private boolean fileExists(File tempFile) {
        return tempFile.exists();
    }

    private void displayCSV(String filename) {
        Scanner sc;
        try {
            sc = new Scanner(new File(filename));
            sc.useDelimiter("");
            while (sc.hasNext()) {
                System.out.print(sc.next());
            }
            sc.close();
        } catch (FileNotFoundException e) {

            e.printStackTrace();
        }

    }

}
