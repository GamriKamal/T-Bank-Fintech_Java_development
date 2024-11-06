package tbank.mr_irmag.tbank_kudago_task.services;

import tbank.mr_irmag.tbank_kudago_task.entity.Location;
import tbank.mr_irmag.tbank_kudago_task.entity.ParameterizedStorage;

public class LocationMemento {
    private final ParameterizedStorage<String, Location> locationsStorageState;

    public LocationMemento(ParameterizedStorage<String, Location> locationsStorageState) {
        this.locationsStorageState = new ParameterizedStorage<>();
        locationsStorageState.getEntry().forEach(entry ->
                this.locationsStorageState.put(entry.getKey(), entry.getValue().clone()));
    }

    public ParameterizedStorage<String, Location> getSavedState() {
        return locationsStorageState;
    }
}
