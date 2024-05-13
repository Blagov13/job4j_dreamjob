package ru.job4j.dreamjob.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.ui.Model;
import javax.servlet.http.HttpSession;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

class IndexControllerTest {
    private IndexController indexController;
    private Model model;
    private HttpSession session;

    @BeforeEach
    void setUp() {
        indexController = new IndexController();
        model = mock(Model.class);
        session = mock(HttpSession.class);
    }

    @Test
    void whenGetIndexPageThenReturnIndexView() {
        String view = indexController.getIndex(model, session);
        assertThat(view).isEqualTo("index");
    }
}