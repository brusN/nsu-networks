package org.nsu.controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.nsu.model.dto.places.Point;
import org.nsu.model.http.HttpClient;

import java.net.URISyntaxException;
import java.net.URL;
import java.util.ResourceBundle;

@Slf4j
public class AppMainViewController implements Initializable {
    private final HttpClient httpClient;

    public AppMainViewController() {
        httpClient = new HttpClient();
    }

    @FXML
    ImageView search_btn;

    @FXML
    TextField search_text_box;

    @FXML
    VBox found_places_vbox;

    @FXML
    Text place_value_text;

    @FXML
    Text temp_value_text;

    @FXML
    Text feels_like_value_text;

    @FXML
    Text wind_speed_value_text;

    @FXML
    Text clouds_value_text;

    @FXML
    VBox found_places_near_vbox;

    @FXML
    Text place_rating_text_value;

    @FXML
    Text place_kinds_text_value;

    @FXML
    Text place_location_text_value;

    @FXML
    Text place_desc_text_value;

    @FXML
    AnchorPane place_desc_board;

    @SneakyThrows
    public void searchPlaces() {
        System.out.println("Clicked search places");
        var places = httpClient.getPlacesWithName(search_text_box.getText()).thenAcceptAsync(foundPlaces -> {
            // Clearing vbox of last search result
            found_places_vbox.getChildren().clear();

            // Adding search result in vbox
            for (var foundPlace: foundPlaces) {
                if (foundPlace == null) {
                    continue;
                }
                Text foundPlaceText = new Text();
                foundPlaceText.setWrappingWidth(370);
                foundPlaceText.setText(foundPlace.toString());
                foundPlaceText.setOnMouseClicked(e -> {
                    handleClickOnPlace(foundPlace.getPoint());
                });
                Separator separator = new Separator();
                found_places_vbox.getChildren().add(foundPlaceText);
                found_places_vbox.getChildren().add(separator);
            }
        }, Platform::runLater);
    }

    @SneakyThrows
    public void handleClickOnPlace(Point point) {
        httpClient.getWeatherByCoords(point.getLat(), point.getLng())
                .thenApply(list -> {
                    if (list.size() < 1) {
                        return null;
                    }
                    return list.get(0);
                })
                .thenAcceptAsync(weather -> {
                    if (weather == null) {
                        place_value_text.setText("");
                        temp_value_text.setText("");
                        feels_like_value_text.setText("");
                        wind_speed_value_text.setText("");
                        clouds_value_text.setText("");
                    } else {
                        place_value_text.setText(weather.getName());
                        temp_value_text.setText(weather.getWeatherMain().getTemperature() + " C");
                        feels_like_value_text.setText(weather.getWeatherMain().getTemperatureFeelsLike() + " C");
                        wind_speed_value_text.setText(weather.getWindInfo().getSpeed() + " m/s");
                        clouds_value_text.setText(weather.getCloudsInfo().getCloudiness() + "%");
                    }
                }, Platform::runLater);

        var placesNear = httpClient.getPlacesNear(point.getLat(), point.getLng(), Integer.toString(1000)).thenAcceptAsync(foundPlacesNear -> {
            // Clearing vbox of last search result
            found_places_near_vbox.getChildren().clear();

            // Adding search result in vbox
            for (var placeNear: foundPlacesNear) {
                if (placeNear == null) {
                    continue;
                }
                Text placeNearText = new Text();
                placeNearText.setOnMouseClicked(e -> {
                    try {
                        handleGetPlaceDescription(placeNear.getXid());
                    } catch (URISyntaxException ex) {
                        ex.printStackTrace();
                    }
                });
                placeNearText.setWrappingWidth(865);
                placeNearText.setText(placeNear.toString());
                Separator separator = new Separator();
                found_places_near_vbox.getChildren().add(placeNearText);
                found_places_near_vbox.getChildren().add(separator);
            }
        }, Platform::runLater);
    }

    void handleGetPlaceDescription(String xid) throws URISyntaxException {
        httpClient.getPlaceDescription(xid)
                .thenApply(list -> {
                    if (list.size() < 1) {
                        place_desc_board.setVisible(false);
                        return null;
                    }
                    place_desc_board.setVisible(true);
                    return list.get(0);
                })
                .thenAcceptAsync(description -> {
                    place_rating_text_value.setText(description.getRate());
                    place_kinds_text_value.setText(description.getKinds());
                    place_location_text_value.setText(description.getAddress().getCountry() + ", " + description.getAddress().getCity());
                    var desc = description.getInfo().getDescr();
                    place_desc_text_value.setText(desc == null ? "" : desc);
                }, Platform::runLater);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
