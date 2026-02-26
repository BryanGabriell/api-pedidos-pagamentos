package com.bryangabriel.sistema_de_pedidos_e_pagamentos.business;

import com.bryangabriel.sistema_de_pedidos_e_pagamentos.business.controller.dto.in.UserRecord;
import com.bryangabriel.sistema_de_pedidos_e_pagamentos.business.controller.dto.out.UserRecordOut;
import com.bryangabriel.sistema_de_pedidos_e_pagamentos.business.mapper.UserMapper;
import com.bryangabriel.sistema_de_pedidos_e_pagamentos.infrastructure.entities.User;
import com.bryangabriel.sistema_de_pedidos_e_pagamentos.infrastructure.exception.EmailDuplicado;
import com.bryangabriel.sistema_de_pedidos_e_pagamentos.infrastructure.exception.UsuarioNotFound;
import com.bryangabriel.sistema_de_pedidos_e_pagamentos.infrastructure.repositories.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;



@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }


    @Transactional
    public UserRecordOut criarUsuario(UserRecord userRecord){
        User user = userMapper.paraEntity(userRecord);

        user.setEmail(user.getEmail().trim().toLowerCase());
        user.setNome(user.getNome().trim());

        if(userRepository.existsByEmail(user.getEmail())){
            throw new EmailDuplicado("E-mail já cadastrado.");
        }

       User usuarioSalvo = userRepository.save(user);
        return userMapper.paraOut(usuarioSalvo);
    }
    public UserRecordOut buscarPorId(Long id) {
        return userRepository.findById(id)
                .map(userMapper::paraOut)
                .orElseThrow(() -> new UsuarioNotFound("Usuário não encontrado"));
    }

    @Transactional(readOnly = true)
    public Page<UserRecordOut> listarUsuarios(Pageable pageable) {
        return userRepository.findAll(pageable)
                .map(userMapper::paraOut);
    }
    @Transactional
    public UserRecordOut atualizarUsuario(Long id, UserRecord userRecord){
        User user = userRepository.findById(id).orElseThrow(() ->
                new UsuarioNotFound("Ops, Usuario não encontrado pra atualização"));

        String emailNormalizado = userRecord.email().trim().toLowerCase();

        if (!user.getEmail().equals(emailNormalizado)
                && userRepository.existsByEmail(emailNormalizado)) {
            throw new EmailDuplicado("E-mail já cadastrado.");
        }

        user.setEmail(emailNormalizado);
        user.setNome(userRecord.nome().trim());
        return userMapper.paraOut(user);
    }
    @Transactional
    public void deletarUsuario(Long id){
       if (!userRepository.existsById(id)){
       throw new UsuarioNotFound("Usuario não existe");
       }
       userRepository.deleteById(id);
    }
    }