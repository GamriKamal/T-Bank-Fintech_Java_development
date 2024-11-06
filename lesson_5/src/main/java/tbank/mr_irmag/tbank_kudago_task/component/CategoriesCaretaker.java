package tbank.mr_irmag.tbank_kudago_task.component;

import lombok.extern.log4j.Log4j2;

import java.util.Stack;

import org.springframework.stereotype.Component;
import tbank.mr_irmag.tbank_kudago_task.services.CategoryService;
import tbank.mr_irmag.tbank_kudago_task.services.CategoryMemento;

@Log4j2
@Component
public class CategoriesCaretaker {
    private final Stack<CategoryMemento> mementoStack = new Stack<>();

    public void save(CategoryService service) {
        CategoryMemento memento = service.save();
        mementoStack.push(memento);
    }

    public void restore(CategoryService service) {
        if (!mementoStack.isEmpty()) {
            service.restore(mementoStack.pop());
        } else {
            log.warn("No memento to restore.");
        }
    }

    public boolean isEmpty() {
        return mementoStack.isEmpty();
    }
}



