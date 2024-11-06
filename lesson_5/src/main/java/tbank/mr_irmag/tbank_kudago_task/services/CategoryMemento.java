package tbank.mr_irmag.tbank_kudago_task.services;


import lombok.extern.log4j.Log4j2;
import tbank.mr_irmag.tbank_kudago_task.entity.Category;
import tbank.mr_irmag.tbank_kudago_task.entity.ParameterizedStorage;

public class CategoryMemento {
    private final ParameterizedStorage<Integer, Category> categoriesStorageState;

    public CategoryMemento(ParameterizedStorage<Integer, Category> categoriesStorageState) {
        this.categoriesStorageState = new ParameterizedStorage<>();
        categoriesStorageState.getEntry().forEach(entry ->
                this.categoriesStorageState.put(entry.getKey(), entry.getValue().clone()));
    }


    public ParameterizedStorage<Integer, Category> getSavedState() {
        return categoriesStorageState;
    }
}



