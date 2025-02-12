package com.jose.springboot.backend.mvc.models.dto;

/**
 * Clase JokeFlagInfoDTO
 * muestra los chistes relacionados con la flag seleccionada
 * 
 * @autor Jose Pinilla
 */
public class JokeFlagInfoDTO {
    
    private int jokeId;
    private String text1;
    private String languageName;

    public JokeFlagInfoDTO() {
    }

    public JokeFlagInfoDTO(int jokeId, String text1, String languageName) {
        this.jokeId = jokeId;
        this.text1 = text1;
        this.languageName = languageName;
    }

    public int getJokeId() {
        return jokeId;
    }

    public void setJokeId(int jokeId) {
        this.jokeId = jokeId;
    }

    public String getText1() {
        return text1;
    }

    public void setText1(String text1) {
        this.text1 = text1;
    }

    public String getLanguageName() {
        return languageName;
    }

    public void setLanguageName(String languageName) {
        this.languageName = languageName;
    }
}

