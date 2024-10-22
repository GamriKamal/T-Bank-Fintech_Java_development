package tbank.mr_irmag.tbank_kudago_task.services;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tbank.mr_irmag.tbank_kudago_task.domain.entity.Place;
import tbank.mr_irmag.tbank_kudago_task.exceptions.PlaceException.InvalidPlaceException;
import tbank.mr_irmag.tbank_kudago_task.exceptions.PlaceException.PlaceAlreadyExistsException;
import tbank.mr_irmag.tbank_kudago_task.exceptions.PlaceException.PlaceNotFoundException;
import tbank.mr_irmag.tbank_kudago_task.repository.PlaceRepository;

import java.util.List;

@Service
public class PlaceService {
    private PlaceRepository placeRepository;

    @Autowired
    public void setPlaceRepository(PlaceRepository placeRepository) {
        this.placeRepository = placeRepository;
    }

    public List<Place> getAllPlaces() {
        return placeRepository.findAll();
    }

    public Place createPlace(Place place) {
        if (place == null) {
            throw new InvalidPlaceException("Place is null!");
        }

        if (place.getId() == null) {
            if (placeRepository.existsByName(place.getName())) {
                throw new PlaceAlreadyExistsException("Place by this name: " + place.getName() + " already exists!");
            }
            return placeRepository.save(place);
        } else {
            if (!placeRepository.existsById(place.getId())) {
                return placeRepository.save(place);
            } else {
                throw new PlaceAlreadyExistsException("Place by this id: " + place.getId() + " already exists!");
            }
        }
    }



    public Place getPlaceById(Long id) {
        return placeRepository.findById(id)
                .orElseThrow(() -> new PlaceNotFoundException("There is no place by this id: " + id));
    }

    public Place getPlaceByName(String name) {
        return placeRepository.findByName(name)
                .orElseThrow(() -> new PlaceNotFoundException("There is no place by this name: " + name));
    }

    public Place updatePlaceById(Long id, Place place) {
        Place existedPlace = placeRepository.findById(id)
                .orElseThrow(() -> new PlaceNotFoundException("There is no event by this id: " + id));

        existedPlace.setSlug(place.getSlug());
        existedPlace.setName(place.getName());
        existedPlace.setEvents(place.getEvents());

        return placeRepository.save(existedPlace);
    }

    public void deletePlaceById(Long id) {
        Place existedPlace = placeRepository.findById(id)
                .orElseThrow(() -> new PlaceNotFoundException("There is no event by this id: " + id));

        placeRepository.delete(existedPlace);
    }

}
