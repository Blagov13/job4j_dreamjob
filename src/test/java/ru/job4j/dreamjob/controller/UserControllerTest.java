package ru.job4j.dreamjob.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.ui.Model;
import ru.job4j.dreamjob.model.User;
import ru.job4j.dreamjob.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class UserControllerTest {
    private UserService userService;
    private UserController userController;
    private Model model;
    private HttpSession session;
    private HttpServletRequest request;

    @BeforeEach
    void setUp() {
        userService = mock(UserService.class);
        userController = new UserController(userService);
        model = mock(Model.class);
        session = mock(HttpSession.class);
        request = mock(HttpServletRequest.class);
    }

    @Test
    void whenGetRegistrationPageThenReturnRegisterView() {
        String view = userController.getRegistrationPage(model, session);
        assertThat(view).isEqualTo("users/register");
    }

    @Test
    void whenRegisterWithExistingEmailThenReturnRegisterViewWithMessage() {
        User user = new User(0, "test@example.com", "test", "password");
        when(userService.save(user)).thenReturn(Optional.empty());

        String view = userController.register(model, user);

        verify(model).addAttribute("message", "Пользователь с такой почтой уже существует");
        assertThat(view).isEqualTo("/users/register");
    }

    @Test
    void whenRegisterWithNewEmailThenRedirectToVacancies() {
        User user = new User(0, "new@example.com", "test", "password");
        when(userService.save(user)).thenReturn(Optional.of(user));

        String view = userController.register(model, user);

        assertThat(view).isEqualTo("redirect:/vacancies");
    }

    @Test
    void whenGetLoginPageThenReturnLoginView() {
        String view = userController.getLoginPage(model, session);
        assertThat(view).isEqualTo("users/login");
    }

    @Test
    void whenLoginWithIncorrectCredentialsThenReturnLoginViewWithError() {
        User user = new User(0, "test@example.com", "test", "wrongpassword");
        when(userService.findByEmailAndPassword(user.getEmail(), user.getPassword())).thenReturn(Optional.empty());

        String view = userController.loginUser(user, model, request);

        verify(model).addAttribute("error", "Почта или пароль введены неверно");
        assertThat(view).isEqualTo("users/login");
    }

    @Test
    void whenLoginWithCorrectCredentialsThenRedirectToVacancies() {
        User user = new User(0, "test@example.com", "test", "password");
        when(userService.findByEmailAndPassword(user.getEmail(), user.getPassword())).thenReturn(Optional.of(user));
        when(request.getSession()).thenReturn(session);

        String view = userController.loginUser(user, model, request);

        verify(session).setAttribute("user", user);
        assertThat(view).isEqualTo("redirect:/vacancies");
    }

    @Test
    void whenLogoutThenInvalidateSessionAndRedirect() {
        String view = userController.logout(session);

        verify(session).invalidate();
        assertThat(view).isEqualTo("redirect:/users/login");
    }
}