package com.financial.app.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.financial.app.dto.ClienteDTO;
import com.financial.app.entity.Cliente;
import com.financial.app.service.ClienteService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.time.LocalDate;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ClienteController.class)
class ClienteControllerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;
    @MockBean private ClienteService clienteService;

    @Test
    void crearCliente_retorna201() throws Exception {
        ClienteDTO dto = ClienteDTO.builder()
                .tipoIdentificacion("CC")
                .numeroIdentificacion("123456")
                .nombres("Juan")
                .apellido("Perez")
                .correoElectronico("juan@mail.com")
                .fechaNacimiento(LocalDate.of(1990, 1, 1))
                .build();

        Cliente cliente = new Cliente();
        cliente.setId(1L);
        cliente.setNombres("Juan");

        when(clienteService.crear(any())).thenReturn(cliente);

        mockMvc.perform(post("/api/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated());
    }

    @Test
    void eliminarCliente_retorna204() throws Exception {
        doNothing().when(clienteService).eliminar(1L);

        mockMvc.perform(delete("/api/clientes/1"))
                .andExpect(status().isNoContent());
    }
}