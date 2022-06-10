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
            try (PrintWriter writer = new PrintWriter(new FileOutputStream(new File(filename), true));) {

                StringBuilder sb = new StringBuilder();
                sb.append(singleMovieInfo(singleMovie));
                writer.write(sb.toString());

            } catch (FileNotFoundException e) {
                System.out.println(e.getMessage());
            }
        } else {

            try (PrintWriter writer = new PrintWriter(new FileOutputStream(new File(filename), true));) {

                StringBuilder sb = new StringBuilder();
                sb.append("Title,Year,Rated,Released,Runtime,Genre,Director,Writer,Actors,Plot,Language,imdbRating");
                sb.append('\n');
                sb.append(singleMovieInfo(singleMovie));
                writer.write(sb.toString());

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
            try (PrintWriter writer = new PrintWriter(new FileOutputStream(new File(filename), true));) {

                StringBuilder sb = new StringBuilder();
                sb.append(listOfMoviesInfo(moviesList));
                writer.write(sb.toString());

            } catch (FileNotFoundException e) {
                System.out.println(e.getMessage());
            }
        } else {

            try (PrintWriter writer = new PrintWriter(new FileOutputStream(new File(filename), true));) {

                StringBuilder sb = new StringBuilder();
                sb.append("Title,Year,imdbID,Type,Poster");
                sb.append('\n');
                sb.append(listOfMoviesInfo(moviesList));
                writer.write(sb.toString());

            } catch (FileNotFoundException e) {
                System.out.println(e.getMessage());
            }
        }
        displayCSV(filename);

    }

    private StringBuilder listOfMoviesInfo(List<Movies> moviesList) {
        StringBuilder sb = new StringBuilder();
        for (Movies movie : moviesList) {
            sb.append(movie.getTitle().replaceAll(",", " | ") + ",");
            sb.append(movie.getYear().replaceAll(",", " | ") + ",");
            sb.append(movie.getImdbID().replaceAll(",", " | ") + ",");
            sb.append(movie.getType().replaceAll(",", " | ") + ",");
            sb.append(movie.getPoster().replaceAll(",", " | ") + "\n");
        }
        return sb;
    }

    private StringBuilder singleMovieInfo(Movie singleMovie) {
        StringBuilder sb = new StringBuilder();
        sb.append(singleMovie.getTitle().replaceAll(",", " | ") + ",");
        sb.append(singleMovie.getYear().replaceAll(",", " | ") + ",");
        sb.append(singleMovie.getRated().replaceAll(",", " | ") + ",");
        sb.append(singleMovie.getReleased().replaceAll(",", " | ") + ",");
        sb.append(singleMovie.getRuntime().replaceAll(",", " | ") + ",");
        sb.append(singleMovie.getGenre().replaceAll(",", " | ") + ",");
        sb.append(singleMovie.getDirector().replaceAll(",", " | ") + ",");
        sb.append(singleMovie.getWriter().replaceAll(",", " | ") + ",");
        sb.append(singleMovie.getActors().replaceAll(",", " | ") + ",");
        sb.append(singleMovie.getPlot().replaceAll(",", " | ") + ",");
        sb.append(singleMovie.getLanguage().replaceAll(",", " | ") + ",");
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
