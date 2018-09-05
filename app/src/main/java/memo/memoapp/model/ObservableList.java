package memo.memoapp.model;

import android.util.Log;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Observer;

import memo.memoapp.controller.Service;

/**
 * Created by Angelo Faella
 */

public class ObservableList<T> extends ArrayList<T>{

    private List<Observer> observers;
    private boolean hasChanged = false;

    public ObservableList(){
        super();
        observers = new ArrayList<>();
    }

    @Override
     public boolean add(T elem){
        boolean ret = super.add(elem);
        setChanged();
        notifyObservers();
        return ret;
    }

    @Override
    public void add(int index, T elem){
        super.add(index,elem);
        setChanged();
        notifyObservers();
    }

    @Override
    public boolean addAll(Collection<? extends T> c){
        boolean ret = super.addAll(c);
        setChanged();
        notifyObservers();
        return ret;
    }

    @Override
    public boolean addAll(int index, Collection<? extends T> c){
        boolean ret = super.addAll(index,c);
        setChanged();
        notifyObservers();
        return ret;
    }

    @Override
    public void clear(){
        super.clear();
        setChanged();
        notifyObservers();
    }

    @Override
    public T remove(int i){
        T elem = super.remove(i);
        setChanged();
        notifyObservers();
        return elem;
    }

    @Override
    public boolean remove(Object o){
        boolean ret = super.remove(o);
        setChanged();
        notifyObservers();
        return ret;
    }

    @Override
    public boolean removeAll(Collection<?> c){
        boolean ret = super.removeAll(c);
        setChanged();
        notifyObservers();
        return ret;
    }

    //remove if
    //remove range
    //replace all
    //retain all

    public void addObserver(Observer o){
        observers.add(o);
    }

    public void removeObserver(Observer o){
        observers.remove(o);
    }

    private void notifyObservers(){
        Log.e(Service.LOG_TAG,"List notifiObservers()");

        for(Observer o : observers){
            o.update(null,null);
        }
        hasChanged = false;
    }

    private void setChanged(){
        hasChanged = true;
    }

    public boolean hasChanged(){
        return hasChanged;
    }






}
