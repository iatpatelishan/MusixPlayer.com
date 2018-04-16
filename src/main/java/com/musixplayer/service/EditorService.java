package com.musixplayer.service;

import com.musixplayer.model.Editor;
import com.musixplayer.model.Person;
import com.musixplayer.repository.EditorRepository;
import com.musixplayer.repository.PersonRepository;
import com.musixplayer.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class EditorService {

    private final EditorRepository editorRepository;
    private final RoleRepository roleRepository;

    @Autowired
    public EditorService(EditorRepository editorRepository, RoleRepository roleRepository) {
        this.editorRepository = editorRepository;
        this.roleRepository = roleRepository;
    }

    public Optional<Editor> findByUsername(String username) {
        return editorRepository.findByUsername(username);
    }

    public Optional<Editor> findByEmail(String email) {
        return editorRepository.findByEmail(email);
    }

    public Editor findByConfirmationToken(String confirmationToken) {
        return editorRepository.findByConfirmationToken(confirmationToken);
    }

    public Editor create(Editor editor){
        return editorRepository.save(editor);
    }


}
