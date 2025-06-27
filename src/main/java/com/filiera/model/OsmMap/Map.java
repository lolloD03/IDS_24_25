package com.filiera.model.OsmMap;

import java.util.List;

public class Map {

    private List<Indirizzo> map;

    public Map() {
    }

    public Map(List<Indirizzo> map) {
        this.map = map;
    }


    public void addAddress(Indirizzo address) {
        map.add(address);
    }
    public void removeAddress(Indirizzo address) {
        map.remove(address);
    }

    public void updateAddress(Indirizzo address) {
        int index = map.indexOf(address);
        if (index >= 0) {
            map.set(index, address);
        }
    }

}
