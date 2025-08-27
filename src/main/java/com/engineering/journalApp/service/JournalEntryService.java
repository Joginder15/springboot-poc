package com.engineering.journalApp.service;

import com.engineering.journalApp.entity.JournalEntry;
import com.engineering.journalApp.entity.User;
import com.engineering.journalApp.repository.JournalEntryRepository;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class JournalEntryService {

    @Autowired
    private JournalEntryRepository journalEntryRepository;

    @Autowired
    private UserService userService;

//    @Transactional
    public void saveEntry(JournalEntry journalEntry, String userName){
        try {
            User user = userService.findByUserName(userName);
            journalEntry.setCreatedDate(LocalDateTime.now());
            JournalEntry saved = journalEntryRepository.save(journalEntry);
            user.getJournalEntries().add(saved);
            userService.saveEntry(user);
        } catch(Exception e){
            System.out.println(e);
            throw new RuntimeException("An error occur while saving the entry.", e);
        }
    }

    public List<JournalEntry> getAll(){
        return journalEntryRepository.findAll();
    }

    public Optional<JournalEntry> getById(ObjectId id){
        return journalEntryRepository.findById(id);
    }

//    @Transactional
    public boolean deleteById(ObjectId id, String userName){
        boolean removed = false;
        try {
            User user = userService.findByUserName(userName);
            removed = user.getJournalEntries().removeIf(journalEntry -> journalEntry.getId().equals(id));
            if (removed){
                userService.saveEntry(user);
                journalEntryRepository.deleteById(id);
            }
        } catch (Exception exception){
            System.out.println(exception);
            throw new RuntimeException("An exception occur while deleting the entry: ",exception);
        }
        return removed;
    }

    public JournalEntry updateById(ObjectId objectId, JournalEntry newEntry){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String name = authentication.getName();
        User user = userService.findByUserName(name);
        List<JournalEntry> journalEntries =
                user.getJournalEntries().stream().filter(x -> x.getId().equals(objectId)).collect(Collectors.toList());
        JournalEntry savedJournalEntry = null;
        if (!journalEntries.isEmpty()) {
            Optional<JournalEntry> journalEntry = journalEntryRepository.findById(objectId);
            if (Objects.nonNull(journalEntry)){
                JournalEntry oldEntry = journalEntry.get();
                oldEntry.setTitle(Objects.nonNull(newEntry.getTitle()) && !newEntry.getTitle().isEmpty() ?
                        newEntry.getTitle() : oldEntry.getTitle());
                oldEntry.setContent(Objects.nonNull(newEntry.getContent()) && !newEntry.getContent().isEmpty() ?
                        newEntry.getContent() : oldEntry.getContent());
                oldEntry.setLastModifiedDate(LocalDateTime.now());
                savedJournalEntry = journalEntryRepository.save(oldEntry);
            }
        }
        return savedJournalEntry;
    }


}
