package com.calcapp.core;

import com.calcapp.core.model.HistoryEntry;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HistoryManager {

    private final List<HistoryEntry> entries = new ArrayList<>();
    private static final int MAX_ENTRIES = 100;

    public void add(HistoryEntry entry) {
        entries.add(entry);
        if (entries.size() > MAX_ENTRIES) {
            entries.remove(0);
        }
    }

    public List<HistoryEntry> getAll() {
        return Collections.unmodifiableList(entries);
    }

    public void clear() {
        entries.clear();
    }

    public int size() {
        return entries.size();
    }
}