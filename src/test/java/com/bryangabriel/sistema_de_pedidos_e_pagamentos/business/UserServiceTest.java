package com.bryangabriel.sistema_de_pedidos_e_pagamentos.business;

import com.bryangabriel.sistema_de_pedidos_e_pagamentos.business.controller.dto.in.UserRecord;
import com.bryangabriel.sistema_de_pedidos_e_pagamentos.business.controller.dto.out.UserRecordOut;
import com.bryangabriel.sistema_de_pedidos_e_pagamentos.business.mapper.UserMapper;
import com.bryangabriel.sistema_de_pedidos_e_pagamentos.infrastructure.entities.User;
import com.bryangabriel.sistema_de_pedidos_e_pagamentos.infrastructure.exception.EmailDuplicado;
import com.bryangabriel.sistema_de_pedidos_e_pagamentos.infrastructure.exception.UsuarioNotFound;
import com.bryangabriel.sistema_de_pedidos_e_pagamentos.infrastructure.repositories.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
@DisplayName("Testes do Serviço de Usuários")
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserService userService;

    @Nested
    @DisplayName("Cenarios de Sucesso")
    class Sucesso{
        @Test
        @DisplayName("Deve retornar sucesso ao criar um usuário no sistema")
        void DeveRetornarSucessoAoCriarUsuario(){
            UserRecord input = new UserRecord("Fulano","teste11@gmail.com");
            User userEntity = new User();
            userEntity.setNome("Fulano");
            userEntity.setEmail("teste11@gmail.com");

            when(userMapper.paraEntity(input)).thenReturn(userEntity);
            when(userRepository.existsByEmail("teste11@gmail.com")).thenReturn(false);
            when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
                User user = invocation.getArgument(0);
                user.setId(1L);
                return user;
            });
            when(userMapper.paraOut(any(User.class))).thenReturn(new UserRecordOut(1L,userEntity.getNome(),userEntity.getEmail()));

           UserRecordOut resultado = userService.criarUsuario(input);

           assertNotNull(resultado);
            verify(userRepository).save(userEntity);
            verify(userMapper).paraOut(userEntity);
            assertEquals("Fulano", resultado.nome());
            assertEquals("teste11@gmail.com",resultado.email());
        }
        @Test
        @DisplayName("Deve Retornar Sucesso Ao Buscar um usuário por ID")
        void deveRetornarSucessoAoBuscarUsuarioPorId(){
            Long userId = 2L;
            var user = new User();
            user.setId(userId);
            user.setNome("Teste");
            user.setEmail("teste@gmail.com");

            when(userRepository.findById(userId)).thenReturn(Optional.of(user));
            when(userMapper.paraOut(any(User.class))).thenReturn(new UserRecordOut(user.getId(),user.getNome(),user.getEmail()));

            var output = userService.buscarPorId(user.getId());

            assertNotNull(output);
            assertEquals(userId, output.id());
            verify(userRepository, times(1)).findById(user.getId());
            verify(userMapper, times(1)).paraOut(user);
        }

        @Test
        @DisplayName("Deve retornar sucesso uma lista paginada de usuarios")
        void deveRetornarSucessoAoListarUsuarios(){
            Pageable pageable = PageRequest.of(0,2);

            User user1 = new User(1L,"Fulaninho","fulaninho1@gmail.com");
            User user2 = new User(2L,"Fulaninho","fulaninho2@gmail.com");

            Page<User> pageMock = new PageImpl<>(List.of(user1,user2));

            when(userRepository.findAll(pageable)).thenReturn(pageMock);
            when(userMapper.paraOut(any(User.class))).thenAnswer(invocation -> {
                User user =  invocation.getArgument(0);
                return new UserRecordOut(user.getId(),user.getNome(),user.getEmail());
            });

            Page<UserRecordOut> resultado = userService.listarUsuarios(pageable);
            assertAll(
                    () -> assertEquals(2, resultado.getContent().size()),
                    () -> assertEquals("Fulaninho", resultado.getContent().get(0).nome()),
                    () -> assertEquals("Fulaninho", resultado.getContent().get(1).nome())
            );
            verify(userRepository, times(1)).findAll(pageable);
            verify(userMapper, times(2)).paraOut(any(User.class));
        }

        @Test
        @DisplayName("Deve retornar sucesso ao atualizar o usuario")
        void deveRetornarSucessoAoAtualizarOsDadosDoUsuario(){
            Long userId = 4L;
            UserRecord input = new UserRecord("Ana","ana123@gmail.com");
            User userEntity = new User();
            userEntity.setId(userId);
            userEntity.setNome("Ana");
            userEntity.setEmail("ANA123@gmail.com");

            when(userRepository.findById(userId)).thenReturn(Optional.of(userEntity));
            when(userRepository.existsByEmail("ana123@gmail.com")).thenReturn(false);
            when(userMapper.paraOut(any(User.class))).thenAnswer(invocation -> {
                User user =  invocation.getArgument(0);
                return new UserRecordOut(user.getId(),user.getNome(),user.getEmail());
            });

            var output = userService.atualizarUsuario(userId,input);

            assertNotNull(output);
            assertEquals("Ana", output.nome());
            assertEquals("ana123@gmail.com", output.email());
            verify(userRepository, times(1)).findById(userId);
            verify(userRepository).existsByEmail("ana123@gmail.com");
            verify(userRepository, never()).save(any());
        }
        @Test
        @DisplayName("Deve fazer a exclusão do usuario com sucesso")
        void deveDeletarOUsuarioComSucesso(){
            Long id = 1L;
            when(userRepository.existsById(id)).thenReturn(true);
             userService.deletarUsuario(id);
             verify(userRepository).deleteById(id);
        }
    }
    @Nested
    @DisplayName("Cenários de Exceção")
    class Excecoes{

        @Test
        @DisplayName("Deve retornar uma exception ao atualizar o usuario e o email estiver duplicado")
        void deveLancaExcecaoAoAtualizarComEmailDuplicado(){
            Long id = 1L;
            User existente = new User();
            existente.setId(id);
            existente.setEmail("antigo@gmail.com");

            UserRecord input = new UserRecord("bryan","novo@gmail.com");
            when(userRepository.findById(id)).thenReturn(Optional.of(existente));
            when(userRepository.existsByEmail("novo@gmail.com")).thenReturn(true);

            var erro = Assertions.catchThrowable(() -> userService.atualizarUsuario(id,input));

            Assertions.assertThat(erro).isInstanceOf(EmailDuplicado.class).hasMessage("E-mail já cadastrado.");
        }

        @Test
        @DisplayName("Deve retornar uma exception caso o usuario não exista ao deletar usuario")
        void deveLancaExcecaoAoDeletarUsuarioInexistente(){
            Long id = 1L;
            when(userRepository.existsById(id)).thenReturn(false);

            var erro = Assertions.catchThrowable(() ->
                    userService.deletarUsuario(id));

            Assertions.assertThat(erro).
                    isInstanceOf(UsuarioNotFound.class).
                    hasMessage("Usuario não existe");
            assertNotNull(erro);
        }
        @Test
        @DisplayName("Deve retornar uma exception quando não existir id no buscar id")
        void deveRetornarUmaExcecaoSeOIdDoBuscarIdEstiverVazio(){
            Long userId = 2L;
            when(userRepository.findById(userId)).thenReturn(Optional.empty());

            var erro = Assertions.catchThrowable(() ->
                    userService.buscarPorId(userId));

            Assertions.assertThat(erro)
                    .isInstanceOf(UsuarioNotFound.class)
                    .hasMessage("Usuário não encontrado");
            verify(userRepository, times(1)).findById(userId);
        }

        @Test
        @DisplayName("Deve retornar uma exceção se o email estiver duplicado")
        void deveRetornarUmaExcecaoCasoEmailForDuplicado(){
            Long userId = 1L;
            User user = new User();
            user.setId(userId);
            user.setEmail("bryan123@gmail.com");
            user.setNome("Bryan");

            UserRecord userRecord = new UserRecord("Bryan","bryan123@gmail.com");

            when(userMapper.paraEntity(userRecord)).thenReturn(user);
            when(userRepository.existsByEmail("bryan123@gmail.com")).thenReturn(true);
            var erro = Assertions.catchThrowable(() -> userService.criarUsuario(userRecord));

            Assertions.assertThat(erro).
                    isInstanceOf(EmailDuplicado.class).
                    hasMessage("E-mail já cadastrado.");
            verify(userRepository, times(1)).existsByEmail(userRecord.email());
        }
    }
}