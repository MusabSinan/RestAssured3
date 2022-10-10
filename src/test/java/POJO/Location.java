package POJO;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

public class Location {

    private String postcode;
    private String country;
    private String countryabbreviation;
    private ArrayList<Place> places;  //places bu sınıfın alt listi olduğu için burada Place türünden bir arraylist tanımlandı


    public String getPostcode() {
        return postcode;
    }

    @JsonProperty("post code")//boşluk olduğu için hata veriyordu dönüşümde onun için eklendi bu annotation sadece setle başlayanlara eklenir
    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCountryabbreviation() {
        return countryabbreviation;
    }

    @JsonProperty("country abbreviation") //boşluk olduğu için hata veriyordu dönüşümde onun için eklendi
    public void setCountryabbreviation(String countryabbreviation) {
        this.countryabbreviation = countryabbreviation;
    }

    public ArrayList<Place> getPlaces() {
        return places;
    }

    public void setPlaces(ArrayList<Place> places) {
        this.places = places;
    }

    @Override
    public String toString() {
        return "Location{" +
                "postcode='" + postcode + '\'' +
                ", country='" + country + '\'' +
                ", countryabbreviation='" + countryabbreviation + '\'' +
                ", places=" + places +
                '}';
    }
}
