package com.klug.noteservice.config;

import com.klug.noteservice.dto.NoteDTO;
import com.klug.noteservice.entity.Note;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        modelMapper.getConfiguration()
                .setMatchingStrategy(MatchingStrategies.STRICT)
                .setSkipNullEnabled(true)
                .setFieldMatchingEnabled(true)
                .setFieldAccessLevel(org.modelmapper.config.Configuration.AccessLevel.PRIVATE);

        // Configuração para ignorar o campo LastUpdated durante o mapeamento de DTO para entidade
        modelMapper.typeMap(NoteDTO.class, Note.class)
                .addMappings(mapper -> mapper.skip(Note::setLastUpdated));

        return modelMapper;
    }
}