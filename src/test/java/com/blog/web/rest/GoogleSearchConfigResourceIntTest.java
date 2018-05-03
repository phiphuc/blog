package com.blog.web.rest;

import com.blog.BlogApp;

import com.blog.domain.GoogleSearchConfig;
import com.blog.repository.GoogleSearchConfigRepository;
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

import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the GoogleSearchConfigResource REST controller.
 *
 * @see GoogleSearchConfigResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BlogApp.class)
public class GoogleSearchConfigResourceIntTest {

    private static final String DEFAULT_GOOGLE_KEY = "AAAAAAAAAA";
    private static final String UPDATED_GOOGLE_KEY = "BBBBBBBBBB";

    private static final String DEFAULT_GOOGLE_CX = "AAAAAAAAAA";
    private static final String UPDATED_GOOGLE_CX = "BBBBBBBBBB";

    private static final String DEFAULT_GOOGLE_OPT = "AAAAAAAAAA";
    private static final String UPDATED_GOOGLE_OPT = "BBBBBBBBBB";

    @Autowired
    private GoogleSearchConfigRepository googleSearchConfigRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restGoogleSearchConfigMockMvc;

    private GoogleSearchConfig googleSearchConfig;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        GoogleSearchConfigResource googleSearchConfigResource = new GoogleSearchConfigResource(googleSearchConfigRepository);
        this.restGoogleSearchConfigMockMvc = MockMvcBuilders.standaloneSetup(googleSearchConfigResource)
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
    public static GoogleSearchConfig createEntity(EntityManager em) {
        GoogleSearchConfig googleSearchConfig = new GoogleSearchConfig()
            .googleKey(DEFAULT_GOOGLE_KEY)
            .googleCx(DEFAULT_GOOGLE_CX)
            .googleOpt(DEFAULT_GOOGLE_OPT);
        return googleSearchConfig;
    }

    @Before
    public void initTest() {
        googleSearchConfig = createEntity(em);
    }

    @Test
    @Transactional
    public void createGoogleSearchConfig() throws Exception {
        int databaseSizeBeforeCreate = googleSearchConfigRepository.findAll().size();

        // Create the GoogleSearchConfig
        restGoogleSearchConfigMockMvc.perform(post("/api/google-search-configs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(googleSearchConfig)))
            .andExpect(status().isCreated());

        // Validate the GoogleSearchConfig in the database
        List<GoogleSearchConfig> googleSearchConfigList = googleSearchConfigRepository.findAll();
        assertThat(googleSearchConfigList).hasSize(databaseSizeBeforeCreate + 1);
        GoogleSearchConfig testGoogleSearchConfig = googleSearchConfigList.get(googleSearchConfigList.size() - 1);
        assertThat(testGoogleSearchConfig.getGoogleKey()).isEqualTo(DEFAULT_GOOGLE_KEY);
        assertThat(testGoogleSearchConfig.getGoogleCx()).isEqualTo(DEFAULT_GOOGLE_CX);
        assertThat(testGoogleSearchConfig.getGoogleOpt()).isEqualTo(DEFAULT_GOOGLE_OPT);
    }

    @Test
    @Transactional
    public void createGoogleSearchConfigWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = googleSearchConfigRepository.findAll().size();

        // Create the GoogleSearchConfig with an existing ID
        googleSearchConfig.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restGoogleSearchConfigMockMvc.perform(post("/api/google-search-configs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(googleSearchConfig)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<GoogleSearchConfig> googleSearchConfigList = googleSearchConfigRepository.findAll();
        assertThat(googleSearchConfigList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllGoogleSearchConfigs() throws Exception {
        // Initialize the database
        googleSearchConfigRepository.saveAndFlush(googleSearchConfig);

        // Get all the googleSearchConfigList
        restGoogleSearchConfigMockMvc.perform(get("/api/google-search-configs?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(googleSearchConfig.getId().intValue())))
            .andExpect(jsonPath("$.[*].googleKey").value(hasItem(DEFAULT_GOOGLE_KEY.toString())))
            .andExpect(jsonPath("$.[*].googleCx").value(hasItem(DEFAULT_GOOGLE_CX.toString())))
            .andExpect(jsonPath("$.[*].googleOpt").value(hasItem(DEFAULT_GOOGLE_OPT.toString())));
    }

    @Test
    @Transactional
    public void getGoogleSearchConfig() throws Exception {
        // Initialize the database
        googleSearchConfigRepository.saveAndFlush(googleSearchConfig);

        // Get the googleSearchConfig
        restGoogleSearchConfigMockMvc.perform(get("/api/google-search-configs/{id}", googleSearchConfig.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(googleSearchConfig.getId().intValue()))
            .andExpect(jsonPath("$.googleKey").value(DEFAULT_GOOGLE_KEY.toString()))
            .andExpect(jsonPath("$.googleCx").value(DEFAULT_GOOGLE_CX.toString()))
            .andExpect(jsonPath("$.googleOpt").value(DEFAULT_GOOGLE_OPT.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingGoogleSearchConfig() throws Exception {
        // Get the googleSearchConfig
        restGoogleSearchConfigMockMvc.perform(get("/api/google-search-configs/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateGoogleSearchConfig() throws Exception {
        // Initialize the database
        googleSearchConfigRepository.saveAndFlush(googleSearchConfig);
        int databaseSizeBeforeUpdate = googleSearchConfigRepository.findAll().size();

        // Update the googleSearchConfig
        GoogleSearchConfig updatedGoogleSearchConfig = googleSearchConfigRepository.findOne(googleSearchConfig.getId());
        updatedGoogleSearchConfig
            .googleKey(UPDATED_GOOGLE_KEY)
            .googleCx(UPDATED_GOOGLE_CX)
            .googleOpt(UPDATED_GOOGLE_OPT);

        restGoogleSearchConfigMockMvc.perform(put("/api/google-search-configs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedGoogleSearchConfig)))
            .andExpect(status().isOk());

        // Validate the GoogleSearchConfig in the database
        List<GoogleSearchConfig> googleSearchConfigList = googleSearchConfigRepository.findAll();
        assertThat(googleSearchConfigList).hasSize(databaseSizeBeforeUpdate);
        GoogleSearchConfig testGoogleSearchConfig = googleSearchConfigList.get(googleSearchConfigList.size() - 1);
        assertThat(testGoogleSearchConfig.getGoogleKey()).isEqualTo(UPDATED_GOOGLE_KEY);
        assertThat(testGoogleSearchConfig.getGoogleCx()).isEqualTo(UPDATED_GOOGLE_CX);
        assertThat(testGoogleSearchConfig.getGoogleOpt()).isEqualTo(UPDATED_GOOGLE_OPT);
    }

    @Test
    @Transactional
    public void updateNonExistingGoogleSearchConfig() throws Exception {
        int databaseSizeBeforeUpdate = googleSearchConfigRepository.findAll().size();

        // Create the GoogleSearchConfig

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restGoogleSearchConfigMockMvc.perform(put("/api/google-search-configs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(googleSearchConfig)))
            .andExpect(status().isCreated());

        // Validate the GoogleSearchConfig in the database
        List<GoogleSearchConfig> googleSearchConfigList = googleSearchConfigRepository.findAll();
        assertThat(googleSearchConfigList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteGoogleSearchConfig() throws Exception {
        // Initialize the database
        googleSearchConfigRepository.saveAndFlush(googleSearchConfig);
        int databaseSizeBeforeDelete = googleSearchConfigRepository.findAll().size();

        // Get the googleSearchConfig
        restGoogleSearchConfigMockMvc.perform(delete("/api/google-search-configs/{id}", googleSearchConfig.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<GoogleSearchConfig> googleSearchConfigList = googleSearchConfigRepository.findAll();
        assertThat(googleSearchConfigList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(GoogleSearchConfig.class);
        GoogleSearchConfig googleSearchConfig1 = new GoogleSearchConfig();
        googleSearchConfig1.setId(1L);
        GoogleSearchConfig googleSearchConfig2 = new GoogleSearchConfig();
        googleSearchConfig2.setId(googleSearchConfig1.getId());
        assertThat(googleSearchConfig1).isEqualTo(googleSearchConfig2);
        googleSearchConfig2.setId(2L);
        assertThat(googleSearchConfig1).isNotEqualTo(googleSearchConfig2);
        googleSearchConfig1.setId(null);
        assertThat(googleSearchConfig1).isNotEqualTo(googleSearchConfig2);
    }
}
