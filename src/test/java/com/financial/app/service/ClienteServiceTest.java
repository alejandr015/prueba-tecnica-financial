package com.financial.app.service;

import com.financial.app.dto.ClienteDTO;
import com.financial.app.entity.Cliente;
import com.financial.app.exception.BusinessException;
import com.financial.app.repository.ClienteRepository;
import com.financial.app.repository.ProductoRepository;
import com.financial.app.service.impl.ClienteServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.time.LocalDate;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ClienteServiceTest {

    @Mock private ClienteRepository clienteRepository;
    @Mock private ProductoRepository productoRepository;
    @InjectMocks private ClienteServiceImpl clienteService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void crearCliente_exitoso() {
        ClienteDTO dto = ClienteDTO.builder()
                .tipoIdentificacion("CC")
                .numeroIdentificacion("123456")
                .nombres("Juan")
                .apellido("Perez")
                .correoElectronico("juan@mail.com")
                .fechaNacimiento(LocalDate.of(1990, 1, 1))
                .build();

        when(clienteRepository.existsByNumeroIdentificacion("123456")).thenReturn(false);
        when(clienteRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        Cliente resultado = clienteService.crear(dto);

        assertNotNull(resultado);
        assertEquals("Juan", resultado.getNombres());
    }

    @Test
    void crearCliente_menorDeEdad_lanzaExcepcion() {
        ClienteDTO dto = ClienteDTO.builder()
                .tipoIdentificacion("CC")
                .numeroIdentificacion("123456")
                .nombres("Juan")
                .apellido("Perez")
                .correoElectronico("juan@mail.com")
                .fechaNacimiento(LocalDate.now().minusYears(16))
                .build();

        assertThrows(BusinessException.class, () -> clienteService.crear(dto));
    }

    @Test
    void eliminarCliente_conProductos_lanzaExcepcion() {
        Cliente cliente = new Cliente();
        cliente.setId(1L);

        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
        when(productoRepository.existsByClienteId(1L)).thenReturn(true);

        assertThrows(BusinessException.class, () -> clienteService.eliminar(1L));
    }

    @Test
    void obtenerPorId_noExiste_lanzaExcepcion() {
        when(clienteRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(BusinessException.class, () -> clienteService.obtenerPorId(99L));
    }
}