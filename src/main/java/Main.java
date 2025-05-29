import org.json.JSONException;
import org.json.JSONObject;

import java.util.Scanner;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        String apiKey = System.getenv("OPENWEATHERMAP_API_KEY");
        if (apiKey == null || apiKey.isEmpty()) {
            System.out.println("Ошибка: API-ключ OPENWEATHERMAP_API_KEY не найден или пуст в переменных окружения.\nПожалуйста, проверьте настройки конфигурации запуска в IntelliJ IDEA.");
            return;
        } else {
            //System.out.println("API-ключ успешно загружен: " + apiKey);
        }

        System.out.println("Введите название города");
        Scanner console = new Scanner(System.in);
        String city = console.nextLine();
        //System.out.println("Вы ввели город" + " " + city);
        console.close();

        String requestUrl = "http://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=" + apiKey + "&units=metric";
        //System.out.println(requestUrl);

        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(requestUrl))
                .GET()
                .build();

        try {
            HttpResponse<String> httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());

            int statusCode = httpResponse.statusCode();
            //System.out.println("Код состояния ответа: " + statusCode);

            if (statusCode == 200) {
                String responseBody = httpResponse.body();
                //System.out.println("Тело ответа (JSON):");
                //System.out.println(responseBody);
                try {
                    JSONObject jsonObject = new JSONObject(responseBody);
                    JSONObject mainObject = jsonObject.getJSONObject("main");
                    double temperature = mainObject.getDouble("temp");
                    System.out.println("Температура в городе " + city + ": " + temperature + " °C");

                } catch (JSONException e) {
                    System.out.println("Ошибка при разборе JSON-ответа:");
                    e.printStackTrace();
                }

            } else {
                System.out.println("Ошибка при запросе. Код состояния: " + statusCode);
            }

        } catch (IOException | InterruptedException e) {
            System.out.println("Произошла ошибка при отправке HTTP-запроса: ");

        }

    }
}
