package com.engineering.journalApp.service;

import com.engineering.journalApp.entity.JournalEntry;
import com.engineering.journalApp.entity.User;
import com.engineering.journalApp.repository.JournalEntryRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Component
public class JournalEntryService {

    @Autowired
    private JournalEntryRepository journalEntryRepository;

    @Autowired
    private UserService userService;

    @Transactional
    public void saveEntry(JournalEntry journalEntry, String userName){
        try {
            User user = userService.findByUserName(userName);
            journalEntry.setCreatedDate(LocalDateTime.now());
            JournalEntry saved = journalEntryRepository.save(journalEntry);
            user.getJournalEntries().add(saved);
            user.setUserName(null);
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

    public void deleteById(ObjectId id, String userName){
        User user = userService.findByUserName(userName);
        user.getJournalEntries().removeIf(journalEntry -> journalEntry.getId().equals(id));
        userService.saveEntry(user);
        journalEntryRepository.deleteById(id);
    }

    public JournalEntry updateById(ObjectId objectId, JournalEntry journalEntry, String userName){
        JournalEntry oldEntry = journalEntryRepository.findById(objectId).orElse(null);
        if (Objects.nonNull(journalEntry)){
            oldEntry.setTitle(Objects.nonNull(journalEntry.getTitle()) && !journalEntry.getTitle().isEmpty() ?
                    journalEntry.getTitle() : oldEntry.getTitle());
            oldEntry.setContent(Objects.nonNull(journalEntry.getContent()) && !journalEntry.getContent().isEmpty() ?
                    journalEntry.getContent() : oldEntry.getContent());
            oldEntry.setLastModifiedDate(LocalDateTime.now());
        }
        return journalEntryRepository.save(oldEntry);
    }

}
