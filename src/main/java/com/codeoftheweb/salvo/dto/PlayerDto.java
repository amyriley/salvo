package com.codeoftheweb.salvo.dto;

public class PlayerDto {

    private long id;
    private String email;

    public long getId() {
        return id;
    }

    public PlayerDto() {}

    public PlayerDto(String userName) {
        this.email = userName;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
