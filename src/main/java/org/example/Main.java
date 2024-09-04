package org.example;


public class Main {
    public static void main(String[] args) {
        ParserInfo parserInfo = new ParserInfo();
        parserInfo.parse("src/main/resources/city.json", "src/main/resources/city_info.xml");
        parserInfo.parse("src/main/resources/city-error.json", "src/main/resources/city_error.xml");
    }
}
