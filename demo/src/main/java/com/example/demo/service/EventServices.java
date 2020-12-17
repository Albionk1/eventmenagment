package com.example.demo.service;

import com.example.demo.model.Event;
import com.example.demo.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventServices {

    @Autowired
    EventRepository eventRepository;

    public List<Event> getAllevents() {
        return eventRepository.findAll();
    }

    public void save(Event event) {
        eventRepository.save(event);
    }
}
