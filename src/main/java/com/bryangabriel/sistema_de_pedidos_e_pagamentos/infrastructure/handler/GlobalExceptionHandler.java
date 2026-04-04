package com.bryangabriel.sistema_de_pedidos_e_pagamentos.infrastructure.handler;

import com.bryangabriel.sistema_de_pedidos_e_pagamentos.infrastructure.exception.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ProdutoNotFound.class)
    public ResponseEntity<ApiErro> handleProdutoNotFound(ProdutoNotFound ex, HttpServletRequest request) {
        return buildError(ex.getMessage(), HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(UsuarioNotFound.class)
    public ResponseEntity<ApiErro> handleUsuarioNotFound(UsuarioNotFound ex, HttpServletRequest request) {
        return buildError(ex.getMessage(), HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(PedidoNotFound.class)
    public ResponseEntity<ApiErro> handlePedidoNotFound(PedidoNotFound ex, HttpServletRequest request) {
        return buildError(ex.getMessage(), HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(ItemProdutoNotFound.class)
    public ResponseEntity<ApiErro> handleItemNotFound(ItemProdutoNotFound ex, HttpServletRequest request) {
        return buildError(ex.getMessage(), HttpStatus.NOT_FOUND, request);
    }


    @ExceptionHandler(EmailDuplicado.class)
    public ResponseEntity<ApiErro> handleEmailDuplicado(EmailDuplicado ex, HttpServletRequest request) {
        return buildError(ex.getMessage(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(EstoqueInsuficiente.class)
    public ResponseEntity<ApiErro> handleEstoque(EstoqueInsuficiente ex, HttpServletRequest request) {
        return buildError(ex.getMessage(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(StatusInvalido.class)
    public ResponseEntity<ApiErro> handleStatus(StatusInvalido ex, HttpServletRequest request) {
        return buildError(ex.getMessage(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(PedidoError.class)
    public ResponseEntity<ApiErro> handlePedidoError(PedidoError ex, HttpServletRequest request) {
        return buildError(ex.getMessage(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(QuantidadeInvalida.class)
    public ResponseEntity<ApiErro> handleQuantidade(QuantidadeInvalida ex, HttpServletRequest request) {
        return buildError(ex.getMessage(), HttpStatus.BAD_REQUEST, request);
    }


    @ExceptionHandler(ErroAoSalvar.class)
    public ResponseEntity<ApiErro> handleErroSalvar(ErroAoSalvar ex, HttpServletRequest request) {
        return buildError(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErro> handleGeneric(Exception ex, HttpServletRequest request) {
        return buildError("Erro interno inesperado", HttpStatus.INTERNAL_SERVER_ERROR, request);
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErro> handleValidation(MethodArgumentNotValidException ex,
                                                     HttpServletRequest request) {

        String mensagem = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(err -> err.getField() + ": " + err.getDefaultMessage())
                .findFirst()
                .orElse("Erro de validação");

        return buildError(mensagem, HttpStatus.BAD_REQUEST, request);
    }

    private ResponseEntity<ApiErro> buildError(String mensagem,
                                                HttpStatus status,
                                                HttpServletRequest request) {

        ApiErro error = new ApiErro(
                mensagem,
                status.value(),
                request.getRequestURI()
        );

        return ResponseEntity.status(status).body(error);
    }
}