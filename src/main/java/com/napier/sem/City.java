package com.napier.sem;

/**
 * City object is an object created by taking data from the world.sql database and processing it
 * Contains different variable to represent specific data in regards to different cities
 * Holds city id, city name, city population information
 * Makes use of getters and setters to get and set all of these values
 * Used in multiple method within dataHolder, for example citiesByPop, citiesInContinentByPop
 */
public class City {

    private int id;
    private String name;
    private int population;
    private District district;
    private Country country;

    public City(int id){this.id=id;}
    public City(String name)
    {
        this.name = name;
    }
    public City(){}
    public City(int id, String name, int population)
    {
        this.id = id;
        this.name = name;
        this.population = population;
    }
    public int getId(){ return id; }
    public String getName(){ return name; }

    public void setName(String name){ this.name = name; }

    public void setPopulation(Integer population) {this.population=population;}
    public void setCode(Country code) { this.country=code;}


    public int getPopulation(){ return population; }


    public District getDistrict(){ return district; }
    public void setDistrict(District district){ this.district = district; }

    public Country getCountry(){ return country; }
    public void setCountry(Country country){ this.country = country; }

    @Override
    public String toString(){

        String str = "id=" + id + ", name=" + name + ", population=" + population;
        if(country != null){
            str += ", country=" + country.getName();
        }
        if(district != null){
            str += ", district=" + district.getName();
        }

        return str;
    }

}
