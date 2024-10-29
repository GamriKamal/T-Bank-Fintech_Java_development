package tbank.mr_irmag.tbank_kudago_task.component;

import tbank.mr_irmag.tbank_kudago_task.services.LocationMemento;
import tbank.mr_irmag.tbank_kudago_task.services.LocationsService;

import java.util.Stack;

public class LocationCaretaker {
    private final Stack<LocationMemento> mementoStack = new Stack<>();

    public void save(LocationsService service) {
        mementoStack.push(service.save());
    }

    public void restore(LocationsService service) {
        if (!mementoStack.isEmpty()) {
            service.restore(mementoStack.pop());
        } else {
            System.out.println("No memento to restore.");
        }
    }
}


