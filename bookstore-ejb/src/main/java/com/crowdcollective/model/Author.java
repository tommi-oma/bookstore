package com.crowdcollective.model;

//import javax.json.bind.annotation.JsonbTransient;
//import org.codehaus.jackson.annotate.JsonBackReference;
import org.codehaus.jackson.annotate.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import java.util.List;

/* Annotation XmlRootElement must be present also for proper JSON marshalling */
@XmlRootElement
@Entity
public class Author {
    @Id
    @GeneratedValue
    private long id;
    @Null
    @Pattern(regexp = "[^0-9]*", message = "Must not contain numbers")
    private String firstname;
    @NotNull
    @Size(min = 1, max = 25)
    @Pattern(regexp = "[^0-9]*", message = "Must not contain numbers")
    private String lastname;
    @JsonIgnore @XmlTransient
    @ManyToMany(mappedBy = "authors")
    private List<Book> books;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public List<Book> getBooks() {
		return books;
	}

	public void setBooks(List<Book> books) {
		this.books = books;
	}

	@Override
	public String toString() {
		return "Author [id=" + id + ", firstname=" + firstname + ", lastname=" + lastname + ", books=" + books + "]";
	}

}
