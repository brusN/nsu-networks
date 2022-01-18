package org.nsu.model.utils;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import lombok.AllArgsConstructor;
import org.nsu.model.dto.DTO;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
public class JSONHandler <DTOType extends DTO> {
    private final ObjectMapper objectMapper;

    public DTOType getDTO(String json, Class<? extends DTOType> dtoClassType) {
        DTOType value = null;
        try {
            value = objectMapper.readValue(json, dtoClassType);
        } catch (JacksonException e) {
            System.err.println(e.getLocalizedMessage());
        }
        return value;
    }
    public List<DTOType> getDTOList(String json, Class dtoClassType) {
        List<DTOType> value = null;
        try {
            value = objectMapper.readValue(json, TypeFactory.defaultInstance().constructCollectionType(ArrayList.class, dtoClassType));
        } catch (JacksonException e) {
            System.err.println(e.getLocalizedMessage());
        }
        return value;
    }
}
