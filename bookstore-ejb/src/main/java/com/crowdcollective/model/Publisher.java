package com.crowdcollective.model;

import javax.persistence.*;
import java.util.List;

@Entity
public class Publisher {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    private long id;
    private String name;
    private String website;
    @OneToMany(mappedBy = "publisher", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH})
    private List<Book> books;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }
}
