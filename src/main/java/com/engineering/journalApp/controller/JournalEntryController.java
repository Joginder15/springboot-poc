//package com.engineering.journalApp.controller;
//
//import com.engineering.journalApp.entity.JournalEntry;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//@RestController
//@RequestMapping("/journal")
//public class JournalEntryController {
//
//    private Map<Long, JournalEntry> journalEntryMap = new HashMap<>();
//
//    @GetMapping
//    public List<JournalEntry> getAll(){ // localhost:8080/journal GET
//        return new ArrayList<>(journalEntryMap.values());
//    }
//
//    @PostMapping
//    public boolean createEntry(@RequestBody JournalEntry myEntry){ // localhost:8080/journal POST
//        journalEntryMap.put(myEntry.getId(), myEntry);
//        return true;
//    }
//
//    @GetMapping("id/{myId}")
//    public JournalEntry getJournalEntryById(@PathVariable Long myId){
//        return journalEntryMap.get(myId);
//    }
//
//    @DeleteMapping("id/{myId}")
//    public JournalEntry deleteJournalEntryById(@PathVariable Long myId){
//        return journalEntryMap.remove(myId);
//    }
//
//    @PutMapping("id/{myId}")
//    public JournalEntry updateJournalEntryById(@PathVariable Long myId, @RequestBody JournalEntry journalEntry){
//        return journalEntryMap.put(myId, journalEntry);
//    }
//
//
//}
