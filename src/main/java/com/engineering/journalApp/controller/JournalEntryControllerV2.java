package com.engineering.journalApp.controller;

import com.engineering.journalApp.entity.JournalEntry;
import com.engineering.journalApp.entity.User;
import com.engineering.journalApp.service.JournalEntryService;
import com.engineering.journalApp.service.UserService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/journal")
public class JournalEntryControllerV2 {

    @Autowired
    private JournalEntryService journalEntryService;

    @Autowired
    private UserService userService;

    private Map<Long, JournalEntry> journalEntryMap = new HashMap<>();

    @GetMapping("/{userName}")
    public ResponseEntity<?> getAllJournalEntriesOfUser(@PathVariable String userName){
        User user = userService.findByUserName(userName);
        List<JournalEntry> all = user.getJournalEntries();
        if (all != null && !all.isEmpty()){
            return new ResponseEntity<>(all, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/{userName}")
    public ResponseEntity<JournalEntry> createEntry(@RequestBody JournalEntry myEntry, @PathVariable String userName){
        try {
            journalEntryService.saveEntry(myEntry, userName);
            return new ResponseEntity<>(myEntry, HttpStatus.CREATED);
        } catch (Exception exception){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("id/{myId}")
    public ResponseEntity<JournalEntry> getJournalEntryById(@PathVariable ObjectId myId){
        Optional<JournalEntry> byId = journalEntryService.getById(myId);
        if (byId.isPresent()){
            return new ResponseEntity<>(byId.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{userName}/{myId}")
    public ResponseEntity<?> deleteJournalEntryById(@PathVariable ObjectId myId, @PathVariable String userName){
        journalEntryService.deleteById(myId, userName);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/{myId}/{userName}")
    public ResponseEntity<?> updateJournalEntryById(@PathVariable ObjectId myId,
                                                    @RequestBody JournalEntry journalEntry,
                                                    @PathVariable String userName){
        try {
            JournalEntry updated = journalEntryService.updateById(myId, journalEntry, userName);
            return new ResponseEntity<>(updated, HttpStatus.OK);
        } catch (Exception exception){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
