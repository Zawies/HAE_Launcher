package uk.co.dooapp.hae_launcher.models;

public class WeatherInfo {
    private String cityName, county, description;
    private int temperature;

    public WeatherInfo() {
    }

    public WeatherInfo(String cityName, String county, String description, int temperature) {
        this.cityName = cityName;
        this.county = county;
        this.description = description;
        this.temperature = temperature;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getTemperature() {
        return temperature;
    }

    public void setTemperature(int temperature) {
        this.temperature = temperature;
    }
}
