package com.blog.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A GoogleSearchConfig.
 */
@Entity
@Table(name = "google_search_config")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class GoogleSearchConfig implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "google_key")
    private String googleKey;

    @Column(name = "google_cx")
    private String googleCx;

    @Column(name = "google_opt")
    private String googleOpt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGoogleKey() {
        return googleKey;
    }

    public GoogleSearchConfig googleKey(String googleKey) {
        this.googleKey = googleKey;
        return this;
    }

    public void setGoogleKey(String googleKey) {
        this.googleKey = googleKey;
    }

    public String getGoogleCx() {
        return googleCx;
    }

    public GoogleSearchConfig googleCx(String googleCx) {
        this.googleCx = googleCx;
        return this;
    }

    public void setGoogleCx(String googleCx) {
        this.googleCx = googleCx;
    }

    public String getGoogleOpt() {
        return googleOpt;
    }

    public GoogleSearchConfig googleOpt(String googleOpt) {
        this.googleOpt = googleOpt;
        return this;
    }

    public void setGoogleOpt(String googleOpt) {
        this.googleOpt = googleOpt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        GoogleSearchConfig googleSearchConfig = (GoogleSearchConfig) o;
        if (googleSearchConfig.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), googleSearchConfig.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "GoogleSearchConfig{" +
            "id=" + getId() +
            ", googleKey='" + getGoogleKey() + "'" +
            ", googleCx='" + getGoogleCx() + "'" +
            ", googleOpt='" + getGoogleOpt() + "'" +
            "}";
    }
}
