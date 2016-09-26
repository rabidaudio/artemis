package audio.rabid.artemis;

import java.util.ArrayList;
import java.util.List;

import rx.Subscription;

/**
 * Created by  charles  on 9/25/16.
 */
public class DisposeBag {

    private List<Subscription> subscriptions = new ArrayList<>();

    public void add(Subscription subscription){
        subscriptions.add(subscription);
    }

    public void addAll(Subscription... subscriptions){
        for(Subscription s : subscriptions){
            add(s);
        }
    }

    public void disposeAll(){
        for(Subscription s : subscriptions){
            if(s != null){
                s.unsubscribe();
            }
        }
    }
}
