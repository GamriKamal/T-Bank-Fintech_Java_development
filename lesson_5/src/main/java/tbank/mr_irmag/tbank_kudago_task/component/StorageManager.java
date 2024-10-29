package tbank.mr_irmag.tbank_kudago_task.component;

import lombok.Getter;
import org.springframework.stereotype.Component;
import tbank.mr_irmag.tbank_kudago_task.entity.Category;
import tbank.mr_irmag.tbank_kudago_task.entity.Location;
import tbank.mr_irmag.tbank_kudago_task.interfaces.Observer;
import tbank.mr_irmag.tbank_kudago_task.entity.ParameterizedStorage;

import java.util.ArrayList;
import java.util.List;

@Component
@Getter
public class StorageManager {
    private final ParameterizedStorage<Integer, Category> categoriesStorage = new ParameterizedStorage<>();
    private final ParameterizedStorage<String, Location> locationsStorage = new ParameterizedStorage<>();

    private final List<Observer> observers = new ArrayList<>();

    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    public void notifyObservers(String message) {
        for (Observer observer : observers) {
            observer.update(message);
        }
    }

    public void addCategory(Integer id, Category category) {
        categoriesStorage.put(id, category);
        notifyObservers("Category created: " + category.toString());
    }

    public void addLocation(String slug, Location location) {
        locationsStorage.put(slug, location);
        notifyObservers("Location created: " + location.toString());
    }
}
