package com.blog.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A ImageBackground.
 */
@Entity
@Table(name = "image_background")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ImageBackground implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Lob
    @Column(name = "img_blob", nullable = false)
    private byte[] imgBlob;

    @Column(name = "img_blob_content_type", nullable = false)
    private String imgBlobContentType;

    @Column(name = "created_date")
    private LocalDate createdDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public byte[] getImgBlob() {
        return imgBlob;
    }

    public ImageBackground imgBlob(byte[] imgBlob) {
        this.imgBlob = imgBlob;
        return this;
    }

    public void setImgBlob(byte[] imgBlob) {
        this.imgBlob = imgBlob;
    }

    public String getImgBlobContentType() {
        return imgBlobContentType;
    }

    public ImageBackground imgBlobContentType(String imgBlobContentType) {
        this.imgBlobContentType = imgBlobContentType;
        return this;
    }

    public void setImgBlobContentType(String imgBlobContentType) {
        this.imgBlobContentType = imgBlobContentType;
    }

    public LocalDate getCreatedDate() {
        return createdDate;
    }

    public ImageBackground createdDate(LocalDate createdDate) {
        this.createdDate = createdDate;
        return this;
    }

    public void setCreatedDate(LocalDate createdDate) {
        this.createdDate = createdDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ImageBackground imageBackground = (ImageBackground) o;
        if (imageBackground.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), imageBackground.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ImageBackground{" +
            "id=" + getId() +
            ", imgBlob='" + getImgBlob() + "'" +
            ", imgBlobContentType='" + imgBlobContentType + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            "}";
    }
}
