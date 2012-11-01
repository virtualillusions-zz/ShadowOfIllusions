/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spectre.util.JSON;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 *
 * @author Kyle Williams
 */
@XmlRootElement(name = "root")
@XmlAccessorType(XmlAccessType.NONE)
public class JAXBHashMap {

    @XmlJavaTypeAdapter(HashMapAdapter.class)
    public Map<String, String> map = new HashMap<String, String>();

    public int size() {
        return map.size();
    }

    public static class MapEntry {

        @XmlAttribute
        public String key;
        @XmlValue
        public String value;
    }

    public static class MapType {

        @XmlElement(name = "entrySet")
        public List<MapEntry> entryList = new ArrayList<MapEntry>();
    }

    public static class HashMapAdapter extends XmlAdapter<MapType, Map<String, String>> {

        @Override
        public MapType marshal(Map<String, String> map) {
            MapType mapType = new MapType();
            for (Entry<String, String> entry : map.entrySet()) {
                MapEntry mapEntry = new MapEntry();
                mapEntry.key = entry.getKey();
                mapEntry.value = entry.getValue();
                mapType.entryList.add(mapEntry);
            }
            return mapType;
        }

        @Override
        public Map<String, String> unmarshal(MapType type) throws Exception {
            Map<String, String> map = new HashMap<String, String>();
            for (MapEntry entry : type.entryList) {
                map.put(entry.key, entry.value);
            }
            return map;
        }
    }
}
