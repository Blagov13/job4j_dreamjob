package ru.job4j.dreamjob.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import ru.job4j.dreamjob.dto.FileDto;
import ru.job4j.dreamjob.service.FileService;

import java.io.IOException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class FileControllerTest {
    private FileService fileService;
    private FileController fileController;
    private MultipartFile testFile;

    @BeforeEach
    void setUp() {
        fileService = mock(FileService.class);
        fileController = new FileController(fileService);
        testFile = new MockMultipartFile("testFile.img", new byte[]{1, 2, 3});
    }

    @Test
    void whenGetByIdFileFoundThenReturnFileContent() throws IOException {
        byte[] content = {1, 2, 3};
        FileDto fileData = new FileDto(testFile.getOriginalFilename(), testFile.getBytes());
        when(fileService.getFileById(1)).thenReturn(Optional.of(fileData));
        ResponseEntity<?> response = fileController.getById(1);
        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).isEqualTo(content);
        verify(fileService).getFileById(1);
    }

    @Test
    void whenGetByIdFileNotFoundThenReturnNotFound() {
        when(fileService.getFileById(1)).thenReturn(Optional.empty());
        ResponseEntity<?> response = fileController.getById(1);
        assertThat(response.getStatusCodeValue()).isEqualTo(404);
        verify(fileService).getFileById(1);
    }
}