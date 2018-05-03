package com.blog.web.rest;

import com.blog.BlogApp;

import com.blog.domain.ImageBackground;
import com.blog.repository.ImageBackgroundRepository;
import com.blog.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the ImageBackgroundResource REST controller.
 *
 * @see ImageBackgroundResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BlogApp.class)
public class ImageBackgroundResourceIntTest {

    private static final byte[] DEFAULT_IMG_BLOB = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_IMG_BLOB = TestUtil.createByteArray(2, "1");
    private static final String DEFAULT_IMG_BLOB_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_IMG_BLOB_CONTENT_TYPE = "image/png";

    private static final LocalDate DEFAULT_CREATED_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CREATED_DATE = LocalDate.now(ZoneId.systemDefault());

    @Autowired
    private ImageBackgroundRepository imageBackgroundRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restImageBackgroundMockMvc;

    private ImageBackground imageBackground;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ImageBackgroundResource imageBackgroundResource = new ImageBackgroundResource(imageBackgroundRepository);
        this.restImageBackgroundMockMvc = MockMvcBuilders.standaloneSetup(imageBackgroundResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ImageBackground createEntity(EntityManager em) {
        ImageBackground imageBackground = new ImageBackground()
            .imgBlob(DEFAULT_IMG_BLOB)
            .imgBlobContentType(DEFAULT_IMG_BLOB_CONTENT_TYPE)
            .createdDate(DEFAULT_CREATED_DATE);
        return imageBackground;
    }

    @Before
    public void initTest() {
        imageBackground = createEntity(em);
    }

    @Test
    @Transactional
    public void createImageBackground() throws Exception {
        int databaseSizeBeforeCreate = imageBackgroundRepository.findAll().size();

        // Create the ImageBackground
        restImageBackgroundMockMvc.perform(post("/api/image-backgrounds")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(imageBackground)))
            .andExpect(status().isCreated());

        // Validate the ImageBackground in the database
        List<ImageBackground> imageBackgroundList = imageBackgroundRepository.findAll();
        assertThat(imageBackgroundList).hasSize(databaseSizeBeforeCreate + 1);
        ImageBackground testImageBackground = imageBackgroundList.get(imageBackgroundList.size() - 1);
        assertThat(testImageBackground.getImgBlob()).isEqualTo(DEFAULT_IMG_BLOB);
        assertThat(testImageBackground.getImgBlobContentType()).isEqualTo(DEFAULT_IMG_BLOB_CONTENT_TYPE);
        assertThat(testImageBackground.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
    }

    @Test
    @Transactional
    public void createImageBackgroundWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = imageBackgroundRepository.findAll().size();

        // Create the ImageBackground with an existing ID
        imageBackground.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restImageBackgroundMockMvc.perform(post("/api/image-backgrounds")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(imageBackground)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<ImageBackground> imageBackgroundList = imageBackgroundRepository.findAll();
        assertThat(imageBackgroundList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkImgBlobIsRequired() throws Exception {
        int databaseSizeBeforeTest = imageBackgroundRepository.findAll().size();
        // set the field null
        imageBackground.setImgBlob(null);

        // Create the ImageBackground, which fails.

        restImageBackgroundMockMvc.perform(post("/api/image-backgrounds")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(imageBackground)))
            .andExpect(status().isBadRequest());

        List<ImageBackground> imageBackgroundList = imageBackgroundRepository.findAll();
        assertThat(imageBackgroundList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllImageBackgrounds() throws Exception {
        // Initialize the database
        imageBackgroundRepository.saveAndFlush(imageBackground);

        // Get all the imageBackgroundList
        restImageBackgroundMockMvc.perform(get("/api/image-backgrounds?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(imageBackground.getId().intValue())))
            .andExpect(jsonPath("$.[*].imgBlobContentType").value(hasItem(DEFAULT_IMG_BLOB_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].imgBlob").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMG_BLOB))))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())));
    }

    @Test
    @Transactional
    public void getImageBackground() throws Exception {
        // Initialize the database
        imageBackgroundRepository.saveAndFlush(imageBackground);

        // Get the imageBackground
        restImageBackgroundMockMvc.perform(get("/api/image-backgrounds/{id}", imageBackground.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(imageBackground.getId().intValue()))
            .andExpect(jsonPath("$.imgBlobContentType").value(DEFAULT_IMG_BLOB_CONTENT_TYPE))
            .andExpect(jsonPath("$.imgBlob").value(Base64Utils.encodeToString(DEFAULT_IMG_BLOB)))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingImageBackground() throws Exception {
        // Get the imageBackground
        restImageBackgroundMockMvc.perform(get("/api/image-backgrounds/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateImageBackground() throws Exception {
        // Initialize the database
        imageBackgroundRepository.saveAndFlush(imageBackground);
        int databaseSizeBeforeUpdate = imageBackgroundRepository.findAll().size();

        // Update the imageBackground
        ImageBackground updatedImageBackground = imageBackgroundRepository.findOne(imageBackground.getId());
        updatedImageBackground
            .imgBlob(UPDATED_IMG_BLOB)
            .imgBlobContentType(UPDATED_IMG_BLOB_CONTENT_TYPE)
            .createdDate(UPDATED_CREATED_DATE);

        restImageBackgroundMockMvc.perform(put("/api/image-backgrounds")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedImageBackground)))
            .andExpect(status().isOk());

        // Validate the ImageBackground in the database
        List<ImageBackground> imageBackgroundList = imageBackgroundRepository.findAll();
        assertThat(imageBackgroundList).hasSize(databaseSizeBeforeUpdate);
        ImageBackground testImageBackground = imageBackgroundList.get(imageBackgroundList.size() - 1);
        assertThat(testImageBackground.getImgBlob()).isEqualTo(UPDATED_IMG_BLOB);
        assertThat(testImageBackground.getImgBlobContentType()).isEqualTo(UPDATED_IMG_BLOB_CONTENT_TYPE);
        assertThat(testImageBackground.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    public void updateNonExistingImageBackground() throws Exception {
        int databaseSizeBeforeUpdate = imageBackgroundRepository.findAll().size();

        // Create the ImageBackground

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restImageBackgroundMockMvc.perform(put("/api/image-backgrounds")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(imageBackground)))
            .andExpect(status().isCreated());

        // Validate the ImageBackground in the database
        List<ImageBackground> imageBackgroundList = imageBackgroundRepository.findAll();
        assertThat(imageBackgroundList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteImageBackground() throws Exception {
        // Initialize the database
        imageBackgroundRepository.saveAndFlush(imageBackground);
        int databaseSizeBeforeDelete = imageBackgroundRepository.findAll().size();

        // Get the imageBackground
        restImageBackgroundMockMvc.perform(delete("/api/image-backgrounds/{id}", imageBackground.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<ImageBackground> imageBackgroundList = imageBackgroundRepository.findAll();
        assertThat(imageBackgroundList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ImageBackground.class);
        ImageBackground imageBackground1 = new ImageBackground();
        imageBackground1.setId(1L);
        ImageBackground imageBackground2 = new ImageBackground();
        imageBackground2.setId(imageBackground1.getId());
        assertThat(imageBackground1).isEqualTo(imageBackground2);
        imageBackground2.setId(2L);
        assertThat(imageBackground1).isNotEqualTo(imageBackground2);
        imageBackground1.setId(null);
        assertThat(imageBackground1).isNotEqualTo(imageBackground2);
    }
}
