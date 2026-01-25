CREATE SCHEMA IF NOT EXISTS public;
SET search_path TO public;

CREATE TABLE users (
                       id BIGSERIAL PRIMARY KEY,
                       nome VARCHAR(30) NOT NULL,
                       email VARCHAR(100) NOT NULL UNIQUE
);

CREATE TABLE produtos (
                          id BIGSERIAL PRIMARY KEY,
                          nome VARCHAR(100) NOT NULL,
                          preco NUMERIC(10,2) NOT NULL,
                          estoque INTEGER NOT NULL
);

CREATE TABLE pedidos (
                         id BIGSERIAL PRIMARY KEY,
                         user_id BIGINT NOT NULL,
                         status_pedido VARCHAR(50) NOT NULL DEFAULT 'CRIADO',
                         valor_total NUMERIC(10,2) NOT NULL,
                         criado_em DATE NOT NULL DEFAULT CURRENT_DATE,
                         CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE item_pedidos (
                              id BIGSERIAL PRIMARY KEY,
                              pedidos_id BIGINT NOT NULL,
                              produto_id BIGINT NOT NULL,
                              quantidade INTEGER NOT NULL,
                              preco_unitario NUMERIC(12, 2) NOT NULL,
                              CONSTRAINT fk_item_pedido FOREIGN KEY (pedidos_id) REFERENCES pedidos(id),
                              CONSTRAINT fk_item_produto FOREIGN KEY (produto_id) REFERENCES produtos(id)
);