package com.codeoftheweb.salvo;

import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;
import java.util.*;
import static java.util.stream.Collectors.toList;

@Entity
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;
    private String userName;

    @OneToMany(mappedBy="player", fetch=FetchType.EAGER)
    Set<GamePlayer> gamePlayers;

    public Player() {}

    public Player(String userName) {
        this.userName = userName;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String username) {
        this.userName = username;
    }

    public String toString() {
        return userName;
    }

    public List<Game> getGames() {
        return gamePlayers.stream().map(g -> g.getGame()).collect(toList());
    }

//    public Map<String, Object> makePlayerDTO() {
//        Map<String, Object> dto = new LinkedHashMap<String, Object>();
//        dto.put("id", getId());
//        dto.put("email", getUserName());
//        return dto;
//    }
}
